package tests.Executor;

import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RunXML {

    @Test
    public void generateXMLPackage() throws Exception {
        List<XmlPackage> packages = new LinkedList<>();
        getAllSubPackages("tests")
                .stream()
                .map(XmlPackage::new)
//                .forEach(System.out::println);
                .forEach(packages::add);

        XmlSuite suite = new XmlSuite();
        suite.setName("PracticeREST");
//        getListeners().forEach(suite::addListener);
        suite.setPreserveOrder(true);

        XmlTest xmlTest = new XmlTest(suite);
        xmlTest.setName("TESTS");
        xmlTest.setPackages(packages);
        xmlTest.setIncludedGroups(getTestGroups());

        if (false) {    // true if parallel is required from file utility
            if (false) { // set true for classes
                suite.setParallel(XmlSuite.ParallelMode.CLASSES);
            } else if (false) {// set true for methods
                suite.setParallel(XmlSuite.ParallelMode.METHODS);
            } else {
                throw new RuntimeException("Not Supported PARALLEL_EXECUTION_TYPE: "+ "");
            }
            //xmlTest.setThreadCount(Integer.parseInt(configFileManager.getProperty(Config.THREAD_COUNT) == null ? "1" : configFileManager.getProperty(Config.THREAD_COUNT)));
            suite.setThreadCount(20); //deviceCount is the number to set thread count
        }

        List<XmlSuite> suites = new LinkedList<>();
        suites.add(suite);

        TestNG testng = new TestNG();
        testng.setUseDefaultListeners(false);
        testng.setXmlSuites(suites);
        testng.run();

        boolean isTestExecutionFailed = testng.hasFailure();
        if (isTestExecutionFailed)
            throw new RuntimeException("Test Execution Failed");
    }

    private List<String> getAllSubPackages(String packageNam) throws Exception {
//        File file = new File(System.getProperty("user.dir")+"/src/test/java/"+packageName);
//         Arrays.stream(file.list()).forEach(System.out::println);
//         Arrays.stream(Package.getPackages())
//                .map(Package::getName)
//                .filter(n -> n.startsWith(packageNam))
////                .collect(Collectors.toList());
//                .forEach(System.out::println);
        Path start = Paths.get(System.getProperty("user.dir")+"/src/test/java");
//        Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE);
//        stream.map(Path::toFile)
//                .filter(File::isDirectory)
//                .map(completePath -> completePath.getAbsolutePath().replace(System.getProperty("user.dir")+"\"src\"test\"java"+ File.separator, ""))
//                .filter(s -> s.startsWith(packageName))
//                .forEach(System.out::println);

        try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)) {
             return stream
                    .map(Path::toFile)
                    .filter(File::isDirectory)
                    .map(d -> d.getAbsolutePath().replace(System.getProperty("user.dir")+"/src/test/java" + File.separator, "").replace(File.separator, "."))
                    .filter(p -> p.startsWith(packageNam))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(String.format("Error while getting sub packages of %s. Error -> %s", packageNam, e.getMessage()));
            return null;
        }
//        return null;
    }

    private List<String> getTestGroups() {
        String testGroups = "";     // Get all group like regression, smoke
        if (!testGroups.isEmpty()) {
            System.out.println("Test groups to be executed : " + testGroups);
            return Arrays.stream(testGroups.split(",")).map(String::trim).collect(Collectors.toList());
        }
        return new ArrayList<String>();
    }
}
