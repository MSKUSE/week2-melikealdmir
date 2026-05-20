package shopeasy;

public class PriceCalculator {

    public double calculate(double basePrice, double discountRate, double taxRate) {
        // PRE-CONDITIONS
        assert basePrice >= 0 : "basePrice must be >= 0";
        assert discountRate >= 0 && discountRate <= 100 : "discountRate must be between 0 and 100";
        assert taxRate >= 0 && taxRate <= 100 : "taxRate must be between 0 and 100";
        
        double discounted = basePrice * (1.0 - discountRate / 100.0);
        double withTax = discounted + (discounted * taxRate / 100.0);
        
        // POST-CONDITION
        assert withTax >= 0 : "result must be >= 0";
        return withTax;
    }

    public double applyDiscountOnly(double basePrice, double discountRate) {
        return calculate(basePrice, discountRate, 0);
    }

    public double applyTaxOnly(double basePrice, double taxRate) {
        return calculate(basePrice, 0, taxRate);
    }
}