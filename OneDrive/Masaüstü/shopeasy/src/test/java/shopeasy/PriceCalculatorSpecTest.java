package shopeasy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.assertj.core.api.Assertions.*;

class PriceCalculatorSpecTest {

    private PriceCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new PriceCalculator();
    }

    @Test
    void zeroBasePrice_returnsZero() {
        assertThat(calculator.calculate(0, 0, 0)).isEqualTo(0.0);
        assertThat(calculator.calculate(0, 50, 20)).isEqualTo(0.0);
    }

    @Test
    void discountRateZero_noDiscount() {
        assertThat(calculator.calculate(100, 0, 10)).isCloseTo(110.0, within(0.001));
    }

    @Test
    void discountRateHundred_fullDiscount() {
        assertThat(calculator.calculate(100, 100, 20)).isEqualTo(0.0);
    }

    @Test
    void taxRateZero_noTax() {
        assertThat(calculator.calculate(100, 20, 0)).isCloseTo(80.0, within(0.001));
    }

    @Test
    void taxRateHundred_doublesPrice() {
        assertThat(calculator.calculate(100, 0, 100)).isCloseTo(200.0, within(0.001));
    }

    @ParameterizedTest
    @CsvSource({
        "100, 10, 20, 108",
        "200, 15, 5, 178.5",
        "50, 20, 8, 43.2",
        "1000, 0, 0, 1000"
    })
    void typicalValues_calculateCorrectly(double base, double disc, double tax, double expected) {
        assertThat(calculator.calculate(base, disc, tax)).isCloseTo(expected, within(0.01));
    }
}