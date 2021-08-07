package org.example;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class SecondTest {

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
                (String.format(fillFields, "fullName"))), "Иванов Иван Иванович");

        //Пол
        String sexXPath = "//input[@name='gender']/..";
        WebElement sexBtn = driver.findElement(By.xpath(sexXPath));
        sexBtn.click();

        //дата рождения
        fillDateField(driver.findElement(By.xpath
                (String.format(fillFields, "birthDate"))), "01011990");

        //Место рождения
        fillInputField(driver.findElement(By.xpath
                (String.format(fillFields, "birthPlace"))), "Москва");


        //Гражданство
            String nationalityXPath = "//span[contains(@class,'Switcherstyles__Jackdaw-sc-1lsh6gn-2')]";
        WebElement nationalityBtn = driver.findElement(By.xpath(nationalityXPath));
        nationalityBtn.click();

        //Страна гражданства
        String countryXPath = "//div[contains(text(),'Страна гражданства')]/..//span[contains(text(),'Выберите вариант')]";
        WebElement countryField = driver.findElement(By.xpath(countryXPath));
        countryField.click();
        countryField = driver.findElement(By.xpath("//div[contains(text(), 'Германия')]"));
        countryField.click();


        // серия паспорта
        fillInputField(driver.findElement(By.xpath
                (String.format(fillFields, "foreignSeries"))), "111");

        //номер паспорта
        fillInputField(driver.findElement(By.xpath
                (String.format(fillFields, "foreignNumber"))), "1111111");

        //дата выдачи
        fillDateField(driver.findElement(By.xpath
                (String.format(fillFields, "foreignIssuedDate"))), "01012020");


        //кем выдан
        fillInputField(driver.findElement(By.xpath
                (String.format(fillFields, "foreignIssuedBy"))), "111");

        //адрес регистрации на территории РФ
        fillInputField(driver.findElement(By.xpath
                (String.format(fillFields, "registrationAddress"))), "г Москва, Ломоносовский пр-кт, д 27Д");

        //телефон
        fillPhoneField(driver.findElement(By.xpath
                (String.format(fillFields, "phone"))), "(909) 999-99-99");


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
