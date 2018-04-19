package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import ru.yandex.qatools.allure.annotations.Step;
import struct.Passenger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.url;
import static config.Values.lang;



public class PassengerPage extends Page {

    List<Passenger> passengerList = new ArrayList<Passenger>();

    @Step("Действие 3, информация о пассажирах")
    public List<Passenger> step3(int ln, String cur) {
        System.out.println("\t3. Set Passengers info");
        checkPageAppear();
        if ((!cur.equals("RUB"))&(Values.currencyChange.equals("link"))) currencyChange(cur);
        fillPassengerData(ln);
        setEmail();
        setPhone();
        setTerms();
        clickContinueButton();
        return passengerList;
    }

    private void checkPageAppear(){
        $(byXpath("//div[@class='passenger-card__gender']")).shouldBe(exist);
    }

    private static String addMonthsFromToday(int months)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, months);
        return new java.text.SimpleDateFormat("ddMMyyyy").format(cal.getTime());
    }

    @Step("Заполнить данные пассажира")
    private void fillPassengerData(int ln){
        Passenger p = new Passenger();
        p.gender = setRandomGender();
        p.lastname = setRandomLastName();
        p.firstname = setRandomFirstName();
        p.dob = setDOB();
        p.number = setRandomNumber();
        setNationality(ln);
        setСountry(ln);
        clickUnlimitedLink();
        passengerList.add(p);
    }

    @Step("Указать пол")
    private int setRandomGender(){
        ElementsCollection gender = $$(byXpath("descendant::div[@class='passenger-card__gender']/a"));
        int g = getRandomNumberLimit(gender.size());
        gender.get(g).click();
        return g;
    }

    @Step("Указать фамилию")
    private String setRandomLastName(){
        String lastName = getRandomString(8);
        $$(byXpath("descendant::input[@type='text']")).get(0).setValue(lastName);
        return lastName;
    }

    @Step("Указать имя")
    private String setRandomFirstName(){
        String firstName = getRandomString(4);
        $$(byXpath("descendant::input[@type='text']")).get(1).setValue(firstName);
        return firstName;
    }

    @Step("Указать день рождения")
    private String setDOB(){
        String dob = addMonthsFromToday(-438);
        $$(byXpath("descendant::input[@type='text']")).get(3).setValue(dob);
        return dob;
    }

    @Step("Указать гражданство")
    private void setNationality(int ln){
        SelenideElement el = $$(byXpath("descendant::input[@role='listbox']")).get(0);
        while(!el.getValue().equals(lang[ln][4])) el.setValue(lang[ln][4]);
    }

    @Step("Указать страну выдачи")
    private void setСountry(int ln){
        SelenideElement el = $$(byXpath("descendant::input[@role='listbox']")).get(1);
        while(!el.getValue().equals(lang[ln][4])) el.setValue(lang[ln][4]);
    }

    @Step("Указать номер")
    private String setRandomNumber(){
        String number = getRandomNumberString(6);
        SelenideElement el = $$(byXpath("descendant::input[@type='text']")).get(4);
        el.click();
        el.setValue(number);
        return number;
    }

    @Step("Кликнуть \"Бессрочно\"")
    private void clickUnlimitedLink(){
        $(byXpath("descendant::a[contains(@class,'card__unlimited-link')]")).click();
    }

    @Step("Указать электронную почту")
    private void setEmail(){
        SelenideElement block = $(byXpath("//div[contains(@class,'--icon-contacts')]/following-sibling::div"));
        block.$(byXpath("descendant::input[@type='text']")).setValue(Values.email);
    }

    @Step("Указать телефон")
    private void setPhone(){
        String phone = getRandomNumberString(10);
        SelenideElement block = $(byXpath("//div[contains(@class,'--icon-contacts')]/following-sibling::div"));
        block.$$(byXpath("descendant::input[@type='text']")).get(1).setValue(phone);
    }

    @Step("Согласиться с правилами")
    private void setTerms(){
        $(byXpath("//label[@for='passangersTermsAndConditions']")).click();
    }

    @Step("Нажать \"Продолжить\"")
    private void clickContinueButton() {
        $(byXpath("//a[@class='next__button']")).shouldBe(visible).click();
        waitPlane();
        waitPlane();
    }

    @Step("Действие 4, смена валюты на: {0}")
    private void currencyChange(String currency) {
        String cur = "";
        if (!currency.equals("RUB")) {
            switch (currency) {
                case "USD":
                    cur = "us";
                    break;
                case "EUR":
                    cur = "fr";
                    break;
                case "CNY":
                    cur = "cn";
                    break;
            }
            String link = url();
            link = link.replaceFirst("app/ru", "app/" + cur);
            System.out.println("New link = " + link);
            getWebDriver().get(link);
            checkPageAppear();
        }
    }
}
