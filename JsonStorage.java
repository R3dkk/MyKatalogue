import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonStorage {

    /**
     * Mengubah list produk menjadi format JSON (serialization) dan menulisnya ke file disk.
     * 
     * ANALISIS KOMPLEKSITAS WAKTU:
     * - O(N)
     * Penjelasan:
     * N adalah jumlah produk yang ada di dalam list. Loop melakukan iterasi sebanyak N kali untuk 
     * memformat setiap objek produk ke bentuk string JSON secara linear.
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
     * Membaca file disk JSON dan mengubahnya kembali menjadi list objek produk (deserialization).
     * 
     * ANALISIS KOMPLEKSITAS WAKTU:
     * - O(N)
     * Penjelasan:
     * N adalah jumlah produk yang berhasil ditemukan dan diparsing di dalam file JSON.
     * Regex matcher memindai konten file secara linear untuk mengekstrak data dari N blok objek { ... } yang ada.
     */
    public static List<Product> loadProducts(String filePath) {
        List<Product> list = new ArrayList<>();
        if (!Files.exists(Paths.get(filePath))) {
            return list;
        }

        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
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
        Pattern p = Pattern.compile("\"" + fieldName + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher m = p.matcher(jsonText);
        if (m.find()) {
            return unescapeJson(m.group(1));
        }
        return "";
    }

    private static double extractDoubleField(String jsonText, String fieldName) {
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
        if (text == null)
            return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    private static String unescapeJson(String text) {
        if (text == null)
            return "";
        return text.replace("\\\\", "\\")
                   .replace("\\\"", "\"")
                   .replace("\\n", "\n")
                   .replace("\\r", "\r")
                   .replace("\\t", "\t");
    }
}
