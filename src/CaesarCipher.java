import java.util.*;

public class CaesarCipher {
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    // Список сотни самых часто употребляемых слов по версии "Национального корпуса русского языка"
    private static final HashSet<String> FREQUENT_WORDS = new HashSet<>() {{
        add("и");
        add("что");
        add("до");
        add("во");
        add("в");
        add("весь");
        add("время");
        add("со");
        add("не");
        add("год");
        add("если");
        add("раз");
        add("на");
        add("от");
        add("сам");
        add("где");
        add("я");
        add("так");
        add("когда");
        add("там");
        add("быть");
        add("о");
        add("другой");
        add("под");
        add("он");
        add("для");
        add("вот");
        add("можно");
        add("с");
        add("ты");
        add("говорить");
        add("ну");
        add("сейчас");
        add("же");
        add("наш");
        add("какой");
        add("а");
        add("все");
        add("мой");
        add("после");
        add("по");
        add("тот");
        add("знать");
        add("их");
        add("это");
        add("мочь");
        add("стать");
        add("работа");
        add("она");
        add("вы");
        add("при");
        add("без");
        add("этот");
        add("человек");
        add("чтобы");
        add("самый");
        add("к");
        add("такой");
        add("дело");
        add("потом");
        add("но");
        add("его");
        add("жизнь");
        add("надо");
        add("они");
        add("сказать");
        add("кто");
        add("хотеть");
        add("мы");
        add("только");
        add("первый");
        add("ли");
        add("тут");
        add("или");
        add("очень");
        add("слово");
        add("из");
        add("ещё");
        add("два");
        add("идти");
        add("у");
        add("бы");
        add("день");
        add("большой");
        add("который");
        add("себя");
        add("её");
        add("должен");
        add("то");
        add("один");
        add("новый");
        add("место");
        add("за");
        add("как");
        add("рука");
        add("иметь");
        add("свой");
        add("уже");
        add("даже");
        add("ничто");
    }};
    private static final int COUNT_WORDS_TO_VALIDATE = 5;

    /**
     *  Шифровка шифром цезаря с ключом key
     *  Исходные данные List<String> data
     *  Возвращает List<String> зашифрованных данных
     */
    public static List<String> encrypt(List<String> data, int key) {
        List<String> result = new ArrayList<>();
        for (String string : data) {
            StringBuilder replaceString = new StringBuilder();
            for (int i = 0; i < string.length(); i++) {
                char originalChar = Character.toLowerCase(string.charAt(i));
                int charPosition = ALPHABET.indexOf(originalChar);
                int keyValue = (charPosition + key) % ALPHABET.length();
                if (charPosition == -1) {
                    replaceString.append(string.charAt(i));
                } else {
                    char replaceChar = ALPHABET.charAt(keyValue);
                    if (Character.isLowerCase(string.charAt(i))) {
                        replaceString.append(replaceChar);
                    } else {
                        replaceString.append(Character.toUpperCase(replaceChar));
                    }
                }
            }
            result.add(replaceString.toString());
        }
        return result;
    }

    /**
     *  Расшифровка шифром цезаря с ключом key
     *  Исходные данные List<String> data
     *  Возвращает List<String> расшифрованных данных
     */
    public static List<String> decrypt(List<String> data, int key) {
        List<String> result = new ArrayList<>();
        for (String string : data) {
            StringBuilder replaceString = new StringBuilder();
            for (int i = 0; i < string.length(); i++) {
                char originalChar = Character.toLowerCase(string.charAt(i));
                int charPosition = ALPHABET.indexOf(originalChar);
                int keyValue = (charPosition - key) % ALPHABET.length();
                if (keyValue < 0) {
                    keyValue += ALPHABET.length();
                }
                if (charPosition == -1) {
                    replaceString.append(string.charAt(i));
                } else {
                    char replaceChar = ALPHABET.charAt(keyValue);
                    if (Character.isLowerCase(string.charAt(i))) {
                        replaceString.append(replaceChar);
                    } else {
                        replaceString.append(Character.toUpperCase(replaceChar));
                    }
                }
            }
            result.add(replaceString.toString());
        }
        return result;
    }

    /**
     *  Подбор ключа методом brute force
     *  Осуществляется последовательный перебор ключа и расшифровка методом decrypt
     *  Проверка успешной расшифровки осуществляется нахождением в расшифрованных данных слов из FREQUENT_WORDS
     *  Если слов из FREQUENT_WORDS найдено больше чем COUNT_WORDS_TO_VALIDATE расшифровка считается успешной
     */
    public static int bruteForce(List<String> data, List<String> decryptedData) {
        for (int keyValue = 0; keyValue < ALPHABET.length(); keyValue++) {
            List<String> result = decrypt(data, keyValue);
            int countValidWords = 0;
            for (String str : result) {
                StringTokenizer tokenizer = new StringTokenizer(str, ",.?! :;'\"_\\/()");
                while (tokenizer.hasMoreTokens()) {
                    String s = tokenizer.nextToken();
                    if (FREQUENT_WORDS.contains(s)) {
                        countValidWords++;
                    }
                }
            }
            if (countValidWords > COUNT_WORDS_TO_VALIDATE) {
                decryptedData.addAll(result);
                return keyValue;
            }
        }
        return -1;
    }

    /**
     *  Взлом шифра методом частотного анализа
     *  Производится поиск наиболее часто встречающегося символа
     *  Согласно данным Национального корпуса русского языка наиболее часто встречаются буквы о(11%) е(9%) а(8%) и(7%)
     *  Предполагается, что найденный символ соответствует одной из этих букв
     */
    public static int frequencyAnalyze(List<String> data, List<String> decryptedData) {
        HashMap<Character, Integer> freq = new HashMap<>();
        for (String str : data) {
            for (int i = 0; i < str.length(); i++) {
                char ch = Character.toLowerCase(str.charAt(i));
                int charPos = ALPHABET.indexOf(ch);
                if (charPos != -1) {
                    if (freq.containsKey(ch)) {
                        freq.put(ch, freq.get(ch) + 1);
                    } else {
                        freq.put(ch, 1);
                    }
                }
            }
        }
        char maxChar = 0;
        int max = 0;
        for (Map.Entry<Character, Integer> elem : freq.entrySet()) {
            if (elem.getValue() > max) {
                maxChar = elem.getKey();
                max = elem.getValue();
            }
        }
        char[] FREQ_CHARS = { 'о', 'е', 'а', 'и'};
        for (char freqChar : FREQ_CHARS) {
            int keyValue = Math.abs(ALPHABET.indexOf(freqChar) - ALPHABET.indexOf(maxChar));
            List<String> result = decrypt(data, keyValue);
            int countValidWords = 0;
            for (String str : result) {
                StringTokenizer tokenizer = new StringTokenizer(str, ",.?! :;'\"_\\/()");
                while (tokenizer.hasMoreTokens()) {
                    String s = tokenizer.nextToken();
                    if (FREQUENT_WORDS.contains(s)) {
                        countValidWords++;
                    }
                }
            }
            if (countValidWords > COUNT_WORDS_TO_VALIDATE) {
                decryptedData.addAll(result);
                return keyValue;
            }
        }
        return -1;
    }
}
