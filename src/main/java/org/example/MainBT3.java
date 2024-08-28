package org.example;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class MainBT3 {

  private static final Duration WAIT_DURATION = Duration.ofSeconds(10);
  private static final String BASE_URL = "https://www.demoblaze.com/index.html";
  private static final String USERNAME_PREFIX = "ZTee";
  private static final String PASSWORD = "password123";
  private static final String DEFAULT_NAME = "Lã Liếu";
  private static final String DEFAULT_COUNTRY = "Nam Cực";
  private static final String DEFAULT_CITY = "Thành phố Băng giá";
  private static final String DEFAULT_CARD = "1234567890123456";
  private static final String DEFAULT_MONTH = "12";
  private static final String DEFAULT_YEAR = "2025";

  public static void main(String[] args) {
    WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver, WAIT_DURATION);

    try {
      driver.get(BASE_URL);
      driver.manage().window().maximize();

      WebElement btnSignUp = wait.until(ExpectedConditions.elementToBeClickable(By.id("signin2")));
      btnSignUp.click();
      String username = generateUsername();
      signUp(driver, wait, username);
      login(driver, wait, username);
      selectAndPurchaseLaptop(driver, wait);
      verifyPurchase(wait);
    } finally {
      driver.quit();
    }
  }

  private static String generateUsername() {
    return USERNAME_PREFIX + new Random().nextInt(100);
  }

  private static boolean isUsernameTaken(WebDriverWait wait) {
    try {
      Alert alert = wait.until(ExpectedConditions.alertIsPresent());
      String alertText = alert.getText();
      boolean isTanked = alertText.contains("This user already exist.");
      alert.accept();
      return isTanked;

    } catch (Exception e) {
      return false;
    }
  }

  private static void signUp(WebDriver driver, WebDriverWait wait, String username) {

    WebElement txtSignUpUsername = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-username")));
    txtSignUpUsername.clear();
    txtSignUpUsername.sendKeys(username);

    WebElement txtSignUpPassword = driver.findElement(By.id("sign-password"));
    txtSignUpPassword.clear();
    txtSignUpPassword.sendKeys(PASSWORD);

    WebElement btnSignUpSubmit = driver.findElement(By.xpath("//button[contains(text(),'Sign up')]"));
    btnSignUpSubmit.click();


    if (isUsernameTaken(wait)) {
      System.out.println("Tên đăng nhập " + username + " đã tồn tại. Thử lại với tên khác.");
      signUp(driver, wait, generateUsername());
    } else {
      System.out.println("Đăng ký thành công.");
      System.out.println("Tên đăng nhập: " + username);
      System.out.println("Mật khẩu: " + PASSWORD);
    }
  }

  private static void login(WebDriver driver, WebDriverWait wait, String username) {


    WebElement btnLogin = wait.until(ExpectedConditions.elementToBeClickable(By.id("login2")));
    btnLogin.click();

    WebElement txtLoginUsername = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername")));
    txtLoginUsername.sendKeys(username);

    WebElement txtLoginPassword = driver.findElement(By.id("loginpassword"));
    txtLoginPassword.sendKeys(PASSWORD);

    WebElement btnLoginSubmit = driver.findElement(By.xpath("//button[contains(text(),'Log in')]"));
    btnLoginSubmit.click();

    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameofuser")));
    System.out.println("-----------------------");
    System.out.println("Đăng nhập thành công.");
    System.out.println("Tên đăng nhập: " + username);
  }

  private static void selectAndPurchaseLaptop(WebDriver driver, WebDriverWait wait) {
    WebElement lnkHome = driver.findElement(By.xpath("//a[contains(text(),'Home')]"));
    lnkHome.click();
    WebElement laptopsCategoryLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@onclick=\"byCat('notebook')\"]")));
    laptopsCategoryLink.click();
    List<WebElement> laptopItems = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@id='tbodyid']//a[contains(@class,'hrefch')]")));
    if (!laptopItems.isEmpty()) {
      laptopItems.getFirst().click();
    } else {
      System.out.println("Không tìm thấy sản phẩm nào trong danh mục Laptops.");
      driver.quit();
    }
    WebElement btnAddToCart = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Add to cart')]")));
    btnAddToCart.click();
    wait.until(ExpectedConditions.alertIsPresent()).accept();

    WebElement lnkCart = driver.findElement(By.id("cartur"));
    lnkCart.click();

    WebElement btnPlaceOrder = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Place Order')]")));
    btnPlaceOrder.click();

    fillOrderForm(driver, wait);
  }

  private static void fillOrderForm(WebDriver driver, WebDriverWait wait) {
    WebElement txtName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
    txtName.sendKeys(DEFAULT_NAME);

    WebElement txtCountry = driver.findElement(By.id("country"));
    txtCountry.sendKeys(DEFAULT_COUNTRY);

    WebElement txtCity = driver.findElement(By.id("city"));
    txtCity.sendKeys(DEFAULT_CITY);

    WebElement txtCard = driver.findElement(By.id("card"));
    txtCard.sendKeys(DEFAULT_CARD);

    WebElement txtMonth = driver.findElement(By.id("month"));
    txtMonth.sendKeys(DEFAULT_MONTH);

    WebElement txtYear = driver.findElement(By.id("year"));
    txtYear.sendKeys(DEFAULT_YEAR);

    WebElement btnPurchase = driver.findElement(By.xpath("//button[contains(text(),'Purchase')]"));
    btnPurchase.click();

  }

  private static void verifyPurchase(WebDriverWait wait) {
    WebElement lblSuccess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='sweet-alert  showSweetAlert visible']")));
    String successText = lblSuccess.getText();
    if (successText.contains(DEFAULT_NAME) && successText.contains(DEFAULT_CARD)) {

      String[] lines = successText.split("\n");
      System.out.println("-------------------------");
      for (String line : lines) {
        if (line.contains("Id:")) {
          System.out.println("Mã đơn hàng: " + line.split(":")[1].trim());
        }
      }
      System.out.println("Tên người nhận hàng: " + DEFAULT_NAME);
      System.out.println("Số thẻ: " + DEFAULT_CARD);
      System.out.println("Địa chỉ nhận hàng: " + DEFAULT_CITY + ", " + DEFAULT_COUNTRY);
      System.out.println("Đặt hàng thành công và khớp dữ liệu truyền vào.");

    } else {
      System.out.println("Đặt hàng không thành công hoặc thông tin không khớp.");
    }
  }
}
