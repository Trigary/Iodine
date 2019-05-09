package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.bukkit.api.gui.IodineGuiImpl;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class GuiManager {
	private final Map<Integer, IodineGuiImpl> guiMap = new HashMap<>();
	private final IodinePlugin plugin;
	private int nextGuiId;
	
	public GuiManager(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public IodineGuiImpl createGui() {
		return new IodineGuiImpl(plugin, nextGuiId++);
	}
	
	@Nullable
	@Contract(pure = true)
	public IodineGuiImpl getGui(int id) {
		return guiMap.get(id);
	}
	
	
	
	public void forgetGui(@NotNull IodineGuiImpl gui) {
		Validate.notNull(guiMap.remove(gui.getId()), "Can only forget registered GUIs");
	}
	
	public void rememberGui(@NotNull IodineGuiImpl gui) {
		Validate.isTrue(guiMap.put(gui.getId(), gui) == null, "Can't remember registered GUIs");
	}
}
