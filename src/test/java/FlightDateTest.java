import com.codeborne.selenide.WebDriverRunner;
import config.Values;
import listeners.AllureOnEventListener;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.testng.TestNG;
import org.testng.annotations.*;
import pages.*;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Title;
import struct.Flight;
import struct.Passenger;
import java.io.IOException;
import java.util.List;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static pages.Page.getLanguageNumber;
import static pages.Page.stringIntoInt;


@Listeners({AllureOnEventListener.class})  //"слушатель" для Allure-отчета
public class FlightDateTest {


    private String browserName = "chrome";//браузер, по умолчанию хром
    private int browserWidth = 1920;//ширина окна браузера, по умолчанию 1920
    private int browserHight = 1080;//высота окна браузера, по умолчанию 1080

    @BeforeClass/* Метод, выполняющийся перед началом тест-сьюта */
    public void begin() {
        browserName = System.getProperty("browser", "chrome");//получить имя браузера из дженкинса, при неудаче браузер=хром
        String res = System.getProperty("resolution", "1920x1080");//получить разрешение браузера из дженкинса, при неудаче разрешение=1920x1080
        browserWidth = stringIntoInt(res.substring(0, res.indexOf("x")));//взять ширину браузера из строки с разрешением
        browserHight = stringIntoInt(res.substring(res.indexOf("x")+1));//взять высоту браузера из строки с разрешением
        System.out.println("Browser = " + browserName);//вывести в лог значение имени браузера
        System.out.println("Resolution = " + res);//вывести в лог значение разрешения
        Values.office_login = System.getProperty("officelogin", "any");//получить логин АРМ ESS из дженкинса
        Values.office_password = System.getProperty("officepassword", "");//получить пароль АРМ ESS из дженкинса
    }

    @BeforeMethod
    public void start() {
        com.codeborne.selenide.Configuration.browser = browserName;   //браузер для тестов
        com.codeborne.selenide.Configuration.timeout = 60000;         //максимальный интервал ожидания вебэлементов в милисекундах
        com.codeborne.selenide.Configuration.savePageSource = false;  //не сохранять дополнительные настройки
        WebDriver myWebDriver = null;
        switch (browserName) {
            case "chrome":
                ChromeOptions options = new ChromeOptions();  //создать обьект для установки опций браузера хром
                options.addArguments("--disable-infobars");   //убрать в браузере полосу infobars
                options.addArguments("--disable-dev-tools");  //отключить в браузере dev-tools
                myWebDriver = new ChromeDriver(options);  //создать вебдрайвер с  указанными выше опциями
                break;
            case "firefox":
                myWebDriver = new FirefoxDriver();
                break;
            case "ie":
                myWebDriver = new InternetExplorerDriver();
                break;
            case "opera":
                OperaOptions oOptions = new OperaOptions();
                oOptions.setBinary("C:\\Program Files\\Opera\\launcher.exe");
                myWebDriver = new OperaDriver(oOptions);
                break;
        }
        WebDriverRunner.setWebDriver(myWebDriver); //запуск браузера
        myWebDriver.manage().window().setSize(new Dimension(browserWidth, browserHight));
    }

    @AfterMethod
    public void stop() throws IOException {
        getWebDriver().quit();

        //костыль, для того чтобы закрыть оперу, т.к. в ее драйвере есть баг
        // https://github.com/operasoftware/operachromiumdriver/issues/44
        if (browserName.equals("opera")) Runtime.getRuntime().exec("taskkill /f /im opera.exe");
    }

    @AfterClass
    public void tearDown() {
        getWebDriver().quit();
    }

