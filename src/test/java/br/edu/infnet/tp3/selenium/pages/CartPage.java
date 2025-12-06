package br.edu.infnet.tp3.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object para a página de Cart - PrestaShop Demo.
 *
 * URL: https://demo.prestashop.com/#/en/front (carrinho)
 * Implementa o padrão Page Object Model (POM).
 *
 * NOTA: PrestaShop é um site dinâmico.
 * Locators podem precisar de ajustes conforme versão.
 * Versão testada: 05/12/2025
 */
public class CartPage extends BasePage {

    // URLs
    private static final String CART_URL = "https://demo.prestashop.com/#/en/front";

    // Locators - PrestaShop Demo (múltiplos seletores para compatibilidade)
    private final By[] cartContainerSelectors = {
        By.cssSelector(".cart-container"),
        By.id("cart"),
        By.cssSelector(".cart-grid"),
        By.cssSelector("#main")
    };

    private final By[] cartProductsSelectors = {
        By.cssSelector(".cart-item"),
        By.cssSelector(".cart-line"),
        By.cssSelector("li.cart-item"),
        By.cssSelector(".product-line-grid-body"),
        By.cssSelector(".cart-overview .product-line-grid"),
        By.cssSelector("ul.cart-items > li")
    };

    private final By[] productNamesSelectors = {
        By.cssSelector(".product-line-info a"),
        By.cssSelector(".product-name"),
        By.cssSelector(".cart-item-name"),
        By.cssSelector("a.label")
    };

    private final By[] productQuantitiesSelectors = {
        By.cssSelector(".qty"),
        By.cssSelector(".quantity"),
        By.cssSelector("input[name*='quantity']"),
        By.cssSelector(".js-cart-line-product-quantity")
    };

    public CartPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Tenta encontrar elemento usando múltiplos seletores (fallback).
     */
    private WebElement findElementWithFallback(By[] selectors) {
        for (By selector : selectors) {
            try {
                List<WebElement> elements = driver.findElements(selector);
                if (!elements.isEmpty()) {
                    return elements.get(0);
                }
            } catch (Exception e) {
                // Continuar tentando
            }
        }
        return null;
    }

    /**
     * Tenta encontrar elementos usando múltiplos seletores (fallback).
     */
    private List<WebElement> findElementsWithFallback(By[] selectors) {
        for (By selector : selectors) {
            try {
                List<WebElement> elements = driver.findElements(selector);
                if (!elements.isEmpty()) {
                    return elements;
                }
            } catch (Exception e) {
                // Continuar tentando
            }
        }
        return List.of();
    }

    /**
     * Navega para a página do carrinho PrestaShop.
     */
    public void navigateToCart() {
        try {
            // Primeiro navegar para a URL base
            navigateTo(CART_URL);
            sleep(5000); // Aguardar página carregar (PrestaShop é SPA)

            // CRITICAL: PrestaShop Demo usa iframe!
            switchToPrestaShopIframe();

            // Tentar clicar no ícone do carrinho
            By[] cartIcons = {
                By.cssSelector("a[href*='cart']"),
                By.cssSelector(".shopping-cart"),
                By.cssSelector(".cart-preview"),
                By.cssSelector(".blockcart")
            };

            WebElement cartIcon = findElementWithFallback(cartIcons);
            if (cartIcon != null) {
                cartIcon.click();
                sleep(2000);
            }
        } catch (Exception e) {
            System.out.println("⚠ Erro ao navegar para carrinho: " + e.getMessage());
        }

        sleep(3000); // Aguardar carrinho carregar
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
                        System.out.println("✓ Switched to PrestaShop iframe (CartPage)");
                        sleep(2000); // Aguardar iframe carregar
                        return;
                    }
                } catch (Exception e) {
                    // Tentar próximo
                }
            }
        } catch (Exception e) {
            // Se não encontrar iframe, continuar no contexto principal
            System.out.println("⚠ No iframe found, continuing in main context (CartPage)");
        }
    }

    /**
     * Verifica se o carrinho está visível.
     */
    public boolean isCartVisible() {
        WebElement container = findElementWithFallback(cartContainerSelectors);
        return container != null;
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
     * Obtém o número de produtos no carrinho.
     */
    public int getProductsCount() {
        List<WebElement> products = findElementsWithFallback(cartProductsSelectors);
        return products.size();
    }

    /**
     * Obtém os nomes de todos os produtos no carrinho.
     */
    public List<String> getProductNames() {
        List<WebElement> nameElements = findElementsWithFallback(productNamesSelectors);
        return nameElements.stream()
                .map(WebElement::getText)
                .filter(text -> !text.isEmpty())
                .toList();
    }

    /**
     * Obtém o nome de um produto específico por índice.
     */
    public String getProductName(int index) {
        List<String> names = getProductNames();
        if (index < names.size()) {
            return names.get(index);
        }
        return "";
    }

    /**
     * Obtém a quantidade de um produto específico por índice.
     */
    public String getProductQuantity(int index) {
        List<WebElement> quantities = findElementsWithFallback(productQuantitiesSelectors);
        if (index < quantities.size()) {
            WebElement qtyElement = quantities.get(index);
            // Pode ser input ou texto
            String value = qtyElement.getAttribute("value");
            if (value != null && !value.isEmpty()) {
                return value;
            }
            return qtyElement.getText();
        }
        return "0";
    }

    /**
     * Remove um produto do carrinho por índice.
     */
    public void removeProduct(int index) {
        By[] deleteSelectors = {
            By.cssSelector(".remove-from-cart"),
            By.cssSelector(".cart-line-delete"),
            By.cssSelector(".delete"),
            By.cssSelector("i.delete")
        };

        List<WebElement> deleteButtons = findElementsWithFallback(deleteSelectors);
        if (index < deleteButtons.size()) {
            deleteButtons.get(index).click();
        }
    }

    /**
     * Clica no botão "Proceed to Checkout".
     */
    public void proceedToCheckout() {
        By[] checkoutSelectors = {
            By.cssSelector("a[href*='checkout']"),
            By.cssSelector(".checkout-button"),
            By.cssSelector(".btn-checkout"),
            By.xpath("//a[contains(text(), 'Checkout')]")
        };

        WebElement button = findElementWithFallback(checkoutSelectors);
        if (button != null) {
            button.click();
        }
    }

    /**
     * Verifica se o carrinho está vazio.
     */
    public boolean isCartEmpty() {
        return getProductsCount() == 0;
    }

    /**
     * Verifica se a mensagem de carrinho vazio está visível.
     */
    public boolean isEmptyCartMessageDisplayed() {
        By[] emptyMessageSelectors = {
            By.cssSelector(".cart-empty"),
            By.cssSelector(".no-items"),
            By.xpath("//*[contains(text(), 'empty') or contains(text(), 'Empty')]")
        };

        WebElement message = findElementWithFallback(emptyMessageSelectors);
        return message != null;
    }
}
