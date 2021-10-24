package homework;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private static TreeMap<Customer, String> customerTree = new TreeMap<Customer, String>();

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        Customer minCustomer=new Customer(customerTree.firstKey().getId(),customerTree.firstKey().getName(),customerTree.firstKey().getScores());
        return new java.util.AbstractMap.SimpleEntry<Customer, String>(minCustomer, customerTree.get(customerTree.firstKey()));
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        TreeMap<Customer, String> rebalancedTree=new TreeMap<Customer, String>();
        rebalancedTree.putAll(customerTree);
        Map.Entry<Customer, String> nextEntry;
        if(!rebalancedTree.containsKey(customer)){
            rebalancedTree.put(customer,"unexistingString");
            nextEntry=rebalancedTree.higherEntry(customer);
            rebalancedTree.remove(customer,"unexistingString");
        }else
        {
            nextEntry=rebalancedTree.higherEntry(customer);
        }
        customerTree=rebalancedTree;
        return nextEntry;
    }

    public void add(Object customer, String data) {
        customerTree.put((Customer) customer, data);
    }
}
