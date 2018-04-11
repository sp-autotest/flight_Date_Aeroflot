package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import ru.yandex.qatools.allure.annotations.Step;
import struct.Flight;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static config.Values.lang;
import static config.Values.ln;
import static config.Values.ticket;

public class SearchPage extends Page {

    List<Flight> flightList = new ArrayList<Flight>();
    String dateThere = null;
    String dateBack = null;
    String duration = "";

    @Step("Действие 1, поиск рейсов")
    public void searchFlight1(String from, String to, int days) {
        selectLocale();
        setFrom(from);
        setTo(to);
        dateThere = addMonthAndDays(0,days);
        setThere(dateThere);
        clickSearchButton();
    }

    @Step("Действие 1, поиск рейсов")
    public void searchFlight2(String from, String to, int days, int backdays) {
        selectLocale();
        setFrom(from);
        setTo(to);
        dateThere = addMonthAndDays(0,days);
        setThere(dateThere);
        dateBack = addMonthAndDays(0,backdays);
        setBack(dateBack);
        clickSearchButton();
    }

    @Step("Действие 2, выбор рейсов")
    public List<Flight> selectFlight1() {
        selectSimpleFlight(1); // 1 - полет в одну сторону, туда
        clickBuyButton();
        saveFlightData(1); // 1 - сохранить данные только одного направления
        clickPassengersButton();
        return flightList;
    }

    @Step("Действие 2, выбор рейсов")
    public List<Flight> selectFlight2() {
        selectSimpleFlight(1); // 2 - полет туда
        clickBuyButton();
        selectSimpleFlight(2); // 2 - полет обратно
        clickBuyButton();
        saveFlightData(2); // 2 - сохранить данные двух направлений
        clickPassengersButton();
        return flightList;
    }

    @Step("Действие 2, выбор рейсов")
    public List<Flight> selectFlight3() {
        selectTransferFlight(1); // 2 - полет туда
        clickBuyButton();
        selectTransferFlight(2); // 2 - полет обратно
        clickBuyButton();
        saveFlightData(2); // 2 - сохранить данные двух направлений
        clickPassengersButton();
        return flightList;
    }

    @Step("Выбрать язык: {0}")
    private void selectLocale() {
        if (getWebDriver().manage().window().getSize().getWidth() < 1280) {
            $(byXpath("//div[@class='header__menu-icon']/..")).shouldBe(visible).click();
        }
        $(byXpath("//div[@class='header__select-items']")).shouldBe(visible).click();
        $(byXpath("//div[text()='" + lang[ln][1] + "']")).shouldBe(visible).click();
        $(byXpath("//input[@class='input__text-input']")).shouldBe(visible);
    }

    @Step("Выбрать город вылета: {0}")
    private void setFrom(String city) {
        $$(byXpath("//input[@class='input__text-input']")).get(0).shouldBe(visible).setValue(city);
    }

    @Step("Выбрать город прибытия: {0}")
    private void setTo(String city) {
        $$(byXpath("//input[@class='input__text-input']")).get(1).shouldBe(visible).setValue(city);

    }

    @Step("Указать дату \"Туда\": {0}")
    private void setThere(String date) {
        date = date.substring(0,2)+"."+date.substring(2,4)+"."+date.substring(4);
        SelenideElement el = $$(byXpath("//input[@class='input__text-input']")).get(2);
        while(!el.getValue().equals(date)) {
            el.setValue(date);
        }
        el.click();
    }

    @Step("Указать дату \"Обратно\": {0}")
    private void setBack(String date) {
        SelenideElement el = $$(byXpath("//input[@class='input__text-input']")).get(3);
        el.setValue(date);
        el.$(byXpath("../../label")).click();

    }

    @Step("Нажать \"Найти\"")
    private void clickSearchButton() {
        $(byXpath("//a[contains(@class,'button--wide')]")).shouldBe(visible).click();
    }

    @Step("Выбрать рейс")
    private void selectSimpleFlight(int i) {
        $(byXpath("//div[contains(@class,'frame__heading')]")).shouldBe(visible);
        Sleep(2);
        ElementsCollection headers = $$(byXpath("//div[@class='row flight-search__header']"));
        ElementsCollection flights = headers.get(i-1).$$(byXpath("following-sibling::*"));
        //int limit = flights.size();
        flights.get(0).click();//выбираем первый рейс из предложенных
    }

    @Step("Выбрать рейс")
    private void selectTransferFlight(int i) {
        $(byXpath("//div[contains(@class,'frame__heading')]")).shouldBe(visible);
        Sleep(2);
        ElementsCollection headers = $$(byXpath("//div[@class='row flight-search__header']"));
        ElementsCollection flights = headers.get(i-1).$$(byXpath("following-sibling::*"));
        int limit = flights.size();
        flights.get(limit-1).click();//выбираем последний рейс из предложенных
    }

