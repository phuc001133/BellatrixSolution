package decoration;

import org.openqa.selenium.By;

public abstract class Element {
    public abstract By getBy();
    public abstract String getText();
    public abstract Boolean isEnable();
    public abstract Boolean isDisplayed();
    public abstract void typeText(String text);
    public abstract void click();
    public abstract String getAttribute(String attribute);
}
