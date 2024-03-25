package org.example;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.By;
import java.time.Duration;

public class ResultPageTest {
    public String baseurl="http://localhost:3000/";
    public WebDriver driver;
    String driverpath="/Users/mchang/bin/chromedriver";
    String expectedPageTitleText = "BC Wildfires";
    int count = 2261; //Should call API and calculated dynamically
    By pageTitle = By.xpath("//h2");
    By pageSizeSelect = By.id("pageSizeSelect");
    By buttons = By.xpath("//button");
    By geoDescSelect = By.id("geoDescSelect");
    By geoDescInput = By.id("geoDescInput");
    By fireCauseSelect = By.id("fireCauseSelect");
    By fireCauseInput = By.id("fireCauseInput");
    By fireStatusSelect = By.id("fireStatusSelect");
    By fireStatusInput = By.id("fireStatusInput");
    By lastRecordObjectIDOnDefaultFirstPage = By.xpath("//table//tbody/tr[100]/td[21]");

    @BeforeTest
    public void setupDriver()
    {
        System.setProperty("webdriver.chrome.driver", driverpath);
        driver=new ChromeDriver();
    }
    @BeforeMethod
    public void setupTest()
    {
        driver.get(baseurl);
        //Wait for the results to be loaded on the initial page.
        waitUntilElementPresent(lastRecordObjectIDOnDefaultFirstPage);
    }

    @Test(description = "Verify that the page displays the expected title.")
    public void verifyPageTitle()
    {
        String actualPageTitleText = driver.findElement(pageTitle).getText();
        Assert.assertEquals(actualPageTitleText, expectedPageTitleText);
    }

    @Test (description = "Choose different page sizes while keeping the default filters, " +
            "navigate to both the initial and final pages, " +
            "and confirm the presence of ObjectID values for the first and last records.",
            dataProvider = "pageSize-data-provider")
    public void verifyPageSizeSelection(String value)
    {
        int pageSize = 100;
        try {
            pageSize = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("Invalid data");
        }
        selectOptionByText(pageSizeSelect, String.valueOf(pageSize));

        int expectedTotalPages = count % pageSize > 0 ? count / pageSize + 1 : count / pageSize;
//        int actualTotalPages = driver.findElements(buttons).size();
//        Assert.assertEquals(actualTotalPages,expectedTotalPages);

        By firstPageButton = By.xpath("//button[1]");
        clickElement(firstPageButton);
        By firstRecordObjectID = By.xpath("//table//tbody/tr[1]/td[21]");
        waitUntilElementAvailable(firstRecordObjectID);
        Assert.assertNotNull(driver.findElement(firstRecordObjectID), "ObjectID should exist and not be null");

        String lastPageXPath = "//button[index]".replace("index", String.valueOf(expectedTotalPages));
        By lastPageButton = By.xpath(lastPageXPath);
        clickElement(lastPageButton);
        int lastRecordIndexOnLastPage = count % pageSize;
        String lastRecordObjectIDXPath = "//table//tbody/tr[index]/td[21]".replace("index", String.valueOf(lastRecordIndexOnLastPage));
        By lastRecordObjectID = By.xpath(lastRecordObjectIDXPath);
        waitUntilElementAvailable(lastRecordObjectID);
        Assert.assertNotNull(driver.findElement(lastRecordObjectID), "ObjectID should exist and not be null");
    }

    @Test (description = "Choose different filters, " +
            "and verify that the results align with the expected outcomes " +
            "corresponding to the selected filters.",
            dataProvider = "geographic-description-data-provider")
    public void verifyFilters(String value)
    {
        selectOptionByText(geoDescSelect, "=");
        fillInputField(geoDescInput, value);

        selectOptionByText(fireCauseSelect, "!=");
        fillInputField(fireCauseInput, "Person");

        selectOptionByText(fireStatusSelect, "=");
        fillInputField(fireStatusInput, "Out");

        selectOptionByText(pageSizeSelect, "200");

        By trs = By.xpath("//table//tbody/tr");
        int recordsCountOnPage = driver.findElements(trs).size();
        for(int i = 1; i<=recordsCountOnPage; i++){
            String currentTr = String.valueOf(i);
            String fireStatusXPath = "//table//tbody/tr[index]/td[8]".replace("index", currentTr);
            By fireStatus = By.xpath(fireStatusXPath);
            String actualFireStatusText = driver.findElement(fireStatus).getText();
            Assert.assertEquals(actualFireStatusText,"Out", "FireStatus should be Out");

            String fireCauseXPath = "//table//tbody/tr[index]/td[9]".replace("index", currentTr);
            By fireCause = By.xpath(fireCauseXPath);
            String actualFireCauseText = driver.findElement(fireCause).getText();
            Assert.assertNotEquals(actualFireCauseText,"Person", "FireCause should not be Person");

            String fireGeoDescXPath = "//table//tbody/tr[index]/td[15]".replace("index", currentTr);
            By fireGeoDesc = By.xpath(fireGeoDescXPath);
            String actualFireGeoDescText = driver.findElement(fireGeoDesc).getText();
            Assert.assertEquals(actualFireGeoDescText,value, "Geographic Description should be " + value);
        }

    }

    @AfterTest
    public void closeBrowser()
    {
        driver.close();
    }

    @DataProvider(name = "geographic-description-data-provider")
    public Object[][] dpMethodGeographicDescription(){
        return new Object[][] {{"Goose Creek"}};
    }

    @DataProvider(name = "pageSize-data-provider")
    public Object[][] dpMethod(){
        return new Object[][] {{"100"}, {"200"}, {"300"}, {"400"}, {"500"}, {"1000"}};
    }

    public void waitUntilElementAvailable(By by){
        WebDriverWait  wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        //WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));
        wait.until(ExpectedConditions.elementToBeClickable(by));

    }

    public void waitUntilElementPresent(By by){
        WebDriverWait  wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public void clickElement(By by){
       waitUntilElementAvailable(by);
       driver.findElement(by).click();
    }

    public void selectOptionByText(By by, String value){
        waitUntilElementAvailable(by);
        WebElement dropdown = driver.findElement(by);
        Select selectElement = new Select(dropdown);
        selectElement.selectByVisibleText(value);
    }

    public void fillInputField(By by, String text){
        waitUntilElementAvailable(by);
        WebElement inputField = driver.findElement(by);
        inputField.clear();
        inputField.sendKeys(text);
    }

}
