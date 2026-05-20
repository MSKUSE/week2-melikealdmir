package shopeasy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ContractTest {

    private ShoppingCart cart;
    private PriceCalculator calculator;
    private Product product;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
        calculator = new PriceCalculator();
        product = new Product("P001", "Widget", 10.0, 50);
    }

    @Test
    void addItem_validInput_noException() {
        assertThatCode(() -> cart.addItem(product, 3)).doesNotThrowAnyException();
    }

    @Test
    void addItem_nullProduct_throwsAssertionError() {
        assertThatThrownBy(() -> cart.addItem(null, 1))
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void addItem_zeroQuantity_throwsAssertionError() {
        assertThatThrownBy(() -> cart.addItem(product, 0))
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void addItem_negativeQuantity_throwsAssertionError() {
        assertThatThrownBy(() -> cart.addItem(product, -5))
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void applyDiscount_validRate_noException() {
        cart.addItem(product, 5);
        assertThatCode(() -> cart.applyDiscount(10)).doesNotThrowAnyException();
    }

    @Test
    void applyDiscount_negativeRate_throwsAssertionError() {
        cart.addItem(product, 5);
        assertThatThrownBy(() -> cart.applyDiscount(-10))
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void applyDiscount_rateAbove100_throwsAssertionError() {
        cart.addItem(product, 5);
        assertThatThrownBy(() -> cart.applyDiscount(150))
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void calculate_validInputs_noException() {
        assertThatCode(() -> calculator.calculate(100, 10, 20))
            .doesNotThrowAnyException();
    }

    @Test
    void calculate_negativeBasePrice_throwsAssertionError() {
        assertThatThrownBy(() -> calculator.calculate(-100, 10, 20))
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void calculate_negativeDiscount_throwsAssertionError() {
        assertThatThrownBy(() -> calculator.calculate(100, -10, 20))
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void calculate_negativeTax_throwsAssertionError() {
        assertThatThrownBy(() -> calculator.calculate(100, 10, -20))
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void calculate_discountAbove100_throwsAssertionError() {
        assertThatThrownBy(() -> calculator.calculate(100, 150, 20))
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void calculate_taxAbove100_throwsAssertionError() {
        assertThatThrownBy(() -> calculator.calculate(100, 10, 150))
            .isInstanceOf(AssertionError.class);
    }
}