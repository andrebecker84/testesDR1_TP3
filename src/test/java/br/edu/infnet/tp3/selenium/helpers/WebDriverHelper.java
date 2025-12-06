package br.edu.infnet.tp3.selenium.helpers;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * Classe auxiliar com métodos úteis para interação com WebDriver.
 *
 * Fornece métodos para esperas explícitas, screenshots, scroll,
 * manipulação de cookies e outras operações comuns.
 * Segue o princípio DRY (Don't Repeat Yourself).
 */
public class WebDriverHelper {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final String SCREENSHOT_DIR = "target/screenshots";

    public WebDriverHelper(WebDriver driver, int timeoutInSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    /**
     * Aguarda até que um elemento esteja visível na página.
     */
    public WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Aguarda até que um elemento esteja clicável.
     */
    public WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Aguarda até que um elemento esteja presente no DOM.
     */
    public WebElement waitForElementPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Aguarda até que um elemento desapareça da tela.
     */
    public boolean waitForElementInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Aguarda até que a URL contenha determinado texto.
     */
    public boolean waitForUrlContains(String urlFragment) {
        return wait.until(ExpectedConditions.urlContains(urlFragment));
    }

    /**
     * Clica em um elemento após aguardar que esteja clicável.
     */
    public void clickElement(By locator) {
        waitForElementClickable(locator).click();
    }

    /**
     * Clica em um elemento usando JavaScript (útil para elementos invisíveis).
     */
    public void clickElementWithJS(By locator) {
        WebElement element = waitForElementPresent(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    /**
     * Clica em um elemento usando JavaScript (versão que aceita WebElement).
     */
    public void clickElementWithJS(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    /**
     * Preenche um campo de texto após aguardar que esteja visível.
     */
    public void fillField(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Rola a página até um elemento específico.
     */
    public void scrollToElement(By locator) {
        WebElement element = waitForElementPresent(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    /**
     * Rola a página até um elemento específico (versão que aceita WebElement).
     */
    public void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    /**
     * Rola a página até o final.
     */
    public void scrollToBottom() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    /**
     * Rola a página até o topo.
     */
    public void scrollToTop() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, 0);");
    }

    /**
     * Captura screenshot da página inteira.
     *
     * @param testName nome do teste para identificar o screenshot
     * @return caminho do arquivo salvo
     */
    public String takeScreenshot(String testName) {
        try {
            createScreenshotDirectory();

            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = String.format("%s_%s.png", testName, timestamp);
            Path destinationPath = Paths.get(SCREENSHOT_DIR, fileName);

            Files.copy(sourceFile.toPath(), destinationPath);

            return destinationPath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao capturar screenshot: " + e.getMessage(), e);
        }
    }

    /**
     * Captura screenshot de um elemento específico.
     */
    public String takeElementScreenshot(By locator, String testName) {
        try {
            createScreenshotDirectory();

            WebElement element = waitForElementVisible(locator);
            File sourceFile = element.getScreenshotAs(OutputType.FILE);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = String.format("%s_element_%s.png", testName, timestamp);
            Path destinationPath = Paths.get(SCREENSHOT_DIR, fileName);

            Files.copy(sourceFile.toPath(), destinationPath);

            return destinationPath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao capturar screenshot do elemento: " + e.getMessage(), e);
        }
    }

    /**
     * Cria o diretório para screenshots se não existir.
     */
    private void createScreenshotDirectory() throws IOException {
        Path screenshotPath = Paths.get(SCREENSHOT_DIR);
        if (!Files.exists(screenshotPath)) {
            Files.createDirectories(screenshotPath);
        }
    }

    /**
     * Adiciona um cookie.
     */
    public void addCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        driver.manage().addCookie(cookie);
    }

    /**
     * Obtém um cookie pelo nome.
     */
    public Cookie getCookie(String name) {
        return driver.manage().getCookieNamed(name);
    }

    /**
     * Obtém todos os cookies.
     */
    public Set<Cookie> getAllCookies() {
        return driver.manage().getCookies();
    }

    /**
     * Remove um cookie específico.
     */
    public void deleteCookie(String name) {
        driver.manage().deleteCookieNamed(name);
    }

    /**
     * Remove todos os cookies.
     */
    public void deleteAllCookies() {
        driver.manage().deleteAllCookies();
    }

    /**
     * Verifica se um elemento está visível.
     */
    public boolean isElementVisible(By locator) {
        try {
            return waitForElementVisible(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Verifica se um elemento está presente no DOM.
     */
    public boolean isElementPresent(By locator) {
        try {
            waitForElementPresent(locator);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Obtém o texto de um elemento.
     */
    public String getElementText(By locator) {
        return waitForElementVisible(locator).getText();
    }

    /**
     * Obtém um atributo de um elemento.
     */
    public String getElementAttribute(By locator, String attribute) {
        return waitForElementVisible(locator).getAttribute(attribute);
    }
}
