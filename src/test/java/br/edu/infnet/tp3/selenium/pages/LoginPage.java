package br.edu.infnet.tp3.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Set;

/**
 * Page Object para a página de Login.
 *
 * URL: https://practicetestautomation.com/practice-test-login/
 * Implementa o padrão Page Object Model (POM).
 */
public class LoginPage extends BasePage {

    // Locators
    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By submitButton = By.id("submit");
    private final By errorMessage = By.id("error");
    private final By successMessage = By.cssSelector(".post-title");
    private final By logoutButton = By.cssSelector("a[href*='logout']");

    // Múltiplos seletores para logout (fallback)
    private final By[] logoutButtonSelectors = {
        By.cssSelector("a[href*='logout']"),
        By.linkText("Log out"),
        By.partialLinkText("Log out"),
        By.xpath("//a[contains(@href, 'logout')]"),
        By.xpath("//a[contains(text(), 'Log out')]")
    };

    // URLs
    private static final String LOGIN_URL = "https://practicetestautomation.com/practice-test-login/";
    private static final String LOGGED_IN_URL = "https://practicetestautomation.com/logged-in-successfully/";

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navega para a página de login.
     */
    public void navigateToLogin() {
        navigateTo(LOGIN_URL);
    }

    /**
     * Realiza login com credenciais.
     *
     * @param username nome de usuário
     * @param password senha
     */
    public void login(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        click(submitButton);
    }

    /**
     * Preenche o campo de usuário.
     */
    public void fillUsername(String username) {
        type(usernameField, username);
    }

    /**
     * Preenche o campo de senha.
     */
    public void fillPassword(String password) {
        type(passwordField, password);
    }

    /**
     * Clica no botão de submit.
     */
    public void clickSubmit() {
        click(submitButton);
    }

    /**
     * Verifica se a mensagem de erro está visível.
     */
    public boolean isErrorMessageDisplayed() {
        return isVisible(errorMessage);
    }

    /**
     * Obtém o texto da mensagem de erro.
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    /**
     * Verifica se o login foi bem-sucedido (URL correta).
     */
    public boolean isLoginSuccessful() {
        helper.waitForUrlContains("logged-in-successfully");
        return getCurrentUrl().contains("logged-in-successfully");
    }

    /**
     * Verifica se a mensagem de sucesso está visível.
     */
    public boolean isSuccessMessageDisplayed() {
        return isVisible(successMessage);
    }

    /**
     * Obtém o texto da mensagem de sucesso.
     */
    public String getSuccessMessage() {
        return getText(successMessage);
    }

    /**
     * Verifica se o botão de logout está visível.
     * Aguarda até 10 segundos para o botão aparecer.
     */
    public boolean isLogoutButtonDisplayed() {
        // Tentar múltiplos seletores
        for (By selector : logoutButtonSelectors) {
            try {
                helper.waitForElementVisible(selector);
                if (isVisible(selector)) {
                    System.out.println("✓ Logout button found with selector: " + selector);
                    return true;
                }
            } catch (Exception e) {
                // Tentar próximo seletor
            }
        }
        System.out.println("⚠ Logout button not found with any selector");
        return false;
    }

    /**
     * Realiza logout.
     */
    public void logout() {
        // Tentar múltiplos seletores para encontrar e clicar no botão de logout
        for (By selector : logoutButtonSelectors) {
            try {
                helper.waitForElementClickable(selector);
                click(selector);
                System.out.println("✓ Logout button clicked with selector: " + selector);
                return; // Sucesso, sair do método
            } catch (Exception e) {
                // Tentar próximo seletor
            }
        }
        // Se nenhum seletor funcionou, lançar exceção
        throw new RuntimeException("Não foi possível encontrar e clicar no botão de logout com nenhum seletor");
    }

    /**
     * Salva todos os cookies da sessão.
     */
    public Set<Cookie> saveCookies() {
        return helper.getAllCookies();
    }

    /**
     * Restaura cookies salvos.
     */
    public void restoreCookies(Set<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            helper.addCookie(cookie.getName(), cookie.getValue());
        }
    }

    /**
     * Remove todos os cookies.
     */
    public void clearCookies() {
        helper.deleteAllCookies();
    }

    /**
     * Verifica se está na URL de login.
     */
    public boolean isOnLoginPage() {
        return getCurrentUrl().equals(LOGIN_URL);
    }

    /**
     * Verifica se está na página de sucesso (logged in).
     */
    public boolean isOnLoggedInPage() {
        return getCurrentUrl().contains(LOGGED_IN_URL);
    }
}