    @DataProvider
    public Object[][] parseLocaleData1() {
        return new Object[][]{
                /*{"Французский", "EUR", "MOW", "PRG", 20},
                {"Испанский",   "EUR", "MOW", "PRG", 20},
                {"Итальянский", "EUR", "MOW", "PRG", 20},
                {"Японский",    "USD", "MOW", "PRG", 20},
                {"Китайский",   "USD", "MOW", "PRG", 20},
                {"Английский",  "USD", "MOW", "PRG", 20},
                {"Корейский",   "RUB", "MOW", "PRG", 20},
                */{"Русский",     "RUB", "MOW", "PRG", 20},/*
                {"Немецкий",    "RUB", "MOW", "PRG", 20},
                {"Русский",     "CNY", "MOW", "PRG", 20},
                {"Китайский",   "CNY", "MOW", "PRG", 20},
                {"Немецкий",    "CNY", "MOW", "PRG", 20},

                {"Французский", "EUR", "MOW", "PRG", 30},
                {"Испанский",   "EUR", "MOW", "PRG", 30},
                {"Итальянский", "EUR", "MOW", "PRG", 30},
                {"Японский",    "USD", "MOW", "PRG", 30},
                {"Китайский",   "USD", "MOW", "PRG", 30},
                {"Английский",  "USD", "MOW", "PRG", 30},
                {"Корейский",   "RUB", "MOW", "PRG", 30},
                {"Русский",     "RUB", "MOW", "PRG", 30},
                {"Немецкий",    "RUB", "MOW", "PRG", 30},
                {"Русский",     "CNY", "MOW", "PRG", 30},
                {"Китайский",   "CNY", "MOW", "PRG", 30},
                {"Немецкий",    "CNY", "MOW", "PRG", 30},

                {"Французский", "EUR", "MOW", "PRG", 40},
                {"Испанский",   "EUR", "MOW", "PRG", 40},
                {"Итальянский", "EUR", "MOW", "PRG", 40},
                {"Японский",    "USD", "MOW", "PRG", 40},
                {"Китайский",   "USD", "MOW", "PRG", 40},
                {"Английский",  "USD", "MOW", "PRG", 40},
                {"Корейский",   "RUB", "MOW", "PRG", 40},
                {"Русский",     "RUB", "MOW", "PRG", 40},
                {"Немецкий",    "RUB", "MOW", "PRG", 40},
                {"Русский",     "CNY", "MOW", "PRG", 40},
                {"Китайский",   "CNY", "MOW", "PRG", 40},
                {"Немецкий",    "CNY", "MOW", "PRG", 40},

                {"Французский", "EUR", "MOW", "PRG", 50},
                {"Испанский",   "EUR", "MOW", "PRG", 50},
                {"Итальянский", "EUR", "MOW", "PRG", 50},
                {"Японский",    "USD", "MOW", "PRG", 50},
                {"Китайский",   "USD", "MOW", "PRG", 50},
                {"Английский",  "USD", "MOW", "PRG", 50},
                {"Корейский",   "RUB", "MOW", "PRG", 50},
                {"Русский",     "RUB", "MOW", "PRG", 50},
                {"Немецкий",    "RUB", "MOW", "PRG", 50},
                {"Русский",     "CNY", "MOW", "PRG", 50},
                {"Китайский",   "CNY", "MOW", "PRG", 50},
                {"Немецкий",    "CNY", "MOW", "PRG", 50},

                {"Французский", "EUR", "MOW", "PRG", 60},
                {"Испанский",   "EUR", "MOW", "PRG", 60},
                {"Итальянский", "EUR", "MOW", "PRG", 60},
                {"Японский",    "USD", "MOW", "PRG", 60},
                {"Китайский",   "USD", "MOW", "PRG", 60},
                {"Английский",  "USD", "MOW", "PRG", 60},
                {"Корейский",   "RUB", "MOW", "PRG", 60},
                {"Русский",     "RUB", "MOW", "PRG", 60},
                {"Немецкий",    "RUB", "MOW", "PRG", 60},
                {"Русский",     "CNY", "MOW", "PRG", 60},
                {"Китайский",   "CNY", "MOW", "PRG", 60},
                {"Немецкий",    "CNY", "MOW", "PRG", 60},*/
        };
    }

