package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import ru.yandex.qatools.allure.annotations.Step;
import struct.Flight;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static config.Values.ln;
import static org.testng.AssertJUnit.assertTrue;


public class EprPage extends Page {

    @Step("Действие {0}, проверка данных на странице оплаты")
    public void checkDataOnPayPage(String n, List<Flight> flyList) {
        System.out.println("\t" + n + ". Checking data on Pay page");
        screenShot("Скриншот");
        ElementsCollection flights = $$(byXpath("//div[@class='flight__row']"));
        for (int i = 0; i < flights.size(); i++) {
            checkFlight(i + 1, flyList.get(i), flights.get(i));
        }
    }

    @Step("Проверка данных о {0}-м маршруте")
    private void checkFlight(int i, Flight f, SelenideElement flight){
        String from = flight.$(byXpath("descendant::div[@class='flight__direction-airport-code ng-binding']")).getText();
        System.out.print(from + " / ");
        assertTrue("Направление «Откуда» в маршруте отличается от забронированного" +
                   "\nОжидалось: " + f.from +"\nФактически: " + from, from.equals(f.from));

        String to = flight.$(byXpath("descendant::div[@class='flight__direction-airport-code ng-binding'][2]")).getText();
        System.out.print(to + " / ");
        assertTrue("Направление «Куда» в маршруте отличается от забронированного" +
                "\nОжидалось: " + f.to +"\nФактически: " + to, to.equals(f.to));

        String number = flight.$(byXpath("descendant::div[@class='flight__flight ng-binding']")).getText();
        number = number.substring(0, number.indexOf("\n"));
        System.out.print(number + "  / ");
        assertTrue("Номер рейса в маршруте отличается от забронированного" +
                   "\nОжидалось: " + f.number.replace(" ", "") +"\nФактически: " + number,
                   number.equals(f.number.replace(" ", "")));

        String duration = flight.$(byXpath("descendant::span[@duration='route.duration']")).getText().replaceAll("[^0-9]", "");
        System.out.print(duration + " ");
        assertTrue("Длительность перелета в маршруте отличается от забронированного" +
                   "\nОжидалось: " + f.duration +"\nФактически: " + duration, duration.equals(f.duration));

        String start = flight.$(byXpath("descendant::div[@class='flight__date ng-binding']")).getText();
        start = start.substring(start.indexOf(",")+1) + " " + new SimpleDateFormat("yyyy").format(f.start);
        Date dStart = new Date();
        try {
            dStart = new SimpleDateFormat("dd MMMM HH:mm yyyy", new Locale(Values.lang[ln][2])).parse(start);
            System.out.print("["+dStart+"] ");
        }catch (ParseException e) {
            System.out.println("Дата нераспаршена");
        }
        assertTrue("Время/дата вылета отличается от забронированного" +
                   "\nОжидалось: " + f.start +"\nФактически: " + dStart, dStart.equals(f.start));

        String end = flight.$(byXpath("descendant::div[@class='flight__date ng-binding'][2]")).getText();
        end = end.substring(end.indexOf(",")+1);
        end = end.substring(0, end.indexOf(",")) + " " + new SimpleDateFormat("yyyy").format(f.end);
        Date dEnd = new Date();
        try {
            dEnd = new SimpleDateFormat("dd MMMM HH:mm yyyy", new Locale(Values.lang[ln][2])).parse(end);
            System.out.println("["+dEnd+"] ");
        }catch (ParseException e) {
            System.out.println("Дата нераспаршена");
        }
        assertTrue("Время/дата прилета отличается от забронированного" +
                "\nОжидалось: " + f.end +"\nФактически: " + dEnd, dEnd.equals(f.end));
    }

    @Step("Нажать кнопку «Оплатить»")
    public void clickPayButton() {
        $(byXpath("//div[@class='next__button-inner ng-scope']")).shouldBe(visible).click();
        waitPlane();
    }


}
