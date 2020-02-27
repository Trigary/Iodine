package hu.trigary.iodine.showcase.minimap;

import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.api.gui.GuiElements;
import hu.trigary.iodine.api.gui.IodineColor;
import hu.trigary.iodine.api.gui.IodineOverlay;
import hu.trigary.iodine.api.gui.IodineRoot;
import hu.trigary.iodine.api.gui.container.GridGuiContainer;
import hu.trigary.iodine.api.gui.element.RectangleGuiElement;
import hu.trigary.iodine.api.gui.element.base.GuiEditable;
import hu.trigary.iodine.api.player.IodinePlayer;
import hu.trigary.iodine.api.player.IodinePlayerStateChangedEvent;
import hu.trigary.iodine.showcase.IodineShowcase;
import hu.trigary.iodine.showcase.Showcase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class MinimapShowcase extends Showcase implements Listener {
	private static final int CHUNK_COUNT = 11;
	private static final int MENU_SIZE = 10;
	private static final int MENU_GAP = 2;
	private static final IodineColor MENU_DEFAULT_COLOR = IodineColor.get(0xFF404040);
	private static final int OVERLAY_SIZE = 6;
	private static final int OVERLAY_GAP = 1;
	private static final IodineColor OVERLAY_BACKGROUND_COLOR = IodineColor.get(0x99404040);
	private static final IodineColor OVERLAY_DEFAULT_COLOR = IodineColor.get(0x99707070);
	private final Map<UUID, MinimapSettings> settingStorage = new HashMap<>();
	private final Map<Player, IodineOverlay> overlayStorage = new HashMap<>();
	private final SampleFactionsApi factions;
	
	public MinimapShowcase(@NotNull IodineShowcase plugin) {
		super(plugin);
		IodineApi.get().addListener(IodinePlayerStateChangedEvent.class, plugin.getName(), this::onPlayerBecameModded);
		Bukkit.getPluginManager().registerEvents(this, plugin);
		
		factions = new SampleFactionsApi();
		createFactions();
		
		Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			overlayStorage.forEach((player, overlay) -> overlay.atomicUpdate(ignored -> {
				GridGuiContainer grid = (GridGuiContainer) overlay.getAttachment();
				updateGrid(grid, ChunkCoordinate.fromLocation(player.getLocation()),
						(e, f) -> e.setColor(f == null ? OVERLAY_DEFAULT_COLOR : IodineColor.get(f.getColor())));
			}));
		}, 7, 20);
	}
	
	
	
	@Override
	public void onCommand(@NotNull Player player) {
		openMenu(player);
	}
	
	private void onPlayerBecameModded(@NotNull IodinePlayerStateChangedEvent event) {
		if (event.getNewState() == IodinePlayer.State.MODDED) {
			Player player = event.getPlayer().getPlayer(Player.class);
			MinimapSettings settings = settingStorage.computeIfAbsent(player.getUniqueId(), k -> new MinimapSettings());
			if (settings.isEnabled()) {
				showOverlay(player, settings);
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	private void onModdedPlayerQuit(@NotNull PlayerQuitEvent event) {
		if (IodineApi.get().isModded(event.getPlayer().getUniqueId())) {
			overlayStorage.remove(event.getPlayer());
		}
	}
	
	private void openMenu(@NotNull Player player) {
		MinimapSettings settings = settingStorage.computeIfAbsent(player.getUniqueId(), k -> new MinimapSettings());
		IodineApi.get().createGui()
				.addElement(GuiElements.CONTAINER_LINEAR, container -> {
					container.setOrientation(true);
					IodineRoot<?> root = container.getRoot();
					root.addElement(GuiElements.TEXT, e -> container.makeChildLast(e)
							.setWidth(245)
							.setPaddingBottom(5)
							.setText("Factions minimap position"));
					
					root.addElement(GuiElements.CONTAINER_LINEAR, c -> {
						container.makeChildLast(c);
						
						root.addElement("left", GuiElements.BUTTON, e -> c.makeChildLast(e)
								.setWidth(75)
								.setText("Top Left")
								.onClicked((ee, p) -> {
									updateSettings(player, p, settings, IodineOverlay.Anchor.TOP_LEFT);
									configureMenu(ee.getRoot(), settings);
								}));
						root.addElement("hidden", GuiElements.BUTTON, e -> c.makeChildLast(e)
								.setWidth(75)
								.setPaddingLeft(10)
								.setPaddingRight(10)
								.setText("Hidden")
								.onClicked((ee, p) -> {
									updateSettings(player, p, settings, null);
									configureMenu(ee.getRoot(), settings);
								}));
						root.addElement("right", GuiElements.BUTTON, e -> c.makeChildLast(e)
								.setWidth(75)
								.setText("Top Right")
								.onClicked((ee, p) -> {
									updateSettings(player, p, settings, IodineOverlay.Anchor.TOP_RIGHT);
									configureMenu(ee.getRoot(), settings);
								}));
						configureMenu(root, settings);
					});
					root.addElement(GuiElements.CONTAINER_GRID, grid -> {
						int gridWidth = CHUNK_COUNT * MENU_SIZE + (CHUNK_COUNT - 1) * MENU_GAP;
						container.makeChildLast(grid)
								.setPaddingLeft((245 - gridWidth) / 2)
								.setPaddingTop(10);
						createGrid(grid, MENU_SIZE, MENU_GAP);
						updateGrid(grid, ChunkCoordinate.fromLocation(player.getLocation()), (e, f) -> {
							if (f == null) {
								e.setColor(MENU_DEFAULT_COLOR);
								e.setTooltip("");
							} else {
								e.setColor(IodineColor.get(f.getColor()));
								e.setTooltip(f.getName());
							}
						});
					});
				})
				.openFor(IodineApi.get().getPlayer(player.getUniqueId()));
	}
	
	
	
	private void showOverlay(@NotNull Player player, @NotNull MinimapSettings settings) {
		IodineOverlay.Anchor anchor = settings.getAnchor();
		IodineOverlay overlay = IodineApi.get().createOverlay(anchor,
				anchor == IodineOverlay.Anchor.TOP_LEFT ? 5 : -5, 5)
				.addElement(GuiElements.RECTANGLE, e -> {
					int size = CHUNK_COUNT * OVERLAY_SIZE + (CHUNK_COUNT + 1) * OVERLAY_GAP;
					e.setWidth(size).setHeight(size).setColor(OVERLAY_BACKGROUND_COLOR);
				})
				.addElement(GuiElements.CONTAINER_GRID, 1, 1, grid -> {
					grid.getRoot().setAttachment(grid);
					createGrid(grid, OVERLAY_SIZE, OVERLAY_GAP);
					updateGrid(grid, ChunkCoordinate.fromLocation(player.getLocation()),
							(e, f) -> e.setColor(f == null ? OVERLAY_DEFAULT_COLOR : IodineColor.get(f.getColor())));
				})
				.openFor(IodineApi.get().getPlayer(player.getUniqueId()));
		overlayStorage.put(player, overlay);
	}
	
	private void updateSettings(@NotNull Player player, @NotNull IodinePlayer iodinePlayer,
			@NotNull MinimapSettings settings, @Nullable IodineOverlay.Anchor newAnchor) {
		if (settings.isEnabled()) {
			iodinePlayer.closeOverlay(overlayStorage.remove(player));
		}
		settings.setAnchor(newAnchor);
		if (settings.isEnabled()) {
			showOverlay(player, settings);
		}
	}
	
	private void configureMenu(@NotNull IodineRoot<?> menu, @NotNull MinimapSettings settings) {
		GuiEditable<?> hidden = (GuiEditable<?>) menu.getElement("hidden");
		GuiEditable<?> left = (GuiEditable<?>) menu.getElement("left");
		GuiEditable<?> right = (GuiEditable<?>) menu.getElement("right");
		
		if (settings.isEnabled()) {
			hidden.setEditable(true);
			IodineOverlay.Anchor anchor = settings.getAnchor();
			left.setEditable(anchor != IodineOverlay.Anchor.TOP_LEFT);
			right.setEditable(anchor != IodineOverlay.Anchor.TOP_RIGHT);
		} else {
			hidden.setEditable(false);
			left.setEditable(true);
			right.setEditable(true);
		}
	}
	
	
	
	private void createGrid(@NotNull GridGuiContainer grid, int size, int gap) {
		grid.setGridSize(CHUNK_COUNT, CHUNK_COUNT);
		for (int column = 0; column < CHUNK_COUNT; column++) {
			for (int row = 0; row < CHUNK_COUNT; row++) {
				int finalColumn = column;
				int finalRow = row;
				grid.getRoot().addElement(GuiElements.RECTANGLE, e -> {
					grid.makeChild(e, finalColumn, finalRow)
							.setWidth(size)
							.setHeight(size);
					if (finalColumn != 0) {
						e.setPaddingLeft(gap);
					}
					if (finalRow != 0) {
						e.setPaddingTop(gap);
					}
				});
			}
		}
	}
	
	private void updateGrid(@NotNull GridGuiContainer grid, @NotNull ChunkCoordinate center,
			@NotNull BiConsumer<RectangleGuiElement, Faction> action) {
		int startX = center.getX() - CHUNK_COUNT / 2;
		int startZ = center.getZ() - CHUNK_COUNT / 2;
		for (int column = 0; column < CHUNK_COUNT; column++) {
			for (int row = 0; row < CHUNK_COUNT; row++) {
				RectangleGuiElement element = (RectangleGuiElement) grid.getChild(column, row);
				Faction faction = factions.getFactionAt(ChunkCoordinate.fromChunkCoords(startX + column, startZ + row));
				action.accept(element, faction);
			}
		}
	}
	
	
	
	private void createFactions() {
		Faction faction = factions.createFaction("Cleansed Warriors", IodineColor.WHITE.getArgb());
		factions.setFactionAt(ChunkCoordinate.fromChunkCoords(-2, 1), faction);
		
		faction = factions.createFaction("Dark Knights", IodineColor.BLACK.getArgb());
		factions.setFactionAt(ChunkCoordinate.fromChunkCoords(-2, 0), faction);
		factions.setFactionAt(ChunkCoordinate.fromChunkCoords(-3, 0), faction);
		
		faction = factions.createFaction("Blood Rangers", IodineColor.RED.getArgb());
		factions.setFactionAt(ChunkCoordinate.fromChunkCoords(0, 0), faction);
		factions.setFactionAt(ChunkCoordinate.fromChunkCoords(1, 0), faction);
		factions.setFactionAt(ChunkCoordinate.fromChunkCoords(1, 1), faction);
		
		faction = factions.createFaction("Jungle Assassins", IodineColor.GREEN.getArgb());
		factions.setFactionAt(ChunkCoordinate.fromChunkCoords(0, -1), faction);
		factions.setFactionAt(ChunkCoordinate.fromChunkCoords(-1, -1), faction);
		
		faction = factions.createFaction("Sea Pirates", IodineColor.BLUE.getArgb());
		factions.setFactionAt(ChunkCoordinate.fromChunkCoords(-1, 1), faction);
		factions.setFactionAt(ChunkCoordinate.fromChunkCoords(-1, 2), faction);
	}
}
