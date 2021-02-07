package hu.trigary.iodine.showcase.minimap;

import hu.trigary.iodine.api.gui.IodineOverlay;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MinimapSettings {
	private IodineOverlay.Anchor anchor;
	
	
	
	@Contract(pure = true)
	public boolean isEnabled() {
		return anchor != null;
	}
	
	@NotNull
	@Contract(pure = true)
	public IodineOverlay.Anchor getAnchor() {
		Validate.isTrue(isEnabled());
		return anchor;
	}
	
	@Nullable
	@Contract(pure = true)
	public IodineOverlay.Anchor getAnchorOrNull() {
		return anchor;
	}
	
	public void setAnchor(@Nullable IodineOverlay.Anchor anchor) {
		this.anchor = anchor;
	}
}
