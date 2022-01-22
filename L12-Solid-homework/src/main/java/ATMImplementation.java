import java.util.*;

public class ATMImplementation implements ATM {

    private final Map<Banknote.Nominal, BanknoteCell> cells = new HashMap<>();

    public ATMImplementation() {
        EnumSet.allOf(Banknote.Nominal.class)
                .forEach(nominal -> cells.put(nominal, new BanknoteCell(nominal)));
    }

    @Override
    public void putBanknote(Banknote banknote) {
        cells.get(banknote.getNominal()).pushBanknote(banknote);
    }

    @Override
    public void putBanknotes(LinkedList<Banknote> banknotes) {
        banknotes.forEach(this::putBanknote);
    }

    @Override
    public void printRest() {
        long rest = getRest();
        System.out.println("Остаток: " + rest);
        EnumSet.allOf(Banknote.Nominal.class)
                .forEach(nominal -> System.out.println("номинал " + nominal.nominal() + ": " + cells.get(nominal).getCount()));
    }

    @Override
    public int getRest() {
        return cells.values().stream().mapToInt(BanknoteCell::getRest).sum();
    }

    @Override
    public LinkedList<Banknote> getSum(long summ) {
        System.out.println("Попытка выдать сумму: " + summ);
        if (summ > getRest()) {
            throw new RuntimeException("Сумму " + summ + " выдать нельзя");
        } else {
            LinkedList<Banknote> outBanknotes = new LinkedList<>();
            List<Banknote.Nominal> toSort = new ArrayList<>(EnumSet.allOf(Banknote.Nominal.class));
            toSort.sort(Comparator.comparing(Banknote.Nominal::nominal).reversed());
            for (Banknote.Nominal nominal : toSort) {
                while (summ % cells.get(nominal).getNominal().nominal() < summ && cells.get(nominal).getRest() > 0) {
                    Banknote banknote = cells.get(nominal).popBanknote();
                    summ -= banknote.getNominal().nominal();
                    outBanknotes.push(banknote);
                }
            }
            return outBanknotes;
        }
    }
}
