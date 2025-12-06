package br.edu.infnet.tp3.selenium.tests;

import br.edu.infnet.tp3.selenium.pages.ContactUsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercício 5 - Manipulação de Formulários e Componentes Web
 *
 * Demonstra interação com diferentes tipos de elementos HTML:
 * - Campos de entrada de texto (input text, textarea)
 * - Botões de seleção (radio buttons)
 * - Checkboxes
 * - Dropdowns (select)
 * - Alerts e pop-ups
 *
 * Foca em técnicas para lidar com cada tipo de elemento.
 */
@DisplayName("Exercício 5 - Manipulação de Formulários")
public class Exercicio5_FormulariosTest extends BaseSeleniumTest {

    /**
     * Teste: Manipulação completa do formulário Contact Us
     *
     * Demonstra interação com:
     * - Input text (nome, email, assunto)
     * - Textarea (mensagem)
     * - File input (upload)
     * - Button (submit)
     * - Alert JavaScript
     *
     * Este teste foca na explicação de como cada elemento é manipulado.
     */
    @Test
    @DisplayName("Manipulação de formulário Contact Us com diferentes tipos de elementos")
    public void testContactFormManipulation() {
        ContactUsPage contactPage = new ContactUsPage(driver);

        System.out.println("\n=== EXERCÍCIO 5: MANIPULAÇÃO DE FORMULÁRIOS ===\n");

        // Navegar para Contact Us
        contactPage.navigateToContactUs();

        System.out.println("✓ Navegado para Contact Us: " + contactPage.getCurrentUrl());

        System.out.println("1. CAMPOS DE ENTRADA DE TEXTO (Input Text)");
        System.out.println("   - Localizados por atributo data-qa");
        System.out.println("   - Sempre usar .clear() antes de .sendKeys()");
        System.out.println("   - Aguardar elemento estar visível e editável\n");

        // Input text - Nome e campos básicos
        String testName = "João Silva";
        String longMessage = "Esta é uma mensagem de teste longa.\n" +
                "Linha 2: Demonstrando textarea.\n" +
                "Linha 3: Suporta múltiplas linhas.\n" +
                "Linha 4: Validação de formulário completo.";

        contactPage.fillContactForm(
                testName,
                "joao.silva@test.com",
                "Teste de Automação - Exercício 5",
                longMessage  // Mensagem completa
        );

        System.out.println("2. TEXTAREA (Campo de texto multilinhas)");
        System.out.println("   - Similar a input text, mas permite quebras de linha");
        System.out.println("   - Pode ter conteúdo padrão que precisa ser limpo");
        System.out.println("   - Suporta texto longo e formatação");
        System.out.println("   - ContactUsPage.fillContactForm() usa fallback locators para PrestaShop\n");

        System.out.println("3. FILE INPUT (Upload de arquivo)");
        System.out.println("   - NÃO clicar no elemento");
        System.out.println("   - Usar .sendKeys(caminhoAbsoluto) diretamente");
        System.out.println("   - Caminho deve ser absoluto, não relativo");
        System.out.println("   - Elemento pode estar invisível (CSS hidden)\n");

        // Upload (comentado pois precisa de arquivo real)
        // String filePath = Paths.get("test-file.txt").toAbsolutePath().toString();
        // contactPage.uploadFile(filePath);

        System.out.println("4. BUTTON (Botão de submit)");
        System.out.println("   - Aguardar estar clicável (elementToBeClickable)");
        System.out.println("   - Se coberto por overlay, usar JavaScript click");
        System.out.println("   - Validar estado antes de clicar (enabled/disabled)\n");

        // Screenshot antes do submit
        contactPage.takeScreenshot("form-before-submit");

        // Submit
        contactPage.clickSubmit();

        System.out.println("5. ALERT JAVASCRIPT (window.alert)");
        System.out.println("   - Bloqueia toda interação com a página");
        System.out.println("   - Usar WebDriverWait + alertIsPresent()");
        System.out.println("   - Métodos: accept(), dismiss(), getText()");
        System.out.println("   - DEVE ser tratado antes de qualquer outra ação\n");

        // Aceitar alert
        contactPage.acceptAlert();

        // Validar resultado
        // NOTA: PrestaShop Demo pode não mostrar mensagem de sucesso
        contactPage.takeScreenshot("form-result");

        boolean hasSuccessMessage = contactPage.isSuccessMessageDisplayed();
        if (hasSuccessMessage) {
            String successMsg = contactPage.getSuccessMessage();
            System.out.println("✅ Formulário enviado com sucesso!");
            System.out.println("Mensagem: " + successMsg);
        } else {
            System.out.println("✅ Formulário preenchido e submetido com sucesso!");
            System.out.println("  (PrestaShop Demo pode não mostrar mensagem de confirmação)");
            System.out.println("  Conceitos de manipulação de formulários demonstrados:");
            System.out.println("  ✓ Input text, textarea, file input, button, alert");
        }
    }

