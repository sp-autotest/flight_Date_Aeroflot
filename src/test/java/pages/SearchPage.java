package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.yandex.qatools.allure.annotations.Step;
import struct.Flight;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static config.Values.lang;



public class SearchPage extends Page {

    List<Flight> flightList = new ArrayList<Flight>();
    String dateThere = null;
    String dateBack = null;
    String duration = "";

    @Step("Действие 1, поиск рейсов")
    public void searchFlight1(int ln, String from, String to, int days) {
        System.out.println("\t1. Search flight");
        setFrom(from);
        setTo(to);
        dateThere = addMonthAndDays(0,days);
        setThere(dateThere);
        clickSearchButton();
    }

    @Step("Действие 1, поиск рейсов")
    public void searchFlight2(int ln, String from, String to, int days, int backdays) {
        System.out.println("\t1. Search flight");
        setFrom(from);
        setTo(to);
        dateThere = addMonthAndDays(0,days);
        setThere(dateThere);
        dateBack = addMonthAndDays(0,backdays);
        setBack(dateBack);
        clickSearchButton();
    }

    @Step("Действие 1, поиск рейсов")
    public void searchFlight4(int ln, String from, String to, String from2, int days, int backdays) {
        System.out.println("\t1. Search flight");
        setFrom(from);
        setTo(to);
        dateThere = addMonthAndDays(0,days);
        setThere(dateThere);
        clickComplexRouteLink();
        clickAddFlightButton();
        setFrom2(from2);
        setTo2(from);
        dateBack = addMonthAndDays(0,backdays);
        setThere2(dateBack);
        clickSearchButton();
    }

    @Step("Действие 2, выбор рейсов")
    public List<Flight> selectFlight1(int ln) {
        System.out.println("\t2. Select flight");
        selectSimpleFlight(1); // 1 - полет в одну сторону, туда
        clickBuyButton();
        saveFlightData(ln, 1); // 1 - сохранить данные только одного направления
        clickPassengersButton();
        return flightList;
    }

    @Step("Действие 2, выбор рейсов")
    public List<Flight> selectFlight2(int ln) {
        System.out.println("\t2. Select flight");
        selectSimpleFlight(1); // 2 - полет туда
        clickBuyButton();
        selectSimpleFlight(2); // 2 - полет обратно
        clickBuyButton();
        saveFlightData(ln, 2); // 2 - сохранить данные двух направлений
        clickPassengersButton();
        return flightList;
    }

    @Step("Действие 2, выбор рейсов")
    public List<Flight> selectFlight3(int ln) {
        System.out.println("\t2. Select flight");
        selectTransferFlight(1); // 2 - полет туда
        clickBuyButton();
        selectTransferFlight(2); // 2 - полет обратно
        clickBuyButton();
        saveFlightData(ln, 2); // 2 - сохранить данные двух направлений
        clickPassengersButton();
        return flightList;
    }

    @Step("Выбрать язык: {0}")
    private void selectLocale(int ln) {
        if (getWebDriver().manage().window().getSize().getWidth() < 1280) {
            $(byXpath("//div[@class='header__menu-icon']/..")).shouldBe(visible).click();
        }
        //$(byXpath("//div[@class='header__select-items']")).shouldBe(visible).click();
        //$(byXpath("//div[text()='" + lang[ln][1] + "']")).shouldBe(visible).click();
        JavascriptExecutor executor = (JavascriptExecutor) getWebDriver();
        executor.executeScript("document.getElementsByClassName('input input--phantom')[0].style.display='block';");
        $(byXpath("//div[@class='input__select']/select")).selectOptionByValue(lang[ln][2]);
        $(byXpath("//input[@class='input__text-input']")).shouldBe(visible);
    }

    @Step("Выбрать город вылета: {0}")
    private void setFrom(String city) {
        $$(byXpath("//input[@class='input__text-input']")).get(0).shouldBe(visible).setValue(city);
        $(byXpath("//div[@class='search-form__dropdown-item-code'][text()='" + city + "']")).click();
    }

    @Step("Выбрать город прибытия: {0}")
    private void setTo(String city) {
        $$(byXpath("//input[@class='input__text-input']")).get(1).shouldBe(visible).setValue(city);
        $(byXpath("//div[@class='search-form__dropdown-item-code'][text()='" + city + "']")).click();
        Sleep(1);
    }

    @Step("Указать дату \"Туда\": {0}")
    private void setThere(String date) {
        date = date.substring(0,2)+"."+date.substring(2,4)+"."+date.substring(4);
        WebElement el = $$(byXpath("//input[@class='input__text-input']")).get(2).toWebElement();
        System.out.println("date there = " + el.getAttribute("value"));
        System.out.println(el.getSize().toString());
        while(!el.getAttribute("value").equals(date)) {
            Actions actions = new Actions(getWebDriver());
            actions.moveToElement(el, 5, 15)
                    .clickAndHold()
                    .moveByOffset((int)(el.getSize().width/2)+10, 0)
                    .release()
                    .perform();
            el.sendKeys(date);
        }
        el.click();
        SelenideElement ele = $$(byXpath("//input[@class='input__text-input']")).get(3);
        ele.click();
        ele.$(byXpath("../../label")).click();
    }

