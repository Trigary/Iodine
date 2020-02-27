package hu.trigary.iodine.showcase.elements;

import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.api.gui.GuiElements;
import hu.trigary.iodine.api.gui.IodineRoot;
import hu.trigary.iodine.showcase.IodineShowcase;
import hu.trigary.iodine.showcase.Showcase;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class ElementsShowcase extends Showcase {
	public ElementsShowcase(@NotNull IodineShowcase plugin) {
		super(plugin);
	}
	
	
	
	@Override
	public void onCommand(@NotNull Player player) {
		IodineApi.get().createGui()
				.addElement(GuiElements.PROGRESS_BAR, e -> e.setOrientation(true)
						.setHeight(155)
						.setProgress(0.65f))
				.addElement(GuiElements.CONTAINER_LINEAR, 20, 0, container -> {
					container.setOrientation(true);
					IodineRoot<?> root = container.getRoot();
					
					root.addElement(GuiElements.CONTAINER_GRID, grid -> {
						container.makeChildLast(grid)
								.setGridSize(4, 3);
						
						for (int row = 0; row < grid.getRowCount(); row++) {
							int finalRow = row;
							root.addElement(GuiElements.CHECKBOX, e -> grid.makeChild(e, 0, finalRow));
							root.addElement(GuiElements.TEXT, e -> grid.makeChild(e, 1, finalRow)
									.setWidth(75)
									.setPaddingTop(5)
									.setText("Checkbox #" + (finalRow + 1)));
							root.addElement(GuiElements.RADIO_BUTTON, e -> grid.makeChild(e, 2, finalRow));
							root.addElement(GuiElements.TEXT, e -> grid.makeChild(e, 3, finalRow)
									.setWidth(75)
									.setPaddingTop(5)
									.setPaddingBottom(10)
									.setPaddingLeft(10)
									.setText("Radio button #" + (finalRow + 1)));
						}
					});
					
					root.addElement(GuiElements.CONTAINER_LINEAR, horizontal -> {
						container.makeChildLast(horizontal);
						
						root.addElement(GuiElements.CONTAINER_LINEAR, vertical -> {
							horizontal.makeChildLast(vertical)
									.setOrientation(true);
							
							root.addElement(GuiElements.CONTINUOUS_SLIDER, e -> vertical.makeChildLast(e)
									.setWidth(100)
									.setPaddingBottom(10));
							
							root.addElement(GuiElements.TEXT_FIELD, e -> vertical.makeChildLast(e)
									.setWidth(100)
									.setPaddingBottom(10)
									.setText("Text input field"));
							
							root.addElement(GuiElements.BUTTON, e -> vertical.makeChildLast(e)
									.setWidth(100)
									.setText("Button"));
						});
						
						root.addElement(GuiElements.CONTAINER_GRID, grid -> {
							horizontal.makeChildLast(grid)
									.setPaddingLeft(15)
									.setGridSize(5, 5);
							int size = 16;
							
							for (int row = 0; row < grid.getRowCount(); row++) {
								int finalRow = row;
								for (int column = 0; column < grid.getColumnCount(); column++) {
									int finalColumn = column;
									String texture;
									if (row == 0) {
										texture = "minecraft:textures/block/grass_block_side.png";
									} else if (row + 1 == grid.getRowCount()) {
										texture = "minecraft:textures/block/bedrock.png";
									} else if (ThreadLocalRandom.current().nextDouble() < 0.2) {
										texture = "minecraft:textures/block/iron_ore.png";
									} else {
										texture = "minecraft:textures/block/stone.png";
									}
									
									String finalTexture = texture;
									root.addElement(GuiElements.TEXTURE, e -> grid.makeChild(e, finalColumn, finalRow)
											.setWidth(size)
											.setHeight(size)
											.setTexture(finalTexture, 16, 16));
								}
							}
						});
					});
				})
				.openFor(IodineApi.get().getPlayer(player.getUniqueId()));
	}
}
