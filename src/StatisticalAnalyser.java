public class StatisticalAnalyser {
    InputDataBox inputDataBox;
    Encryptable encryptable;

    public StatisticalAnalyser(InputDataBox inputDataBox, Encryptable encryptable) {
        this.inputDataBox = inputDataBox;
        this.encryptable = encryptable;
    }

    OutputDataBox statisticalAnalyse() {
        return encryptable.statisticalAnalysis(inputDataBox.getSource());
    }
}
