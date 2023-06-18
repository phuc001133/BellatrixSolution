package decoration;

import org.openqa.selenium.By;

public class ElementDecoration extends Element {
    protected final Element element;
    protected ElementDecoration(Element element) {
        this.element = element;
    }

    @Override
    public By getBy() {
        return element.getBy();
    }

    @Override
    public String getText() {
        return element.getText();
    }

    @Override
    public Boolean isEnable() {
        return element.isEnable();
    }

    @Override
    public Boolean isDisplayed() {
        return element.isDisplayed();
    }

    @Override
    public void typeText(String text) {
        element.typeText(text);
    }

    @Override
    public void click() {
        element.click();
    }

    @Override
    public String getAttribute(String attribute) {
        return element.getAttribute(attribute);
    }
}
