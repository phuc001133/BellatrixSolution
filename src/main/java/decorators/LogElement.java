package decorators;

import org.openqa.selenium.By;

public class LogElement extends ElementDecorator {
    protected LogElement(Element element) {
        super(element);
    }

    @Override
    public By getBy() {
        return element.getBy();
    }

    @Override
    public String getText() {
        System.out.printf("decoration.Element Text = %s%n", element.getText());
        return element.getText();
    }

    @Override
    public Boolean isEnable() {
        System.out.printf("decoration.Element Enable = %b%n", element.isEnable());
        return element.isEnable();
    }

    @Override
    public Boolean isDisplayed() {
        System.out.printf("decoration.Element Displayed = %b%n", element.isDisplayed());
        return element.isDisplayed();
    }

    @Override
    public void typeText(String text) {
        System.out.printf("Type text = = %s%n", text);
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
