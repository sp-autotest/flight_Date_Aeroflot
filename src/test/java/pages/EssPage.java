package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.yandex.qatools.allure.annotations.Step;
import struct.Flight;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static config.Values.*;
import static org.testng.AssertJUnit.assertTrue;


/**
 * Created by mycola on 22.02.2018.
 */
public class EssPage extends Page {

    @Step("Действие 6, Проверка данных на форме ESS")
    public void checkEss1(List<Flight> flightList) {
        System.out.println("\t6. Check ESS form");
        checkPageAppear();
        screenShot("Скриншот");
        ElementsCollection flights = $$(byXpath("//div[@class='cart__item-details']"));
        //checkPriceData();
        for (int i = 0; i < flights.size(); i++) {
            checkFlightData(i+1, flightList, flights);
            checkNumberData(i+1, flightList, flights);
            checkDateData(i+1, flightList, flights);
            checkDurationData(i+1, flightList, flights);
        }
    }

    @Step("Действие 7, Проверка данных в блоке «Перелет»")
    public void step7(List<Flight> flightList) {
        System.out.println("\t7. Check Fly group");

    }

    @Step("Действие 8, Проверка данных в блоке «Страховка»")
    public void step8() {
        System.out.println("\t8. Check Insurance group");
        checkFlyInsuranceInCard();
        checkPriceOfFlyInsurance();
    }

    @Step("Действие 9, Проверка добавления Медицинской страховки {0}")
    public void step9(String type) {
        System.out.println("\t9. Add Medical Insurance");
        clickAddMedicalButton(type);
        screenShot("Скриншот");
        checkPriceOfMedicalInsurance(type);
        checkMedicalButtonName(type);
        checkTotalAndInsurensPrices();
        screenShot("Скриншот");
    }

    @Step("Действие 9, Удалить услугу «Полётная страховка»")
    public void deleteFlyInsurance() {
        System.out.println("\t9. Delete Fly Insurance");
        clickFlyInsuranceButton();
        checkMissFlyInsuranceInCard();
        checkFlyInsuranceButton(Values.text[5][ln]);
        checkTotalAndFlyPrices();
        screenShot("Скриншот");
    }

    @Step("Действие 10, Нажать Оплатить в корзине")
    public void clickPayInCart() {
        System.out.println("\t10. Click Pay in cart");
        $(byXpath("//a[@class='cart__item-counter-link']")).click();
        waitPlane();
    }

    private void checkPageAppear(){
        $(byXpath("//div[@class='cart__item-title']")).shouldBe(visible).shouldBe(text(pnr)).click();
    }

    @Step("Маршрут")
    private void checkFlight(){
        $(byXpath("//div[@class='cart__item-details-item']")).shouldBe(visible);
    }

    @Step("Номер рейса")
    private void checkNumber(){
        $(byXpath("//div[@class='cart__item-details-model']")).shouldBe(visible);
    }

    @Step("Дата и время рейса")
    private void checkDateTime(){
        $(byXpath("//div[@class='h-color--gray h-mt--4']")).shouldBe(visible);
    }

    @Step("Стоимость для всех")
    private void checkPrice(){
        $(byXpath("//div[@class='cart__item-price']")).shouldBe(visible);
    }

    @Step("Блок полетной страховки")
    private void checkFlightInsurance(){
        $(byXpath("//div[contains(text(),'" + text[0][ln] + "')]")).shouldBe(visible);
    }

    @Step("Блок медицинской страховки")
    private void checkMedicalInsurance(){
        $(byXpath("//div[contains(text(),'" + text[1][ln] + "')][contains(@class,'icon-medicial')]")).shouldBe(visible);
    }

    @Step("Блок корзины")
    private void checkCart(){
        $("#window-main-cart").shouldBe(visible);
    }

    @Step("Кнопка перехода")
    private void checkNextButton(){
        $(byXpath("//div[@class='next__button-inner']")).shouldBe(visible);
    }

    @Step("Транспорт")
    private void checkTransport(){
        $("#left-column-transport").shouldBe(visible).shouldBe(exactText(text[2][ln]));
    }

    @Step("Проживание")
    private void checkDwelling(){
        $(byXpath("//div[@class='cart__item']")).shouldBe(visible).shouldBe(exactText(text[3][ln]));
    }

    @Step("Таймер")
    private void checkTimer(){
        $(byXpath("//div[@class='cart__item-counter-time']")).shouldBe(visible);
    }

