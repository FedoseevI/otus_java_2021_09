package homework;

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

        return this.id == customer.id;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public int compareTo(Object obj) {
        Customer customer = (Customer) obj;
        return (this.getScores() < customer.getScores() ? -1 : (this.getScores() == customer.getScores() ? 0 : 1));
    }
}
