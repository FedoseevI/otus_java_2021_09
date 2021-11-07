package ru.otus.testFramework.framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassInfo<T> {

    private Constructor<T> constructor;
    private Method before;
    private List<Method> testsList = new ArrayList<>();
    private Method after;


    public void setConstructor(Constructor<T> constructor) {
        this.constructor = constructor;
    }

    public void setBefore(Method beforeMethod) {
        this.before = beforeMethod;
    }

    public void setTestsList(List<Method> testsList) {
        this.testsList = testsList;
    }

    public void setAfter(Method after) {
        this.after = after;
    }

    public Constructor<T> getConstructor() {
        return this.constructor;
    }

    public Method getBefore() {
        return this.before;
    }

    public List<Method> getTestsList() {
        return this.testsList;
    }

    public Method getAfter() {
        return this.after;
    }
}
