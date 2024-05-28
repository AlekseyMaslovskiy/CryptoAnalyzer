public class Decryptor {
    InputDataBox inputDataBox;
    Encryptable encryptable;

    public Decryptor(InputDataBox inputDataBox, Encryptable encryptable) {
        this.inputDataBox = inputDataBox;
        this.encryptable = encryptable;
    }

    OutputDataBox decrypt() {
        return encryptable.decrypt(inputDataBox.getSource(), inputDataBox.getKey());
    }
}
