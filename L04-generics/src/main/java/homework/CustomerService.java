package homework;

import java.util.Map;
import java.util.TreeMap;
import java.util.AbstractMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private static TreeMap<Customer, String> customerTree = new TreeMap<Customer, String>();

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        return getEntryCopy(customerTree.firstEntry());
    }

    private Map.Entry<Customer, String> getEntryCopy(Map.Entry<Customer, String> customerMapEntry) {
        if (customerMapEntry == null) {
            return null;
        }
        // возвращаем копию элемента
        return new AbstractMap.SimpleImmutableEntry<>(new Customer(customerMapEntry.getKey().getId(), customerMapEntry.getKey().getName(), customerMapEntry.getKey().getScores()), customerMapEntry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return getEntryCopy(customerTree.higherEntry(customer));
    }

    public void add(Object customer, String data) {
        customerTree.put((Customer) customer, data);
    }
}
