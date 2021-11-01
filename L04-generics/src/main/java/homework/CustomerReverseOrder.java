package homework;

import java.util.Deque;
import java.util.ArrayDeque;

public class CustomerReverseOrder {

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"
    private final Deque<Customer> stackCustomer = new ArrayDeque<Customer>();

    public void add(Customer customer) {
        // добавляем элемент в конец
        stackCustomer.push(customer);
    }

    public Customer take() {
        // берем последний элемент
        return stackCustomer.pop();
    }
}
