public class Bruteforcer {
    InputDataBox inputDataBox;
    Encryptable encryptable;

    public Bruteforcer(InputDataBox inputDataBox, Encryptable encryptable) {
        this.inputDataBox = inputDataBox;
        this.encryptable = encryptable;
    }

    OutputDataBox bruteforce() {
        return encryptable.bruteforce(inputDataBox.getSource());
    }
}
