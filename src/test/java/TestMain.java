import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;

public class TestMain {

  private static final String LOGIN_URL = "https://dev.payos.vn/login";
  private static final String HOME_URL = "https://dev.payos.vn/";
  private static final int SLEEP_TIME_MS = 2000;
  // Inner class for storing locators
  public static class Locators {
    // login page
    public static final By txtEmailLocator = By.xpath("//input[@name='email']");
    public static final By txtPasswordLocator = By.xpath("//input[@name='password']");
    public static final By btnLoginLocator = By.xpath("//button[@type='submit']");
    // dashboard page
    public static final By btnTenant = By.xpath("//h1/following-sibling::div/div");
    public static final By btnPaymentChannel = By.xpath("//*[local-name()='svg' and @data-testid='AccountBalanceWalletIcon']");
    public static final By btnAddPaymentChannel = By.xpath("//button/*[local-name()='svg' and @data-testid='AddIcon']");
    public static final By txtNamePaymentChannel = By.xpath("//input[@type='text']");
    public static final By txtImage = By.xpath("//input[@type='file']");
    public static final By btnContinue = By.xpath("//button[contains(text(), 'Tiếp tục')]");
    public static final By btnOnSuccess = By.xpath("//span[contains(@class, 'Mui-completed')]");
  }

  // Static login method
  public static void login(WebDriver driver, String email, String password) {
    driver.findElement(Locators.txtEmailLocator).sendKeys(email);
    driver.findElement(Locators.txtPasswordLocator).sendKeys(password);
    driver.findElement(Locators.btnLoginLocator).click();
  }

  // Static method to perform actions
  /**
   * Performs a series of actions on the web application using the provided WebDriver.
   *
   * @param driver the WebDriver instance to interact with the web application
   */
  private static void performActions(WebDriver driver) {
    // Click on the tenant button
    driver.findElement(Locators.btnTenant).click();
    waitFor();

    // Click on the payment channel button
    driver.findElement(Locators.btnPaymentChannel).click();
    waitFor();

    // Click on the add payment channel button
    driver.findElement(Locators.btnAddPaymentChannel).click();
    waitFor();

    // Enter the name for the payment channel
    driver.findElement(Locators.txtNamePaymentChannel).sendKeys("Lá Liễu");

    // Get the absolute path of the image file and upload it
    String filePath = new File("src/test/java/cute.jpg").getAbsolutePath();
    driver.findElement(Locators.txtImage).sendKeys(filePath);

    // Click on the continue button
    driver.findElement(Locators.btnContinue).click();
    waitFor();

    // Check if the success indicator is displayed and print the result
    if (driver.findElement(Locators.btnOnSuccess).isDisplayed()) {
      System.out.println("Action successful");
    } else {
      System.out.println("Action failed");
    }
  }

  private static void waitFor() {
    try {
      Thread.sleep(SLEEP_TIME_MS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    WebDriver driver = new ChromeDriver();
    try {
      driver.get(LOGIN_URL);
      login(driver, "congvo.2018@gmail.com", "06779516");
      waitFor();
      if (driver.getCurrentUrl().equals(HOME_URL)) {
        System.out.println("Login successful");
      } else {
        System.out.println("Login failed");
        return;
      }
      performActions(driver);
    } finally {
      driver.quit();
    }
  }


}
