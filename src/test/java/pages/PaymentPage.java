package pages;

import config.Values;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.url;
import static config.Values.ln;
import static config.Values.pnr;
import static config.Values.text;
import static org.testng.AssertJUnit.assertTrue;


public class PaymentPage extends Page {

    @Step("Действие 12, Ввод реквизитов карточки и оплата")
    public void setCardDetails() {
        System.out.println("\t12. Filling bank card details and click Pay");
        setPan();
        setOwner();
        setMonth();
        setYear();
        setCVC();
        clickPayButton();
        checkPaySuccessfull();
        checkResultPageAppear();
        Sleep(10);
    }

    @Step("Заполнить поле \"Номер карты\"")
    private void setPan() {
        $("#pan_main").setValue(Values.card[0][0]);
    }

    @Step("Заполнить поле \"Владелец карты\"")
    private void setOwner() {
        $("#cardholder_main").setValue(Values.card[0][4]);
    }

    @Step("Заполнить поле \"Месяц\"")
    private void setMonth() {
        $("#exp_month_main").selectOptionByValue(Values.card[0][1]);
    }

    @Step("Заполнить поле \"Год\"")
    private void setYear() {
        $("#exp_year_main").selectOptionByValue(Values.card[0][2]);
    }

    @Step("Заполнить поле \"CVC\"")
    private void setCVC() {
        $("#cvc_main").setValue(Values.card[0][3]);
    }

    @Step("Нажать кнопку \"Заплатить\"")
    private void clickPayButton() {
        $("#cardButton").click();
    }

    @Step("Проверить сообщение об успешной оплате")
    private void checkPaySuccessfull() {
        String text = $(byXpath("//div[contains(@translate,'paymentSuccessful')]")).shouldBe(visible).getText();
        System.out.println("SUCCESS = " + text);
        assertTrue("Сообщение об успешной оплате отсутствует", text.equals(Values.text[11][ln]));
    }

    @Step("Проверить появление страницы результатов оплаты")
    private void checkResultPageAppear(){
        Sleep(25);
        $(byXpath("//div[contains(@class,'text text--bold')]")).shouldBe(visible).shouldBe(exactText(pnr));
        System.out.println("URL = " + url());
        $(byXpath("//div[contains(text(),'" + text[12][ln] + "')]")).shouldBe(visible);
    }

}