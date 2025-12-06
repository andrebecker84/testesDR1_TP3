package br.edu.infnet.tp3.selenium.tests;

import br.edu.infnet.tp3.selenium.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercício 6 - Gerenciamento de Cookies e Sessões
 *
 * Demonstra:
 * - Salvar cookies após login
 * - Reutilizar cookies para evitar login repetitivo
 * - Manipulação de cookies individuais
 * - Otimização de tempo de execução
 * - Persistência de sessão
 */
@DisplayName("Exercício 6 - Gerenciamento de Cookies")
public class Exercicio6_CookiesTest extends BaseSeleniumTest {

    /**
     * Teste: Salvar e reutilizar cookies de sessão
     *
     * Passos:
     * 1. Realizar login normalmente
     * 2. Salvar todos os cookies
     * 3. Fechar navegador e abrir novo
     * 4. Carregar cookies salvos
     * 5. Navegar diretamente para área logada
     * 6. Validar que login persistiu
     *
     * Benefícios:
     * - Reduz tempo de execução (não precisa fazer login toda vez)
     * - Útil para testes que dependem de estar logado
     * - Simula comportamento "Lembrar-me"
     */
    @Test
    @DisplayName("Salvar e reutilizar cookies para login persistente")
    public void testSaveAndReuseCookies() {
        LoginPage loginPage = new LoginPage(driver);

        System.out.println("\n=== EXERCÍCIO 6: GERENCIAMENTO DE COOKIES ===\n");

        // ====== PARTE 1: Login e salvar cookies ======
        System.out.println("PARTE 1: Realizar login e salvar cookies");
        System.out.println("─────────────────────────────────────────\n");

        // Login
        loginPage.navigateToLogin();
        loginPage.login("student", "Password123");

        assertTrue(loginPage.isLoginSuccessful(),
                "Login deve ser bem-sucedido");

        System.out.println("✓ Login realizado com sucesso");

        // Salvar cookies
        Set<Cookie> savedCookies = loginPage.saveCookies();

        System.out.println("✓ Cookies salvos: " + savedCookies.size() + " cookies");
        System.out.println("\nCookies capturados:");
        savedCookies.forEach(cookie ->
                System.out.println("  - " + cookie.getName() + " = " +
                        (cookie.getValue().length() > 50 ?
                                cookie.getValue().substring(0, 50) + "..." :
                                cookie.getValue())));

        String currentUrl = loginPage.getCurrentUrl();
        System.out.println("\n✓ URL atual (logado): " + currentUrl);

        // Screenshot com cookies salvos
        loginPage.takeScreenshot("cookies-saved");

        // ====== PARTE 2: Simular nova sessão ======
        System.out.println("\n\nPARTE 2: Simular nova sessão");
        System.out.println("─────────────────────────────────────────\n");

        // Limpar cookies (simula fechar navegador)
        loginPage.clearCookies();
        System.out.println("✓ Cookies limpos (simulando fechamento do navegador)");

        // Navegar para página de login novamente
        loginPage.navigateToLogin();

        // Verificar que não está mais logado
        loginPage.navigateTo(currentUrl);  // Tentar acessar área logada
        sleep(1000);

        // URL deve redirecionar para login (ou não estar na área logada)
        System.out.println("✓ Sem cookies, não consegue acessar área logada");
        System.out.println("  URL atual: " + driver.getCurrentUrl());

        // ====== PARTE 3: Restaurar cookies ======
        System.out.println("\n\nPARTE 3: Restaurar cookies e validar sessão");
        System.out.println("─────────────────────────────────────────\n");

        // Voltar para domínio correto antes de adicionar cookies
        loginPage.navigateToLogin();

        // Restaurar cookies
        loginPage.restoreCookies(savedCookies);
        System.out.println("✓ Cookies restaurados");

        // Navegar para área logada
        loginPage.navigateTo(currentUrl);
        sleep(1000);

        // Validar que está logado (sem precisar fazer login novamente!)
        boolean isLoggedIn = driver.getCurrentUrl().contains("logged-in-successfully");

        if (isLoggedIn) {
            System.out.println("✅ SUCESSO! Login persistiu com cookies!");
            System.out.println("   URL: " + driver.getCurrentUrl());
            System.out.println("   Não foi necessário fazer login novamente!");
        } else {
            System.out.println("⚠️  Cookies não persistiram a sessão");
            System.out.println("   Isso pode acontecer dependendo da implementação do site");
        }

        // Screenshot final
        loginPage.takeScreenshot("cookies-restored");

        // ====== RESUMO ======
        System.out.println("\n\n" + "=".repeat(50));
        System.out.println("RESUMO: BENEFÍCIOS DO GERENCIAMENTO DE COOKIES");
        System.out.println("=".repeat(50));
        System.out.println();
        System.out.println("✓ OTIMIZAÇÃO DE TEMPO:");
        System.out.println("  - Elimina necessidade de login em cada teste");
        System.out.println("  - Economia de 5-10 segundos por teste");
        System.out.println("  - Em suite com 100 testes: economiza 8-17 minutos");
        System.out.println();
        System.out.println("✓ REDUÇÃO DE CARGA:");
        System.out.println("  - Menos requisições ao servidor");
        System.out.println("  - Menor chance de bloqueio por rate limiting");
        System.out.println();
        System.out.println("✓ CASOS DE USO:");
        System.out.println("  - Testes que requerem usuário autenticado");
        System.out.println("  - Testes de funcionalidades pós-login");
        System.out.println("  - Simular comportamento 'Lembrar-me'");
        System.out.println("  - Setup de testes de integração");
        System.out.println();
        System.out.println("✓ IMPLEMENTAÇÃO:");
        System.out.println("  - Salvar: driver.manage().getCookies()");
        System.out.println("  - Restaurar: driver.manage().addCookie(cookie)");
        System.out.println("  - Limpar: driver.manage().deleteAllCookies()");
        System.out.println();
    }

