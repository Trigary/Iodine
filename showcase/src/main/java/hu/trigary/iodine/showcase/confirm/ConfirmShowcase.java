package hu.trigary.iodine.showcase.confirm;

import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.api.gui.GuiElements;
import hu.trigary.iodine.api.gui.IodineRoot;
import hu.trigary.iodine.showcase.IodineShowcase;
import hu.trigary.iodine.showcase.Showcase;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ConfirmShowcase extends Showcase {
	public ConfirmShowcase(@NotNull IodineShowcase plugin) {
		super(plugin);
	}
	
	
	
	@Override
	public void onCommand(@NotNull Player player) {
		openConfirmWindow(player, "Do you want to proceed?",
				result -> player.sendMessage("Result: " + result));
	}
	
	
	
	private void openConfirmWindow(@NotNull Player player, @NotNull String text, @NotNull Consumer<Boolean> callback) {
		boolean[] handled = new boolean[1];
		IodineApi.get().createGui()
				.onClosed((gui, p, byPlayer) -> {
					if (!handled[0]) { //our GUI might get closed by some poorly written plugin :(
						callback.accept(false);
					}
				})
				.addElement(GuiElements.CONTAINER_LINEAR, container -> {
					IodineRoot<?> root = container.getRoot();
					container.setOrientation(true);
					
					root.addElement(GuiElements.TEXT, e -> container.makeChildLast(e)
							.setWidth(165)
							.setPaddingBottom(5)
							.setText(text));
					
					root.addElement(GuiElements.CONTAINER_LINEAR, c -> {
						container.makeChildLast(c);
						
						root.addElement(GuiElements.BUTTON, e -> c.makeChildLast(e)
								.setWidth(75)
								.setPaddingRight(15)
								.setText("Confirm")
								.onClicked((ee, p) -> {
									callback.accept(true);
									handled[0] = true;
									p.closeOpenGui();
								}));
						
						root.addElement(GuiElements.BUTTON, e -> c.makeChildLast(e)
								.setWidth(75)
								.setText("Cancel")
								.onClicked((ee, p) -> {
									callback.accept(false);
									handled[0] = true;
									p.closeOpenGui();
								}));
					});
				})
				.openFor(IodineApi.get().getPlayer(player.getUniqueId()));
	}
}
