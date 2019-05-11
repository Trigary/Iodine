package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.bukkit.api.gui.IodineGuiImpl;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The manager whose responsibility is keeping track of {@link IodineGuiImpl} instances.
 */
public class GuiManager {
	private final Map<Integer, IodineGuiImpl> guiMap = new HashMap<>();
	private final IodinePlugin plugin;
	private int nextGuiId;
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link IodinePlugin}.
	 *
	 * @param plugin the plugin instance
	 */
	public GuiManager(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
	}
	
	
	
	/**
	 * Creates a new GUI instance.
	 *
	 * @return a new GUI instance
	 */
	@NotNull
	@Contract(pure = true)
	public IodineGuiImpl create() {
		return new IodineGuiImpl(plugin, nextGuiId++);
	}
	
	/**
	 * Gets the GUI instance associated with the specified ID.
	 * Returns null if no matching GUI instances were found.
	 *
	 * @param id the ID to search for
	 * @return the associated GUI instance or null, if none were found
	 */
	@Nullable
	@Contract(pure = true)
	public IodineGuiImpl get(int id) {
		return guiMap.get(id);
	}
	
	
	
	/**
	 * Removes the specified GUI from the internal cache,
	 * allowing it to be garbage collected.
	 * Should only be called by {@link IodineGuiImpl}.
	 *
	 * @param gui the GUI to unregister
	 */
	public void forget(@NotNull IodineGuiImpl gui) {
		Validate.notNull(guiMap.remove(gui.getId()), "Can only forget registered GUIs");
	}
	
	/**
	 * Adds the specified GUI to the internal cache,
	 * disallowing it from being garbage collected.
	 * Should only be called by {@link IodineGuiImpl}.
	 *
	 * @param gui the GUI to register
	 */
	public void remember(@NotNull IodineGuiImpl gui) {
		Validate.isTrue(guiMap.put(gui.getId(), gui) == null, "Can't remember registered GUIs");
	}
	
	/**
	 * Closes all GUIs for all of their viewers.
	 */
	public void closeAll() {
		new ArrayList<>(guiMap.values()).forEach(gui -> gui.getViewers().forEach(gui::closeFor));
	}
}
