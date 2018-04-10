package pages;

import com.codeborne.selenide.ElementsCollection;
import config.Values;
import ru.yandex.qatools.allure.annotations.Step;
import struct.Flight;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static org.testng.AssertJUnit.assertTrue;


public class OfficePage extends Page{

    @Step("Действие 14, проверка лога АРМ")
    public void checkLog(List<Flight> flyList) {
        System.out.println("\t14. Checking log in office ESS");
        authorization();
        searchOrder(Values.pnr);
        openOrderDetails(Values.pnr, flyList);
    }

    @Step("Авторизоваться в АРМ ESS")
    private void authorization (){
        open(Values.office_host);
        if ($$(byName("login")).size()>0) {
            setLogin();
            setPassword();
            clickEnterButton();
        }
        checkOrderFormAppear();
    }

    @Step("Найти заказ с PNR = {0}")
    private void searchOrder (String pnr) {
        System.out.println("Search order with PNR = " + pnr);
        setQueryField(pnr);
        clickSearchButton();
        checkOrderIsFound(pnr);
    }

    @Step("Открыть детализацию заказа {0}")
    private void openOrderDetails (String pnr, List<Flight> flyList) {
        System.out.println("Open order details");
        clickOrder(pnr);
        checkOrderDetailsTabAppear(pnr);
        checkFlight(flyList);
    }

    @Step("Форма «Заказы» открылась")
    private void checkOrderFormAppear() {
        $(byXpath("//h2[text()=' Заказы']")).shouldBe(visible);
        System.out.println("Order form appeared");
    }

    @Step("Вкладка с детализацией заказа открылась")
    private void checkOrderDetailsTabAppear(String pnr) {
        $(byXpath("//h2[contains(text(),'[" + pnr + "]')]")).shouldBe(visible);
        System.out.println("Order details Tab appeared");
    }

    @Step("Ввести логин")
    private void setLogin(){
        $(byName("login")).shouldBe(visible).setValue(Values.office_login);
    }

    @Step("Ввести пароль")
    private void setPassword(){
        $(byName("password")).shouldBe(visible).setValue(Values.office_password);
    }

    @Step("Нажать кнопку \"Вход\"")
    private void clickEnterButton(){
        $(byXpath("//button[@type='submit']")).shouldBe(visible).click();
    }

    @Step("Нажать кнопку поиска")
    private void clickSearchButton(){
        $(byXpath("//div/button[@type='submit']")).shouldBe(visible).click();
    }

    @Step("Ввести PNR в строку поиска")
    private void setQueryField(String pnr){
        $("#inputQuery").shouldBe(visible).setValue(pnr);
    }

    @Step("Заказ {0} найден")
    private void checkOrderIsFound(String pnr){
        $(byXpath("//td/a[text()='" + pnr + "']")).shouldBe(exist).shouldBe(visible);
    }

    @Step("Открыть заказ {0}")
    private void clickOrder(String pnr){
        String link = $(byXpath("//td/a[text()='" + pnr + "']")).getAttribute("href");
        open(link);
        Sleep(3);
    }

    @Step("Проверить данные перелета")
    private void checkFlight(List<Flight> flyList){
        ElementsCollection rows = $$(byXpath("//h5[text()='Перелет']/following-sibling::table/tbody/tr"));
        for (int i=0; i<rows.size(); i++){
            String number = rows.get(i).$(byXpath("td[1]")).getText();
            assertTrue("Номер " + (i+1) +"-го рейса не совпадает с забронированным" +
                       "\nОжидалось : " + flyList.get(i).number +
                       "\nФактически: " + number,
                       number.equals(flyList.get(i).number));

            String date = rows.get(i).$(byXpath("td[5]")).getText();
            String dates = new SimpleDateFormat("HH:mm/").format(flyList.get(i).start);
            dates = dates + new SimpleDateFormat("HH:mm ").format(flyList.get(i).end);
            dates = dates + new SimpleDateFormat("dd.MM.yyyy").format(flyList.get(i).start);
            assertTrue("Дата/время " + (i+1) +"-го рейса не совпадает с забронированным" +
                       "\nОжидалось : " + dates +
                       "\nФактически: " + date,
                       dates.equals(date));

            String from = rows.get(i).$(byXpath("td[2]")).getText();
            String ofrom = getCityByCode(flyList.get(i).from);
            assertTrue("Направление ОТКУДА " + (i+1) +"-го рейса не совпадает с забронированным" +
                       "\nОжидалось : " + ofrom + "(" + flyList.get(i).from + ")" +
                       "\nФактически: " + from,
                       ofrom.equals(from));

            String to = rows.get(i).$(byXpath("td[4]")).getText();
            String oto = getCityByCode(flyList.get(i).to);
            assertTrue("Направление КУДА " + (i+1) +"-го рейса не совпадает с забронированным" +
                       "\nОжидалось : " + oto + "(" + flyList.get(i).to + ")" +
                       "\nФактически: " + to,
                       oto.equals(to));
        }
    }

    private String getCityByCode(String code) {
        String result = "";
        for(int i=0; i<Values.city.length; i++){
            if (code.equals(Values.city[i][0])) {
                result = Values.city[i][1];
                break;
            }
        }
        return result;
    }

}
