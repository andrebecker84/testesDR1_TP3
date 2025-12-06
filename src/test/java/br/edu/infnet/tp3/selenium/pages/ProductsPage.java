package br.edu.infnet.tp3.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object para a página de Products - PrestaShop Demo.
 *
 * URL: https://demo.prestashop.com/#/en/front
 * Implementa o padrão Page Object Model (POM).
 *
 * NOTA: PrestaShop é um site dinâmico com Angular/React.
 * Locators podem precisar de ajustes conforme versão do demo.
 */
public class ProductsPage extends BasePage {

    // URLs
    private static final String PRESTASHOP_URL = "https://demo.prestashop.com/#/en/front";

    // Locators - PrestaShop Demo (múltiplos seletores para maior compatibilidade)
    // NOTA: PrestaShop atualiza frequentemente. Versão testada: 05/12/2025

    // Produtos - múltiplas tentativas
    private final By[] productsSelectors = {
        By.cssSelector(".product-miniature"),
        By.cssSelector("article.product"),
        By.cssSelector(".js-product"),
        By.cssSelector("[data-id-product]"),
        By.cssSelector(".product-item"),
        By.tagName("article")
    };

    // Botões Add to Cart - múltiplas tentativas
    private final By[] addToCartSelectors = {
        By.cssSelector("button[data-button-action='add-to-cart']"),
        By.cssSelector(".add-to-cart"),
        By.cssSelector("button.btn-add-to-cart"),
        By.cssSelector("button[type='submit']"),
        By.cssSelector(".btn-primary")
    };

    // Modal Continue/View Cart
    private final By[] continueShoppingSelectors = {
        By.cssSelector(".continue-shopping"),
        By.cssSelector("button.btn-secondary"),
        By.xpath("//button[contains(text(), 'Continue')]")
    };

    private final By[] viewCartSelectors = {
        By.cssSelector("a[href*='cart']"),
        By.cssSelector(".cart-link"),
        By.xpath("//a[contains(text(), 'cart') or contains(text(), 'Cart')]")
    };

    // Cookie consent (GDPR)
    private final By[] acceptCookiesSelectors = {
        By.cssSelector(".btn-accept"),
        By.cssSelector("button[aria-label='Accept']"),
        By.id("onetrust-accept-btn-handler"),
        By.xpath("//button[contains(text(), 'Accept')]")
    };

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Tenta encontrar elemento usando múltiplos seletores (fallback).
     *
     * @param selectors array de seletores By para tentar
     * @return WebElement encontrado ou null
     */
    private WebElement findElementWithFallback(By[] selectors) {
        for (By selector : selectors) {
            try {
                List<WebElement> elements = driver.findElements(selector);
                if (!elements.isEmpty()) {
                    return elements.get(0);
                }
            } catch (Exception e) {
                // Continuar tentando próximo seletor
            }
        }
        return null;
    }

    /**
     * Tenta encontrar elementos usando múltiplos seletores (fallback).
     *
     * @param selectors array de seletores By para tentar
     * @return Lista de WebElements encontrados
     */
    private List<WebElement> findElementsWithFallback(By[] selectors) {
        for (By selector : selectors) {
            try {
                List<WebElement> elements = driver.findElements(selector);
                if (!elements.isEmpty()) {
                    return elements;
                }
            } catch (Exception e) {
                // Continuar tentando próximo seletor
            }
        }
        return List.of(); // Retorna lista vazia se não encontrar
    }

