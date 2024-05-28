public class ActionExecutor {
    InputDataBox inputDataBox;
    Operation operation;

    public ActionExecutor(InputDataBox inputDataBox, Operation operation) {
        this.inputDataBox = inputDataBox;
        this.operation = operation;
    }

    public OutputDataBox execute() {
        return switch (operation) {
            case ENCRYPT -> new Encryptor(inputDataBox, new CaesarCipher()).encrypt();
            case DECRYPT -> new Decryptor(inputDataBox, new CaesarCipher()).decrypt();
            case BRUTEFORCE -> new Bruteforcer(inputDataBox, new CaesarCipher()).bruteforce();
            case STATISTICAL_ANALYSIS -> new StatisticalAnalyser(inputDataBox, new CaesarCipher()).statisticalAnalyse();
        };
    }
}
