package tests;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestRunner {

    public static void main(String[] args) throws Exception {

        String[] includedGroups = {"group1", "group2"}; // replace with your included groups

        XmlSuite suite = new XmlSuite();
        suite.setName("Dynamic TestNG Suite");

        XmlTest test = new XmlTest(suite);
        test.setName("Dynamic TestNG Test");

        List<XmlClass> classes = getXmlClassesFromIncludedGroups(includedGroups);
        test.setXmlClasses(classes);

        String xmlString = suite.toXml();
        System.out.println(xmlString);
    }

    public static List<XmlClass> getXmlClassesFromIncludedGroups(String[] includedGroups) throws Exception {

        // You can modify this method to retrieve XmlClasses based on your logic
        // This example code simply returns a hard-coded list of XmlClasses

//        List<String> a = getClassNamesFromPackage(System.getProperty("user.dir")+"/src/test/java/tests");
//        System.out.println(a);
        getAllTestcaseClass("tests");

        XmlClass class1 = new XmlClass("com.example.tests.TestClass1");
        XmlInclude include1 = new XmlInclude("testMethod1");
//        include1.setInvocationCount(5);
        class1.getIncludedMethods().add(include1);

        XmlClass class2 = new XmlClass("com.example.tests.TestClass2");

        return Collections.singletonList(class1);
    }

    public static List<String> getClassNamesFromPackage(String packageName) throws Exception {

        List<String> classNames = new ArrayList<>();
        String path = packageName.replace('.', '/');
        System.out.println(path);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);
        File file = new File(resource.getFile());

        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                classNames.addAll(getClassNamesFromPackage(packageName + "." + f.getName()));
            } else if (f.getName().endsWith(".class")) {
                String className = packageName + '.' + f.getName().substring(0, f.getName().length() - 6);
                classNames.add(className);
            }
        }
        return classNames;
    }

    public static void getClasses() throws IOException {
        // Specify the package to scan
        String packageName = "basics";

        // Create a Reflections object to scan the package
//        Reflections reflections = new Reflections(packageName);

        // Get all the classes in the package and its sub-packages
//        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class).stream()
                .collect(Collectors.toSet());


//        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

//        final ClassLoader loader = TestNGR.class.getClassLoader();
//        Set<Class<? extends Object>> allClasses = new HashSet<Class<? extends Object>>();
//        for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
//            if (info.getName().startsWith("tests.")) {
//                allClasses.add(info.load());
//            }
//        }

        // Iterate over the classes and print their names and packages
        for (Class<?> cls : classes) {
            System.out.println(cls.getName());
        }
    }

    private static Reflections getAllTestcaseClass(String testcasePackagePath) {

        final ConfigurationBuilder config = new ConfigurationBuilder()
                .setScanners(new ResourcesScanner(), new SubTypesScanner(false))
                .setUrls(ClasspathHelper.forPackage(testcasePackagePath))
                .filterInputsBy(new FilterBuilder().includePackage(testcasePackagePath));

        final Reflections reflect = new Reflections(config);
        Set<Class<?>> classes = reflect.getSubTypesOf(Object.class).stream()
                .collect(Collectors.toSet());
        for (Class<?> cls : classes) {
            System.out.println(cls.getName());
        }
        return reflect;
    }
}

