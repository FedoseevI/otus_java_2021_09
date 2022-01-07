import java.util.LinkedList;
import java.util.function.Consumer;

public class ATM {
    private final BanknoteCell cell10 = new BanknoteCell(10);
    private final BanknoteCell cell50 = new BanknoteCell(50);
    private final BanknoteCell cell100 = new BanknoteCell(100);
    private final BanknoteCell cell500 = new BanknoteCell(500);
    private final BanknoteCell cell1000 = new BanknoteCell(1000);

    public void putBanknote(Banknote banknote) {
        switch (banknote.getNominal()) {
            case (10):
                cell10.pushBanknote(banknote);
                break;
            case (50):
                cell50.pushBanknote(banknote);
                break;
            case (100):
                cell100.pushBanknote(banknote);
                break;
            case (500):
                cell500.pushBanknote(banknote);
                break;
            case (1000):
                cell1000.pushBanknote(banknote);
                break;
            default:
                throw new RuntimeException("Банкнота не поддерживается");
        }
    }

    public void putBanknotes(LinkedList<Banknote> banknotes) {
        banknotes.forEach(banknote -> putBanknote(banknote));
    }

    public void printRest() {
        long rest = getRest();
        System.out.println("Остаток: " + rest);
        System.out.println("номинал 10: " + cell10.getCount() + " шт");
        System.out.println("номинал 50: " + cell50.getCount() + " шт");
        System.out.println("номинал 100: " + cell100.getCount() + " шт");
        System.out.println("номинал 500: " + cell500.getCount() + " шт");
        System.out.println("номинал 1000: " + cell1000.getCount() + " шт");
    }

    public long getRest() {
        var rest = cell10.getRest() + cell50.getRest() + cell100.getRest() + cell500.getRest() + cell1000.getRest();
        return rest;
    }

    public LinkedList<Banknote> getSum(long summ) {
        System.out.println("Попытка выдать сумму: " + summ);
        if (summ > getRest()) {
            throw new RuntimeException("Сумму " + summ + " выдать нельзя");
        } else {
            LinkedList<Banknote> outBanknotes = new LinkedList<>();
            while (summ % cell1000.getNominal() < summ && cell1000.getRest() > 0) {
                Banknote banknote = cell1000.popBanknote();
                summ -= banknote.getNominal();
                outBanknotes.push(banknote);
            }
            while (summ % cell500.getNominal() < summ && cell500.getRest() > 0) {
                Banknote banknote = cell500.popBanknote();
                summ -= banknote.getNominal();
                outBanknotes.push(banknote);
            }
            while (summ % cell100.getNominal() < summ && cell100.getRest() > 0) {
                Banknote banknote = cell100.popBanknote();
                summ -= banknote.getNominal();
                outBanknotes.push(banknote);
            }
            while (summ % cell50.getNominal() < summ && cell50.getRest() > 0) {
                Banknote banknote = cell50.popBanknote();
                summ -= banknote.getNominal();
                outBanknotes.push(banknote);
            }
            return outBanknotes;
        }
    }
}
