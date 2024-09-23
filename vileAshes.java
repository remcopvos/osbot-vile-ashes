package Osbot;

import api.invoking.InvokeHelper;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import api.util.Sleep;

@ScriptManifest(info = "LolMarcus", logo = "", name = "Vile Ashes", author = "", version = 0.0)
public class vileAshes extends Script {

    private InvokeHelper invokeHelper;

    public void onStart() throws InterruptedException {
        super.onStart();
        invokeHelper = new InvokeHelper(this);
    }

    @Override
    public int onLoop() {
        if (getInventory().contains("Vile ashes")) {
            scatterAshes();
        } else {
            if (getBank().isOpen()) {
                if (getBank().contains("Vile ashes")) {
                    getBank().withdrawAll("Vile ashes");
                    Sleep.until(() -> getInventory().contains("Vile ashes"), 5000); 
                    getBank().close();
                    Sleep.until(() -> !getBank().isOpen(), 5000); 
                } else {
                    log("No more Vile ashes left in the bank.");
                    stop(false);
                }
            } else {
                final RS2Object bankBooth = getBank().closest();
                if (bankBooth != null) {
                    if (invokeHelper.invoke(bankBooth, "Bank")) {
                        Sleep.until(() -> getBank().isOpen(), 5000);
                    }
                }
            }
        }

        return 600;
    }

    private void scatterAshes() {
        for (Item item : getInventory().getItems()) {
            if (item != null && item.getName() != null && item.getName().equals("Vile ashes")) {
                if (invokeHelper.invoke(item, "Scatter")) {
                    Sleep.sleep(600);
                }
            }
        }
    }
}
