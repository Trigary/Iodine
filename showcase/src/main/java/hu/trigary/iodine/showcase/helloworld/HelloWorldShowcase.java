package hu.trigary.iodine.showcase.helloworld;

import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.api.gui.GuiElements;
import hu.trigary.iodine.showcase.IodineShowcase;
import hu.trigary.iodine.showcase.Showcase;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HelloWorldShowcase extends Showcase {
	public HelloWorldShowcase(@NotNull IodineShowcase plugin) {
		super(plugin);
	}
	
	
	
	@Override
	public void onCommand(@NotNull Player player) {
		IodineApi.get().createGui()
				.addElement(GuiElements.TEXT, e -> e.setText("Hello, world!").setWidth(75))
				.addElement(GuiElements.BUTTON, 0, 15, e -> e.setText("Exit").setWidth(75)
						.onClicked((ee, p) -> p.closeOpenGui()))
				.onClosed((gui, p, byPlayer) -> p.sendMessage(
						byPlayer ? "You pressed the escape key" : "You clicked the exit button"))
				.openFor(IodineApi.get().getPlayer(player.getUniqueId()));
	}
}
