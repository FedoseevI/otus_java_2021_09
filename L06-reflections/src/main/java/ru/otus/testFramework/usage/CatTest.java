package ru.otus.testFramework.usage;

import ru.otus.testFramework.annotations.After;
import ru.otus.testFramework.annotations.Before;
import ru.otus.testFramework.annotations.Test;

public class CatTest {

    private Cat cat;

    @Before
    public void init() {
        cat = new Cat("Мурка",4,10);
        System.out.printf("%ninitialized cat = " + cat+"%n");
    }

    @Test
    public void testgetName() {
        String name = cat.getName();
        System.out.println("cat name = " + name);
    }

    @Test
    public void testgetAge() {
        int age = cat.getAge();
        System.out.println("cat age = " + age);
    }

    @Test
    public void testgetWeight() {
        int weight = cat.getWeight();
        System.out.println("cat weight = " + weight);
    }

    @Test
    public void testsetAge0() {
        cat.setAge(0);
        System.out.println("cat age set to "+cat.getAge());
    }

    @Test
    public void testsetAge1() {
        cat.setAge(1);
        System.out.println("cat age set to "+cat.getAge());
    }

    @Test
    public void testsetWeight0() {
        cat.setWeight(0);
        System.out.println("cat age set to "+cat.getWeight());
    }

    @Test
    public void testsetWeight1() {
        cat.setWeight(1);
        System.out.println("cat age set to "+cat.getWeight());
    }

    @After
    public void destroy() {
        cat = null;
        System.out.printf("cat destoyed%n");
    }
}
