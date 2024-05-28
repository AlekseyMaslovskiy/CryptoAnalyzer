import java.util.List;

public class OutputDataBox {
    List<String> result;
    int key;

    public OutputDataBox(List<String> result, int key) {
        this.result = result;
        this.key = key;
    }

    public List<String> getResult() {
        return result;
    }

    public int getKey() {
        return key;
    }
}
