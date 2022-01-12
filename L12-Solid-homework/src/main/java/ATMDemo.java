import java.util.LinkedList;

public class ATMDemo {
    public static void main(String[] args) {
        // создадим банкомат
        ATM atm = new ATMImplementation();

        // добавим банкноты
        LinkedList<Banknote> banknotes = new LinkedList<>();
        banknotes.push(new BanknoteImplementation(Banknote.Nominal.nominal10));
        banknotes.push(new BanknoteImplementation(Banknote.Nominal.nominal10));
        banknotes.push(new BanknoteImplementation(Banknote.Nominal.nominal100));
        banknotes.push(new BanknoteImplementation(Banknote.Nominal.nominal1000));
        banknotes.push(new BanknoteImplementation(Banknote.Nominal.nominal500));
        banknotes.push(new BanknoteImplementation(Banknote.Nominal.nominal50));
        banknotes.push(new BanknoteImplementation(Banknote.Nominal.nominal50));
        atm.putBanknotes(banknotes);
        // выведем остаток бакномата
        atm.printRest();

        // попытаемся взять сумму которой нет в банокмате
        try {
            var outBanknotes = atm.getSum(1000000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // попытаемся взять сумму которая есть в банокмате
        try {
            var outBanknotes = atm.getSum(1660);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // выведем остаток
        atm.printRest();

        // добавим банкноты еще раз
        atm.putBanknotes(banknotes);
        // выведем остаток
        atm.printRest();
    }
}
