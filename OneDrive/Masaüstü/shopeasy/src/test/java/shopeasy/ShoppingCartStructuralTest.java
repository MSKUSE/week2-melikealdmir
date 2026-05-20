package shopeasy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ShoppingCartStructuralTest {

    private ShoppingCart cart;
    private Product apple;
    private Product banana;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
        apple = new Product("P001", "Apple", 1.50, 100);
        banana = new Product("P002", "Banana", 0.80, 50);
    }

    @Test
    void addItem_newProduct_increasesItemCount() {
        cart.addItem(apple, 2);
        assertThat(cart.itemCount()).isEqualTo(1);
        assertThat(cart.total()).isEqualTo(3.0);
    }

    @Test
    void addItem_existingProduct_combinesQuantity() {
        cart.addItem(apple, 2);
        cart.addItem(apple, 3);
        assertThat(cart.itemCount()).isEqualTo(1);
        assertThat(cart.total()).isEqualTo(7.5);
    }

    @Test
    void removeItem_existingProduct_removesIt() {
        cart.addItem(apple, 2);
        cart.addItem(banana, 1);
        assertThat(cart.itemCount()).isEqualTo(2);
        cart.removeItem("P001");
        assertThat(cart.itemCount()).isEqualTo(1);
        assertThat(cart.total()).isEqualTo(0.8);
    }

    @Test
    void removeItem_nonExistentProduct_doesNothing() {
        cart.addItem(apple, 2);
        cart.removeItem("NONEXISTENT");
        assertThat(cart.itemCount()).isEqualTo(1);
    }

    @Test
    void updateQuantity_validProduct_updates() {
        cart.addItem(apple, 2);
        cart.updateQuantity("P001", 5);
        assertThat(cart.total()).isEqualTo(7.5);
    }

    @Test
    void updateQuantity_productNotFound_throwsException() {
        assertThatThrownBy(() -> cart.updateQuantity("P001", 2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Product not found");
    }

    @Test
    void applyDiscount_zeroDiscount_returnsSameTotal() {
        cart.addItem(apple, 10);
        double total = cart.total();
        assertThat(cart.applyDiscount(0)).isEqualTo(total);
    }

    @Test
    void applyDiscount_positiveDiscount_reducesTotal() {
        cart.addItem(apple, 10);
        double total = cart.total();
        assertThat(cart.applyDiscount(10)).isLessThan(total);
    }

    @Test
    void total_emptyCart_returnsZero() {
        assertThat(cart.total()).isEqualTo(0.0);
    }

    @Test
    void clear_removesAllItems() {
        cart.addItem(apple, 2);
        cart.addItem(banana, 3);
        cart.clear();
        assertThat(cart.itemCount()).isEqualTo(0);
        assertThat(cart.total()).isEqualTo(0.0);
    }
}