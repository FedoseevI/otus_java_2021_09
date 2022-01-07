import com.sun.jdi.ClassType;

import java.util.LinkedList;

public class BanknoteCell {
    private final LinkedList<Banknote> banknotes = new LinkedList<>();
    private int nominal;
    private long rest = 0;
    private int count = 0;

    public BanknoteCell(int nominal) {
        this.nominal = nominal;
    }

    public void pushBanknote(Banknote banknote) {
        banknotes.push(banknote);
        count++;
        rest += banknote.getNominal();
        System.out.println("в ячейку для номинала " + nominal + " добавлена купюра номиналом " + banknote.getNominal());
    }

    public Banknote popBanknote() {
        Banknote result = banknotes.pop();
        rest -= result.getNominal();
        count--;
        System.out.println("из ячейки для номинала " + nominal + " выдана купюра номиналом " + result.getNominal());
        return result;
    }

    public long getRest() {
        return rest;
    }

    public int getCount() {
        return count;
    }

    public int getNominal() {
        return nominal;
    }

}
