import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private static JButton btn4;
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
        openFileDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(".");
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "txt files", "txt");
                fileChooser.setFileFilter(filter);
                int returnVal = fileChooser.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    fileName.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Path pathToEncrypt = Path.of(fileName.getText());
                int keyValue = Integer.parseInt(key.getText());
                if (Files.isRegularFile(pathToEncrypt) && keyValue > 0) {
                    Path pathEncrypted = pathToEncrypt.getParent().resolve("(encrypted)" + pathToEncrypt.getFileName());
                    List<String> toEncStrings;
                    try {
                        toEncStrings = Files.readAllLines(pathToEncrypt);
                        List<String> encryptedStrings = CaesarCipher.encrypt(toEncStrings, keyValue);
                        Files.deleteIfExists(pathEncrypted);
                        Files.createFile(pathEncrypted);
                        String encStrings = encryptedStrings.toString();
                        Files.writeString(pathEncrypted, encStrings.substring(1, encStrings.length() - 1));
                    } catch (IOException ex) {
                        log.setForeground(Color.RED);
                        log.setText("Возникла ошибка!");
                        throw new RuntimeException(ex);
                    }
                    log.setForeground(Color.BLACK);
                    log.setText("<html>Файл " + pathToEncrypt.getFileName() + " успешно зашифрован с ключом " + keyValue + "<br/>" +
                            "Результат сохранён в директории исходного файла с именем:<br />" + "(encrypted)" + pathToEncrypt.getFileName() + "</html>");
                }
            }
        });
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Path pathToDecrypt = Path.of(fileName.getText());
                int keyValue = Integer.parseInt(key.getText());
                if (Files.isRegularFile(pathToDecrypt) && keyValue > 0) {
                    Path pathDecrypted = pathToDecrypt.getParent().resolve("(decrypted)" + pathToDecrypt.getFileName());
                    List<String> toDecStrings;
                    try {
                        toDecStrings = Files.readAllLines(pathToDecrypt);
                        List<String> decryptedStrings = CaesarCipher.decrypt(toDecStrings, keyValue);
                        Files.deleteIfExists(pathDecrypted);
                        Files.createFile(pathDecrypted);
                        String decStrings = decryptedStrings.toString();
                        Files.writeString(pathDecrypted, decStrings.substring(1, decStrings.length() - 1));
                    } catch (IOException ex) {
                        log.setForeground(Color.RED);
                        log.setText("Возникла ошибка!");
                        throw new RuntimeException(ex);
                    }
                    log.setForeground(Color.BLACK);
                    log.setText("<html>Файл " + pathToDecrypt.getFileName() + " успешно расшифрован с ключом " + keyValue + "<br/>" +
                            "Результат сохранён в директории исходного файла с именем:<br />" + "(decrypted)" + pathToDecrypt.getFileName() + "</html>");
                }
            }
        });
        bruteForceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
            }
        });
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
        btn4 = new JButton("Частотный анализ");
        encryptButton.setPreferredSize(new Dimension(150, 30));
        decryptButton.setPreferredSize(new Dimension(150, 30));
        bruteForceButton.setPreferredSize(new Dimension(150, 30));
        btn4.setPreferredSize(new Dimension(150, 30));
        buttonsPanel.add(encryptButton);
        buttonsPanel.add(decryptButton);
        buttonsPanel.add(bruteForceButton);
        buttonsPanel.add(btn4);
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
    /* public static void main(String[] args) {

        Path encSource = Path.of("ToEncrypt.txt");
        Path encDest = Path.of("Encrypted.txt");
        Path decSource = Path.of("ToDecrypt.txt");
        Path decDest = Path.of("Decrypted.txt");
        //
        Path orig = Path.of("C:\\Dir1\\file.txt");
        Path val = orig.getParent().resolve("Encrypted_" + String.valueOf(orig.getFileName()));
        System.out.println(val);
        //
        List<String> toEncStrings;
        try {
            toEncStrings = Files.readAllLines(encSource);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading file for encryption\n" + e);
        }
        List<String> encryptedStrings = CaesarCipher.encrypt(toEncStrings, 5);
        Files.deleteIfExists(encDest);
        try {
            Files.createFile(encDest);
        } catch (IOException e) {
            throw new RuntimeException("Error while creating file for encryption result\n" + e);
        }
        String encStrings = encryptedStrings.toString();
        Files.writeString(encDest, encStrings.substring(1, encStrings.length() - 1));
        //
        List<String> toDecStrings;
        try {
            toDecStrings = Files.readAllLines(decSource);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading file for decryption\n" + e);
        }
        //List<String> decryptedStrings = CaesarCipher.decrypt(toDecStrings, 5);
        List<String> decryptedStrings = CaesarCipher.bruteForce(toDecStrings);
        Files.deleteIfExists(decDest);
        try {
            Files.createFile(decDest);
        } catch (IOException e) {
            throw new RuntimeException("Error while creating file for encryption result\n" + e);
        }
        String decStrings = decryptedStrings.toString();
        Files.writeString(decDest, decStrings.substring(1, decStrings.length() - 1));
    } */
}