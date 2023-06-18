package decoration;

import org.openqa.selenium.By;

public class LogElement extends ElementDecoration {
    protected LogElement(Element element) {
        super(element);
    }

    @Override
    public By getBy() {
        return element.getBy();
    }

    @Override
    public String getText() {
        System.out.println(String.format("decoration.Element Text = %s", element.getText()));
        return element.getText();
    }

    @Override
    public Boolean isEnable() {
        System.out.println(String.format("decoration.Element Enable = %b", element.isEnable()));
        return element.isEnable();
    }

    @Override
    public Boolean isDisplayed() {
        System.out.println(String.format("decoration.Element Displayed = %b", element.isDisplayed()));
        return element.isDisplayed();
    }

    @Override
    public void typeText(String text) {
        System.out.println(String.format("Type text = = %s", text));
        element.typeText(text);
    }

    @Override
    public void click() {
        System.out.println("decoration.Element Clicked");
        element.click();
    }

    @Override
    public String getAttribute(String attribute) {
        System.out.println("decoration.Element Clicked");
        return element.getAttribute(attribute);
    }
}
