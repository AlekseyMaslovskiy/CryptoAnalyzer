import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileReadManager {
    Path path;
    List<String> source;

    public FileReadManager(Path path) {
        this.path = path;
    }

    public boolean isExist() {
        return Files.isRegularFile(path);
    }

    public void readFile() throws IOException {
        source = Files.readAllLines(path);
    }

    public List<String> getSource() {
        return source;
    }
}
