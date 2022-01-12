import java.util.LinkedList;

public interface ATM {
    public void putBanknote(Banknote banknote);

    public void putBanknotes(LinkedList<Banknote> banknotes);

    public void printRest();

    public int getRest();

    public LinkedList<Banknote> getSum(long summ);
}
