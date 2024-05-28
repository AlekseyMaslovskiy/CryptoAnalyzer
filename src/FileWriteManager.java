import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileWriteManager {
    List<String> source;

    public FileWriteManager(List<String> source) {
        this.source = source;
    }

    public void writeFile(Path path) throws IOException {
        Files.deleteIfExists(path);
        Files.createFile(path);
        StringBuilder strings = new StringBuilder();
        for (int i = 0; i < source.size(); i++) {
            strings.append(source.get(i));
            if (i != source.size() - 1) strings.append("\n");
        }
        Files.writeString(path, strings.toString());
    }
}
