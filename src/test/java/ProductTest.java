import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Unit tests for Product validation rules (productId, productName, price > 0, quantity > 0). */
class ProductTest {

    @Test
    void validProductKeepsAllValues() {
        Product p = new Product("P01", "Keyboard", 500.5, 3);
        assertEquals("P01", p.getProductId());
        assertEquals("Keyboard", p.getProductName());
        assertEquals(500.5, p.getPrice(), 0.001);
        assertEquals(3, p.getQuantity());
    }

    @Test
    void smallestValidPriceAndQuantityAreAccepted() {
        Product p = new Product("P01", "Pen", 0.01, 1);
        assertEquals(0.01, p.getPrice(), 0.0001);
        assertEquals(1, p.getQuantity());
    }

    @Test
    void nullProductIdIsRejected() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Product(null, "Item", 10, 1));
        assertEquals("Product ID is required", ex.getMessage());
    }

    @Test
    void emptyProductIdIsRejected() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Product("", "Item", 10, 1));
        assertEquals("Product ID is required", ex.getMessage());
    }

    @Test
    void nullProductNameIsRejected() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Product("P01", null, 10, 1));
        assertEquals("Product name is required", ex.getMessage());
    }

    @Test
    void emptyProductNameIsRejected() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Product("P01", "", 10, 1));
        assertEquals("Product name is required", ex.getMessage());
    }

    @ParameterizedTest(name = "price={0} is rejected")
    @CsvSource({"0", "-1", "-0.5"})
    void nonPositivePriceIsRejected(double price) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Product("P01", "Item", price, 1));
        assertEquals("Price must be greater than 0", ex.getMessage());
    }

    @ParameterizedTest(name = "quantity={0} is rejected")
    @CsvSource({"0", "-1", "-10"})
    void nonPositiveQuantityIsRejected(int quantity) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Product("P01", "Item", 10, quantity));
        assertEquals("Quantity must be greater than 0", ex.getMessage());
    }
}
