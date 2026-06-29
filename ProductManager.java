import java.util.*;

public class ProductManager {
    private final List<Product> products;
    private final Map<String, List<Product>> categoryIndex;
    private final Map<String, List<Product>> keywordIndex;
    private final WordFilter wordFilter;
    private final String filePath;

    public ProductManager(WordFilter wordFilter, String filePath) {
        this.products = new ArrayList<>();
        this.categoryIndex = new HashMap<>();
        this.keywordIndex = new HashMap<>();
        this.wordFilter = wordFilter;
        this.filePath = filePath;
    }

    public ProductManager(WordFilter wordFilter) {
        this(wordFilter, "products.json");
    }

    /**
     * Adds a product to memory, censors its fields, indexes it,
     * and automatically saves the updated list to products.json.
     */
    public void addProduct(Product product) {
        if (product == null) {
            return;
        }
        addProductInMemory(product);
        saveToDisk();
    }

    /**
     * Helper to add a product to memory and index it without saving to disk.
     */
    private void addProductInMemory(Product product) {
        // Censor fields using WordFilter
        product.setName(wordFilter.censorText(product.getName()));
        product.setSummary(wordFilter.censorText(product.getSummary()));
        product.setDescription(wordFilter.censorText(product.getDescription()));

        products.add(product);

        // Index by Category (Case-insensitive)
        String catKey = product.getCategory().trim().toLowerCase();
        categoryIndex.computeIfAbsent(catKey, k -> new ArrayList<>()).add(product);

        // Index by Keywords in Product Summary
        indexProductKeywords(product);
    }

    /**
     * Splits the product's summary into cleaned, lowercase tokens
     * and indexes them.
     */
    private void indexProductKeywords(Product product) {
        String summary = product.getSummary();
        if (summary == null || summary.trim().isEmpty()) {
            return;
        }

        String cleanSummary = summary.replaceAll("[^a-zA-Z0-9\\s]", " ").toLowerCase();
        String[] tokens = cleanSummary.split("\\s+");

        Set<String> uniqueWords = new HashSet<>();
        for (String token : tokens) {
            String word = token.trim();
            if (word.length() >= 2) {
                uniqueWords.add(word);
            }
        }

        for (String word : uniqueWords) {
            keywordIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(product);
        }
    }

    /**
     * Saves the current product list to products.json.
     */
    public void saveToDisk() {
        JsonStorage.saveProducts(filePath, products);
    }

    /**
     * Clears current memory and loads products from products.json.
     */
    public void loadFromDisk() {
        products.clear();
        categoryIndex.clear();
        keywordIndex.clear();

        List<Product> loaded = JsonStorage.loadProducts(filePath);
        for (Product p : loaded) {
            addProductInMemory(p);
        }
    }

    /**
     * Returns a copy of the list of all products.
     */
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    /**
     * Searches for products matching the given category (case-insensitive).
     */
    public List<Product> getProductsByCategory(String category) {
        if (category == null) {
            return new ArrayList<>();
        }
        String catKey = category.trim().toLowerCase();
        return new ArrayList<>(categoryIndex.getOrDefault(catKey, Collections.emptyList()));
    }

    /**
     * Searches for products matching any keywords in the query text.
     */
    public List<Product> searchByKeyword(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String cleanQuery = query.replaceAll("[^a-zA-Z0-9\\s]", " ").toLowerCase();
        String[] queryWords = cleanQuery.split("\\s+");

        Set<Product> matchedProducts = new LinkedHashSet<>();
        for (String word : queryWords) {
            word = word.trim();
            if (word.length() >= 2) {
                List<Product> matches = keywordIndex.get(word);
                if (matches != null) {
                    matchedProducts.addAll(matches);
                }
            }
        }

        return new ArrayList<>(matchedProducts);
    }

    /**
     * Clears all products, indexes in memory, and clears the JSON file.
     */
    public void clearAll() {
        products.clear();
        categoryIndex.clear();
        keywordIndex.clear();
        saveToDisk();
    }
}
