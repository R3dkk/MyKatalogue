import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortingHelper {

    // Comparators
    public static final Comparator<Product> BY_RATING_DESC = new Comparator<Product>() {
        @Override
        public int compare(Product p1, Product p2) {
            return Double.compare(p2.getRating(), p1.getRating()); // Descending
        }
    };

    public static final Comparator<Product> BY_PRICE_ASC = new Comparator<Product>() {
        @Override
        public int compare(Product p1, Product p2) {
            return Double.compare(p1.getPrice(), p2.getPrice()); // Ascending
        }
    };

    public static final Comparator<Product> BY_PRICE_DESC = new Comparator<Product>() {
        @Override
        public int compare(Product p1, Product p2) {
            return Double.compare(p2.getPrice(), p1.getPrice()); // Descending
        }
    };

    public static final Comparator<Product> BY_NAME_ASC = new Comparator<Product>() {
        @Override
        public int compare(Product p1, Product p2) {
            return p1.getName().compareToIgnoreCase(p2.getName()); // Alphabetical
        }
    };

    /**
     * Sorts the given list of products using a manual, stable Merge Sort algorithm.
     * Time Complexity: O(N log N)
     * Space Complexity: O(N)
     */
    public static void mergeSort(List<Product> list, Comparator<Product> comparator) {
        if (list == null || list.size() <= 1) {
            return;
        }
        // Create an auxiliary list to store copies of references during merge
        List<Product> temp = new ArrayList<>(list);
        mergeSortHelper(list, temp, 0, list.size() - 1, comparator);
    }

    private static void mergeSortHelper(List<Product> list, List<Product> temp, int left, int right, Comparator<Product> comparator) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortHelper(list, temp, left, mid, comparator);
            mergeSortHelper(list, temp, mid + 1, right, comparator);
            merge(list, temp, left, mid, right, comparator);
        }
    }

    private static void merge(List<Product> list, List<Product> temp, int left, int mid, int right, Comparator<Product> comparator) {
        // Copy elements to temp list
        for (int i = left; i <= right; i++) {
            temp.set(i, list.get(i));
        }

        int i = left;       // Left subarray pointer
        int j = mid + 1;    // Right subarray pointer
        int k = left;       // Merged array pointer

        while (i <= mid && j <= right) {
            if (comparator.compare(temp.get(i), temp.get(j)) <= 0) {
                list.set(k, temp.get(i));
                i++;
            } else {
                list.set(k, temp.get(j));
                j++;
            }
            k++;
        }

        // Copy remaining elements from the left subarray if any
        while (i <= mid) {
            list.set(k, temp.get(i));
            i++;
            k++;
        }
        // Note: Right subarray elements are already in place in the original list
    }
}
