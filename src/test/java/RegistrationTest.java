import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationTest {

    private WebDriver driver;

    @BeforeAll
    public static void SetUpCLass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setupTest() {
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--disable-dev-shm-usage");
        //options.addArguments("--no-sandbox");
        //options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void teardown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldSendForm() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Сергей Стрельников");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+71231525664");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String success = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", success);
    }

    @Test
    public void shouldShowErrorWhenNameIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+71231525664");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String error = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim();
        assertEquals("Поле обязательно для заполнения", error);
    }

    @Test
    public void shouldShowErrorWhenNameIsIncorrect() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Strelnikov Sergey");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+71231525664");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String error = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", error);
    }

    @Test
    public void shouldShowErrorWhenPhoneIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Сергей Стрельников");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String error = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();
        assertEquals("Поле обязательно для заполнения", error);
    }

    @Test
    public void shouldShowErrorWhenPhoneIsIncorrect() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Сергей Стрельников");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("asgah");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String error = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", error);
    }

    @Test
    public void shouldShowErrorWhenCheckboxInactive() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Сергей Стрельников");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79855456336");
        driver.findElement(By.cssSelector("button")).click();
        assertTrue(driver.findElement(By.cssSelector(".input_invalid>.checkbox__box")).isDisplayed());
    }

}