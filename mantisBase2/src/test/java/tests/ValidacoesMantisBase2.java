package tests;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.*;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class ValidacoesMantisBase2 {
    private WebDriver chromeDriver;
    private String IdBugCriado;


    @Before
    public void SetUp(){
        //Abrir Navegador Chrome
        System.setProperty("webdriver.chrome.driver", "C:\\Automacao\\WebDriver\\chromedriver.exe");
        chromeDriver = new ChromeDriver();
        chromeDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //Abrir Navegador Chrome para a página mantis-prova.base2.com.br
        chromeDriver.get("http://mantis-prova.base2.com.br");

        //Informar o "Usuário"
        chromeDriver.findElement(By.name("username")).sendKeys("henrique.morais");

        //Informar "Password"
        chromeDriver.findElement(By.name("password")).sendKeys("123456");

        //Acionar o botão "Login"
        chromeDriver.findElement(By.xpath("//input[@value='Login']")).click();

        //Verificar que o usuário "henrique.morais" é o usuário que realizou o login np Mantis
        WebElement usuariologado = chromeDriver.findElement(By.xpath("//*[@class=\"login-info-left\"]//*[@class=\"italic\"]"));
        String textoUsuario = usuariologado.getText();
        Assert.assertEquals("henrique.morais", textoUsuario);

    }

    @Test
    public void CriarNovoBug(){

        //Acionar o lik "Report Issue"
        chromeDriver.findElement(By.xpath("//*[@class='menu']//following-sibling::a[2]")).click();

        //Caso ele exiba a tela "Seleção de Projeto", deve ser selecionado um projeto, senão continua para o próximo passo
        String selecionarprojeto = chromeDriver.findElement(By.xpath("//td[contains(@class,'form-title')]")).getText();
        if (selecionarprojeto.contains("Select Project")){
            List<WebElement> listaComboProjetos = chromeDriver.findElements(By.name("project_id"));
            listaComboProjetos.get(1).click();
            new Helpers().espera(0.5);
            new Select((listaComboProjetos.get(1))).selectByVisibleText("Henrique Morais's Project");
            new Helpers().espera(1);
            chromeDriver.findElement(By.xpath("//input[@value='Select Project']")).click();
        }
        //Serão informados apenas os campos obrigatórios da tela (*)
        //Infomar o campo "Category"
            WebElement campoSummary = chromeDriver.findElement(By.xpath("//*[@class='row-1']//following-sibling::select[1]"));
        new Select(campoSummary).selectByIndex(2);

        //Infomar o campo "Summary"
        chromeDriver.findElement(By.name("summary")).sendKeys("Bug Teste Base2 001");

        //Infomar o campo "Description"
        chromeDriver.findElement(By.name("description")).sendKeys("Bug Teste Base2 001");

        //Clicar no botão "Submit Report"
        chromeDriver.findElement(By.xpath("//input[@value='Submit Report']")).click();

        //Verificar se mensagem de sucesso é exibida
        String mensagemSucesso = chromeDriver.findElement(By.xpath("//div[@align='center']")).getText();
        if (!mensagemSucesso.contains("Operation successful.")){
            Assert.fail("Mensagem esparada não encontrada.");
        }

        //Verificar se o Bug cadastrado foi listado corretamente
        String BugCriado = chromeDriver.findElement(By.xpath("//a[contains(@href,'view.php?id=')]")).getText();
        IdBugCriado = BugCriado.replaceAll("View Submitted Issue ","");
    }

    @Test
    public void PesquisarMantisCriado(){
        //Informar o Bug criado no campo de pesquisa
        chromeDriver.findElement(By.name("bug_id")).sendKeys("0006991");

        //Acionar o botão  "Jump"
        chromeDriver.findElement((By.xpath("//input[@value=\"Jump\"]"))).click();

        //Verificar que o Bug pesquisado é o mesmo exibido na tela
        String campoIdMantis = chromeDriver.findElement(By.xpath("//*[@class='row-1']//ancestor::td")).getText();
        Assert.assertEquals("0006991", campoIdMantis);
        System.out.println("Teste exectado com sucesso!");
    }

    @After
    public void logout() {
        //Acionar o botão de Logout
        chromeDriver.findElement(By.xpath("//a[contains(@href,'logout_page')]")).click();

        //Verificar que a tela de Login é exibida
        String telalogin = chromeDriver.findElement(By.xpath("//input[contains(@name,'return')]")).getText();
        if (telalogin.contains("Login")){
            System.out.println("Tela de Login exibida com sucesso.");
        }

        //Fechar o navegador
        chromeDriver.quit();
    }


}