    @Step("Нажать \"Купить\"")
    private void clickBuyButton() {
        saveDuration();
        ElementsCollection buttons = $$(byXpath("//a[contains(@class,'button--bordered')][@aria-disabled='false']"));
        if (buttons.size()>0) {
            buttons.get(0).shouldBe(visible).click();
        } else {
            $(byXpath("//a[contains(@class,'button--bordered')]")).shouldBe(visible).click();
        }
    }

    @Step("Нажать \"Указать пассажиров\"")
    private void clickPassengersButton() {
        $(byXpath("//a[@class='next__button']")).shouldBe(visible).click();
    }

    private static String addMonthAndDays(int months, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, months);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return new java.text.SimpleDateFormat("ddMMyyyy").format(cal.getTime());
    }

    private void saveFlightData(int dir) {
        Values.price.fly = $(byXpath("//div[@class='cart__item-price js-popover']")).getText().replaceAll("\\D+","");
        System.out.println("Fly price = " + Values.price.fly);
        Flight f;
        String d;
        ElementsCollection groups = $$(byXpath("//div[@class='flight-search flight-search--active']"));
        for (int m=0; m < dir; m++) {
            ElementsCollection flights = groups.get(m).$$(byXpath("descendant::div[@class='row flight-search__flights']"));
            for (int i = 0; i < flights.size(); i++) {
                ElementsCollection el = flights.get(i).$$(byXpath("child::*"));
                if (el.size() < 3) {
                    String transfer = flights.get(i).$(byXpath("descendant::div[@class='flight-search__transfer']/span")).getText();
                    String[] arr = transfer.split(" "); //заносим информацию о пересадке в массив
                    int minutes = stringIntoInt(arr[1])*60; //переводим часы ожидания в минуты
                    if (arr.length>3) minutes = minutes + stringIntoInt(arr[3]); //добавляем минуты ожидания, если указаны
                    flightList.get(flightList.size() - 1).transfer = minutes*60000; //сохраняем время ожидания в милисекундах
                    continue;
                }
                f = new Flight();
                f.from = flights.get(i).$(byXpath("descendant::div[@class='time-destination__from']/div[@class='time-destination__airport']")).getText();
                f.to = flights.get(i).$(byXpath("descendant::div[@class='time-destination__to']/div[@class='time-destination__airport']")).getText();
                f.number = flights.get(i).$(byXpath("descendant::div[@class='flight-search__plane-number']")).getText();
                d = (m==0) ? dateThere : dateBack;
                d = d + " " + flights.get(i).$(byXpath("descendant::div[@class='time-destination__from']/div[@class='time-destination__time']")).getText();
                f.start = stringToDate(d);
                d = (m==0) ? dateThere : dateBack;
                d = d + " " + flights.get(i).$(byXpath("descendant::div[@class='time-destination__to']/div[@class='time-destination__time']")).getText();
                f.end = stringToDate(d);
                f.duration = duration.substring(0, duration.indexOf(" "));
                duration = duration.substring(duration.indexOf(" ")+1);
                flightList.add(f);
            }
        }
        for(Flight fl : flightList) {
            System.out.println(fl.from + " / " + fl.to + " / " + fl.number + " / " + fl.duration +
                    " [" + fl.start + "] [" + fl.end + "] " + fl.transfer);
        }
    }

    private Date stringToDate(String d) {
        Date parsingDate=null;
        try {
            parsingDate = new SimpleDateFormat("ddMMyyyy HH:mm").parse(d);
        }catch (ParseException e) {
            System.out.println("Parsing date error");
        }
        return parsingDate;
    }

    private void saveDuration() {
        String temp;
        $(byXpath("//a[contains(@class,'modal__close')]")).shouldBe(visible);
        ElementsCollection dur = $$(byXpath("//div[@class='flight__date']"));
        for (int i=1; i<dur.size(); i=i+2) {
            int last = dur.get(i).$$(byXpath("descendant::span")).size();
            temp = dur.get(i).$(byXpath("descendant::span["+last+"]")).getText();
            if (temp.indexOf("：")>0) {
                temp = temp.substring(temp.indexOf("：") + 2);
            } else {
                temp = temp.substring(temp.indexOf(":") + 2);
            }
            temp = temp.substring(0, temp.length() - 3);
            if (temp.contains(" ")){
                String[] arr = temp.split(" ");
                duration = duration + arr[0];
                if (arr.length>2) {
                    if (arr[2].length() == 1) duration = duration + "0" + arr[2];
                    else duration = duration + arr[2];
                } else duration = duration + "00";
            }else {
                for (int c = 0; c < temp.length(); c++) {
                    if (Character.isDigit(temp.charAt(c))) {
                        duration = duration + temp.charAt(c);
                    }
                }
            }
            duration = duration + " ";
        }
    }


}
