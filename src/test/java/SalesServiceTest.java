import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for SalesService (JUnit 5).
 * Business rules: see the lab handout (Subtotal, Discount, Shipping, Total, Customer type).
 * Test IDs (S/D/H/T/C xx) match the Test Report (Report5_Unit Test.xlsx).
 */
class SalesServiceTest {

    private static final double DELTA = 0.001;

    private SalesService service;

    @BeforeEach
    void setUp() {
        service = new SalesService();
    }

    // ---------------- calculateSubtotal ----------------

    @DisplayName("S01-S04: subtotal = price x quantity")
    @ParameterizedTest(name = "price={0}, qty={1} -> {2}")
    @CsvSource({
        "500, 2, 1000",
        "100, 1, 100",
        "1999.5, 3, 5998.5",
        "2000, 5, 10000"
    })
    void calculateSubtotal(double price, int quantity, double expected) {
        Product p = new Product("P01", "Item", price, quantity);
        assertEquals(expected, service.calculateSubtotal(p), DELTA);
    }

    @Test
    @DisplayName("S05: null product is rejected")
    void calculateSubtotalNullProduct() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.calculateSubtotal(null));
        assertEquals("Product cannot be null", ex.getMessage());
    }

    // ---------------- calculateDiscount ----------------

    @DisplayName("D01-D11: discount tiers incl. boundary values")
    @ParameterizedTest(name = "subtotal={0} -> discount {1}")
    @CsvSource({
        "0, 0",
        "500, 0",
        "999.99, 0",
        "1000, 50",
        "2500, 125",
        "4999.99, 249.9995",
        "5000, 500",
        "7500, 750",
        "9999.99, 999.999",
        "10000, 1500",
        "20000, 3000"
    })
    void calculateDiscount(double subtotal, double expected) {
        assertEquals(expected, service.calculateDiscount(subtotal), DELTA);
    }

    @ParameterizedTest(name = "D12-D13: subtotal={0} is rejected")
    @CsvSource({"-1", "-0.01"})
    void calculateDiscountNegativeSubtotal(double subtotal) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.calculateDiscount(subtotal));
        assertEquals("Subtotal cannot be negative", ex.getMessage());
    }

    // ---------------- calculateShippingFee ----------------

    @DisplayName("H01-H06: shipping 50 below 2,000, free from 2,000")
    @ParameterizedTest(name = "subtotal={0} -> shipping {1}")
    @CsvSource({
        "0, 50",
        "500, 50",
        "1999.99, 50",
        "2000, 0",
        "2000.01, 0",
        "5000, 0"
    })
    void calculateShippingFee(double subtotal, double expected) {
        assertEquals(expected, service.calculateShippingFee(subtotal), DELTA);
    }

    @Test
    @DisplayName("H07: negative subtotal is rejected")
    void calculateShippingFeeNegativeSubtotal() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.calculateShippingFee(-1));
        assertEquals("Subtotal cannot be negative", ex.getMessage());
    }

    // ---------------- calculateTotal ----------------

    @DisplayName("T01-T06: total = subtotal - discount + shipping")
    @ParameterizedTest(name = "price={0}, qty={1} -> total {2}")
    @CsvSource({
        "500, 1, 550",
        "500, 3, 1475",
        "1000, 2, 1900",
        "1000, 5, 4500",
        "2000, 5, 8500",
        "3000, 10, 25500"
    })
    void calculateTotal(double price, int quantity, double expected) {
        Product p = new Product("P01", "Item", price, quantity);
        assertEquals(expected, service.calculateTotal(p), DELTA);
    }

    @Test
    @DisplayName("T07: null product is rejected")
    void calculateTotalNullProduct() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateTotal(null));
    }

    // ---------------- classifyCustomer ----------------

    @DisplayName("C01-C10: customer type incl. boundary values")
    @ParameterizedTest(name = "total={0} -> {1}")
    @CsvSource({
        "500, REGULAR",
        "999.99, REGULAR",
        "1000, SILVER",
        "3000, SILVER",
        "4999.99, SILVER",
        "5000, GOLD",
        "7500, GOLD",
        "9999.99, GOLD",
        "10000, VIP",
        "25000, VIP"
    })
    void classifyCustomer(double total, String expected) {
        assertEquals(expected, service.classifyCustomer(total));
    }
}
