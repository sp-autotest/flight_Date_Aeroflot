package listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;


public class MyTransformer implements IAnnotationTransformer {

    // Do not worry about calling this method as testNG calls it behind the scenes before EVERY method (or test).
    // It will disable single tests, not the entire suite like SkipException

    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod){

        String enabled = null;
        if (annotation.getGroups()[0].equals("case1")) {
            enabled = System.getProperty("case1");
            System.out.println("case1 = " + enabled);
        }
        if (annotation.getGroups()[0].equals("case2")) {
            enabled = System.getProperty("case2");
            System.out.println("case2 = " + enabled);
        }
        if (annotation.getGroups()[0].equals("case3")) {
            enabled = System.getProperty("case3");
            System.out.println("case3 = " + enabled);
        }
        if (annotation.getGroups()[0].equals("case4")) {
            enabled = System.getProperty("case4");
            System.out.println("case4 = " + enabled);
        }
        if (enabled!=null) {
            annotation.setEnabled(enabled.equals("true"));
        }
    }
}
