package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import ru.yandex.qatools.allure.annotations.Step;
import struct.Flight;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$$;
import static config.Values.*;
import static org.testng.AssertJUnit.assertTrue;


public class ResultPage extends Page {


    @Step("Действие 13, проверка страницы результатов оплаты")
    public void checkServicesData(List<Flight> flyList) {
        System.out.println("\t13. Cheking final page with pay result");
        ElementsCollection flights = $$(byXpath("//div[@class='flight-booking__group']"));
        for (int i = 0; i < flights.size(); i++) {
            checkFlight(i + 1, flyList.get(i), flights.get(i));
        }
    }

    @Step("Проверка данных о {0}-м маршруте")
    private void checkFlight(int i, Flight f, SelenideElement flight){
        String from = flight.$(byXpath("descendant::div[@class='time-destination__from']/div[@class='time-destination__airport']")).getText();
        System.out.print(from + " / ");
        assertTrue("Направление «Откуда» в маршруте отличается от забронированного" +
                "\nОжидалось: " + f.from +"\nФактически: " + from, from.equals(f.from));

        String to = flight.$(byXpath("descendant::div[@class='time-destination__to']/div[@class='time-destination__airport']")).getText();
        System.out.print(to + " / ");
        assertTrue("Направление «Куда» в маршруте отличается от забронированного" +
                "\nОжидалось: " + f.to +"\nФактически: " + to, to.equals(f.to));

        String number = flight.$(byXpath("descendant::div[@class='flight-booking__flight-number']")).getText();
        System.out.print(number + " / ");
        assertTrue("Номер рейса в маршруте отличается от забронированного" +
                   "\nОжидалось: " + f.number +"\nФактически: " + number, number.equals(f.number));

        String duration = flight.$(byXpath("descendant::div[@class='flight-booking__flight-time']")).getText().replaceAll("[^0-9]", "");
        System.out.print(duration + " / ");
        assertTrue("Длительность перелета в маршруте отличается от забронированного" +
                "\nОжидалось: " + f.duration +"\nФактически: " + duration, duration.equals(f.duration));

        String date = flight.$(byXpath("descendant::div[@class='flight-booking__day-title']")).getText();
        date = date.substring(0, date.indexOf(",")-2).trim();
        System.out.print(date + " / ");
        String fdate = new SimpleDateFormat("d MMMM yyyy", new Locale(Values.lang[ln][2])).format(f.start);
        assertTrue("Дата прилета отличается от забронированной" +
                "\nОжидалось: " + fdate +"\nФактически: " + date, date.equals(fdate));

        String start = flight.$(byXpath("descendant::div[@class='time-destination__from']/div[@class='time-destination__time']")).getText();
        System.out.print(start + " / ");
        String fstart = new SimpleDateFormat("HH:mm").format(f.start);
        assertTrue("Время вылета отличается от забронированного" +
                   "\nОжидалось: " + fstart +"\nФактически: " + start, start.equals(fstart));

        String end = flight.$(byXpath("descendant::div[@class='time-destination__to']/div[@class='time-destination__time']")).getText();
        System.out.println(end);
        String fend = new SimpleDateFormat("HH:mm").format(f.end);
        assertTrue("Время прилета отличается от забронированного" +
                "\nОжидалось: " + fend +"\nФактически: " + end, end.equals(fend));
    }


}