    @DataProvider
    public Object[][] parseLocaleData2() {
        return new Object[][]{
                /*{"Французский", "EUR", "MOW", "BKK", 25, 35},
                {"Испанский",   "EUR", "MOW", "BKK", 25, 35},
                {"Итальянский", "EUR", "MOW", "BKK", 25, 35},
                {"Японский",    "USD", "MOW", "BKK", 25, 35},
                {"Китайский",   "USD", "MOW", "BKK", 25, 35},
                {"Английский",  "USD", "MOW", "BKK", 25, 35},
                {"Корейский",   "RUB", "MOW", "BKK", 25, 35},
                */{"Русский",     "RUB", "MOW", "BKK", 25, 35},/*
                {"Немецкий",    "RUB", "MOW", "BKK", 25, 35},
                {"Русский",     "CNY", "MOW", "BKK", 25, 35},
                {"Китайский",   "CNY", "MOW", "BKK", 25, 35},
                {"Немецкий",    "CNY", "MOW", "BKK", 25, 35},

                {"Французский", "EUR", "MOW", "BKK", 35, 45},
                {"Испанский",   "EUR", "MOW", "BKK", 35, 45},
                {"Итальянский", "EUR", "MOW", "BKK", 35, 45},
                {"Японский",    "USD", "MOW", "BKK", 35, 45},
                {"Китайский",   "USD", "MOW", "BKK", 35, 45},
                {"Английский",  "USD", "MOW", "BKK", 35, 45},
                {"Корейский",   "RUB", "MOW", "BKK", 35, 45},
                {"Русский",     "RUB", "MOW", "BKK", 35, 45},
                {"Немецкий",    "RUB", "MOW", "BKK", 35, 45},
                {"Русский",     "CNY", "MOW", "BKK", 35, 45},
                {"Китайский",   "CNY", "MOW", "BKK", 35, 45},
                {"Немецкий",    "CNY", "MOW", "BKK", 35, 45},

                {"Французский", "EUR", "MOW", "BKK", 45, 55},
                {"Испанский",   "EUR", "MOW", "BKK", 45, 55},
                {"Итальянский", "EUR", "MOW", "BKK", 45, 55},
                {"Японский",    "USD", "MOW", "BKK", 45, 55},
                {"Китайский",   "USD", "MOW", "BKK", 45, 55},
                {"Английский",  "USD", "MOW", "BKK", 45, 55},
                {"Корейский",   "RUB", "MOW", "BKK", 45, 55},
                {"Русский",     "RUB", "MOW", "BKK", 45, 55},
                {"Немецкий",    "RUB", "MOW", "BKK", 45, 55},
                {"Русский",     "CNY", "MOW", "BKK", 45, 55},
                {"Китайский",   "CNY", "MOW", "BKK", 45, 55},
                {"Немецкий",    "CNY", "MOW", "BKK", 45, 55},

                {"Французский", "EUR", "MOW", "BKK", 55, 65},
                {"Испанский",   "EUR", "MOW", "BKK", 55, 65},
                {"Итальянский", "EUR", "MOW", "BKK", 55, 65},
                {"Японский",    "USD", "MOW", "BKK", 55, 65},
                {"Китайский",   "USD", "MOW", "BKK", 55, 65},
                {"Английский",  "USD", "MOW", "BKK", 55, 65},
                {"Корейский",   "RUB", "MOW", "BKK", 55, 65},
                {"Русский",     "RUB", "MOW", "BKK", 55, 65},
                {"Немецкий",    "RUB", "MOW", "BKK", 55, 65},
                {"Русский",     "CNY", "MOW", "BKK", 55, 65},
                {"Китайский",   "CNY", "MOW", "BKK", 55, 65},
                {"Немецкий",    "CNY", "MOW", "BKK", 55, 65}*/
        };
    }

