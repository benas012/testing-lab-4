package lab_4;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class lab_4Test {
    
    public static ChromeDriver driver;
    private static final String email = "TestUser" + generateRandomString(10) + "@gmail.com";
    private static final String password = "adminAdmin";
    
    @BeforeClass
    public static void beforeClass() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\benas\\OneDrive\\Desktop\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://demowebshop.tricentis.com/");
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        wait.pollingEvery(Duration.ofMillis(500));
        driver.findElement(By.xpath("//*[@class='ico-login']")).click();
        driver.findElement(By.xpath("//input[@value='Register']")).click();
        driver.findElement(By.xpath("//input[@value='M']")).click();
        driver.findElement(By.id("FirstName")).sendKeys("Bob");
        driver.findElement(By.id("LastName")).sendKeys("Jenkins");
        driver.findElement(By.id("Email")).sendKeys(email);
        driver.findElement(By.id("Password")).sendKeys(password);
        driver.findElement(By.id("ConfirmPassword")).sendKeys(password);
        driver.findElement(By.xpath("//input[@value='Register']")).click();
        driver.findElement(By.xpath("//input[@value='Continue']")).click();
        driver.close();
        
    }
    
    @Before
    public void before() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    
    @After
    public void after() {
        driver.close();
    }
    
    @AfterClass
    public static void afterClass() {
        driver.quit();
    }
    
    @Test
    public void test1() throws InterruptedException {
        driver.get("https://demowebshop.tricentis.com/");
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        wait.pollingEvery(Duration.ofMillis(500));
        String value = logic(driver, "data1.txt", wait, true);
        Assert.assertEquals(value, "Your order has been successfully processed!");
    }
    
    @Test
    public void test2() throws InterruptedException {
        driver.get("https://demowebshop.tricentis.com/");
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        wait.pollingEvery(Duration.ofMillis(1000));
        String value = logic(driver, "data2.txt", wait, false);
        Assert.assertEquals(value, "Your order has been successfully processed!");
    }
    
    
    
    //Utility functions
    public String logic(ChromeDriver driver, String productsFileName, WebDriverWait wait, Boolean fillInBillingDetails) {
    	driver.findElement(By.xpath("//*[@class='ico-login']")).click();
        driver.findElement(By.id("Email")).sendKeys(email);
        driver.findElement(By.id("Password")).sendKeys(password);
        driver.findElement(By.xpath("//input[@value='Log in']")).click();
        driver.findElement(By.xpath("//ul[@class='top-menu']//a[@href='/digital-downloads']")).click();
        ArrayList<String> products = ReadFile(productsFileName);
        for (String product : products) {
            driver.findElement(By.xpath("//*[contains(text(),\""+product+"\")]/ancestor::div[contains(@class, 'details')]//input[@value='Add to cart']")).click();
            wait.until(ExpectedConditions.attributeToBe(By.className("ajax-loading-block-window"), "style", "display: none;"));
        }
        driver.findElement(By.xpath("//span[text()='Shopping cart']")).click();
        driver.findElement(By.id("termsofservice")).click();
        driver.findElement(By.id("checkout")).click();
        if(fillInBillingDetails) {
        	driver.findElement(By.id("BillingNewAddress_CountryId")).click();
            driver.findElement(By.xpath("//option[contains(text(),'United Kingdom')]")).click();
            driver.findElement(By.id("BillingNewAddress_City")).sendKeys("London");
            driver.findElement(By.id("BillingNewAddress_Address1")).sendKeys("Baker Street 221B");
            driver.findElement(By.id("BillingNewAddress_ZipPostalCode")).sendKeys("123");
            driver.findElement(By.id("BillingNewAddress_PhoneNumber")).sendKeys("1234567890");
        }
        driver.findElement(By.xpath("//*[@title='Continue']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@class='button-1 payment-method-next-step-button']")));
        driver.findElement(By.xpath("//input[@class='button-1 payment-method-next-step-button']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@class='button-1 payment-info-next-step-button']")));
        driver.findElement(By.xpath("//input[@class='button-1 payment-info-next-step-button']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@class='button-1 confirm-order-next-step-button']")));
        driver.findElement(By.xpath("//input[@class='button-1 confirm-order-next-step-button']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//strong[contains(text(),'Your order has been successfully processed!')]")));
        WebElement confirmation = driver.findElement(By.xpath("//strong[contains(text(),'Your order has been successfully processed!')]"));
        return confirmation.getText();
    }
    
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(index));
        }
        
        return stringBuilder.toString();
    }
    
    public ArrayList<String> ReadFile(String filename) {
        ArrayList<String> ar = new ArrayList<String>();
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                ar.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return ar;
    }
}