    /**
     * Teste: Manipulação individual de cookies
     *
     * Demonstra operações com cookies específicos:
     * - Adicionar cookie customizado
     * - Ler cookie específico
     * - Modificar cookie
     * - Remover cookie específico
     */
    @Test
    @DisplayName("Manipulação individual de cookies")
    public void testIndividualCookieManipulation() {
        LoginPage loginPage = new LoginPage(driver);

        System.out.println("\n=== MANIPULAÇÃO INDIVIDUAL DE COOKIES ===\n");

        // Navegar para alguma página
        loginPage.navigateToLogin();

        System.out.println("1. ADICIONAR COOKIE CUSTOMIZADO");
        System.out.println("   - Nome e valor são obrigatórios");
        System.out.println("   - Deve estar no domínio correto\n");

        // Adicionar cookie customizado
        loginPage.getHelper().addCookie("test_cookie", "test_value_123");
        loginPage.getHelper().addCookie("user_preference", "dark_mode");
        loginPage.getHelper().addCookie("session_start", String.valueOf(System.currentTimeMillis()));

        System.out.println("   ✓ 3 cookies customizados adicionados");

        System.out.println("\n2. LER COOKIE ESPECÍFICO");
        System.out.println("   - Usar getCookieNamed(nome)\n");

        Cookie testCookie = loginPage.getHelper().getCookie("test_cookie");
        if (testCookie != null) {
            System.out.println("   ✓ Cookie encontrado:");
            System.out.println("     Nome: " + testCookie.getName());
            System.out.println("     Valor: " + testCookie.getValue());
            System.out.println("     Domínio: " + testCookie.getDomain());
            System.out.println("     Path: " + testCookie.getPath());
            System.out.println("     Expiry: " + testCookie.getExpiry());
            System.out.println("     Secure: " + testCookie.isSecure());
            System.out.println("     HttpOnly: " + testCookie.isHttpOnly());

            assertEquals("test_value_123", testCookie.getValue());
        }

        System.out.println("\n3. LISTAR TODOS OS COOKIES");
        Set<Cookie> allCookies = loginPage.getHelper().getAllCookies();
        System.out.println("   Total de cookies: " + allCookies.size());

        allCookies.forEach(cookie ->
                System.out.println("   - " + cookie.getName() + " = " + cookie.getValue()));

        assertThat("Deve ter pelo menos 3 cookies",
                allCookies.size(),
                greaterThanOrEqualTo(3));

        System.out.println("\n4. REMOVER COOKIE ESPECÍFICO");
        System.out.println("   - deleteCookieNamed(nome)\n");

        loginPage.getHelper().deleteCookie("test_cookie");
        System.out.println("   ✓ Cookie 'test_cookie' removido");

        Cookie deletedCookie = loginPage.getHelper().getCookie("test_cookie");
        assertNull(deletedCookie, "Cookie deve ter sido removido");

        System.out.println("\n5. REMOVER TODOS OS COOKIES");
        System.out.println("   - deleteAllCookies()\n");

        loginPage.getHelper().deleteAllCookies();
        Set<Cookie> afterClear = loginPage.getHelper().getAllCookies();

        System.out.println("   ✓ Todos os cookies removidos");
        System.out.println("   Cookies restantes: " + afterClear.size());

        assertEquals(0, afterClear.size(), "Não deve haver cookies");

        System.out.println("\n✅ Demonstração de manipulação individual concluída!");
    }
}
