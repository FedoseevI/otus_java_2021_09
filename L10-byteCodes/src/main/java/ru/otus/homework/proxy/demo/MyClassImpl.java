package ru.otus.homework.proxy.demo;

import ru.otus.homework.proxy.annotation.Log;

public class MyClassImpl implements MyClassInterface {

    @Log
    @Override
    public void function1(String str) {
        System.out.println("with @Log");
    }

    @Log
    @Override
    public void function2(String str) {
        System.out.println("with @Log");
    }

    @Log
    @Override
    public int function3(int int1, int int2) {
        System.out.println("with @Log");
        System.out.println("int1 + 10 = " + (int1 + 10));
        return int1 + 10;
    }

    @Override
    public void function4(long long1) {
        System.out.println("without @Log");
    }

    @Override
    public void function5(long long1) {
        System.out.println("without @Log");
    }

    @Log
    @Override
    public void function6() {
        System.out.println("with @Log");
    }

}
