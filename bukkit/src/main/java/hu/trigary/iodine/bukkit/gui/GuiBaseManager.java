package hu.trigary.iodine.bukkit.gui;

import hu.trigary.iodine.api.gui.IodineOverlay;
import hu.trigary.iodine.api.gui.container.GridGuiContainer;
import hu.trigary.iodine.api.gui.container.LinearGuiContainer;
import hu.trigary.iodine.api.gui.element.*;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.gui.container.GridGuiContainerImpl;
import hu.trigary.iodine.bukkit.gui.container.LinearGuiContainerImpl;
import hu.trigary.iodine.bukkit.gui.container.base.GuiBaseImpl;
import hu.trigary.iodine.bukkit.gui.element.*;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * The manager whose responsibility is keeping track of
 * {@link IodineGuiImpl} and {@link IodineOverlayImpl} instances.
 */
public class GuiBaseManager {
	private final Map<Class<? extends GuiElement<?>>, ElementConstructor<?>> elements = new HashMap<>();
	private final Map<Integer, GuiBaseImpl<?>> guiMap = new HashMap<>();
	private final IodinePlugin plugin;
	private int nextGuiId;
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link IodinePlugin}.
	 *
	 * @param plugin the plugin instance
	 */
	public GuiBaseManager(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
		element(GridGuiContainer.class, GridGuiContainerImpl::new);
		element(LinearGuiContainer.class, LinearGuiContainerImpl::new);
		
		element(ButtonGuiElement.class, ButtonGuiElementImpl::new);
		element(CheckboxGuiElement.class, CheckboxGuiElementImpl::new);
		element(ContinuousSliderGuiElement.class, ContinuousSliderGuiElementImpl::new);
		element(DiscreteSliderGuiElement.class, DiscreteSliderGuiElementImpl::new);
		element(DropdownGuiElement.class, DropdownGuiElementImpl::new);
		element(ImageGuiElement.class, ImageGuiElementImpl::new);
		element(ProgressBarGuiElement.class, ProgressBarGuiElementImpl::new);
		element(RadioButtonGuiElement.class, RadioButtonGuiElementImpl::new);
		element(RectangleGuiElement.class, RectangleGuiElementImpl::new);
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
		plugin.log(Level.OFF, "GuiManager > new GUI {0}", nextGuiId);
		return new IodineGuiImpl(plugin, nextGuiId++);
	}
	
	/**
	 * Creates a new GUI instance.
	 *
	 * @param anchor the specified anchor
	 * @param horizontalOffset the overlay's horizontal offset
	 * @param verticalOffset the overlay's vertical offset
	 * @return a new GUI instance
	 */
	@NotNull
	@Contract(pure = true)
	public IodineOverlayImpl createOverlay(@NotNull IodineOverlay.Anchor anchor, int horizontalOffset, int verticalOffset) {
		plugin.log(Level.OFF, "GuiManager > new overlay {0}", nextGuiId);
		return new IodineOverlayImpl(plugin, nextGuiId++, anchor, horizontalOffset, verticalOffset);
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
	public GuiBaseImpl<?> getGui(int id) {
		return guiMap.get(id);
	}
	
	/**
	 * Creates a new {@link GuiElementImpl} instance based on the specified {@link Class}.
	 * Should only be called by {@link GuiBaseImpl} subclasses.
	 *
	 * @param type the API class of the element to create
	 * @param gui the GUI which will contain this element
	 * @param internalId the internal ID of the element, specified by the GUI
	 * @param id the API-friendly ID of the element
	 * @param <T> the type of the element
	 * @return the created element instance
	 */
	@NotNull
	@Contract(pure = true)
	public <T extends GuiElement<T>> GuiElementImpl<T> createElement(@NotNull Class<T> type,
			@NotNull GuiBaseImpl<?> gui, int internalId, @NotNull Object id) {
		//noinspection unchecked
		return (GuiElementImpl<T>) elements.get(type).apply(gui, internalId, id);
	}
	
	
	
	/**
	 * Removes the specified GUI from the internal cache,
	 * allowing it to be garbage collected.
	 * Should only be called by {@link GuiBaseImpl} subclasses.
	 *
	 * @param gui the GUI to unregister
	 */
	public void forgetGui(@NotNull GuiBaseImpl<?> gui) {
		plugin.log(Level.OFF, "GuiManager > forgetting {0}", gui.getId());
		Validate.notNull(guiMap.remove(gui.getId()), "Can only forget registered GUIs");
	}
	
	/**
	 * Adds the specified GUI to the internal cache,
	 * disallowing it from being garbage collected.
	 * Should only be called by {@link GuiBaseImpl} subclasses.
	 *
	 * @param gui the GUI to register
	 */
	public void rememberGui(@NotNull GuiBaseImpl<?> gui) {
		plugin.log(Level.OFF, "GuiManager > remembering {0}", gui.getId());
		Validate.isTrue(guiMap.put(gui.getId(), gui) == null, "Can't remember registered GUIs");
	}
	
	/**
	 * Closes all GUIs for all of their viewers.
	 */
	public void closeAllGuiInstances() {
		for (GuiBaseImpl<?> gui : new ArrayList<>(guiMap.values())) {
			for (Player player : gui.getViewers()) {
				gui.closeFor(player);
			}
		}
	}
	
	
	
	private <T extends GuiElement<T>> void element(Class<T> clazz, ElementConstructor<T> constructor) {
		Validate.isTrue(elements.put(clazz, constructor) == null,
				"Only one constructor can be mapped to a type");
	}
	
	@FunctionalInterface
	private interface ElementConstructor<R extends GuiElement<R>> {
		R apply(GuiBaseImpl<?> gui, int internalId, Object id);
	}
}
