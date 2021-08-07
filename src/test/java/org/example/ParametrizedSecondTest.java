package org.example;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@RunWith(Parameterized.class)
public class ParametrizedSecondTest {

    @Parameterized.Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][]{
                {"Иванов Иван Иванович", "01011990", "Москва", "Германия", "111", "1111111", "01012020", "111",
                        "г Москва, Ломоносовский пр-кт, д 27Д", "(909) 999-99-99"},

                {"Антонов Антон Антонович", "02021992", "Москва", "Болгария", "222", "2222222", "02022020", "222",
                        "г Санкт-Петербург, пр-кт Королёва, д 2", "(909) 777-77-77"},

                {"Михайлов Михаил Михайлович", "03031993", "Москва", "Оман", "333", "3333333", "03032020", "333",
                        "г Москва, пр-кт Вернадского, д 2", "(909) 555-55-55"}

        });
    }

    @Parameterized.Parameter(0)
    public String fullName;

    @Parameterized.Parameter(1)
    public String birthDate;

    @Parameterized.Parameter(2)
    public String birthPlace;

    @Parameterized.Parameter(3)
    public String nationalityCountry;

    @Parameterized.Parameter(4)
    public String series;

    @Parameterized.Parameter(5)
    public String passport;

    @Parameterized.Parameter(6)
    public String issuedDate;

    @Parameterized.Parameter(7)
    public String issuedBy;

    @Parameterized.Parameter(8)
    public String registration;

    @Parameterized.Parameter(9)
    public String phoneNumber;



    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void before() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        wait = new WebDriverWait(driver, 10, 1000);

        String baseUrl = "https://www.raiffeisen.ru";
        driver.get(baseUrl);
    }

    @Test
    public void test() {

        //Отметить город Москва
        String menuBtnXPath = "//*[@class='informer__btn js-region-check-yes']";
        WebElement menuBtn = driver.findElement(By.xpath(menuBtnXPath));
        menuBtn.click();

        //Перейти на вкладку "Ипотека"
        String companiesBtnXPath = "//a[contains(text(),'Ипотека')]";
        WebElement companiesBtn = driver.findElement(By.xpath(companiesBtnXPath));
        companiesBtn.click();


        //Перейти на "Рефинансирование"
        String wellnessInsuranceXPath = "//div[@id='menu2']//a[contains(text(),'Рефинансирование')]";
        WebElement wellnessInsuranceBtn = driver.findElement(By.xpath(wellnessInsuranceXPath));
        wellnessInsuranceBtn.click();
        Assert.assertEquals("Заголовок не соответствует",
                "Рефинансирование ипотеки и других кредитов", driver.findElement(By.xpath("//h1")).getText());

        //Оставить заявку
        String healthInsuranceXPath = "//div[@class='b-intro__block-buttons']/a[contains(text(),'Оставить заявку')]";
        WebElement healthInsuranceBtn = driver.findElement(By.xpath(healthInsuranceXPath));
        scrollToElement(healthInsuranceBtn);
        healthInsuranceBtn.click();

        // ЗАПОЛНЕНИЕ ПОЛЕЙ
        String fillFields = "//input[@name='%s']";
        //ФИО
        fillInputField(driver.findElement(By.xpath
                (String.format(fillFields, "fullName"))), fullName);

        //Пол
        String sexXPath = "//input[@name='gender']/..";
        WebElement sexBtn = driver.findElement(By.xpath(sexXPath));
        sexBtn.click();

        //дата рождения
        fillDateField(driver.findElement(By.xpath
                (String.format(fillFields, "birthDate"))), birthDate);

        //Место рождения
        fillInputField(driver.findElement(By.xpath
                (String.format(fillFields, "birthPlace"))), birthPlace);


        //Гражданство
        String nationalityXPath = "//span[contains(@class,'Switcherstyles__Jackdaw-sc-1lsh6gn-2')]";
        WebElement nationalityBtn = driver.findElement(By.xpath(nationalityXPath));
        nationalityBtn.click();

        //Страна гражданства
        String countryXPath = "//div[contains(text(),'Страна гражданства')]/..//span[contains(text(),'Выберите вариант')]";
        WebElement countryField = driver.findElement(By.xpath(countryXPath));
        countryField.click();
        String countryXpath = "//div[contains(text(), '%s')]";
        countryField = driver.findElement(By.xpath(String.format(countryXpath, nationalityCountry)));
        countryField.click();


        // серия паспорта
        fillInputField(driver.findElement(By.xpath
                (String.format(fillFields, "foreignSeries"))), series);

        //номер паспорта
        fillInputField(driver.findElement(By.xpath
                (String.format(fillFields, "foreignNumber"))), passport);

        //дата выдачи
        fillDateField(driver.findElement(By.xpath
                (String.format(fillFields, "foreignIssuedDate"))), issuedDate);


        //кем выдан
        fillInputField(driver.findElement(By.xpath
                (String.format(fillFields, "foreignIssuedBy"))), issuedBy);

        //адрес регистрации на территории РФ
        fillInputField(driver.findElement(By.xpath
                (String.format(fillFields, "registrationAddress"))), registration);

        //телефон
        fillPhoneField(driver.findElement(By.xpath
                (String.format(fillFields, "phone"))), phoneNumber);


    }

    @After
    public void after() {
        driver.quit();
    }


    private void scrollToElement(WebElement element) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private void fillInputField(WebElement element, String value) {
        //scrollToElement(element);
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.perform();

        element.click();
        element.clear();
        element.sendKeys(value);
        //Проверка что поля заполнены введенными значениями
        boolean checkFlag = wait.until(ExpectedConditions.attributeContains(element, "value", value));
        Assert.assertTrue("Поле заполнено некорректно", checkFlag);
    }

    private void fillPhoneField(WebElement element, String value){
        String expectedValue = "+7 " + value;
        //scrollToElement(element);
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.perform();

        element.click();
        //element.clear();
        element.sendKeys(value);
        boolean checkFlag = wait.until(ExpectedConditions.attributeContains(element, "value", expectedValue));
        Assert.assertTrue("Поле заполнено некорректно", checkFlag);

    }

    private void fillDateField(WebElement element, String value)
    {
        String expectedValue = value.substring(0, 2) + "." + value.substring(2, 4) + "." + value.substring(4);
        //scrollToElement(element);
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.perform();

        element.click();
        //element.clear();
        element.sendKeys(value);
        boolean checkFlag = wait.until(ExpectedConditions.attributeContains(element, "value", expectedValue));
        Assert.assertTrue("Поле заполнено некорректно", checkFlag);
    }

}
