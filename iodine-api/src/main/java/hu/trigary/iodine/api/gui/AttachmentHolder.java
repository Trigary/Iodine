package hu.trigary.iodine.api.gui;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Classes can implement this interface in order to allow {@link Object}s to be attached to its instances.
 * These {@link Object}s are never serialized or interacted with in any way,
 * they are simply stored in memory so that they can be retrieved when needed.
 */
public interface AttachmentHolder {
	/**
	 * Sets the attached value.
	 * This is a quality-of-life method that allows attaching any data to this class instance.
	 *
	 * @param attachment the new attached value
	 */
	void setAttachment(@Nullable Object attachment);
	
	/**
	 * Gets the attached value.
	 * This is a quality-of-life method that allows attaching any data to this class instance.
	 *
	 * @return the currently attached value
	 */
	@Nullable
	@Contract(pure = true)
	Object getAttachment();
}
