package ru.otus.testFramework.framework;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassTestGenerator<T> {

    private final ClassScanner classScanner;

    public ClassTestGenerator(ClassScanner classScanner) {
        this.classScanner = classScanner;
    }

    public List<ClassSingleTest<T>> generateTests(Class<T> testClass) {
        List<ClassSingleTest<T>> result = new ArrayList<>();
        ClassInfo<T> classInfo = classScanner.scanClass(testClass);
        for (Method test : classInfo.getTestsList()) {
            ClassSingleTest<T> classSingleTest = new ClassSingleTest<>(
                    classInfo.getConstructor(),
                    classInfo.getBefore(),
                    test,
                    classInfo.getAfter()
            );
            result.add(classSingleTest);
        }
        return result;
    }
}
