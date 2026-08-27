package web.modal.selectitemasagift;

import org.openqa.selenium.By;
import web.core.BaseMap;

public class Map extends BaseMap {

    public By giftOptionModalBy = By.xpath("//div[contains(@class,' a-popover-modal')]");

    public By giftOptionModalBackdropBy = By.xpath("//div[contains(@class,'a-popover-backdrop')]");

    public By giftOptionModalCheckBoxBy = By.xpath("//div[contains(@class,'a-popover-modal') and @aria-hidden='false']//input[@type='checkbox']");
}
