package br.edu.infnet.tp3.selenium.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Classe de configuração para WebDriver.
 *
 * Responsável por criar e configurar instâncias do WebDriver
 * seguindo o princípio SRP (Single Responsibility Principle).
 */
public class SeleniumConfig {

    private static final int DEFAULT_TIMEOUT = 10;
    private static final int DEFAULT_PAGE_LOAD_TIMEOUT = 30;

    public enum Browser {
        CHROME, FIREFOX, EDGE
    }

    /**
     * Cria uma instância do WebDriver baseado no navegador especificado.
     *
     * @param browser   o navegador a ser utilizado
     * @param headless  se true, executa em modo headless
     * @return instância configurada do WebDriver
     */
    public static WebDriver createDriver(Browser browser, boolean headless) {
        return switch (browser) {
            case CHROME -> createChromeDriver(headless);
            case FIREFOX -> createFirefoxDriver(headless);
            case EDGE -> createEdgeDriver(headless);
        };
    }

    /**
     * Cria uma instância do ChromeDriver.
     */
    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        configureTimeouts(driver);
        return driver;
    }

    /**
     * Cria uma instância do FirefoxDriver.
     */
    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("--headless");
        }

        options.addArguments("--width=1920");
        options.addArguments("--height=1080");

        WebDriver driver = new FirefoxDriver(options);
        configureTimeouts(driver);
        return driver;
    }

    /**
     * Cria uma instância do EdgeDriver.
     */
    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();

        if (headless) {
            options.addArguments("--headless");
        }

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        WebDriver driver = new EdgeDriver(options);
        configureTimeouts(driver);
        return driver;
    }

    /**
     * Configura os timeouts padrão do WebDriver.
     */
    private static void configureTimeouts(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_TIMEOUT));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(DEFAULT_PAGE_LOAD_TIMEOUT));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT));
    }
}
