# EXERCÍCIOS DR1-TP3 - SELENIUM WEBDRIVER

Este documento contém as respostas conceituais e explicações técnicas dos 9 exercícios obrigatórios do trabalho DR1-TP3.

---

## Exercício 1 – Conceitos Fundamentais do Selenium

### 1.1 Os três principais componentes do Selenium

O ecossistema Selenium é composto por três componentes principais:

#### **Selenium WebDriver**
É a API principal para automação de navegadores web. Funciona como uma interface de programação que permite controlar navegadores de forma programática. O WebDriver se comunica diretamente com o navegador através de drivers específicos (ChromeDriver, GeckoDriver, EdgeDriver), enviando comandos e recebendo respostas.

**Características principais:**
- Controle programático do navegador (abrir, fechar, navegar)
- Interação com elementos web (clicar, preencher campos, selecionar)
- Suporte a múltiplas linguagens (Java, Python, C#, JavaScript, Ruby)
- Execução local em uma única máquina
- Base para automação de testes E2E (End-to-End)

#### **Selenium Grid**
É uma solução de distribuição e paralelização de testes. Permite executar testes em múltiplos navegadores, versões e sistemas operacionais simultaneamente, utilizando uma arquitetura hub-nodes.

**Características principais:**
- Execução paralela de testes
- Testes em diferentes navegadores e plataformas
- Arquitetura distribuída (Hub centraliza, Nodes executam)
- Redução do tempo total de execução da suíte de testes
- Ideal para CI/CD pipelines

#### **Selenium IDE**
É uma extensão de navegador (Chrome/Firefox) que permite gravar e reproduzir interações do usuário sem necessidade de código. Funciona como uma ferramenta de record-and-playback.

**Características principais:**
- Interface gráfica intuitiva
- Gravação de ações do usuário
- Exportação para código (WebDriver)
- Ideal para prototipagem rápida
- Não requer conhecimento de programação

---

### 1.2 Diferença entre WebDriver e Grid

| Aspecto           | Selenium WebDriver               | Selenium Grid                            |
|-------------------|----------------------------------|------------------------------------------|
| **Propósito**     | Automação de navegador           | Distribuição de testes                   |
| **Escopo**        | Execução local, uma máquina      | Execução distribuída, múltiplas máquinas |
| **Paralelização** | Não nativa (requer framework)    | Nativa, projetada para isso              |
| **Complexidade**  | Simples, direto                  | Complexa, requer configuração            |
| **Caso de uso**   | Desenvolvimento e testes básicos | Testes em larga escala, CI/CD            |

**Quando usar WebDriver:**
- Desenvolvimento local de testes
- Suítes pequenas de testes
- Prototipagem e aprendizado
- Testes em um único ambiente

**Quando usar Grid:**
- Necessidade de executar centenas de testes rapidamente
- Testes cross-browser (Chrome, Firefox, Safari, Edge)
- Testes cross-platform (Windows, Linux, macOS)
- Integração com CI/CD para feedback rápido
- Múltiplos ambientes de teste simultâneos

---

### 1.3 Vantagens e Limitações do Selenium IDE

#### **Vantagens:**

1. **Baixa curva de aprendizado**: Não requer conhecimento de programação, ideal para QAs sem background técnico.

2. **Prototipagem rápida**: Permite criar testes funcionais rapidamente através de gravação de ações.

3. **Feedback visual imediato**: Mostra em tempo real o que está sendo testado, facilitando debug.

4. **Exportação de código**: Gera código em várias linguagens (Java, Python, C#) que pode ser refinado posteriormente.

5. **Comandos prontos**: Oferece comandos pré-definidos para ações comuns (assert, verify, wait).

6. **Reprodução e debug**: Permite executar passo a passo e identificar pontos de falha facilmente.

#### **Limitações:**

1. **Testes frágeis**: Testes gravados tendem a quebrar facilmente com mudanças na UI (seletores absolutos, XPath gerado).

2. **Manutenibilidade baixa**: Difícil manter suítes grandes criadas apenas com gravação.

3. **Lógica complexa limitada**: Não suporta bem loops, condicionais complexas, ou integração com dados externos.

4. **Sem controle fino**: Limitações em esperas dinâmicas, manipulação de JavaScript, ou interações avançadas.

5. **Escalabilidade**: Não é adequado para suítes grandes ou execução paralela.

6. **Integração CI/CD limitada**: Embora seja possível, não é o ideal para pipelines automatizados.

7. **Dependência de extensão**: Limitado aos navegadores que suportam a extensão.

#### **Conclusão:**
O Selenium IDE é excelente para:
- Aprendizado inicial
- Prototipagem rápida de casos de teste
- Demonstrações e provas de conceito
- Geração de código base para refatoração

**NÃO é adequado para:**
- Testes de produção robustos
- Suítes complexas e de longo prazo
- Ambientes CI/CD críticos
- Projetos que exigem manutenibilidade

---

## Exercício 2 – Configuração do Ambiente para Testes com Selenium WebDriver

### 2.1 Passos para configurar ambiente de testes em Java com Selenium WebDriver

#### **Passo 1: Instalar Java Development Kit (JDK)**
```bash
# Verificar instalação do Java
java -version

# Deve mostrar Java 21 ou superior
# Configurar JAVA_HOME apontando para o diretório do JDK
```

**Requisitos:**
- JDK 8 ou superior (recomendado: JDK 21 para este projeto)
- Variável de ambiente `JAVA_HOME` configurada

---

#### **Passo 2: Instalar Maven**
```bash
# Verificar instalação do Maven
mvn -version

# Deve mostrar Maven 3.6+ e Java configurado
```

**Requisitos:**
- Maven 3.6 ou superior
- Variável de ambiente `M2_HOME` configurada
- Maven no PATH do sistema

---

#### **Passo 3: Criar projeto Maven**
```bash
# Criar estrutura Maven
mvn archetype:generate -DgroupId=br.edu.infnet.tp3 \
  -DartifactId=tp3-selenium-tests \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false
```

Ou criar manualmente a estrutura:
```
projeto/
├── pom.xml
├── src/
│   ├── main/java/
│   └── test/java/
```

---

#### **Passo 4: Configurar pom.xml com dependências**
```xml
<dependencies>
    <!-- Selenium WebDriver -->
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.16.1</version>
    </dependency>

    <!-- WebDriver Manager -->
    <dependency>
        <groupId>io.github.bonigarcia</groupId>
        <artifactId>webdrivermanager</artifactId>
        <version>5.6.3</version>
    </dependency>

    <!-- JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

#### **Passo 5: Baixar dependências**
```bash
mvn clean install
```

Este comando:
- Baixa todas as dependências definidas no `pom.xml`
- Armazena em `~/.m2/repository`
- Valida a configuração do projeto

---

#### **Passo 6: Instalar navegadores e drivers**

**Opção 1: Manual**
- Baixar ChromeDriver de https://chromedriver.chromium.org/
- Colocar no PATH do sistema

**Opção 2: Automática (WebDriver Manager) - RECOMENDADO**
```java
import io.github.bonigarcia.wdm.WebDriverManager;

// No código, antes de criar o driver:
WebDriverManager.chromedriver().setup();
WebDriver driver = new ChromeDriver();
```

O WebDriver Manager:
- Detecta a versão do navegador instalado
- Baixa o driver compatível automaticamente
- Configura o PATH temporariamente
- Elimina erros de incompatibilidade

---

#### **Passo 7: Criar primeiro teste**
```java
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class PrimeiroTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    public void testGoogleTitle() {
        driver.get("https://www.google.com");
        String title = driver.getTitle();
        Assertions.assertEquals("Google", title);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
```

---

#### **Passo 8: Executar testes**
```bash
# Via Maven
mvn test

# Via IDE (IntelliJ/Eclipse)
# Clicar com botão direito no teste > Run
```

---

### 2.2 Importância do WebDriver Manager

O **WebDriver Manager** é uma biblioteca que automatiza o gerenciamento de drivers de navegadores, resolvendo um dos principais problemas na configuração de ambientes Selenium.

#### **Problemas que resolve:**

**1. Incompatibilidade de versões**
- Navegadores são atualizados automaticamente
- Drivers antigos param de funcionar
- Erro comum: "SessionNotCreatedException: session not created: This version of ChromeDriver only supports Chrome version XX"

**2. Configuração manual complexa**
- Baixar driver manualmente
- Descompactar arquivo
- Configurar PATH do sistema
- Repetir para cada sistema operacional

**3. Manutenção contínua**
- Drivers precisam ser atualizados frequentemente
- Diferentes desenvolvedores com diferentes versões
- CI/CD com configurações complexas

#### **Como funciona:**

```java
// Sem WebDriver Manager
System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver.exe");
WebDriver driver = new ChromeDriver();
// Problema: path hardcoded, driver desatualizado, não portável

// Com WebDriver Manager
WebDriverManager.chromedriver().setup();
WebDriver driver = new ChromeDriver();
// Automático: detecta versão, baixa driver, configura PATH
```

#### **Benefícios:**

1. **Detecção automática**: Identifica a versão do navegador instalado
2. **Download inteligente**: Baixa o driver compatível automaticamente
3. **Cache local**: Reutiliza drivers já baixados
4. **Cross-platform**: Funciona em Windows, Linux, macOS
5. **Manutenção zero**: Não precisa atualizar manualmente
6. **CI/CD friendly**: Configuração consistente em todos os ambientes

#### **Exemplo prático:**

```java
// Configurar Chrome
WebDriverManager.chromedriver().setup();

// Configurar Firefox
WebDriverManager.firefoxdriver().setup();

// Configurar Edge
WebDriverManager.edgedriver().setup();

// Configurar versão específica
WebDriverManager.chromedriver().driverVersion("120.0.6099.109").setup();

// Forçar download mesmo com cache
WebDriverManager.chromedriver().clearDriverCache().setup();
```

---

### 2.3 Passo a passo resumido de instalação

#### **Resumo Executivo:**

```bash
# 1. Instalar Java 21
# Download: https://www.oracle.com/java/technologies/downloads/

# 2. Instalar Maven
# Download: https://maven.apache.org/download.cgi

# 3. Criar projeto Maven
mkdir projeto-selenium
cd projeto-selenium

# 4. Criar pom.xml com dependências:
# - org.seleniumhq.selenium:selenium-java:4.16.1
# - io.github.bonigarcia:webdrivermanager:5.6.3
# - org.junit.jupiter:junit-jupiter:5.10.1

# 5. Baixar dependências
mvn clean install

# 6. Criar estrutura de pacotes
mkdir -p src/test/java/br/edu/infnet/tp3/selenium

# 7. Criar classe de teste com:
# - @BeforeEach: WebDriverManager.chromedriver().setup()
# - @Test: seu teste
# - @AfterEach: driver.quit()

# 8. Executar testes
mvn test
```

#### **Checklist de validação:**

- [ ] Java instalado e `java -version` funciona
- [ ] Maven instalado e `mvn -version` funciona
- [ ] pom.xml criado com dependências corretas
- [ ] `mvn clean install` executa sem erros
- [ ] Estrutura de diretórios criada (src/test/java)
- [ ] Primeiro teste criado
- [ ] `mvn test` executa o teste com sucesso
- [ ] Navegador abre e fecha automaticamente

---

### 2.4 Configuração no projeto atual (DR1-TP3)

Neste projeto, já implementamos todas as melhores práticas:

✅ **pom.xml configurado** com:
- Selenium WebDriver 4.16.1
- WebDriver Manager 5.6.3
- JUnit 5 (via Spring Boot Starter Test)
- Hamcrest, Jqwik, JaCoCo

✅ **Arquitetura modular**:
- `SeleniumConfig.java`: Factory de WebDriver com suporte a Chrome, Firefox, Edge
- `WebDriverHelper.java`: Utilitários para esperas, screenshots, cookies, scroll
- `BasePage.java`: Classe base para Page Object Model
- `BaseSeleniumTest.java`: Classe base para testes com setup/teardown automático

✅ **Configurações externalizadas**:
- `application-test.properties`: Configurações de navegador, timeouts, paths de screenshots

✅ **Boas práticas**:
- WebDriverManager para gerenciamento automático de drivers
- Page Object Model para organização
- Esperas explícitas (WebDriverWait) ao invés de implícitas
- Screenshots automáticos para evidências
- Arquitetura seguindo SOLID e Clean Code

---

## Exercício 3 – Interação com Elementos Web

### 3.1 Testes Implementados

Para este exercício, foram implementados **2 testes práticos** utilizando o site **https://demo.prestashop.com/#/en/front** (PrestaShop Demo), conforme especificação do professor:

#### **Teste 1: Contact Us Form** (Formulário de Contato)
**Arquivo:** `Exercicio3_InteracaoElementosTest.java` - método `testContactUsFormSubmission()`

**Page Objects criados:**
- `ContactUsPage.java`: Encapsula interações com o formulário de contato

**Fluxo do teste:**
1. Navegar para `/contact_us`
2. Preencher campos: nome, email, assunto, mensagem
3. (Opcional) Upload de arquivo
4. Submeter formulário
5. Aceitar alert JavaScript de confirmação
6. Validar mensagem de sucesso
7. Retornar para home page
8. Capturar screenshots em pontos críticos

**Validações realizadas:**
- URL contém "contact_us"
- Mensagem de sucesso é exibida
- Texto da mensagem contém "Success"
- Navegação de retorno funciona corretamente

---

#### **Teste 2: Add Products in Cart** (Adicionar Produtos ao Carrinho)
**Arquivo:** `Exercicio3_InteracaoElementosTest.java` - método `testAddProductsToCart()`

**Page Objects criados:**
- `ProductsPage.java`: Encapsula interações com listagem de produtos
- `CartPage.java`: Encapsula validações do carrinho de compras

**Fluxo do teste:**
1. Navegar para `/products`
2. Validar que produtos estão visíveis
3. Adicionar primeiro produto ao carrinho
4. Fechar modal (Continue Shopping)
5. Adicionar segundo produto ao carrinho
6. Visualizar carrinho (View Cart)
7. Validar que 2 produtos estão no carrinho
8. Validar nomes e quantidades dos produtos
9. Capturar screenshots

**Validações realizadas:**
- Produtos estão sendo exibidos (mínimo 2)
- Modal aparece após adicionar produto
- Carrinho contém exatamente 2 produtos
- Nomes dos produtos não estão vazios
- Quantidades são corretas (1 para cada)

---

### 3.2 Desafios Encontrados e Soluções

#### **a) Interação com Campos de Texto**

**Desafios:**

1. **Campos não editáveis imediatamente:**
   - Alguns campos de texto só ficam editáveis após a página carregar completamente
   - Tentar preencher antes do tempo causa `ElementNotInteractableException`

**Solução implementada:**
```java
// Em ContactUsPage.java
public void fillContactForm(String name, String email, String subject, String message) {
    type(nameField, name);      // type() usa waitForElementVisible()
    type(emailField, email);
    type(subjectField, subject);
    type(messageField, message);
}

// Em BasePage.java
protected void type(By locator, String text) {
    helper.fillField(locator, text);  // Aguarda visibilidade antes
}

// Em WebDriverHelper.java
public void fillField(By locator, String text) {
    WebElement element = waitForElementVisible(locator);  // Espera explícita
    element.clear();  // Limpa campo antes de preencher
    element.sendKeys(text);
}
```

**Princípio aplicado:** Sempre usar **esperas explícitas** (WebDriverWait) ao invés de `Thread.sleep()` ou esperas implícitas.

---

2. **Textarea vs Input:**
   - Campos `<textarea>` e `<input>` se comportam diferentemente
   - `textarea` pode ter conteúdo padrão que precisa ser limpo

**Solução:**
```java
element.clear();  // Sempre limpar antes de preencher
element.sendKeys(text);
```

---

#### **b) Interação com Botões**

**Desafios:**

1. **Botões parcialmente ocultos ou fora da viewport:**
   - Botão "Add to cart" pode estar fora da tela visível
   - Click normal falha com `ElementNotInteractableException`

**Solução implementada:**
```java
// Em ProductsPage.java
public void addProductToCartByIndex(int index) {
    List<WebElement> addButtons = driver.findElements(addToCartButtons);
    WebElement button = addButtons.get(index);

    helper.scrollToElement(addToCartButtons);  // Scroll até o elemento
    sleep(500);  // Pequena pausa para garantir estabilidade
    button.click();
}

// Em WebDriverHelper.java - scroll com JavaScript
public void scrollToElement(By locator) {
    WebElement element = waitForElementPresent(locator);
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript(
        "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
        element
    );
}
```

---

2. **Botões cobertos por overlays ou modais:**
   - Modal aparece sobre o botão
   - Click interceptado: `ElementClickInterceptedException`

**Solução:**
```java
// Opção 1: Click com JavaScript (ignora overlays)
helper.clickElementWithJS(addToCartButtons);

// Opção 2: Aguardar modal desaparecer
helper.waitForElementInvisible(modalLocator);
click(targetButton);
```

---

#### **c) Interação com Pop-ups (Modals e Alerts)**

**Desafios:**

1. **Alerts JavaScript (window.alert):**
   - Bloqueiam toda interação com a página
   - Precisa ser tratado antes de continuar

**Solução implementada:**
```java
// Em ContactUsPage.java
public void acceptAlert() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    Alert alert = wait.until(ExpectedConditions.alertIsPresent());
    alert.accept();  // Clica em "OK"
}

// Alternativas:
// alert.dismiss();  // Clica em "Cancel"
// String text = alert.getText();  // Lê mensagem
```

**Princípio:** Sempre aguardar alert estar presente antes de interagir.

---

2. **Modals DOM (pop-ups HTML/CSS):**
   - Modais que fazem parte do DOM da página
   - Aparecem com animações (transições CSS)
   - Elementos dentro do modal podem não estar clicáveis imediatamente

**Solução:**
```java
// Em ProductsPage.java
public void clickContinueShopping() {
    helper.waitForElementClickable(continueShoppingButton);  // Aguarda estar clicável
    click(continueShoppingButton);
}

// Em WebDriverHelper.java
public WebElement waitForElementClickable(By locator) {
    return wait.until(ExpectedConditions.elementToBeClickable(locator));
}
```

---

3. **Timing de modais:**
   - Modal leva tempo para aparecer (animação)
   - Tentar interagir muito cedo causa falha

**Solução:**
```java
// Abordagem pragmática para modais com animação
sleep(1000);  // Aguardar animação (usar com moderação!)
productsPage.clickContinueShopping();

// Abordagem ideal (quando possível):
helper.waitForElementVisible(modalLocator);
helper.waitForElementClickable(buttonInsideModal);
```

**Nota:** `Thread.sleep()` deve ser usado com **extrema moderação**. Preferir sempre esperas explícitas baseadas em condições.

---

#### **d) Upload de Arquivos**

**Desafio:**
- Input file é geralmente invisível (escondido via CSS)
- Alguns sites usam overlays sobre o input

**Solução implementada:**
```java
// Em ContactUsPage.java
public void uploadFile(String filePath) {
    waitForElement(uploadFileInput).sendKeys(filePath);
}

// Locator:
private final By uploadFileInput = By.name("upload_file");
```

**Pontos importantes:**
1. Não usar `.click()` no input file
2. Usar `.sendKeys(absolutePath)` diretamente
3. Caminho deve ser **absoluto**, não relativo
4. Arquivo deve existir no sistema de arquivos

**Exemplo de uso:**
```java
String testFile = Paths.get("src/test/resources/test-upload.txt")
                       .toAbsolutePath()
                       .toString();
contactPage.uploadFile(testFile);
```

---

#### **e) Elementos Dinâmicos e Listas**

**Desafio:**
- Número de produtos varia
- Elementos são carregados dinamicamente (AJAX)
- Índices podem mudar

**Solução implementada:**
```java
// Em ProductsPage.java
public int getProductsCount() {
    return driver.findElements(productsList).size();
}

// No teste - validação flexível:
int productsCount = productsPage.getProductsCount();
assertThat("Deve haver pelo menos 2 produtos",
        productsCount,
        greaterThanOrEqualTo(2));  // Não assumir número exato
```

**Princípios:**
1. Não assumir números fixos de elementos
2. Validar existência mínima, não exata
3. Usar índices com validação de bounds
4. Aguardar carregamento completo antes de contar

---

### 3.3 Boas Práticas Aplicadas

#### **1. Page Object Model (POM)**
```java
// ❌ Ruim: Locators e lógica no teste
@Test
public void testBad() {
    driver.findElement(By.id("name")).sendKeys("João");
    driver.findElement(By.id("submit")).click();
}

// ✅ Bom: Encapsulado em Page Object
@Test
public void testGood() {
    ContactUsPage page = new ContactUsPage(driver);
    page.fillContactForm("João", "joao@email.com", "Assunto", "Mensagem");
    page.clickSubmit();
}
```

**Benefícios:**
- Reutilização de código
- Manutenibilidade (mudanças de UI centralizadas)
- Legibilidade dos testes
- Separação de responsabilidades (SRP)

---

#### **2. Esperas Explícitas**
```java
// ❌ Ruim: Sleep fixo
Thread.sleep(5000);  // Sempre espera 5s, mesmo se carregar em 1s
driver.findElement(locator).click();

// ✅ Bom: Espera explícita com condição
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
// Espera no máximo 10s, mas continua assim que estiver pronto
```

**Vantagens:**
- Performance (não espera desnecessariamente)
- Confiabilidade (aguarda condição real)
- Timeout configurável

---

#### **3. Validações com Hamcrest**
```java
// ❌ Ruim: Mensagens de erro pouco descritivas
assertTrue(url.contains("contact"));  // "expected: true but was: false"

// ✅ Bom: Mensagens descritivas
assertThat("URL deve conter 'contact_us'",
        url,
        containsString("contact_us"));
// "Expected: URL deve conter 'contact_us', but: was 'https://example.com/'"
```

---

#### **4. Screenshots para Evidências**
```java
// Capturar em momentos críticos:
contactPage.takeScreenshot("contact-before-submit");
contactPage.clickSubmit();
contactPage.takeScreenshot("contact-success");
```

**Utilidade:**
- Debug de falhas
- Documentação de execução
- Evidências para relatórios
- Análise de comportamento inesperado

---

### 3.4 Arquivos Criados para Este Exercício

**Page Objects:**
- `src/test/java/.../selenium/pages/ContactUsPage.java`
- `src/test/java/.../selenium/pages/ProductsPage.java`
- `src/test/java/.../selenium/pages/CartPage.java`

**Testes:**
- `src/test/java/.../selenium/tests/Exercicio3_InteracaoElementosTest.java`
  - `testContactUsFormSubmission()`
  - `testAddProductsToCart()`

**Evidências:**
- Screenshots salvos em: `target/screenshots/`

---

### 3.5 Conclusão

O Exercício 3 demonstrou:
- ✅ Interação com diversos tipos de elementos (input, textarea, button, file)
- ✅ Manipulação de alerts JavaScript
- ✅ Tratamento de modais e pop-ups
- ✅ Validação de elementos dinâmicos
- ✅ Captura de screenshots para evidências
- ✅ Aplicação de Page Object Model
- ✅ Uso de esperas explícitas
- ✅ Validações assertivas com Hamcrest

Os desafios encontrados (elementos não clicáveis, modais, timing) foram resolvidos seguindo boas práticas de automação e princípios de Clean Code.

---

**Próximos exercícios** continuarão explorando funcionalidades avançadas do Selenium.
