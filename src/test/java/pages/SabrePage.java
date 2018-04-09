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
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static config.Values.ln;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by mycola on 06.04.2018.
 */
public class SabrePage extends Page {


    @Step("Действие 15, проверка лога Sabre")
    public void checkSabreLog(List<Flight> flyList) {
        System.out.println("\t15. Cheking Sabre log");
        enterSabre();
        clickSessionOpenButton();
        setCommandField();
        clickCommandButton();
        String[] info = getSabreInfo();
        for (int i = 0; i < info.length; i++)  checkFlight(i + 1, flyList.get(i), info[i]);
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
    private void setCommandField(){
        $("#CommandRequest").setValue("*" + Values.pnr);
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
        String[] arr = log.split(" ");
        if (arr[2].length()==2) log = log.replaceFirst(arr[2]+" ", arr[2]+"0");
        arr = log.split(" ");
        //for(String s : arr) System.out.println(s);
        String find = f.number.replace(" ", "");
        assertTrue("Номер рейса не найден в логе" +
                   "\nЛог: " + log + "\nОжидалось :" + find + "\nФактически:" + arr[2].substring(0,arr[2].length()-1) +"",
                   find.equals(arr[2].substring(0,arr[2].length()-1)));

        find = new SimpleDateFormat("ddMMM", new Locale("en")).format(f.start).toUpperCase();
        assertTrue("Дата маршрута не найдена в логе" +
                   "\nЛог: " + log + "\nОжидалось :" + find + "\nФактически:" + arr[3], find.equals(arr[3]));

        find = f.from+f.to;
        assertTrue("Направление маршрута не найдено в логе" +
                   "\nЛог: " + log + "\nОжидалось :" + find + "\nФактически:" + arr[5], find.equals(arr[5]));

        find = new SimpleDateFormat("HHmm").format(f.start);
        assertTrue("Время вылета не найдено в логе" +
                   "\nЛог: " + log + "\nОжидалось :" + find + "\nФактически:" + arr[8], find.equals(arr[8]));

        find = new SimpleDateFormat("HHmm").format(f.end);
        assertTrue("Время прилета не найдено в логе" +
                   "\nЛог: " + log + "\nОжидалось :" + find + "\nФактически:" + arr[10], find.equals(arr[10]));
    }

}
