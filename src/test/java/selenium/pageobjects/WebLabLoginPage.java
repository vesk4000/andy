package selenium.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class WebLabLoginPage extends BasePageObject {

    public WebLabLoginPage(WebDriver driver, String url) {
        super(driver, url);
    }

    @FindBy(xpath = "/html/body/div[2]/div[5]/div[1]/form/fieldset/div[1]/div/input")
    private WebElement username;

    @FindBy(xpath = "/html/body/div[2]/div[5]/div[1]/form/fieldset/div[2]/div/input")
    private WebElement password;

    @FindBy(xpath = "/html/body/div[2]/div[5]/div[1]/form/fieldset/div[3]/div/button[1]")
    private WebElement signInButton;


    public String login(String username, String password) {
        this.username.clear();
        this.password.clear();
        this.username.sendKeys(username);
        this.password.sendKeys(password);

        this.signInButton.click();

        return this.driver.getCurrentUrl();
    }
}
