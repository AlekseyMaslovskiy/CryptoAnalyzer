import java.util.List;

public interface Encryptable {
    OutputDataBox encrypt(List<String> source, int key);
    OutputDataBox decrypt(List<String> source, int key);
    OutputDataBox bruteforce(List<String> source);
    OutputDataBox statisticalAnalysis(List<String> source);
}
