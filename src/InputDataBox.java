import java.util.List;

public class InputDataBox {
    List<String> source;
    int key;

    public InputDataBox(List<String> source, int key) {
        this.source = source;
        this.key = key;
    }

    public List<String> getSource() {
        return source;
    }

    public int getKey() {
        return key;
    }
}