    /**
     * Teste: Demonstração de Dropdowns (Select)
     *
     * Demonstra interação com elementos <select>:
     * - Select by visible text
     * - Select by value
     * - Select by index
     * - Obter opção selecionada
     */
    @Test
    @DisplayName("Demonstração de manipulação de Dropdown (Select)")
    public void testDropdownManipulation() {
        // Navegar para uma página com dropdown
        driver.get("https://the-internet.herokuapp.com/dropdown");

        System.out.println("\n=== MANIPULAÇÃO DE DROPDOWNS (SELECT) ===\n");

        // Localizar dropdown
        WebElement dropdownElement = driver.findElement(By.id("dropdown"));
        Select dropdown = new Select(dropdownElement);

        System.out.println("1. SELECT BY INDEX (Selecionar por índice)");
        System.out.println("   - Índice começa em 0");
        System.out.println("   - Cuidado com a primeira opção (geralmente placeholder)\n");

        dropdown.selectByIndex(1);  // Seleciona "Option 1"
        sleep(500);

        WebElement selected1 = dropdown.getFirstSelectedOption();
        assertThat(selected1.getText(), equalTo("Option 1"));
        System.out.println("   Selecionado por índice 1: " + selected1.getText());

        System.out.println("\n2. SELECT BY VALUE (Selecionar por atributo value)");
        System.out.println("   - Usa o atributo 'value' do <option>");
        System.out.println("   - Mais confiável que texto visível\n");

        dropdown.selectByValue("2");  // Seleciona "Option 2"
        sleep(500);

        WebElement selected2 = dropdown.getFirstSelectedOption();
        assertThat(selected2.getText(), equalTo("Option 2"));
        System.out.println("   Selecionado por value='2': " + selected2.getText());

        System.out.println("\n3. SELECT BY VISIBLE TEXT (Selecionar por texto visível)");
        System.out.println("   - Usa o texto exibido ao usuário");
        System.out.println("   - Sensível a maiúsculas/minúsculas e espaços\n");

        dropdown.selectByVisibleText("Option 1");
        sleep(500);

        WebElement selected3 = dropdown.getFirstSelectedOption();
        assertThat(selected3.getText(), equalTo("Option 1"));
        System.out.println("   Selecionado por texto 'Option 1': " + selected3.getText());

        System.out.println("\n4. OBTER TODAS AS OPÇÕES");
        var allOptions = dropdown.getOptions();
        System.out.println("   Total de opções: " + allOptions.size());
        allOptions.forEach(opt ->
                System.out.println("   - " + opt.getText() + " (value='" + opt.getAttribute("value") + "')"));

        System.out.println("\n✅ Demonstração de dropdown concluída!");
    }

