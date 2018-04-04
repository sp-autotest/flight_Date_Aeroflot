package listeners;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.io.IOException;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;


public class ScreenShoter {

    private  ScreenShoter(){};

    @Attachment(value = "{0}", type = "image/png")
    public static byte[] makeScreenshot(String name) throws IOException {
        return ((TakesScreenshot)getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

}
