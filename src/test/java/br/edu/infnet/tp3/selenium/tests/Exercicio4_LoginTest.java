package br.edu.infnet.tp3.selenium.tests;

import br.edu.infnet.tp3.selenium.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercício 4 - Navegação entre Páginas e Teste de Login
 *
 * Implementa testes de:
 * - Login com credenciais corretas
 * - Login com credenciais incorretas
 * - Logout
 * - Verificação de URL
 * - Gerenciamento de cookies e sessões
 *
 * Site: https://practicetestautomation.com/practice-test-login/
 * Credenciais válidas: student / Password123
 */
@DisplayName("Exercício 4 - Navegação e Login")
public class Exercicio4_LoginTest extends BaseSeleniumTest {

    /**
     * Teste: Login com credenciais corretas
     *
     * Passos:
     * 1. Navegar para página de login
     * 2. Preencher credenciais válidas
     * 3. Submeter formulário
     * 4. Validar URL de sucesso
     * 5. Validar mensagem de sucesso
     * 6. Validar presença do botão logout
     */
    @Test
    @DisplayName("Login com credenciais corretas")
    public void testLoginWithCorrectCredentials() {
        LoginPage loginPage = new LoginPage(driver);

        // Passo 1: Navegar para login
        loginPage.navigateToLogin();

        // Validar que está na página de login
        assertTrue(loginPage.isOnLoginPage(),
                "Deve estar na página de login");

        // Passo 2 e 3: Login com credenciais corretas
        loginPage.login("student", "Password123");

        // Passo 4: Validar URL
        assertTrue(loginPage.isLoginSuccessful(),
                "Login deve ser bem-sucedido (URL contém 'logged-in-successfully')");

        assertThat("URL deve conter 'logged-in-successfully'",
                loginPage.getCurrentUrl(),
                containsString("logged-in-successfully"));

        // Passo 5: Validar mensagem de sucesso
        assertTrue(loginPage.isSuccessMessageDisplayed(),
                "Mensagem de sucesso deve estar visível");

        String successMessage = loginPage.getSuccessMessage();
        assertThat("Mensagem deve confirmar login bem-sucedido",
                successMessage,
                containsString("Logged In Successfully"));

        // Passo 6: Validar botão de logout
        assertTrue(loginPage.isLogoutButtonDisplayed(),
                "Botão de logout deve estar visível");

        // Screenshot de sucesso
        loginPage.takeScreenshot("login-success");

        System.out.println("✅ Login bem-sucedido!");
        System.out.println("URL atual: " + loginPage.getCurrentUrl());
        System.out.println("Mensagem: " + successMessage);
    }

    /**
     * Teste: Login com credenciais incorretas
     *
     * Passos:
     * 1. Navegar para página de login
     * 2. Preencher credenciais inválidas
     * 3. Submeter formulário
     * 4. Validar que permanece na página de login
     * 5. Validar mensagem de erro
     */
    @Test
    @DisplayName("Login com credenciais incorretas")
    public void testLoginWithIncorrectCredentials() {
        LoginPage loginPage = new LoginPage(driver);

        // Passo 1: Navegar para login
        loginPage.navigateToLogin();

        // Passo 2 e 3: Login com credenciais incorretas
        loginPage.login("invalidUser", "wrongPassword");

        // Passo 4: Validar que permanece na página de login
        assertThat("URL deve ainda conter 'practice-test-login'",
                loginPage.getCurrentUrl(),
                containsString("practice-test-login"));

        assertFalse(loginPage.isOnLoggedInPage(),
                "Não deve estar na página de sucesso");

        // Passo 5: Validar mensagem de erro
        assertTrue(loginPage.isErrorMessageDisplayed(),
                "Mensagem de erro deve estar visível");

        String errorMessage = loginPage.getErrorMessage();
        assertThat("Mensagem de erro deve indicar credenciais inválidas",
                errorMessage,
                anyOf(
                        containsString("invalid"),
                        containsString("incorrect"),
                        containsString("username"),
                        containsString("password")
                ));

        // Screenshot do erro
        loginPage.takeScreenshot("login-error");

        System.out.println("✅ Validação de erro funcionou corretamente!");
        System.out.println("Mensagem de erro: " + errorMessage);
    }

    /**
     * Teste: Logout
     *
     * Passos:
     * 1. Realizar login com sucesso
     * 2. Clicar em logout
     * 3. Validar redirecionamento para página de login
     * 4. Validar que não está mais logado
     */
    @Test
    @DisplayName("Logout após login bem-sucedido")
    public void testLogout() {
        LoginPage loginPage = new LoginPage(driver);

        // Passo 1: Login com sucesso
        loginPage.navigateToLogin();
        loginPage.login("student", "Password123");

        // Validar login
        assertTrue(loginPage.isLoginSuccessful(),
                "Login deve ser bem-sucedido");

        assertTrue(loginPage.isLogoutButtonDisplayed(),
                "Botão de logout deve estar visível");

        // Screenshot antes do logout
        loginPage.takeScreenshot("before-logout");

        // Passo 2: Logout
        loginPage.logout();

        // Aguardar redirecionamento
        sleep(1000);

        // Passo 3: Validar retorno para página de login
        assertThat("Deve retornar para página de login",
                loginPage.getCurrentUrl(),
                containsString("practice-test-login"));

        // Screenshot após logout
        loginPage.takeScreenshot("after-logout");

        System.out.println("✅ Logout realizado com sucesso!");
        System.out.println("URL após logout: " + loginPage.getCurrentUrl());
    }
}
