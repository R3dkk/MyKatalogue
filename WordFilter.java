import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordFilter {
    private final Set<String> forbiddenWords;
    private Pattern compiledPattern;

    // Constructor
    public WordFilter() {
        this.forbiddenWords = new HashSet<>();
        // Default forbidden words
        addForbiddenWord("palsu");
        addForbiddenWord("kw");
        addForbiddenWord("tiruan");
        addForbiddenWord("scam");
        addForbiddenWord("bohong");
        addForbiddenWord("illegal");
    }

    // Add ForbiddenWord Method
    public synchronized void addForbiddenWord(String word) {
        if (word != null && !word.trim().isEmpty()) {
            forbiddenWords.add(word.trim().toLowerCase());
            updatePattern();
        }
    }

    // Remove Forbidden Word Method
    public synchronized void removeForbiddenWord(String word) {
        if (word != null) {
            forbiddenWords.remove(word.trim().toLowerCase());
            updatePattern();
        }
    }

    // Get the forbidden Word
    public synchronized Set<String> getForbiddenWords() {
        return new HashSet<>(forbiddenWords);
    }

    // Compile dan update pattern Regex setiap ada perubahan frobiddenWord
    private void updatePattern() {
        if (forbiddenWords.isEmpty()) {
            compiledPattern = null;
            return;
        }

        // Escape regex special characters in each forbidden word and join with |
        StringBuilder sb = new StringBuilder();
        sb.append("\\b(");
        int i = 0;
        for (String word : forbiddenWords) {
            if (i > 0) {
                sb.append("|");
            }
            sb.append(Pattern.quote(word));
            i++;
        }
        sb.append(")\\b");

        compiledPattern = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
    }

    // Censor Forbidden Word Method
    public synchronized String censorText(String text) {
        if (text == null || text.isEmpty() || compiledPattern == null) {
            return text;
        }

        Matcher matcher = compiledPattern.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String match = matcher.group();
            String censored = repeatChar('*', match.length());
            // Matcher.quoteReplacement is important to avoid regex syntax errors in the replacement string
            matcher.appendReplacement(sb, Matcher.quoteReplacement(censored));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    // Method for checking is the word Contains a forbidden Word
    public synchronized boolean containsForbiddenWord(String text) {
        if (text == null || text.isEmpty() || compiledPattern == null) {
            return false;
        }
        return compiledPattern.matcher(text).find();
    }


    // Helper Method to change character on spesific length
    private String repeatChar(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}