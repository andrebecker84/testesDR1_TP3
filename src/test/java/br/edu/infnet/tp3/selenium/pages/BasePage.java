package br.edu.infnet.tp3.selenium.pages;

import br.edu.infnet.tp3.selenium.helpers.WebDriverHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Classe base para Page Objects.
 *
 * Implementa o padrão Page Object Model (POM).
 * Todas as páginas devem estender esta classe.
 * Segue o princípio OCP (Open-Closed Principle).
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverHelper helper;

    /**
     * Construtor que inicializa o WebDriver e o Helper.
     *
     * @param driver instância do WebDriver
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.helper = new WebDriverHelper(driver, 10);
    }

    /**
     * Navega para uma URL específica.
     *
     * @param url URL de destino
     */
    public void navigateTo(String url) {
        driver.get(url);
    }

    /**
     * Obtém o título da página.
     *
     * @return título da página
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Obtém a URL atual.
     *
     * @return URL atual
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Aguarda até que um elemento esteja visível.
     *
     * @param locator localizador do elemento
     * @return o elemento visível
     */
    protected WebElement waitForElement(By locator) {
        return helper.waitForElementVisible(locator);
    }

    /**
     * Clica em um elemento.
     *
     * @param locator localizador do elemento
     */
    protected void click(By locator) {
        helper.clickElement(locator);
    }

    /**
     * Preenche um campo de texto.
     *
     * @param locator localizador do campo
     * @param text    texto a ser preenchido
     */
    protected void type(By locator, String text) {
        helper.fillField(locator, text);
    }

    /**
     * Obtém o texto de um elemento.
     *
     * @param locator localizador do elemento
     * @return texto do elemento
     */
    protected String getText(By locator) {
        return helper.getElementText(locator);
    }

    /**
     * Verifica se um elemento está visível.
     *
     * @param locator localizador do elemento
     * @return true se visível, false caso contrário
     */
    protected boolean isVisible(By locator) {
        return helper.isElementVisible(locator);
    }

    /**
     * Rola a página até um elemento.
     *
     * @param locator localizador do elemento
     */
    protected void scrollTo(By locator) {
        helper.scrollToElement(locator);
    }

    /**
     * Captura screenshot da página.
     *
     * @param testName nome do teste
     * @return caminho do arquivo
     */
    public String takeScreenshot(String testName) {
        return helper.takeScreenshot(testName);
    }

    /**
     * Obtém o helper para operações avançadas.
     * Útil para testes que precisam acessar funcionalidades não expostas.
     *
     * @return WebDriverHelper
     */
    public WebDriverHelper getHelper() {
        return helper;
    }
}
