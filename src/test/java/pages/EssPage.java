package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
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
import static config.Values.text;
import static org.testng.AssertJUnit.assertTrue;


public class EssPage extends Page {

    @Step("Действие 6, Проверка данных на форме ESS")
    public void checkEss(int ln, List<Flight> flightList) {
        System.out.println("\t6. Check ESS form");
        checkPageAppear();
        if (getWebDriver().manage().window().getSize().getWidth() < 1280) {
            $("#left-column-insurance-block").click();//раскрыть блок Страховка
            Sleep(1);
        }else moveMouseToFlight();
        screenShot("Скриншот");
        ElementsCollection flights = $$(byXpath("//div[@class='cart__item-details']"));
        //checkPriceData();
        for (int i = 0; i < flights.size(); i++) {
            checkFlightData(i+1, flightList, flights);
            checkNumberData(i+1, flightList, flights);
            checkDateData(ln, i+1, flightList, flights);
            checkDurationData(i+1, flightList, flights);
        }
    }

    @Step("Действие 7, Нажать на кнопку «Транспорт», проверка данных на форме ESS")
    public void checkTransportEss(int ln, List<Flight> flightList) {
        System.out.println("\t7. Check ESS form after Transport");
        clickTransportButton();
        checkTransportBlock(ln);
        if (getWebDriver().manage().window().getSize().getWidth() < 1280) {
            $("#left-column-transport").click();//раскрыть блок Транспорт
            Sleep(1);
        }else moveMouseToFlight();
        screenShot("Скриншот");
        ElementsCollection flights = $$(byXpath("//div[@class='cart__item-details']"));
        //checkPriceData();
        for (int i = 0; i < flights.size(); i++) {
            checkFlightData(i+1, flightList, flights);
            checkNumberData(i+1, flightList, flights);
            checkDateData(ln, i+1, flightList, flights);
            checkDurationData(i+1, flightList, flights);
        }
    }

    @Step("Действие 8, Нажать на кнопку «Отели», проверка данных на форме ESS")
    public void checkHotelEss(int ln, List<Flight> flightList) {
        System.out.println("\t8. Check ESS form after Hotel");
        clickHotelButton();
        checkHotelFormAppear(ln);
        if (getWebDriver().manage().window().getSize().getWidth() < 1280) {
            $(byXpath("//div[@class='cart__item cart__item--active']")).click();//раскрыть блок Проживание
            Sleep(1);
        }else moveMouseToFlight();
        screenShot("Скриншот");
        ElementsCollection flights = $$(byXpath("//div[@class='cart__item-details']"));
        //checkPriceData();
        for (int i = 0; i < flights.size(); i++) {
            checkFlightData(i+1, flightList, flights);
            checkNumberData(i+1, flightList, flights);
            checkDateData(ln, i+1, flightList, flights);
            checkDurationData(i+1, flightList, flights);
        }
    }

    @Step("Действие 9, Нажать Продолжить")
    public void clickContinue() {
        System.out.println("\t9. Click Continue button");
        $(byXpath("//a[@class='next__button']")).click();
        waitPlane();
    }

    public void skipHotel() {
        $(byXpath("//a[@class='cart__item-counter-link']")).click(); // пропуск отелей
        Sleep(15);
    }

    private void checkPageAppear(){
        $("#left-column-insurance-block").shouldBe(visible);
    }

    @Step("Проверка данных о стоимости")
    private void checkPriceData(){
        //String price = $(byXpath("//div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        //assertTrue("Стоимость не совпадает c указанной при бронировании" +
        //        "\nОжидалось: " + Values.price.fly +"\nФактически: " + price,
        //        price.equals(Values.price.fly));
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
    private void checkDateData(int ln, int i, List<Flight> flightList, ElementsCollection flights){
        String date = flights.get(i-1).$(byXpath("descendant::div[@class='h-color--gray h-mt--4']")).getText().replace(" ", "");
        int plus = date.indexOf("+");
        if (plus>0) date = date.substring(0, plus);
        else date = date.substring(0, date.indexOf("("));
        System.out.println("site = " + date);
        String format = Values.lang[ln][3];
        String month = new SimpleDateFormat("MMMM", new Locale(Values.lang[ln][2])).format(flightList.get(i-1).start);
        if (month.length()>5) format = format.replaceFirst("MMMM", "MMM.");
        String dd = new SimpleDateFormat(format, new Locale(Values.lang[ln][2])).format(flightList.get(i-1).start);
        dd = dd + new SimpleDateFormat("HH:mm").format(flightList.get(i-1).end);
        System.out.println("book = " + dd);
        assertTrue("Дата авиаперелета не совпадает с забронированной" +
                   "\nОжидалось : " + dd +"\nФактически: " + date, date.equals(dd));
    }

    @Step("Проверка данных о длительности {0}-го авиаперелета")
    private void checkDurationData(int i, List<Flight> flightList, ElementsCollection flights){
        String duration = "";
        String time = flights.get(i-1).$(byXpath("descendant::div[@class='h-color--gray h-mt--4']")).getText();
        time = time.substring(time.indexOf("("), time.indexOf(")")-1);
        String[] arr = time.split(" ");
        if (arr.length>1) {
            duration = arr[1].replaceAll("\\D+", "");
            if (duration.length() == 1) duration = "0" + duration;
        } else duration = "00";
        duration = arr[0].replaceAll("\\D+", "") + duration;
        System.out.println("site = " + duration);
        System.out.println("book = " + flightList.get(i-1).duration);
        assertTrue("Длительность авиаперелета не совпадает с забронированной" +
                   "\nОжидалось: " + flightList.get(i-1).duration +"\nФактически: " + duration,
                   duration.equals(flightList.get(i-1).duration));
    }


    @Step("Нажать кнопку «Транспорт»")
    private void clickTransportButton() {
        $(byXpath("//a[@class='next__button']")).shouldBe(visible).click();
        waitPlane();
    }

    @Step("Нажать кнопку «Отели»")
    private void clickHotelButton() {
        $(byXpath("//a[@class='next__button']")).shouldBe(visible).shouldBe(enabled).click();
        Sleep(2);
        $(byXpath("//div[@class='cart__item']")).shouldBe(visible).shouldBe(enabled).click();
        waitPlane();
    }

    @Step("Проверка перехода в раздел «Транспорт»")
    private void checkTransportBlock(int ln){
        $(byXpath("//div[@id='left-column-transport'][contains(@class,'--active')]")).
                shouldBe(visible).shouldBe(exactText(text[2][ln]));
        System.out.println("Transport form appeared");
    }

    @Step("Проверка перехода в раздел «Проживание»")
    private void checkHotelFormAppear(int ln) {
        $(byXpath("//h1[contains(text(),'" + text[17][ln] + "')]")).shouldBe(visible);
        System.out.println("Accommodation form appeared");
    }

    @Step("Переместить мышку в блок маршрутов")
    private void moveMouseToFlight() {
        SelenideElement el = $(byXpath("//div[@data-toggle-id='cart-booking']"));
        Actions actions = new Actions(getWebDriver());
        actions.moveToElement(el).perform();
        Sleep(1);
    }

}
