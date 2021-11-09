package ru.otus.testFramework.framework;

import java.util.List;

public class ClassTestExecutor {

    private ClassTestExecutor() {
    }

    public static <T> void testClass(Class<T> testClass) {

        ClassScanner classScanner = new ClassScanner();
        ClassTestGenerator testGenerator = new ClassTestGenerator<>(classScanner);

        List<ClassSingleTest<T>> tests = testGenerator.generateTests(testClass);

        String message = "%n%nstart testing class: %s%n";
        System.out.printf(message, testClass.getName());

        int cntSuccessTests = 0;

        for (ClassSingleTest<T> test : tests) {
            boolean testResullt = test.runTest();
            if (testResullt) {
                cntSuccessTests = cntSuccessTests + 1;
            }
        }
        message = "%ntest result statistics:%n   test count: %d%n   successful count: %d%n   failure count: %d%n";
        System.out.printf(message, tests.size(), cntSuccessTests, tests.size()-cntSuccessTests);
    }
}
