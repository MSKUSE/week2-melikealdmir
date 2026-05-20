package shopeasy;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import static org.assertj.core.api.Assertions.*;

class ShopEasyPropertyTest {

    private final PriceCalculator calculator = new PriceCalculator();

    @Property
    void finalPriceIsAlwaysNonNegative(
            @ForAll @DoubleRange(min = 0, max = 10000) double basePrice,
            @ForAll @DoubleRange(min = 0, max = 100) double discountRate,
            @ForAll @DoubleRange(min = 0, max = 100) double taxRate) {
        
        double result = calculator.calculate(basePrice, discountRate, taxRate);
        assertThat(result).isGreaterThanOrEqualTo(0);
    }

    @Property
    void zeroDiscountAndZeroTax_returnsBasePrice(
            @ForAll @DoubleRange(min = 0, max = 10000) double basePrice) {
        
        double result = calculator.calculate(basePrice, 0, 0);
        assertThat(result).isEqualTo(basePrice);
    }

    @Property
    void increasingDiscountNeverIncreasesPrice(
            @ForAll @DoubleRange(min = 0, max = 10000) double basePrice,
            @ForAll @DoubleRange(min = 0, max = 100) double taxRate,
            @ForAll @DoubleRange(min = 0, max = 90) double discount1,
            @ForAll @DoubleRange(min = 0, max = 10) double delta) {
        
        double discount2 = discount1 + delta;
        double result1 = calculator.calculate(basePrice, discount1, taxRate);
        double result2 = calculator.calculate(basePrice, discount2, taxRate);
        
        assertThat(result2).isLessThanOrEqualTo(result1);
    }

    @Property
    void cartCommutativity(
            @ForAll("distinctProducts") Product product1,
            @ForAll("distinctProducts") Product product2,
            @ForAll @IntRange(min = 1, max = 10) int qty1,
            @ForAll @IntRange(min = 1, max = 10) int qty2) {
        
        // Ensure products are different
        Assume.that(!product1.getId().equals(product2.getId()));
        
        ShoppingCart cart1 = new ShoppingCart();
        cart1.addItem(product1, qty1);
        cart1.addItem(product2, qty2);
        double total1 = cart1.total();
        
        ShoppingCart cart2 = new ShoppingCart();
        cart2.addItem(product2, qty2);
        cart2.addItem(product1, qty1);
        double total2 = cart2.total();
        
        assertThat(total1).isEqualTo(total2);
    }

    @Provide
    Arbitrary<Product> distinctProducts() {
        return Combinators.combine(
                Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(5),
                Arbitraries.doubles().between(0.01, 1000.0),
                Arbitraries.integers().between(0, 1000)
        ).as((name, price, stock) -> new Product("P-" + name, name, price, stock));
    }
}