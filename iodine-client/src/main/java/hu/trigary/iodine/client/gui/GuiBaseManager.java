package hu.trigary.iodine.client.gui;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.client.IodineModBase;
import hu.trigary.iodine.client.gui.container.GridGuiContainer;
import hu.trigary.iodine.client.gui.container.LinearGuiContainer;
import hu.trigary.iodine.client.gui.container.RootGuiContainer;
import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.element.*;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class GuiBaseManager {
	private final Map<GuiElementType, ElementConstructor<?>> constructors = new EnumMap<>(GuiElementType.class);
	private final Map<Integer, IodineOverlay> openOverlays = new HashMap<>(); //TODO draw these somehow
	private final IodineModBase mod;
	private IodineGui openGui;
	
	public GuiBaseManager(@NotNull IodineModBase mod) {
		this.mod = mod;
		
		constructors.put(GuiElementType.CONTAINER_ROOT, RootGuiContainer::new);
		constructors.put(GuiElementType.CONTAINER_GRID, GridGuiContainer::new);
		constructors.put(GuiElementType.CONTAINER_LINEAR, LinearGuiContainer::new);
		constructors.put(GuiElementType.BUTTON, ButtonGuiElement::new);
		constructors.put(GuiElementType.CHECKBOX, CheckboxGuiElement::new);
		constructors.put(GuiElementType.DROPDOWN, DropdownGuiElement::new);
		constructors.put(GuiElementType.IMAGE, ImageGuiElement::new);
		constructors.put(GuiElementType.PROGRESS_BAR, ProgressBarGuiElement::new);
		constructors.put(GuiElementType.RADIO_BUTTON, RadioButtonGuiElement::new);
		constructors.put(GuiElementType.SLIDER, SliderGuiElement::new);
		constructors.put(GuiElementType.TEXT_FIELD, TextFieldGuiElement::new);
		constructors.put(GuiElementType.TEXT, TextGuiElement::new);
	}
	
	
	
	public void openGui(@NotNull ByteBuffer buffer) {
		IodineGui gui = new IodineGui(mod, buffer.getInt());
		if (openGui != null) {
			closeGuiImpl();
			mod.getLogger().info("Closed GUI to open another: " + openGui.getId());
		}
		
		openGui = gui;
		gui.deserialize(buffer);
		gui.onResolutionChanged();
		//TODO actually open gui
		mod.getLogger().info("Opened GUI: " + gui.getId());
	}
	
	public void openOverlay(@NotNull ByteBuffer buffer) {
		IodineOverlay overlay = new IodineOverlay(mod, buffer.getInt());
		openOverlays.put(overlay.getId(), overlay);
		overlay.deserialize(buffer);
		overlay.onResolutionChanged();
		mod.getLogger().info("Opened overlay: " + overlay.getId());
	}
	
	
	
	public void updateGui(@NotNull ByteBuffer buffer) {
		if (openGui != null) {
			openGui.deserialize(buffer);
			openGui.onResolutionChanged();
			mod.getLogger().info("Updated GUI: " + openGui.getId());
		} else {
			mod.getLogger().info("Can't update GUI: it's no longer opened");
		}
	}
	
	public void updateOverlay(@NotNull ByteBuffer buffer) {
		IodineOverlay overlay = openOverlays.get(buffer.getInt());
		overlay.deserialize(buffer);
		overlay.onResolutionChanged();
		mod.getLogger().info("Updated overlay: " + overlay.getId());
	}
	
	
	
	public void closeGui() {
		if (openGui != null) {
			closeGuiImpl();
			mod.getLogger().info("Closed GUI: " + openGui.getId());
		} else {
			mod.getLogger().info("Can't close GUI: it's no longer opened");
		}
	}
	
	public void closeOverlay(@NotNull ByteBuffer buffer) {
		IodineOverlay overlay = openOverlays.remove(buffer.getInt());
		mod.getLogger().info("Closed overlay: " + overlay.getId());
	}
	
	
	
	public void onGuiClosedByPlayer() {
		closeGuiImpl();
		mod.getLogger().info("Player closed GUI: " + openGui.getId());
	}
	
	private void closeGuiImpl() {
		//TODO close GUI
	}
	
	
	
	@NotNull
	public GuiElement deserializeElement(@NotNull GuiBase gui,
			@NotNull Map<Integer, GuiElement> storage, @NotNull ByteBuffer buffer) {
		GuiElementType type = GuiElementType.fromId(buffer.get());
		if (type == null) {
			throw new RuntimeException("Encountered invalid GuiElementType while deserializing");
		}
		
		int id = buffer.getInt();
		GuiElement element = storage.computeIfAbsent(id, ignored -> constructors.get(type).apply(gui, id));
		element.deserialize(buffer);
		return element;
	}
	
	
	
	@FunctionalInterface
	private interface ElementConstructor<R extends GuiElement> {
		R apply(@NotNull GuiBase gui, int id);
	}
}