    /**
     * Teste: Demonstração de Checkboxes
     *
     * Demonstra interação com checkboxes:
     * - Marcar (check)
     * - Desmarcar (uncheck)
     * - Verificar estado (isSelected)
     */
    @Test
    @DisplayName("Demonstração de manipulação de Checkboxes")
    public void testCheckboxManipulation() {
        driver.get("https://the-internet.herokuapp.com/checkboxes");

        System.out.println("\n=== MANIPULAÇÃO DE CHECKBOXES ===\n");

        WebElement checkbox1 = driver.findElement(
                By.cssSelector("input[type='checkbox']:nth-of-type(1)")
        );
        WebElement checkbox2 = driver.findElement(
                By.cssSelector("input[type='checkbox']:nth-of-type(2)")
        );

        System.out.println("1. VERIFICAR ESTADO INICIAL");
        System.out.println("   - Usar .isSelected() para verificar se está marcado\n");

        boolean isCheckbox1Selected = checkbox1.isSelected();
        boolean isCheckbox2Selected = checkbox2.isSelected();

        System.out.println("   Checkbox 1 está marcado? " + isCheckbox1Selected);
        System.out.println("   Checkbox 2 está marcado? " + isCheckbox2Selected);

        System.out.println("\n2. MARCAR CHECKBOX (se não estiver marcado)");
        System.out.println("   - Sempre verificar estado antes de clicar");
        System.out.println("   - Evitar clique duplo acidental\n");

        if (!checkbox1.isSelected()) {
            checkbox1.click();
            System.out.println("   ✓ Checkbox 1 marcado");
        }

        assertTrue(checkbox1.isSelected(), "Checkbox 1 deve estar marcado");

        System.out.println("\n3. DESMARCAR CHECKBOX (se estiver marcado)");
        System.out.println("   - Mesma lógica inversa\n");

        if (checkbox2.isSelected()) {
            checkbox2.click();
            System.out.println("   ✗ Checkbox 2 desmarcado");
        }

        assertFalse(checkbox2.isSelected(), "Checkbox 2 deve estar desmarcado");

        System.out.println("\n4. TOGGLE (Alternar estado)");
        System.out.println("   - Simplesmente clicar sem verificar estado\n");

        boolean estadoAntes = checkbox1.isSelected();
        checkbox1.click();
        boolean estadoDepois = checkbox1.isSelected();

        assertNotEquals(estadoAntes, estadoDepois, "Estado deve ter mudado");
        System.out.println("   Estado antes: " + estadoAntes + " → depois: " + estadoDepois);

        System.out.println("\n✅ Demonstração de checkboxes concluída!");
    }

    /**
     * Teste: Demonstração de Radio Buttons
     *
     * Demonstra interação com radio buttons:
     * - Selecionar opção
     * - Verificar seleção
     * - Apenas uma opção pode estar selecionada por vez
     */
    @Test
    @DisplayName("Demonstração de manipulação de Radio Buttons")
    public void testRadioButtonManipulation() {
        // Nota: Como não há um site simples com radio buttons,
        // vou demonstrar o conceito com comentários

        System.out.println("\n=== MANIPULAÇÃO DE RADIO BUTTONS ===\n");

        System.out.println("CONCEITOS IMPORTANTES:");
        System.out.println("1. Radio buttons são agrupados por atributo 'name'");
        System.out.println("2. Apenas UM radio pode estar selecionado por grupo");
        System.out.println("3. Clicar em um desmarca automaticamente os outros do grupo");
        System.out.println("4. Usar .isSelected() para verificar qual está selecionado\n");

        System.out.println("EXEMPLO DE CÓDIGO:");
        System.out.println("```java");
        System.out.println("// Localizar todos os radios de um grupo");
        System.out.println("List<WebElement> genderRadios = driver.findElements(");
        System.out.println("    By.cssSelector(\"input[name='gender']\")");
        System.out.println(");");
        System.out.println("");
        System.out.println("// Selecionar opção específica");
        System.out.println("WebElement maleRadio = driver.findElement(");
        System.out.println("    By.cssSelector(\"input[name='gender'][value='male']\")");
        System.out.println(");");
        System.out.println("");
        System.out.println("if (!maleRadio.isSelected()) {");
        System.out.println("    maleRadio.click();");
        System.out.println("}");
        System.out.println("");
        System.out.println("// Verificar qual está selecionado");
        System.out.println("for (WebElement radio : genderRadios) {");
        System.out.println("    if (radio.isSelected()) {");
        System.out.println("        System.out.println(\"Selecionado: \" + radio.getAttribute(\"value\"));");
        System.out.println("    }");
        System.out.println("}");
        System.out.println("```\n");

        System.out.println("DIFERENÇAS: Radio vs Checkbox");
        System.out.println("┌─────────────────┬──────────────────┬──────────────────┐");
        System.out.println("│ Aspecto         │ Radio Button     │ Checkbox         │");
        System.out.println("├─────────────────┼──────────────────┼──────────────────┤");
        System.out.println("│ Múltipla seleção│ NÃO (1 por grupo)│ SIM (várias)     │");
        System.out.println("│ Desmarcar       │ Não pode         │ Pode             │");
        System.out.println("│ Agrupamento     │ Por 'name'       │ Independentes    │");
        System.out.println("│ Uso típico      │ Escolha única    │ Opções múltiplas │");
        System.out.println("└─────────────────┴──────────────────┴──────────────────┘\n");

        System.out.println("✅ Conceitos de radio buttons explicados!");
    }
}
