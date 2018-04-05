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

    @Step("Действие 7, Нажать на кнопку «Транспорт», проверка данных на форме ESS")
    public void checkTransportEss1(List<Flight> flightList) {
        System.out.println("\t7. Check ESS form after Transport");
        clickTransportButton();
        checkTransportBlock();
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

    @Step("Действие 8, Проверка данных в блоке «Страховка»")
    public void step8() {
        System.out.println("\t8. Check Insurance group");

    }

    @Step("Действие 9, Проверка добавления Медицинской страховки {0}")
    public void step9(String type) {
        System.out.println("\t9. Add Medical Insurance");
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


    @Step("Нажать кнопку «Транспорт»")
    public void clickTransportButton() {
        $(byXpath("//a[@class='next__button']")).shouldBe(visible).click();
        waitPlane();
    }

    @Step("Проверка перехода в раздел «Транспорт»")
    private void checkTransportBlock(){
        $(byXpath("//div[@id='left-column-transport'][contains(@class,'--active')]")).
                shouldBe(visible).shouldBe(exactText(text[2][ln]));
    }

}
