package pages;

import com.codeborne.selenide.ElementsCollection;
import config.Values;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.testng.AssertJUnit.assertTrue;


public class ChoosePage extends Page {

    private String env = System.getProperty("area", "RC");//получить имя площадки из дженкинса, при неудаче площадка=RC


    @Step("Действие 4, выбор стенда")
    public String step4(String cur) {
        System.out.println("\t4. Choose Test Stend");
        String pnr = checkChoosePage();
        System.out.println("URL = " + url());
        if ((!cur.equals("RUB"))&(Values.currencyChange.equals("soap"))) changeCurrency();
        clickEnvironment();
        return pnr;
    }

    @Step("Действие 9, выбор стенда")
    public void chooseTestStend () {
        System.out.println("\t9. Choose Test Stend");
        $("h1").shouldBe(exactText("Вход в тестовую среду системы ЕПР"));
        System.out.println("URL = " + url());
        clickEnvironment();
        checkEprPageRedirect();
    }

    @Step("Подождать страницу выбора стенда")
    private String checkChoosePage(){
        $("h1").shouldBe(exactText("Вход в тестовую среду системы ЕПР"));
        int start = url().indexOf("&PNR") + 5;
        String pnr = url().substring(start, start + 6);
        System.out.println("PNR = " + pnr);
        return pnr;
    }

    @Step("Проверить переход на платёжную страницу ЕПР")
    private void checkEprPageRedirect(){
        String epr = "https://pay.test.aeroflot.ru/test-" + env.toLowerCase() + "/aeropayment/epr/payment2.html";
        assertTrue("URL платежной страницы ЕПР не соответствует эталону", url().contains(epr));
    }

    @Step("Выбрать стенд")
    private void clickEnvironment() {
        ElementsCollection buttons = $$(byXpath("//span[text()='TEST-" + env + "']"));
        buttons.get(1).click();
        waitPlane();
    }

    @Step("Действие 5, смена валюты")
    private void changeCurrency() {
        //new SoapRequest().changeCurrency();
    }



}