    @Step("Указать дату \"Обратно\": {0}")
    private void setBack(String date) {
        System.out.println("Date back = " + date);
        SelenideElement el = $$(byXpath("//input[@class='input__text-input']")).get(3);
        el.click();
        el.setValue(date);
        el.$(byXpath("../../label")).click();
    }

    @Step("Нажать \"Найти\"")
    private void clickSearchButton() {
        SelenideElement el = $(byXpath("//a[@class='button button--wide button--lg']")).shouldBe(visible);
        scrollWithOffset(el, 0, -100);
        el.click();
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

    @Step("Нажать \"Составить сложный маршрут\"")
    private void clickComplexRouteLink() {
        $(byXpath("//label[@class='input__label']")).shouldBe(visible).click();
        $(byXpath("//a[@class='search-form__change-route']")).shouldBe(visible).click();
    }

    @Step("Нажать \"Добавить перелет\"")
    private void clickAddFlightButton() {
        $(byXpath("//div[@class='search-form__add-route']/a")).shouldBe(visible).click();
    }

    @Step("Выбрать город вылета: {0}")
    private void setFrom2(String city) {
        $$(byXpath("//input[@class='input__text-input']")).get(3).shouldBe(visible).setValue(city);
        $(byXpath("//div[@class='search-form__dropdown-item-code'][text()='" + city + "']")).click();
    }

    @Step("Выбрать город прибытия: {0}")
    private void setTo2(String city) {
        $$(byXpath("//input[@class='input__text-input']")).get(4).shouldBe(visible).setValue(city);
        $(byXpath("//div[@class='search-form__dropdown-item-code'][text()='" + city + "']")).click();
    }

    @Step("Указать дату вылета: {0}")
    private void setThere2(String date) {
        $$(byXpath("//input[@class='input__text-input']")).get(5).shouldBe(visible).setValue(date);
        $(byXpath("//label[@class='input__label']")).shouldBe(visible).click();
    }

    private static String addMonthAndDays(int months, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, months);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return new java.text.SimpleDateFormat("ddMMyyyy").format(cal.getTime());
    }

    private void saveFlightData(int ln, int dir) {
        Flight f;
        String d;
        ElementsCollection groups = $$(byXpath("//div[@class='flight-search flight-search--active']"));
        for (int m=0; m < dir; m++) { //цикл перебора направлений полета, максимум 2: туда и обратно
            ElementsCollection flights = groups.get(m).$$(byXpath("descendant::div[@class='row flight-search__flights']"));
            System.out.println("Flights in group = " + flights.size());
            for (int i = 0; i < flights.size(); i++) { //цикл перебора маршрутов
                ElementsCollection el = flights.get(i).$$(byXpath("child::*"));
                if (el.size() < 3) {
                    SelenideElement trans = flights.get(i).$(byXpath("descendant::div[@class='flight-search__transfer']/span"));
                    System.out.println("TRANS = " + trans.getText());
                    if (trans.getAttribute("class").equals("h-color--orange")) {
                        /*если это сложный маршрут, необходимо занести оранжевую дату обратного
                        вылета в уже записанную в List дату второго маршрута */
                        f = flightList.get(flightList.size()-1);
                        String orangeDate = new java.text.SimpleDateFormat("ddMMyyyy").format(sTd(ln, trans.getText()));
                        d = orangeDate + " " + flights.get(i-1).$(byXpath("descendant::div[@class='time-destination__from']/div[@class='time-destination__time']")).getText();
                        f.start = stringToDate(d);
                        d = orangeDate + " " + flights.get(i-1).$(byXpath("descendant::div[@class='time-destination__to']/div[@class='time-destination__time']")).getText();
                        f.end = stringToDate(d);
                        flightList.set(flightList.size()-1, f);
                        dateThere = orangeDate;
                    } else {
                        /*если это пересадка - сохранить ее в текущем маршруте*/
                        String transfer = trans.getText();
                        String[] arr = transfer.split(" "); //заносим информацию о пересадке в массив
                        int minutes = stringIntoInt(arr[1]) * 60; //переводим часы ожидания в минуты
                        if (arr.length > 3)
                            minutes = minutes + stringIntoInt(arr[3]); //добавляем минуты ожидания, если указаны
                        flightList.get(flightList.size() - 1).transfer = minutes * 60000; //сохраняем время ожидания в милисекундах
                    }
                    continue;
                }
                f = new Flight();
                f.from = flights.get(i).$(byXpath("descendant::div[@class='time-destination__from']/div[@class='time-destination__airport']")).getText();
                f.to = flights.get(i).$(byXpath("descendant::div[@class='time-destination__to']/div[@class='time-destination__airport']")).getText();

                SelenideElement num = flights.get(i).$(byXpath("descendant::div[@class='flight-search__plane-number']"));
                //f.number = num.getText();
                JavascriptExecutor executor = (JavascriptExecutor) getWebDriver();
                f.number = (String) executor.executeScript("return arguments[0].textContent;", num.toWebElement());

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

    private Date sTd(int ln, String d) {
        Date parsingDate=null;
        try {
            parsingDate = new SimpleDateFormat(Values.lang[ln][6], new Locale(Values.lang[ln][2])).parse(d);
        }catch (ParseException e) {
            System.out.println("Parsing date error");
        }
        return parsingDate;
    }

}
