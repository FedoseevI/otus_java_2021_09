package ru.otus.testFramework.usage;

public class Cat {
    private final String name;
    private int weight;
    private int age;

    public Cat(String name, int weight, int age) {
        this.name = name;
        this.weight = weight;
        this.age = age;
    }

    public void setWeight(int weight) {
        if(weight<=0) throw new RuntimeException("cant set weight<=0");
        this.weight = weight;
    }

    public void setAge(int age) {
        if(age<=0) throw new RuntimeException("cant set age<=0");
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public int getWeight() {
        return this.weight;
    }

    public int getAge() {
        return this.age;
    }

    @Override
    public String toString() {
        return "name: " + name +
                "; weight:" + weight +
                "; age:" + age;
    }
}
