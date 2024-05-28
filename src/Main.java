import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static JFrame frame;
    private static FlowLayout mainLayout;
    private static JTextField fileName;
    private static JButton openFileDialogButton;
    private static JTextField key;
    private static JPanel buttonsPanel;
    private static JButton encryptButton;
    private static JButton decryptButton;
    private static JButton bruteForceButton;
    private static JButton freqAnalyzeButton;
    private static JLabel log;

    public static void main(String[] args) {
        frame = new JFrame("Шифр Цезаря GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        createMainLayout();         // Создание главного слоя
        createOpenFileFields();     // Создание поля выбора файла и ввода ключа
        createButtonsPanel();       // Создание кнопок основных функцмй
        createLogField();           // Создание поля статуса
        addListeners();             // Добавление методов реакций на нажатие кнопок
        frame.setVisible(true);
    }

    private static void addListeners() {
        openFileDialogButton.addActionListener(e -> {
            File selectedFile = chooseFile();
            if (selectedFile != null) {
                fileName.setText(selectedFile.getAbsolutePath());
            }
        });
        encryptButton.addActionListener(e -> {
            long startTime = Instant.now().toEpochMilli();
            Path path = Path.of(fileName.getText());
            int key = Integer.parseInt(Main.key.getText());
            FileReadManager inputFile = new FileReadManager(path);
            if (inputFile.isExist()) {
                try {
                    inputFile.readFile();
                } catch (IOException ex) {
                    log.setForeground(Color.RED);
                    log.setText("Возникла ошибка!");
                    throw new RuntimeException(ex);
                }
                InputDataBox inputDataBox = new InputDataBox(inputFile.getSource(), key);
                OutputDataBox outputDataBox = new ActionExecutor(inputDataBox, Operation.ENCRYPT).execute();
                FileWriteManager outputFile = new FileWriteManager(outputDataBox.getResult());
                try {
                    outputFile.writeFile(path.getParent().resolve("(encrypted)" + path.getFileName()));
                } catch (IOException ex) {
                    log.setForeground(Color.RED);
                    log.setText("Возникла ошибка!");
                    throw new RuntimeException(ex);
                }
                long time = Instant.now().toEpochMilli() - startTime;
                log.setForeground(Color.BLACK);
                log.setText("<html>Файл " + path.getFileName() + " успешно зашифрован с ключом " + key + "<br/>"
                        + "Результат сохранён в директории исходного файла с именем:<br />" + "(encrypted)" + path.getFileName() + "<br/>"
                        + "Время затрачено " + time + " миллисекунд" + "</html>");
            } else {
                log.setForeground(Color.RED);
                log.setText("Файл не найден!");
            }
        });
        decryptButton.addActionListener(e -> {
            long startTime = Instant.now().toEpochMilli();
            Path path = Path.of(fileName.getText());
            int key = Integer.parseInt(Main.key.getText());
            FileReadManager inputFile = new FileReadManager(path);
            if (inputFile.isExist()) {
                try {
                    inputFile.readFile();
                } catch (IOException ex) {
                    log.setForeground(Color.RED);
                    log.setText("Возникла ошибка!");
                    throw new RuntimeException(ex);
                }
                InputDataBox inputDataBox = new InputDataBox(inputFile.getSource(), key);
                OutputDataBox outputDataBox = new ActionExecutor(inputDataBox, Operation.DECRYPT).execute();
                FileWriteManager outputFile = new FileWriteManager(outputDataBox.getResult());
                try {
                    outputFile.writeFile(path.getParent().resolve("(decrypted)" + path.getFileName()));
                } catch (IOException ex) {
                    log.setForeground(Color.RED);
                    log.setText("Возникла ошибка!");
                    throw new RuntimeException(ex);
                }
                long time = Instant.now().toEpochMilli() - startTime;
                log.setForeground(Color.BLACK);
                log.setText("<html>Файл " + path.getFileName() + " успешно расшифрован с ключом " + key + "<br/>"
                        + "Результат сохранён в директории исходного файла с именем:<br />" + "(decrypted)" + path.getFileName() + "<br/>"
                        + "Время затрачено " + time + " миллисекунд" + "</html>");
            } else {
                log.setForeground(Color.RED);
                log.setText("Файл не найден!");
            }
        });
        bruteForceButton.addActionListener(e -> {
            long startTime = Instant.now().toEpochMilli();
            Path path = Path.of(fileName.getText());
            FileReadManager inputFile = new FileReadManager(path);
            if (inputFile.isExist()) {
                try {
                    inputFile.readFile();
                } catch (IOException ex) {
                    log.setForeground(Color.RED);
                    log.setText("Возникла ошибка!");
                    throw new RuntimeException(ex);
                }
                InputDataBox inputDataBox = new InputDataBox(inputFile.getSource(), 0);
                OutputDataBox outputDataBox = new ActionExecutor(inputDataBox, Operation.BRUTEFORCE).execute();
                FileWriteManager outputFile = new FileWriteManager(outputDataBox.getResult());
                try {
                    outputFile.writeFile(path.getParent().resolve("(decrypted)" + path.getFileName()));
                } catch (IOException ex) {
                    log.setForeground(Color.RED);
                    log.setText("Возникла ошибка!");
                    throw new RuntimeException(ex);
                }
                long time = Instant.now().toEpochMilli() - startTime;
                log.setForeground(Color.BLACK);
                log.setText("<html>Ключ успешно подобран<br />" +
                        "Файл " + path.getFileName() + " успешно расшифрован с ключом " + outputDataBox.getKey() + "<br/>" +
                        "Результат сохранён в директории исходного файла с именем:<br />" + "(decrypted)" + path.getFileName() + "<br/>" +
                        "Время затрачено " + time + " миллисекунд" + "</html>");
                if (outputDataBox.getKey() == -1) {
                    log.setForeground(Color.RED);
                    log.setText("<html>Ключ не удалось подобрать</html>");
                }
            } else {
                log.setForeground(Color.RED);
                log.setText("Файл не найден!");
            }
        });
        freqAnalyzeButton.addActionListener(e -> {
            long startTime = Instant.now().toEpochMilli();
            Path path = Path.of(fileName.getText());
            FileReadManager inputFile = new FileReadManager(path);
            if (inputFile.isExist()) {
                try {
                    inputFile.readFile();
                } catch (IOException ex) {
                    log.setForeground(Color.RED);
                    log.setText("Возникла ошибка!");
                    throw new RuntimeException(ex);
                }
                InputDataBox inputDataBox = new InputDataBox(inputFile.getSource(), 0);
                OutputDataBox outputDataBox = new ActionExecutor(inputDataBox, Operation.STATISTICAL_ANALYSIS).execute();
                FileWriteManager outputFile = new FileWriteManager(outputDataBox.getResult());
                try {
                    outputFile.writeFile(path.getParent().resolve("(decrypted)" + path.getFileName()));
                } catch (IOException ex) {
                    log.setForeground(Color.RED);
                    log.setText("Возникла ошибка!");
                    throw new RuntimeException(ex);
                }
                long time = Instant.now().toEpochMilli() - startTime;
                log.setForeground(Color.BLACK);
                log.setText("<html>Ключ успешно подобран<br />" +
                        "Файл " + path.getFileName() + " успешно расшифрован с ключом " + outputDataBox.getKey() + "<br/>" +
                        "Результат сохранён в директории исходного файла с именем:<br />" + "(decrypted)" + path.getFileName() + "<br/>" +
                        "Время затрачено " + time + " миллисекунд" + "</html>");
                if (outputDataBox.getKey() == -1) {
                    log.setForeground(Color.RED);
                    log.setText("<html>Ключ не удалось подобрать</html>");
                }
            } else {
                log.setForeground(Color.RED);
                log.setText("Файл не найден!");
            }
        });
    }

    private static File chooseFile() {
        JFileChooser fileChooser = new JFileChooser(".");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("txt files", "txt");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile();
        else return null;
    }

    private static void createLogField() {
        log = new JLabel();
        log.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        log.setPreferredSize(new Dimension(470, 90));
        frame.add(log);
    }

    private static void createButtonsPanel() {
        buttonsPanel = new JPanel();
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonsPanel.setPreferredSize(new Dimension(480, 80));
        encryptButton = new JButton("Зашифровать");
        decryptButton = new JButton("Расшифровать");
        bruteForceButton = new JButton("BruteForce");
        freqAnalyzeButton = new JButton("Частотный анализ");
        encryptButton.setPreferredSize(new Dimension(150, 30));
        decryptButton.setPreferredSize(new Dimension(150, 30));
        bruteForceButton.setPreferredSize(new Dimension(150, 30));
        freqAnalyzeButton.setPreferredSize(new Dimension(150, 30));
        buttonsPanel.add(encryptButton);
        buttonsPanel.add(decryptButton);
        buttonsPanel.add(bruteForceButton);
        buttonsPanel.add(freqAnalyzeButton);
        frame.add(buttonsPanel);
    }

    private static void createOpenFileFields() {
        fileName = new JTextField();
        fileName.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        fileName.setPreferredSize(new Dimension(300, 20));
        openFileDialogButton = new JButton("...");
        openFileDialogButton.setPreferredSize(new Dimension(50, 20));
        frame.add(new JLabel("Путь к файлу:"));
        frame.add(fileName);
        frame.add(openFileDialogButton);
        key = new JTextField();
        key.setPreferredSize(new Dimension(50, 20));
        frame.add(new JLabel("Ключ:"));
        frame.add(key);
    }

    private static void createMainLayout() {
        mainLayout = new FlowLayout(FlowLayout.LEFT, 10, 10);
        frame.setLayout(mainLayout);
    }
}