package br.edu.infnet.tp3.selenium.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object para a página de Contact Us - PrestaShop Demo.
 *
 * URL: https://demo.prestashop.com/#/en/front (contact/customer service)
 * Implementa o padrão Page Object Model (POM).
 *
 * NOTA: PrestaShop pode ter formulário de contato em diferentes locais.
 * Locators são genéricos para maior compatibilidade.
 * Versão testada: 05/12/2025
 */
public class ContactUsPage extends BasePage {

    // URLs
    private static final String CONTACT_URL = "https://demo.prestashop.com/#/en/front";

    // Locators - PrestaShop Demo (genéricos com fallback)
    private final By[] nameFieldSelectors = {
        By.cssSelector("input[name='from']"),
        By.cssSelector("input#contactform-firstname"),
        By.cssSelector("input[name*='name']"),
        By.cssSelector("input[name='firstname']"),
        By.cssSelector("input[placeholder*='Name']")
    };

    private final By[] emailFieldSelectors = {
        By.cssSelector("input[name='email']"),
        By.cssSelector("input[type='email']"),
        By.id("email"),
        By.cssSelector("input[name='from']")
    };

    private final By[] subjectFieldSelectors = {
        By.cssSelector("select[name='id_contact']"),
        By.cssSelector("select.form-control"),
        By.cssSelector("select[name='subject']")
    };

    private final By[] messageFieldSelectors = {
        By.cssSelector("textarea[name='message']"),
        By.cssSelector("textarea.form-control"),
        By.id("contactform-message"),
        By.tagName("textarea")
    };

    private final By[] submitButtonSelectors = {
        By.cssSelector("button[type='submit']"),
        By.cssSelector("input[type='submit']"),
        By.cssSelector(".btn-primary"),
        By.xpath("//button[contains(text(), 'Send') or contains(text(), 'Submit')]")
    };

    private final By[] successMessageSelectors = {
        By.cssSelector(".alert-success"),
        By.cssSelector(".success-message"),
        By.cssSelector(".alert.alert-success"),
        By.xpath("//*[contains(@class, 'success')]")
    };

