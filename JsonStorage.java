import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonStorage {

    /**
     * Serializes a list of products into JSON and writes it to a file.
     */
    public static void saveProducts(String filePath, List<Product> products) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            sb.append("  {\n");
            sb.append("    \"name\": \"").append(escapeJson(p.getName())).append("\",\n");
            sb.append("    \"category\": \"").append(escapeJson(p.getCategory())).append("\",\n");
            sb.append("    \"price\": ").append(p.getPrice()).append(",\n");
            sb.append("    \"rating\": ").append(p.getRating()).append(",\n");
            sb.append("    \"summary\": \"").append(escapeJson(p.getSummary())).append("\",\n");
            sb.append("    \"description\": \"").append(escapeJson(p.getDescription())).append("\"\n");
            sb.append("  }");
            if (i < products.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("]");

        try {
            Files.write(Paths.get(filePath), sb.toString().getBytes());
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal menyimpan file JSON: " + e.getMessage());
        }
    }

    /**
     * Reads a list of products from a JSON file.
     */
    public static List<Product> loadProducts(String filePath) {
        List<Product> list = new ArrayList<>();
        if (!Files.exists(Paths.get(filePath))) {
            return list;
        }

        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
            // Find all JSON object blocks: { ... }
            Pattern pattern = Pattern.compile("\\{[^\\}]+\\}");
            Matcher matcher = pattern.matcher(jsonContent);

            while (matcher.find()) {
                String objText = matcher.group();
                String name = extractStringField(objText, "name");
                String category = extractStringField(objText, "category");
                double price = extractDoubleField(objText, "price");
                double rating = extractDoubleField(objText, "rating");
                String summary = extractStringField(objText, "summary");
                String description = extractStringField(objText, "description");

                list.add(new Product(name, category, price, rating, summary, description));
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal membaca file JSON: " + e.getMessage());
        }
        return list;
    }

    private static String extractStringField(String jsonText, String fieldName) {
        // Match: "fieldName" : "value" (supporting escape sequences like \")
        Pattern p = Pattern.compile("\"" + fieldName + "\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"");
        Matcher m = p.matcher(jsonText);
        if (m.find()) {
            return unescapeJson(m.group(1));
        }
        return "";
    }

    private static double extractDoubleField(String jsonText, String fieldName) {
        // Match: "fieldName" : value
        Pattern p = Pattern.compile("\"" + fieldName + "\"\\s*:\\s*([0-9\\.]+)");
        Matcher m = p.matcher(jsonText);
        if (m.find()) {
            try {
                return Double.parseDouble(m.group(1));
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    private static String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    private static String unescapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\\\", "\\")
                   .replace("\\\"", "\"")
                   .replace("\\n", "\n")
                   .replace("\\r", "\r")
                   .replace("\\t", "\t");
    }
}
