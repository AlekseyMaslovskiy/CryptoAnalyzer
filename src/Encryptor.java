public class Encryptor {
    InputDataBox inputDataBox;
    Encryptable encryptable;

    public Encryptor(InputDataBox inputDataBox, Encryptable encryptable) {
        this.inputDataBox = inputDataBox;
        this.encryptable = encryptable;
    }

    OutputDataBox encrypt() {
        return encryptable.encrypt(inputDataBox.getSource(), inputDataBox.getKey());
    }
}
