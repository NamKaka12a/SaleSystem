public class SalesService {

    public double calculateSubtotal(Product product) {
        if (product == null) {
            throw new IllegalArgumentException(
                "Product cannot be null");
        }
        return product.getPrice() * product.getQuantity(); // FIX B01: price x quantity
    }

    public double calculateDiscount(double subtotal) {
        if (subtotal < 0) {
            throw new IllegalArgumentException(
                "Subtotal cannot be negative");
        }
        if (subtotal < 1000) {
            return 0;
        } else if (subtotal < 5000) {
            return subtotal * 0.05; // FIX B02: 5% for 1,000 - < 5,000
        } else if (subtotal < 10000) {
            return subtotal * 0.10;
        } else {
            return subtotal * 0.15;
        }
    }

    public double calculateShippingFee(double subtotal) {
        if (subtotal < 0) {
            throw new IllegalArgumentException(
                "Subtotal cannot be negative");
        }
        if (subtotal < 2000) { // FIX B03: shipping is free from 2,000
            return 50;
        }
        return 0;
    }

    public double calculateTotal(Product product) {
        double subtotal = calculateSubtotal(product);
        double discount = calculateDiscount(subtotal);
        double shipping = calculateShippingFee(subtotal);
        return subtotal - discount + shipping; // FIX B04: Total = Subtotal - Discount + Shipping
    }

    public String classifyCustomer(double total) {
        if (total < 1000) {
            return "REGULAR";
        } else if (total < 5000) {
            return "SILVER";
        } else if (total < 10000) { // FIX B05: VIP starts at 10,000
            return "GOLD";
        } else {
            return "VIP";
        }
    }

}
