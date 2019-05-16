package hu.trigary.iodine.bukkit.gui.element;

import hu.trigary.iodine.api.gui.element.DropdownGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.IodineGuiImpl;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The implementation of {@link DropdownGuiElement}.
 */
public class DropdownGuiElementImpl extends GuiElementImpl<DropdownGuiElement> implements DropdownGuiElement {
	private boolean editable = true;
	private List<String> choices;
	private String selected; //TODO serialize based on index
	private ChosenAction chosenAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param type the type of this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public DropdownGuiElementImpl(@NotNull IodineGuiImpl gui,
			@NotNull GuiElementType type, int internalId, @NotNull Object id) {
		super(gui, type, internalId, id);
	}
	
	
	
	@Contract(pure = true)
	@Override
	public boolean isEditable() {
		return editable;
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public List<String> getChoices() {
		return Collections.unmodifiableList(choices);
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String getSelected() {
		return selected;
	}
	
	
	
	@NotNull
	@Override
	public DropdownGuiElementImpl setEditable(boolean editable) {
		this.editable = editable;
		gui.update();
		return this;
	}
	
	@NotNull
	@Override
	public DropdownGuiElementImpl setChoices(@NotNull Collection<String> choices) {
		Validate.isTrue(!choices.isEmpty(), "The count of choices must be at least 1");
		this.choices = new ArrayList<>(choices);
		selected = this.choices.get(0);
		gui.update();
		return this;
	}
	
	@NotNull
	@Override
	public DropdownGuiElementImpl setSelected(@NotNull String value) {
		int index = choices.indexOf(value);
		Validate.isTrue(index != -1, "The selected value must be among the choices");
		selected = value;
		gui.update();
		return this;
	}
	
	@NotNull
	@Override
	public DropdownGuiElementImpl onChosen(@Nullable ChosenAction action) {
		chosenAction = action;
		return this;
	}
}