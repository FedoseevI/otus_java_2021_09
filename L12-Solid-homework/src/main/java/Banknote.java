public interface Banknote {
    enum Nominal {
        nominal10(10),
        nominal50(50),
        nominal100(100),
        nominal500(500),
        nominal1000(1000);

        private int nominal;

        Nominal(int nominal) {
            this.nominal = nominal;
        }

        public int nominal() {
            return nominal;
        }
    }

    Nominal getNominal();
}
