package br.edu.infnet.tp3.selenium.tests;

import br.edu.infnet.tp3.selenium.config.SeleniumConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

/**
 * Classe base para testes Selenium.
 *
 * Configura e gerencia o ciclo de vida do WebDriver.
 * Todos os testes Selenium devem estender esta classe.
 * Segue boas práticas de setup e teardown.
 */
public abstract class BaseSeleniumTest {

    protected WebDriver driver;
    protected static final int DEFAULT_TIMEOUT = 10;

    /**
     * Configuração executada antes de cada teste.
     * Inicializa o WebDriver.
     */
    @BeforeEach
    public void setUp() {
        driver = SeleniumConfig.createDriver(
            SeleniumConfig.Browser.CHROME,
            isHeadless()
        );
    }

    /**
     * Limpeza executada após cada teste.
     * Fecha o WebDriver.
     */
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Define se os testes devem rodar em modo headless.
     * Pode ser sobrescrito nas classes filhas.
     *
     * @return true para headless, false para exibir navegador
     */
    protected boolean isHeadless() {
        String headless = System.getProperty("headless", "false");
        return Boolean.parseBoolean(headless);
    }

    /**
     * Aguarda um tempo específico (use com moderação).
     *
     * @param milliseconds tempo em milissegundos
     */
    protected void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep interrompido", e);
        }
    }
}
