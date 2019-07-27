package hu.trigary.iodine.bukkit.gui;

import hu.trigary.iodine.api.gui.container.GridGuiContainer;
import hu.trigary.iodine.api.gui.container.LinearGuiContainer;
import hu.trigary.iodine.api.gui.element.*;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.gui.container.GridGuiContainerImpl;
import hu.trigary.iodine.bukkit.gui.container.LinearGuiContainerImpl;
import hu.trigary.iodine.bukkit.gui.element.*;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
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
	private final Map<Class<? extends GuiElement<?>>, ElementConstructor<?>> elements = new HashMap<>();
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
		element(GridGuiContainer.class, GridGuiContainerImpl::new);
		element(LinearGuiContainer.class, LinearGuiContainerImpl::new);
		
		element(ButtonGuiElement.class, ButtonGuiElementImpl::new);
		element(CheckboxGuiElement.class, CheckboxGuiElementImpl::new);
		element(DropdownGuiElement.class, DropdownGuiElementImpl::new);
		element(ImageGuiElement.class, ImageGuiElementImpl::new);
		element(ProgressBarGuiElement.class, ProgressBarGuiElementImpl::new);
		element(RadioButtonGuiElement.class, RadioButtonGuiElementImpl::new);
		element(SliderGuiElement.class, SliderGuiElementImpl::new);
		element(TextFieldGuiElement.class, TextFieldGuiElementImpl::new);
		element(TextGuiElement.class, TextGuiElementImpl::new);
	}
	
	
	
	/**
	 * Creates a new GUI instance.
	 *
	 * @return a new GUI instance
	 */
	@NotNull
	@Contract(pure = true)
	public IodineGuiImpl createGui() {
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
	public IodineGuiImpl getGui(int id) {
		return guiMap.get(id);
	}
	
	/**
	 * Creates a new {@link GuiElementImpl} instance based on the specified {@link Class<GuiElement>}.
	 * Should only be called by {@link IodineGuiImpl}.
	 *
	 * @param type the API class of the element to create
	 * @param gui the GUI which will contain this element
	 * @param internalId the internal ID of the element, specified by the GUI
	 * @param id the API-friendly ID of the element
	 * @return the created element instance
	 */
	@NotNull
	@Contract(pure = true)
	public <T extends GuiElement<T>> GuiElementImpl<T> createElement(@NotNull Class<T> type,
			@NotNull IodineGuiImpl gui, int internalId, @NotNull Object id) {
		ElementConstructor<?> constructor = elements.get(type);
		Validate.notNull(constructor, "A valid Class<? extends GuiElement> must be provided");
		//noinspection unchecked
		return (GuiElementImpl<T>) constructor.apply(gui, internalId, id);
	}
	
	
	
	/**
	 * Removes the specified GUI from the internal cache,
	 * allowing it to be garbage collected.
	 * Should only be called by {@link IodineGuiImpl}.
	 *
	 * @param gui the GUI to unregister
	 */
	public void forgetGui(@NotNull IodineGuiImpl gui) {
		Validate.notNull(guiMap.remove(gui.getId()), "Can only forget registered GUIs");
	}
	
	/**
	 * Adds the specified GUI to the internal cache,
	 * disallowing it from being garbage collected.
	 * Should only be called by {@link IodineGuiImpl}.
	 *
	 * @param gui the GUI to register
	 */
	public void rememberGui(@NotNull IodineGuiImpl gui) {
		Validate.isTrue(guiMap.put(gui.getId(), gui) == null, "Can't remember registered GUIs");
	}
	
	/**
	 * Closes all GUIs for all of their viewers.
	 */
	public void closeAllGuiInstances() {
		new ArrayList<>(guiMap.values()).forEach(gui -> gui.getViewers().forEach(gui::closeFor));
	}
	
	
	
	private <T extends GuiElement<T>> void element(Class<T> clazz, ElementConstructor<T> constructor) {
		//noinspection unchecked
		Validate.isTrue(elements.put(clazz, constructor) == null,
				"Only one constructor can be mapped to a type");
	}
	
	@FunctionalInterface
	private interface ElementConstructor<R extends GuiElement<R>> {
		R apply(IodineGuiImpl gui, int internalId, Object id);
	}
}