    /**
     * Navega para a página de produtos PrestaShop.
     * Também tenta aceitar cookies GDPR se aparecer.
     */
    public void navigateToProducts() {
        navigateTo(PRESTASHOP_URL);

        // Aguardar página carregar (PrestaShop é SPA com muito AJAX)
        sleep(5000);

        // CRITICAL: PrestaShop Demo usa iframe!
        switchToPrestaShopIframe();

        // Tentar aceitar cookies GDPR com múltiplos seletores
        for (By selector : acceptCookiesSelectors) {
            try {
                if (isVisible(selector)) {
                    click(selector);
                    sleep(1000);
                    break; // Sucesso, sair do loop
                }
            } catch (Exception e) {
                // Tentar próximo seletor
            }
        }

        // Aguardar AJAX finalizar
        sleep(3000);
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
                        System.out.println("✓ Switched to PrestaShop iframe");
                        sleep(2000); // Aguardar iframe carregar
                        return;
                    }
                } catch (Exception e) {
                    // Tentar próximo
                }
            }
        } catch (Exception e) {
            // Se não encontrar iframe, continuar no contexto principal
            System.out.println("⚠ No iframe found, continuing in main context");
        }
    }

    /**
     * Adiciona um produto ao carrinho por índice.
     * Tenta múltiplos seletores para maior compatibilidade.
     *
     * @param index índice do produto (0-based)
     */
    public void addProductToCartByIndex(int index) {
        List<WebElement> addButtons = findElementsWithFallback(addToCartSelectors);

        if (addButtons.isEmpty()) {
            System.out.println("⚠️ AVISO: Nenhum botão 'Add to Cart' encontrado no PrestaShop");
            System.out.println("   Possível mudança na estrutura do site desde 05/12/2025");
            return;
        }

        if (index < addButtons.size()) {
            WebElement button = addButtons.get(index);
            System.out.println("Tentando adicionar produto ao carrinho (índice " + index + ")...");

            // Scroll até o botão e aguardar
            helper.scrollToElement(button);
            sleep(2000); // Aguardar mais tempo para animações

            try {
                button.click();
                System.out.println("✓ Clique executado no botão Add to Cart");
            } catch (Exception e) {
                // Se falhar, tentar com JavaScript
                System.out.println("Clique normal falhou, tentando com JavaScript...");
                helper.clickElementWithJS(button);
                System.out.println("✓ Clique executado com JavaScript");
            }

            // Aguardar após adicionar para garantir que foi processado
            sleep(3000);
        } else {
            System.out.println("⚠️ Índice " + index + " fora do range. Total de botões: " + addButtons.size());
        }
    }

    /**
     * Clica em "Continue Shopping" no modal.
     * Tenta múltiplos seletores.
     */
    public void clickContinueShopping() {
        sleep(3000); // Aguardar modal aparecer
        WebElement button = findElementWithFallback(continueShoppingSelectors);
        if (button != null) {
            System.out.println("Clicando em 'Continue Shopping'...");
            try {
                button.click();
                System.out.println("✓ Continue Shopping clicado");
            } catch (Exception e) {
                System.out.println("Tentando Continue Shopping com JavaScript...");
                helper.clickElementWithJS(button);
                System.out.println("✓ Continue Shopping clicado (JS)");
            }
            sleep(2000); // Aguardar modal fechar
        } else {
            System.out.println("⚠️ Botão 'Continue Shopping' não encontrado");
        }
    }

    /**
     * Clica em "View Cart" no modal.
     * Tenta múltiplos seletores.
     */
    public void clickViewCart() {
        sleep(3000); // Aguardar modal aparecer
        WebElement link = findElementWithFallback(viewCartSelectors);
        if (link != null) {
            System.out.println("Clicando em 'View Cart'...");
            try {
                link.click();
                System.out.println("✓ View Cart clicado");
            } catch (Exception e) {
                System.out.println("Tentando View Cart com JavaScript...");
                helper.clickElementWithJS(link);
                System.out.println("✓ View Cart clicado (JS)");
            }
            sleep(3000); // Aguardar navegação para carrinho
        } else {
            System.out.println("⚠️ Link 'View Cart' não encontrado");
        }
    }

    /**
     * Verifica se há produtos exibidos.
     * Tenta múltiplos seletores.
     *
     * @return true se há produtos
     */
    public boolean areProductsDisplayed() {
        List<WebElement> products = findElementsWithFallback(productsSelectors);
        return !products.isEmpty();
    }

    /**
     * Obtém o número de produtos exibidos.
     * Tenta múltiplos seletores.
     *
     * @return número de produtos
     */
    public int getProductsCount() {
        List<WebElement> products = findElementsWithFallback(productsSelectors);
        return products.size();
    }

    /**
     * Aguarda um tempo (use com moderação).
     * Necessário para PrestaShop devido a carregamentos AJAX.
     */
    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Aguarda AJAX/JavaScript do PrestaShop finalizar.
     * PrestaShop usa muito JavaScript assíncrono.
     */
    private void waitForAjax() {
        sleep(1500); // Aguardar animações e carregamentos
    }
}
