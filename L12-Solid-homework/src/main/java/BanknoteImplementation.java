public class BanknoteImplementation implements Banknote {
    private final Nominal nominal;

    public BanknoteImplementation(Nominal nominal) {
        this.nominal = nominal;
    }

    @Override
    public Nominal getNominal() {
        return this.nominal;
    }
}
