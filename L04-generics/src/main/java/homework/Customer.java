package homework;

import java.util.*;

public class Customer implements Comparable {
    private long id;
    private String name;
    private long scores;

    //todo: 1. в этом классе надо исправить ошибки
    public Customer(long id, String name, long scores) {
        this.id = id;
        this.name = name;
        this.scores = scores;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScores() {
        return this.scores;
    }

    public void setScores(long scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", scores=" + scores +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (this.id != customer.id) return false;
        if (this.scores != customer.scores) return false;
        return name != null ? name.equals(customer.name) : customer.name == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
        result = 31 * result + (int) (this.scores ^ (this.scores >>> 32));
        return result;
    }

    @Override
    public int compareTo(Object obj) {
        Customer customer=(Customer) obj;
        return (this.getScores() < customer.getScores() ? -1 : (this.getScores() == customer.getScores() ? 0 : 1));
    }

    /*@Override
    public int compare(Object obj1, Object obj2) {
        // сортировка TreeMap на основании Scores
        Customer customer1=(Customer) obj1;
        Customer customer2=(Customer) obj2;
        if (customer1.getScores() > customer2.getScores()) return 1;
        else if (customer1.getScores() < customer2.getScores()) return -1;
        else return 0;
    }*/
}
