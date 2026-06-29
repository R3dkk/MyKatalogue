public class Product {
    private String name;
    private String category;
    private double price;
    private double rating;
    private String summary;
    private String description;

    public Product(String name, String category, double price, double rating, String summary, String description) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.rating = rating;
        this.summary = summary;
        this.description = description;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format(
            "=========================================\n" +
            "Nama Produk  : %s\n" +
            "Kategori     : %s\n" +
            "Harga        : Rp %,.2f\n" +
            "Rating       : %.1f / 5.0\n" +
            "Ringkasan    : %s\n" +
            "Deskripsi    : %s\n" +
            "=========================================",
            name, category, price, rating, summary, description
        );
    }
}
