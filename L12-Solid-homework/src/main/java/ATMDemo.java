import java.util.LinkedList;

public class ATMDemo {
    public static void main(String[] args) {
        // создадим банкомат
        ATM atm = new ATM();

        // добавим банкноты
        LinkedList<Banknote> banknotes = new LinkedList<>();
        banknotes.push(new Banknote10());
        banknotes.push(new Banknote10());
        banknotes.push(new Banknote100());
        banknotes.push(new Banknote1000());
        banknotes.push(new Banknote500());
        banknotes.push(new Banknote50());
        banknotes.push(new Banknote50());
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
            var outBanknotes = atm.getSum(1600);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // выведем остаток
        atm.printRest();

        // добавим банкноты
        atm.putBanknotes(banknotes);
        // выведем остаток
        atm.printRest();
    }
}
