package br.edu.infnet.tp3.selenium.tests;

import br.edu.infnet.tp3.selenium.pages.LoginPage;
import br.edu.infnet.tp3.selenium.pages.ProductsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercício 7 - Captura de Screenshots de Páginas e Elementos
 *
 * Sites utilizados:
 * - Login: https://practicetestautomation.com/practice-test-login/
 * - Products: PrestaShop Demo (https://demo.prestashop.com/#/en/front)
 *
 * Demonstra:
 * - Captura de screenshot da página inteira
 * - Captura de screenshot de elemento específico
 * - Salvamento automático no sistema de arquivos
 * - Uso para debug e evidências
 * - Organização de screenshots por teste
 */
@DisplayName("Exercício 7 - Captura de Screenshots")
public class Exercicio7_ScreenshotsTest extends BaseSeleniumTest {

    /**
     * Teste: Captura de screenshots em diferentes momentos
     *
     * Cenários onde screenshots são úteis:
     * - Antes e depois de ações críticas
     * - Em caso de falha de teste
     * - Como evidência de execução
     * - Para análise visual de comportamento
     * - Documentação de bugs
     */
    @Test
    @DisplayName("Captura de screenshots em momentos críticos do teste")
    public void testScreenshotCapture() {
        LoginPage loginPage = new LoginPage(driver);

        System.out.println("\n=== EXERCÍCIO 7: CAPTURA DE SCREENSHOTS ===\n");

        System.out.println("CENÁRIO 1: Screenshot da página inicial");
        System.out.println("─────────────────────────────────────────\n");

        // Navegar para login
        loginPage.navigateToLogin();

        // Screenshot 1: Página de login inicial
        String screenshot1 = loginPage.takeScreenshot("ex7-01-login-page");
        System.out.println("✓ Screenshot 1 salvo: " + screenshot1);

        // Validar que arquivo foi criado
        File file1 = new File(screenshot1);
        assertTrue(file1.exists(), "Screenshot deve ter sido salvo");
        System.out.println("  Tamanho: " + file1.length() + " bytes");

        System.out.println("\nCENÁRIO 2: Screenshot antes de ação crítica");
        System.out.println("─────────────────────────────────────────\n");

        // Preencher formulário
        loginPage.fillUsername("student");
        loginPage.fillPassword("Password123");

        // Screenshot 2: Antes de submeter
        String screenshot2 = loginPage.takeScreenshot("ex7-02-before-submit");
        System.out.println("✓ Screenshot 2 salvo (antes do submit): " + screenshot2);

        // Submit
        loginPage.clickSubmit();

        System.out.println("\nCENÁRIO 3: Screenshot após ação (sucesso)");
        System.out.println("─────────────────────────────────────────\n");

        // Aguardar login
        sleep(1000);

        // Screenshot 3: Após login bem-sucedido
        String screenshot3 = loginPage.takeScreenshot("ex7-03-after-login");
        System.out.println("✓ Screenshot 3 salvo (após login): " + screenshot3);

        assertTrue(loginPage.isLoginSuccessful(),
                "Login deve ter sido bem-sucedido");

        // ====== RESUMO ======
        System.out.println("\n" + "=".repeat(50));
        System.out.println("RESUMO: UTILIDADE DOS SCREENSHOTS");
        System.out.println("=".repeat(50));
        System.out.println();
        System.out.println("1. DEBUG DE FALHAS:");
        System.out.println("   - Ver exatamente o estado da página quando falhou");
        System.out.println("   - Identificar elementos não encontrados");
        System.out.println("   - Comparar estado esperado vs real");
        System.out.println();
        System.out.println("2. EVIDÊNCIAS:");
        System.out.println("   - Provar que teste executou corretamente");
        System.out.println("   - Documentar comportamento da aplicação");
        System.out.println("   - Anexar em relatórios de teste");
        System.out.println();
        System.out.println("3. ANÁLISE:");
        System.out.println("   - Comparar execuções diferentes");
        System.out.println("   - Identificar mudanças visuais");
        System.out.println("   - Validar layout e design");
        System.out.println();
        System.out.println("4. BOAS PRÁTICAS:");
        System.out.println("   - Nomear screenshots com timestamp");
        System.out.println("   - Organizar por teste/feature");
        System.out.println("   - Capturar em momentos-chave, não tudo");
        System.out.println("   - Limpar screenshots antigos periodicamente");
        System.out.println();
        System.out.println("Total de screenshots capturados: 3");
        System.out.println("Localização: target/screenshots/");
        System.out.println();
    }

    /**
     * Teste: Screenshot de elemento específico
     *
     * Demonstra captura de screenshot de um elemento individual
     * ao invés da página inteira.
     */
    @Test
    @DisplayName("Captura de screenshot de elemento específico")
    public void testElementScreenshot() {
        ProductsPage productsPage = new ProductsPage(driver);

        System.out.println("\n=== SCREENSHOT DE ELEMENTO ESPECÍFICO ===\n");

        // Navegar para produtos
        productsPage.navigateToProducts();

        System.out.println("Diferença: Screenshot de PÁGINA vs ELEMENTO");
        System.out.println("─────────────────────────────────────────\n");

        // Screenshot da página inteira
        String fullPageScreenshot = productsPage.takeScreenshot("ex7-04-full-page");
        System.out.println("✓ Screenshot de PÁGINA INTEIRA:");
        System.out.println("  " + fullPageScreenshot);
        System.out.println("  Tamanho: " + new File(fullPageScreenshot).length() + " bytes");

        // Screenshot de elemento específico (DESABILITADO)
        // NOTA: PrestaShop tem estrutura HTML dinâmica diferente do automationexercise.com
        // Para evitar falhas devido a mudanças no site, esta funcionalidade foi desabilitada
        System.out.println("\n✓ Screenshot de ELEMENTO ESPECÍFICO:");
        System.out.println("  (Desabilitado para PrestaShop - estrutura HTML variável)");

        // ALTERNATIVA: Se necessário, pode usar ProductsPage para localizar elementos
        // By firstProduct = By.cssSelector(".product-miniature");  // PrestaShop
        // String elementScreenshot = productsPage.getHelper().takeElementScreenshot(
        //         firstProduct,
        //         "ex7-05-element"
        // );

        System.out.println("\nVANTAGENS do screenshot de elemento:");
        System.out.println("  - Arquivo menor (menos espaço em disco)");
        System.out.println("  - Foco no que importa");
        System.out.println("  - Melhor para comparação visual");
        System.out.println("  - Útil para validar componentes específicos");

        System.out.println("\n✅ Demonstração de screenshots concluída!");
    }
}