    @DataProvider
    public Object[][] parseLocaleData3() {
        return new Object[][]{
                /*{"Французский", "EUR", "VIE", "LED", 27, 47},
                {"Испанский",   "EUR", "VIE", "LED", 27, 47},
                {"Итальянский", "EUR", "VIE", "LED", 27, 47},
                {"Японский",    "USD", "VIE", "LED", 27, 47},
                {"Китайский",   "USD", "VIE", "LED", 27, 47},
                {"Английский",  "USD", "VIE", "LED", 27, 47},
                {"Корейский",   "RUB", "VIE", "LED", 27, 47},
                */{"Русский",     "RUB", "VIE", "LED", 27, 47},/*
                {"Немецкий",    "RUB", "VIE", "LED", 27, 47},
                {"Русский",     "CNY", "VIE", "LED", 27, 47},
                {"Китайский",   "CNY", "VIE", "LED", 27, 47},
                {"Немецкий",    "CNY", "VIE", "LED", 27, 47},*/

                /*{"Французский", "EUR", "VIE", "LED", 37, 57},
                {"Испанский",   "EUR", "VIE", "LED", 37, 57},
                {"Итальянский", "EUR", "VIE", "LED", 37, 57},
                {"Японский",    "USD", "VIE", "LED", 37, 57},
                {"Китайский",   "USD", "VIE", "LED", 37, 57},
                {"Английский",  "USD", "VIE", "LED", 37, 57},
                {"Корейский",   "RUB", "VIE", "LED", 37, 57},
                {"Русский",     "RUB", "VIE", "LED", 37, 57},
                {"Немецкий",    "RUB", "VIE", "LED", 37, 57},
                {"Русский",     "CNY", "VIE", "LED", 37, 57},
                {"Китайский",   "CNY", "VIE", "LED", 37, 57},
                {"Немецкий",    "CNY", "VIE", "LED", 37, 57},

                {"Французский", "EUR", "VIE", "LED", 47, 67},
                {"Испанский",   "EUR", "VIE", "LED", 47, 67},
                {"Итальянский", "EUR", "VIE", "LED", 47, 67},
                {"Японский",    "USD", "VIE", "LED", 47, 67},
                {"Китайский",   "USD", "VIE", "LED", 47, 67},
                {"Английский",  "USD", "VIE", "LED", 47, 67},
                {"Корейский",   "RUB", "VIE", "LED", 47, 67},
                {"Русский",     "RUB", "VIE", "LED", 47, 67},
                {"Немецкий",    "RUB", "VIE", "LED", 47, 67},
                {"Русский",     "CNY", "VIE", "LED", 47, 67},
                {"Китайский",   "CNY", "VIE", "LED", 47, 67},
                {"Немецкий",    "CNY", "VIE", "LED", 47, 67}*/
        };
    }

    @Title("Направление Туда")
    @Description("Карта VISA;\nБилеты: 1 взрослый;\nДополнительные услуги: «Полетная страховка»")
    @Test(priority = 1, dataProvider = "parseLocaleData1")
    public void directionFrom(String locale, String currency, String from, String to, int days) {
        Values.ln = getLanguageNumber(locale);
        Values.cur = currency;
        Values.ticket = 1;
        System.out.println("============================================================"+
                "\n*** AUTOTEST *** : direction From, " + Values.lang[Values.ln][2].toUpperCase()+
                ", " + currency + ", " + from + "->" + to + ", " + days +"days" +
                "\n============================================================");
        open(Values.host);
        SearchPage searchPg = new SearchPage();
        searchPg.searchFlight1(from, to, days);//шаг 1
        List<Flight> flightList = searchPg.selectFlight1();//шаг 2
        new PassengerPage().step3();//шаг 3
        new PlacePage().clickPay();//кликнуть Оплатить на странице выбора места
        ChoosePage choosePg = new ChoosePage();
        choosePg.step4();//шаг 4(смена валюты) и 5
        EssPage essPg = new EssPage();
        essPg.checkEss1(flightList);//шаг 6
        essPg.checkTransportEss1(flightList);//шаг 7
        essPg.checkHotelEss1(flightList);//шаг 8
        essPg.clickContinue();//шаг 9
        choosePg.chooseTestStend("9");//шаг 9
        EprPage eprPg = new EprPage();
        eprPg.checkDataOnPayPage("10", flightList);//шаг 10
        eprPg.clickPay();//шаг 11
        new PaymentPage().setCardDetails();//шаг 12
        new ResultPage().checkServicesData(flightList);//шаг 13
        new OfficePage().checkLog(flightList);//шаг 14
        new SabrePage().checkSabreLog(flightList);//шаг 15
    }


