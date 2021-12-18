package ru.otus.testFramework.framework;

import ru.otus.testFramework.annotations.After;
import ru.otus.testFramework.annotations.Before;
import ru.otus.testFramework.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ClassScanner {

    public <T> ClassInfo<T> scanClass(Class<T> clazz) {
        ClassInfo<T> classInfo = new ClassInfo<>();
        Constructor<T> constructor = getConstructor(clazz);
        classInfo.setConstructor(constructor);
        Method[] classMethods = clazz.getMethods();
        boolean beforeExists = false;
        boolean afterExists = false;

        for (Method method : classMethods) {
            if (method.isAnnotationPresent(Before.class)) {
                if (beforeExists) {
                    throw new RuntimeException("only one annotation Before allowed");
                }
                classInfo.setBefore(method);
                beforeExists = true;
            }
            if (method.isAnnotationPresent(Test.class)) {
                classInfo.getTestsList().add(method);
            }
            if (method.isAnnotationPresent(After.class)) {
                if (afterExists) {
                    throw new RuntimeException("only one annotation After allowed");
                }
                classInfo.setAfter(method);
                afterExists = true;
            }
        }
        return classInfo;
    }

    private <T> Constructor<T> getConstructor(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("cant find constructor", e);
        }
    }
}
