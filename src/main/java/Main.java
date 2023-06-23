import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    private static final AtomicInteger beauty3, beauty4, beauty5;
    private static List<Thread> threads = new ArrayList<>(3);

    static {
        beauty3 = new AtomicInteger(0);
        beauty4 = new AtomicInteger(0);
        beauty5 = new AtomicInteger(0);
    }

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        List<String> distText = Arrays.stream(texts).distinct().collect(Collectors.toList());

        //palindrome
        Thread rule1 = new Thread(() -> {
            for (String word : distText) {
                boolean beauty = true;
                if (word.chars().distinct().count() != 1) {
                    for (int i = 0, j = word.length() - 1; i < word.length() / 2; i++, j--) {
                        if (word.charAt(i) == word.charAt(j)) {
                        } else {
                            beauty = false;
                            break;
                        }
                    }
                    if (beauty == true) {
                        incrementBeautyCounter(word.length());
                    }
                }
            }
        });
        rule1.start();
        threads.add(rule1);

        //one-letter word
        Thread rule2 = new Thread(() -> {
            for (String word : distText) {
                if (word.chars().distinct().count() == 1) {
                    incrementBeautyCounter(word.length());
                }
            }
        });
        rule2.start();
        threads.add(rule2);

        //a-z rule
        Thread rule3 = new Thread(() -> {
            for (String word : distText) {
                boolean beauty = true;
                if (word.chars().distinct().count() != 1) {
                    char prevCh = word.charAt(0);
                    for (int i = 1; i < word.length(); i++) {
                        if (prevCh <= word.charAt(i)) {
                            prevCh = word.charAt(i);
                        } else {
                            beauty = false;
                            break;
                        }
                    }
                    if (beauty == true) {
                        incrementBeautyCounter(word.length());
                    }
                }
            }
        });
        rule3.start();
        threads.add(rule3);

        for (Thread th : threads) {
            th.join();
        }

        System.out.println(beauty3);
        System.out.println(beauty4);
        System.out.println(beauty5);
    }

    public static void incrementBeautyCounter(int wordLength) {
        switch (wordLength) {
            case 3:
                beauty3.getAndIncrement();
                break;
            case 4:
                beauty4.getAndIncrement();
                break;
            case 5:
                beauty5.getAndIncrement();
                break;
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
