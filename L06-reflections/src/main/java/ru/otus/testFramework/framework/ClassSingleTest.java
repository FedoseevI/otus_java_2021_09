package ru.otus.testFramework.framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Optional;

public class ClassSingleTest<T> {

    private final Constructor<T> classConstructor;
    private final Method before;
    private final Method test;
    private final Method after;

    public ClassSingleTest(Constructor<T> classConstructor, Method before, Method test, Method after) {
        this.classConstructor = classConstructor;
        this.before = before;
        this.test = test;
        this.after = after;
    }

    public boolean runTest() {
        T classInstance = genClassInstance();
        boolean beforeResult = true;
        if (before != null) {
            beforeResult = execBefore(classInstance);
        }
        boolean testExecutionResult = false;
        if (beforeResult) {
            testExecutionResult = execTest(classInstance);
        }
        Optional.ofNullable(after)
                .ifPresent(method -> execAfter(classInstance));
        return testExecutionResult;
    }

    public T genClassInstance() {
        try {
            return this.classConstructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("failed initializing class constructor", e);
        }
    }

    public boolean execBefore(T instance) {
        try {
            before.invoke(instance);
            return true;
        } catch (Exception e) {
            System.err.printf("%s BEFORE execution failed%n", before.getName());
            e.printStackTrace();
            return false;
        }
    }

    public boolean execTest(T instance) {
        try {
            test.invoke(instance);
            return true;
        } catch (Exception e) {
            System.err.printf("execution of test %s was failed%n", test.getName());
            e.printStackTrace();
            return false;
        }
    }

    public void execAfter(T instance) {
        try {
            after.invoke(instance);
        } catch (Exception e) {
            System.err.printf("%s AFTER execution failed%n", after.getName());
            e.printStackTrace();
        }
    }
}