    public ContactUsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Tenta encontrar elemento usando múltiplos seletores (fallback).
     */
    private WebElement findElementWithFallback(By[] selectors) {
        for (By selector : selectors) {
            try {
                List<WebElement> elements = driver.findElements(selector);
                if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                    return elements.get(0);
                }
            } catch (Exception e) {
                // Continuar tentando
            }
        }
        return null;
    }

    /**
     * Navega para a página de Contact Us do PrestaShop.
     * No PrestaShop, geralmente é acessado via footer ou menu.
     */
    public void navigateToContactUs() {
        navigateTo(CONTACT_URL);

        sleep(5000); // Aguardar página carregar (PrestaShop é SPA com muito AJAX)

        // CRITICAL: PrestaShop Demo usa iframe!
        switchToPrestaShopIframe();

        // Tentar encontrar e clicar no link "Contact Us" ou similar
        By[] contactLinkSelectors = {
            By.cssSelector("a[href*='contact']"),
            By.cssSelector("a[title*='Contact']"),
            By.xpath("//a[contains(text(), 'Contact')]"),
            By.linkText("Contact us")
        };

        for (By selector : contactLinkSelectors) {
            try {
                if (isVisible(selector)) {
                    click(selector);
                    sleep(2000);
                    break;
                }
            } catch (Exception e) {
                // Tentar próximo seletor
            }
        }
    }

    /**
     * PrestaShop Demo usa iframe para o conteúdo.
     * Precisa trocar de contexto para o iframe.
     */
    private void switchToPrestaShopIframe() {
        try {
            // PrestaShop Demo geralmente usa um iframe
            By[] iframeSelectors = {
                By.tagName("iframe"),
                By.id("framelive"),
                By.cssSelector("iframe[src*='prestashop']")
            };

            for (By selector : iframeSelectors) {
                try {
                    List<WebElement> iframes = driver.findElements(selector);
                    if (!iframes.isEmpty()) {
                        driver.switchTo().frame(iframes.get(0));
                        System.out.println("✓ Switched to PrestaShop iframe (ContactUsPage)");
                        sleep(2000); // Aguardar iframe carregar
                        return;
                    }
                } catch (Exception e) {
                    // Tentar próximo
                }
            }
        } catch (Exception e) {
            // Se não encontrar iframe, continuar no contexto principal
            System.out.println("⚠ No iframe found, continuing in main context (ContactUsPage)");
        }
    }

    /**
     * Aguarda um tempo (use com moderação).
     */
    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Preenche o formulário de contato.
     *
     * @param name    nome do usuário
     * @param email   email do usuário
     * @param subject assunto da mensagem
     * @param message mensagem
     */
    public void fillContactForm(String name, String email, String subject, String message) {
        // Tentar preencher cada campo com fallback
        WebElement nameField = findElementWithFallback(nameFieldSelectors);
        if (nameField != null) {
            nameField.clear();
            nameField.sendKeys(name);
        }

        WebElement emailField = findElementWithFallback(emailFieldSelectors);
        if (emailField != null) {
            emailField.clear();
            emailField.sendKeys(email);
        }

        // Subject pode ser select ou input
        WebElement subjectField = findElementWithFallback(subjectFieldSelectors);
        if (subjectField != null) {
            try {
                subjectField.sendKeys(subject);
            } catch (Exception e) {
                // Pode falhar se for select, ignorar
            }
        }

        WebElement messageField = findElementWithFallback(messageFieldSelectors);
        if (messageField != null) {
            messageField.clear();
            messageField.sendKeys(message);
        }
    }

    /**
     * Faz upload de um arquivo.
     *
     * @param filePath caminho absoluto do arquivo
     */
    public void uploadFile(String filePath) {
        By[] fileInputSelectors = {
            By.cssSelector("input[type='file']"),
            By.cssSelector("input[name='fileUpload']")
        };

        WebElement fileInput = findElementWithFallback(fileInputSelectors);
        if (fileInput != null) {
            fileInput.sendKeys(filePath);
        }
    }

    /**
     * Clica no botão de submit.
     */
    public void clickSubmit() {
        WebElement submitButton = findElementWithFallback(submitButtonSelectors);
        if (submitButton != null) {
            try {
                submitButton.click();
            } catch (Exception e) {
                // Tentar com JavaScript se falhar
                helper.clickElementWithJS(submitButton);
            }
        }
    }

    /**
     * Aceita o alert de confirmação.
     */
    public void acceptAlert() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (Exception e) {
            // Alert pode não aparecer em todas as versões
            System.out.println("⚠️ Alert não encontrado (normal para PrestaShop)");
        }
    }

    /**
     * Verifica se a mensagem de sucesso está visível.
     *
     * @return true se a mensagem estiver visível
     */
    public boolean isSuccessMessageDisplayed() {
        WebElement successMessage = findElementWithFallback(successMessageSelectors);
        return successMessage != null;
    }

    /**
     * Obtém o texto da mensagem de sucesso.
     *
     * @return texto da mensagem
     */
    public String getSuccessMessage() {
        WebElement successMessage = findElementWithFallback(successMessageSelectors);
        if (successMessage != null) {
            return successMessage.getText();
        }
        return "";
    }

    /**
     * Clica no botão Home.
     */
    public void clickHomeButton() {
        By[] homeButtonSelectors = {
            By.cssSelector("a[href*='index']"),
            By.cssSelector(".btn-home"),
            By.cssSelector("a.home"),
            By.linkText("Home")
        };

        WebElement homeButton = findElementWithFallback(homeButtonSelectors);
        if (homeButton != null) {
            homeButton.click();
        }
    }
}