    @Title("Направление Туда-Обратно")
    @Description("Карта VISA;\nБилеты: 1 взрослый;\nДополнительные услуги: «Полетная страховка»")
    @Test(priority = 2, dataProvider = "parseLocaleData2")
    public void directionRoundtrip(String locale, String currency, String from, String to, int days, int backdays) {
        Values.ln = getLanguageNumber(locale);
        Values.cur = currency;
        Values.ticket = 1;
        System.out.println("============================================================"+
                "\n*** AUTOTEST *** : direction Roundtrip, " + Values.lang[Values.ln][2].toUpperCase()+
                ", " + currency + ", " + from + "->" + to + ", " + days +"days, " + backdays +"days" +
                "\n============================================================");
        open(Values.host);
        SearchPage searchPg = new SearchPage();
        searchPg.searchFlight2(from, to, days, backdays);//шаг 1
        List<Flight> flightList = searchPg.selectFlight2();//шаг 2
        new PassengerPage().step3();//шаг 3
        new PlacePage().clickPay();//кликнуть Оплатить на странице выбора места
        ChoosePage choosePg = new ChoosePage();
        choosePg.step4();//шаг 4(смена валюты) и 5
        EssPage essPg = new EssPage();
        essPg.checkEss1(flightList);//шаг 6
        essPg.checkTransportEss1(flightList);//шаг 7
        essPg.checkHotelEss1(flightList);//шаг 8
        essPg.clickContinue();//шаг 9
        choosePg.chooseTestStend("9");//шаг 9
        EprPage eprPg = new EprPage();
        eprPg.checkDataOnPayPage("10", flightList);//шаг 10
        eprPg.clickPay();//шаг 11
        new PaymentPage().setCardDetails();//шаг 12
        new ResultPage().checkServicesData(flightList);//шаг 13
        new OfficePage().checkLog(flightList);//шаг 14
        new SabrePage().checkSabreLog(flightList);//шаг 15
    }


    @Title("Направление Туда-Обратно(с пересадкой)")
    @Description("Карта VISA;\nБилеты: 1 взрослый;\nДополнительные услуги: «Полетная страховка»")
    @Test(priority = 3, dataProvider = "parseLocaleData3")
    public void directionRoundtripTransfer(String locale, String currency, String from, String to, int days, int backdays) {
        Values.ln = getLanguageNumber(locale);
        Values.cur = currency;
        Values.ticket = 1;
        System.out.println("======================================================================================="+
                "\n*** AUTOTEST *** : direction Roundtrip with transfer, " + Values.lang[Values.ln][2].toUpperCase()+
                ", " + currency + ", " + from + "->" + to + ", " + days +"days, " + backdays +"days" +
                "\n=======================================================================================");
        open(Values.host);
        SearchPage searchPg = new SearchPage();
        searchPg.searchFlight2(from, to, days, backdays);//шаг 1
        List<Flight> flightList = searchPg.selectFlight3();//шаг 2
        new PassengerPage().step3();//шаг 3
        new PlacePage().clickPay();//кликнуть Оплатить на странице выбора места
        ChoosePage choosePg = new ChoosePage();
        choosePg.step4();//шаг 4(смена валюты) и 5
        EssPage essPg = new EssPage();
        essPg.checkEss1(flightList);//шаг 6
        essPg.checkTransportEss1(flightList);//шаг 7
        essPg.checkHotelEss1(flightList);//шаг 8
        essPg.clickContinue();//шаг 9
        choosePg.chooseTestStend("9");//шаг 9
        EprPage eprPg = new EprPage();
        eprPg.checkDataOnPayPage("10", flightList);//шаг 10
        eprPg.clickPay();//шаг 11
        new PaymentPage().setCardDetails();//шаг 12
        new ResultPage().checkServicesData(flightList);//шаг 13
        new OfficePage().checkLog(flightList);//шаг 14
        new SabrePage().checkSabreLog(flightList);//шаг 15
    }
}