    @Step("Проверка данных о стоимости")
    private void checkPriceData(){
        String price = $(byXpath("//div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        assertTrue("Стоимость не совпадает c указанной при бронировании" +
                "\nОжидалось: " + Values.price.fly +"\nФактически: " + price,
                price.equals(Values.price.fly));
    }

    @Step("Проверка данных о {0}-м маршруте")
    private void checkFlightData(int i, List<Flight> flightList, ElementsCollection flights){
        String flight = flights.get(i-1).$(byXpath("descendant::div[@class='cart__item-details-item']")).getText().trim();
        System.out.println("site = " + flight);
        String f = flightList.get(i-1).from+" → "+flightList.get(i-1).to;
        if (f.contains("SVO")) f = f.replaceFirst("SVO", "MOW");
        if (f.contains("VKO")) f = f.replaceFirst("VKO", "MOW");
        System.out.println("book = " + f);
        assertTrue("Маршрут не совпадает с забронированным" +
                   "\nОжидалось: " + f +"\nФактически: " + flight, flight.equals(f));
    }

    @Step("Проверка данных о номере {0}-го рейса")
    private void checkNumberData(int i, List<Flight> flightList, ElementsCollection flights){
        String number = flights.get(i-1).$(byXpath("descendant::div[@class='cart__item-details-model']")).getText().trim();
        System.out.println("site = " + number);
        System.out.println("book = " + flightList.get(i-1).number);
        assertTrue("Номер рейса не совпадает с забронированным" +
                   "\nОжидалось: " + flightList.get(i-1).number +"\nФактически: " + number,
                   number.equals(flightList.get(i-1).number));
    }

    @Step("Проверка данных о дате {0}-го авиаперелета")
    private void checkDateData(int i, List<Flight> flightList, ElementsCollection flights){
        String date = flights.get(i-1).$(byXpath("descendant::div[@class='h-color--gray h-mt--4']")).getText().replace(" ", "");
        date = date.substring(0, date.indexOf("("));
        System.out.println("site = " + date);
        String format = Values.lang[ln][3];
        String month = new SimpleDateFormat("MMMM", new Locale(Values.lang[ln][2])).format(flightList.get(i-1).start);
        if (month.length()>5) format = format.replaceFirst("MMMM", "MMM.");
        String dd = new SimpleDateFormat(format, new Locale(Values.lang[ln][2])).format(flightList.get(i-1).start);
        dd = dd + new SimpleDateFormat("HH:mm").format(flightList.get(i-1).end);
        System.out.println("book = " + dd);
        assertTrue("Дата авиаперелета не совпадает с забронированной" +
                   "\nОжидалось: " + dd +"\nФактически: " + date, date.equals(dd));
    }

    @Step("Проверка данных о длительности {0}-го авиаперелета")
    private void checkDurationData(int i, List<Flight> flightList, ElementsCollection flights){
        String duration = "";
        String time = flights.get(i-1).$(byXpath("descendant::div[@class='h-color--gray h-mt--4']")).getText();
        time = time.substring(time.indexOf("("), time.indexOf(")")-1);
        for (int c = 0; c < time.length(); c++) {
            if (Character.isDigit(time.charAt(c))) {
                duration = duration + time.charAt(c);
            }
        }
        System.out.println("site = " + duration);
        System.out.println("book = " + flightList.get(i-1).duration);
        assertTrue("Длительность авиаперелета не совпадает с забронированной" +
                   "\nОжидалось: " + flightList.get(i-1).duration +"\nФактически: " + duration,
                   duration.equals(flightList.get(i-1).duration));
    }

    @Step("Полетная страховка в корзине")
    private void checkFlyInsuranceInCard(){
        $("#left-column-insurance-block").$(byXpath("descendant::div[@class='cart__item-priceondemand-item-title']"))
                .shouldBe(exist).shouldBe(visible).shouldBe(exactText(text[0][ln]));
    }

    @Step("Полетная страховка отсутствует в корзине")
    private void checkMissFlyInsuranceInCard(){
        $("#left-column-insurance-block").$(byXpath("descendant::div[text()='" + text[0][ln] + "']"))
                .shouldNotBe(exist).shouldNotBe(visible);
    }

    @Step("Проверка общей суммы полетной страховки")
    private  void checkPriceOfFlyInsurance(){
        String summ = $("#left-column-insurance-block").$(byXpath("descendant::" +
                "div[@class='cart__item-priceondemand-item-price']")).getText().trim();
        Values.price.iflight = summ.replaceAll("\\D+","");
        int s = stringIntoInt(Values.price.iflight);
        System.out.println("Summ = " + s);
        String price = $(byXpath("//div[@class='frame__heading frame__heading--icon frame__heading--icon-safe']/span")).getText();
        price = price.substring(0, price.indexOf("(")).replaceAll("\\D+","");
        if (Values.cur.equals("EUR")|Values.cur.equals("USD")) {
            if (price.length() == 1) price = price + "00";
            if (price.length() == 2) price = price + "0";
        }
        int p = stringIntoInt(price);
        System.out.println("price = " + p);
        System.out.println("ticket = " + ticket);
        System.out.println("price * ticket = " + p*ticket);
        assertTrue("Общая сумма страховки не равняется сумме страховок каждого пассажира", s == p*ticket);
    }


    @Step("Нажать кнопку «Добавить в заказ»")
    private void clickAddMedicalButton(String type){
        SelenideElement ins = $("#medIns" + type);
        ins.scrollTo();
        ins.$(byXpath("descendant::a[contains(@class,'button--micro-padding')]"))
                .shouldBe(visible).shouldBe(exactText(text[5][ln])).click();
    }

    @Step("Проверка стоимости медицинской страховки")
    private void checkPriceOfMedicalInsurance(String type) {
        SelenideElement box = $("#medIns" + type);
        String price = box.$(byXpath("descendant::div[@class='tile__price']")).getText().replaceAll("\\D+","");
        String name = box.$(byXpath("descendant::div[@class='tile__title']")).getText();
        System.out.println("Type of medical insurance = " + name);
        SelenideElement p = $(byXpath("//div[@class='cart__item-priceondemand-item-title']" +
                "[contains(text(),'" + name + "')]")).shouldBe(visible);
        Sleep(3);
    }

    @Step("Проверка кнопки «В заказе»")
    private void checkMedicalButtonName(String type){
        $("#medIns" + type).$(byXpath("descendant::a[contains(@class,'button--micro-padding')]"))
                .shouldBe(visible).shouldBe(exactText(text[6][ln])).scrollTo();
    }

    @Step("Проверка общей суммы заказа (включает в себя стоимость услуг страхования)")
    private void checkTotalAndInsurensPrices(){
        String itemPrice;
        String flyPrice = $(byXpath("//div[@class='cart__item-price']")).scrollTo().getText().replaceAll("\\D+","");
        int summ = stringIntoInt(flyPrice);
        System.out.println("Fly price = " + flyPrice);
        ElementsCollection items = $("#left-column-insurance-block").$$(byXpath("descendant::div[@class='cart__item-priceondemand-item-price']"));
        for (int i=0; i<items.size(); i++) {
            itemPrice = items.get(i).getText().replaceAll("\\D+","");
            summ = summ + stringIntoInt(itemPrice);
            System.out.println("Item[" + (i+1) + "] price = " + itemPrice);
        }
        Sleep(3);
        String totalPrice = $("#cart-total-incarts").$(byXpath("descendant::div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        System.out.println("Total price = " + totalPrice);
        Values.price.total = totalPrice;
        assertTrue("Общая сумма заказа некорректна", summ == stringIntoInt(Values.price.total));
    }

    @Step("Проверка общей суммы заказа")
    private void checkTotalAndFlyPrices(){
        String flyPrice = $(byXpath("//div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        Values.price.total = $("#cart-total-incarts").$(byXpath("descendant::div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        System.out.println("Total price = " + Values.price.total);
        assertTrue("Общая сумма заказа некорректна", flyPrice.equals(Values.price.total));
    }

    @Step("Нажать кнопку «Транспорт»")
    public void clickTransportButton() {
        $(byXpath("//a[@class='next__button']")).shouldBe(visible).click();
        waitPlane();
    }

    @Step("Нажать кнопку выбора в полетной страховке")
    private void clickFlyInsuranceButton() {
        WebElement el = $("#flight_insurance_select_button").toWebElement();
        Actions actions = new Actions(getWebDriver());
        actions.moveToElement(el).perform();
        $("#flight_insurance_deselect_button").shouldBe(visible).click();
    }

    @Step("Проверить текст кнопки в полетной страховке")
    private void checkFlyInsuranceButton(String text){
        String name = $("#flight_insurance_select_button").getText();
        assertTrue("Название кнопки в блоке полетной страховки некорректно" +
                   "\nОжидалось: " + text +
                   "\nФактически: " + name,
                   name.equals(text));
    }



}
