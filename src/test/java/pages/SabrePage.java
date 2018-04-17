package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import ru.yandex.qatools.allure.annotations.Step;
import struct.Flight;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by mycola on 06.04.2018.
 */
public class SabrePage extends Page {


    @Step("Действие 15, проверка лога Sabre")
    public void checkSabreLog(List<Flight> flyList, String pnr) {
        System.out.println("\t15. Cheking Sabre log");
        enterSabre();
        clickSessionOpenButton();
        setCommandField(pnr);
        clickCommandButton();
        String[] info = getSabreInfo();
        for (int i = 0; i < info.length; i++) {
            System.out.println("info"+i+" = "+info[i]);
            if (info[i].contains("/OPERATED")) continue;
            checkFlight(i + 1, flyList.get(i), info[i]);
        }
        clickSessionCloseButton();
    }

    @Step("Войти в терминал Sabre")
    private void enterSabre(){
        open(Values.sabrehost);
        checkSabreAppeared();
    }

    private void checkSabreAppeared(){
        $("#ButtonSessionOpen").shouldBe(visible);
    }

    @Step("Открыть сессию")
    private void clickSessionOpenButton(){
        $("#ButtonSessionOpen").click();
        $(byXpath("//div[@id='CommandData']/div/pre[text()='Сессия Открыта']")).shouldBe(visible);
    }

    @Step("Набрать команду вывода бронирования")
    private void setCommandField(String pnr){
        $("#CommandRequest").setValue("*" + pnr);
    }

    @Step("Отправить команду")
    private void clickCommandButton(){
        $("#CommandButton").click();
    }

    @Step("Получить информацию по бронированию")
    private String[] getSabreInfo(){
        SelenideElement info = $(byXpath("//div[@id='CommandData']/div/pre[2]")).shouldBe(visible);
        String text = info.getText();
        text = text.substring(text.indexOf("*ADT")+5, text.indexOf("TKT/TIME LIMIT"));
        return text.split("\n");
    }

    @Step("Закрыть сессию")
    private void clickSessionCloseButton(){
        $("#ButtonSessionClose").click();
        $(byXpath("//div[@id='CommandData']/div/pre[text()='Сессия Закрыта']")).shouldBe(visible);
    }

    @Step("Проверка данных о {0}-м маршруте")
    private void checkFlight(int i, Flight f, String log){
        System.out.println(log);
        String part = log.substring(3,9).replace(" ", "0");
        String find = f.number.replace(" ", "");
        assertTrue("Номер рейса не найден в логе" +
                   "\nЛог: " + log + "\nОжидалось :" + find + "\nФактически:" + part, find.equals(part));
        part = log.substring(11,16);
        find = new SimpleDateFormat("ddMMM", new Locale("en")).format(f.start).toUpperCase();
        assertTrue("Дата маршрута не найдена в логе" +
                   "\nЛог: " + log + "\nОжидалось :" + find + "\nФактически:" + part, find.equals(part));
        part = log.substring(19,25);
        find = f.from+f.to;
        assertTrue("Направление маршрута не найдено в логе" +
                   "\nЛог: " + log + "\nОжидалось :" + find + "\nФактически:" + part, find.equals(part));
        part = log.substring(31,35);
        find = new SimpleDateFormat("HHmm").format(f.start);
        assertTrue("Время вылета не найдено в логе" +
                   "\nЛог: " + log + "\nОжидалось :" + find + "\nФактически:" + part, find.equals(part));
        part = log.substring(37,41);
        find = new SimpleDateFormat("HHmm").format(f.end);
        assertTrue("Время прилета не найдено в логе" +
                   "\nЛог: " + log + "\nОжидалось :" + find + "\nФактически:" + part, find.equals(part));
    }

}
