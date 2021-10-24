package homework;

import java.util.LinkedList;

public class CustomerReverseOrder {

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"
    private static final LinkedList<Customer> customerList = new LinkedList<Customer>();

    public void add(Customer customer) {
        // добавляем элемент в конец
        customerList.push(customer);
    }

    public Customer take() {
        // берем последний элемент
        return customerList.pop();
    }
}
