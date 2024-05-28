import java.util.*;

public class CaesarCipher implements Encryptable {
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    // Список сотни самых часто употребляемых слов по версии "Национального корпуса русского языка"
    private static final HashSet<String> FREQUENT_WORDS = new HashSet<>() {{
        add("лицо");
        add("что");
        add("до");
        add("во");
        add("каждый");
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
        add("друг");
        add("так");
        add("когда");
        add("там");
        add("быть");
        add("нет");
        add("другой");
        add("под");
        add("он");
        add("для");
        add("вот");
        add("можно");
        add("теперь");
        add("ты");
        add("говорить");
        add("ну");
        add("сейчас");
        add("же");
        add("наш");
        add("какой");
        add("ни");
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
        add("глаз");
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
        add("тоже");
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
    @Override
    public OutputDataBox encrypt(List<String> source, int key) {
        List<String> result = new ArrayList<>();
        for (String string : source) {
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
        return new OutputDataBox(result, key);
    }

    @Override
    public OutputDataBox decrypt(List<String> source, int key) {
        List<String> result = new ArrayList<>();
        for (String string : source) {
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
        return new OutputDataBox(result, key);
    }

    @Override
    public OutputDataBox bruteforce(List<String> source) {
        OutputDataBox result = null;
        for (int keyValue = 0; keyValue < ALPHABET.length(); keyValue++) {
            result = decrypt(source, keyValue);
            int countValidWords = 0;
            for (String str : result.getResult()) {
                StringTokenizer tokenizer = new StringTokenizer(str, ",.?! :;'\"_\\/()");
                while (tokenizer.hasMoreTokens()) {
                    String s = tokenizer.nextToken();
                    if (FREQUENT_WORDS.contains(s)) {
                        countValidWords++;
                    }
                }
            }
            if (countValidWords > COUNT_WORDS_TO_VALIDATE) {
                return result;
            }
        }
        return new OutputDataBox(result.getResult(), -1);
    }

    @Override
    public OutputDataBox statisticalAnalysis(List<String> source) {
        OutputDataBox result = null;
        HashMap<Character, Integer> freq = new HashMap<>();
        for (String str : source) {
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
            result = decrypt(source, keyValue);
            int countValidWords = 0;
            for (String str : result.getResult()) {
                StringTokenizer tokenizer = new StringTokenizer(str, ",.?! :;'\"_\\/()");
                while (tokenizer.hasMoreTokens()) {
                    String s = tokenizer.nextToken();
                    if (FREQUENT_WORDS.contains(s)) {
                        countValidWords++;
                    }
                }
            }
            if (countValidWords > COUNT_WORDS_TO_VALIDATE) {
                return result;
            }
        }
        return new OutputDataBox(result.getResult(), -1);
    }
}
