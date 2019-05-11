package hu.trigary.iodine.bukkit.api.gui.element;

import com.google.common.base.Charsets;
import hu.trigary.iodine.api.gui.element.TextGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.api.gui.IodineGuiImpl;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The implementation of {@link TextGuiElement}.
 */
public class TextGuiElementImpl extends GuiElementImpl implements TextGuiElement {
	private String text = "";
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param type the type of this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public TextGuiElementImpl(@NotNull IodineGuiImpl gui,
			@NotNull GuiElementType type, int internalId, @NotNull Object id) {
		super(gui, type, internalId, id);
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String getText() {
		return text;
	}
	
	@NotNull
	@Override
	public TextGuiElement setText(@NotNull String text) {
		Validate.notNull(text, "Text must be non-null");
		this.text = text;
		gui.update();
		return this;
	}
	
	
	
	@Override
	public void serialize(@NotNull ByteBuffer buffer) {
		super.serialize(buffer);
		buffer.put(text.getBytes(Charsets.UTF_8));
	}
}