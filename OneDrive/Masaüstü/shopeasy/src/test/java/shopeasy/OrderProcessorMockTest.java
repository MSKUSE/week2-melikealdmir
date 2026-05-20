package shopeasy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderProcessorMockTest {

    @Mock
    private InventoryService inventoryService;

    @Mock
    private PaymentGateway paymentGateway;

    @InjectMocks
    private OrderProcessor orderProcessor;

    private ShoppingCart cart;
    private Product widget;
    private Product gadget;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
        widget = new Product("P001", "Widget", 25.0, 100);
        gadget = new Product("P002", "Gadget", 50.0, 10);
    }

    @Test
    void process_happyPath_returnsOrder() {
        cart.addItem(widget, 2);

        when(inventoryService.isAvailable(widget, 2)).thenReturn(true);
        when(paymentGateway.charge("customer-1", 50.0)).thenReturn(true);

        Order order = orderProcessor.process("customer-1", cart);

        assertThat(order).isNotNull();
        assertThat(order.getCustomerId()).isEqualTo("customer-1");
        assertThat(order.getTotal()).isEqualTo(50.0);
        verify(paymentGateway).charge("customer-1", 50.0);
    }

    @Test
    void process_inventoryFailure_returnsNullAndPaymentNeverCalled() {
        cart.addItem(widget, 2);

        when(inventoryService.isAvailable(widget, 2)).thenReturn(false);

        Order order = orderProcessor.process("customer-1", cart);

        assertThat(order).isNull();
        verify(paymentGateway, never()).charge(anyString(), anyDouble());
    }

    @Test
    void process_paymentFailure_returnsNull() {
        cart.addItem(widget, 2);

        when(inventoryService.isAvailable(widget, 2)).thenReturn(true);
        when(paymentGateway.charge("customer-1", 50.0)).thenReturn(false);

        Order order = orderProcessor.process("customer-1", cart);

        assertThat(order).isNull();
        verify(paymentGateway).charge("customer-1", 50.0);
    }

    @Test
    void process_partialQuantityDefinedBehavior_itemUnavailable_returnsNull() {
        cart.addItem(widget, 5);  // 5 adet isteniyor

        when(inventoryService.isAvailable(widget, 5)).thenReturn(false);

        Order order = orderProcessor.process("customer-1", cart);

        // Partial quantity durumunda, stokta tam miktar yoksa işlem iptal oluyor
        assertThat(order).isNull();
        verify(paymentGateway, never()).charge(anyString(), anyDouble());
    }

    @Test
    void process_multipleItems_allAvailable_success() {
        cart.addItem(widget, 2);
        cart.addItem(gadget, 1);

        when(inventoryService.isAvailable(widget, 2)).thenReturn(true);
        when(inventoryService.isAvailable(gadget, 1)).thenReturn(true);
        when(paymentGateway.charge("customer-1", 100.0)).thenReturn(true);

        Order order = orderProcessor.process("customer-1", cart);

        assertThat(order).isNotNull();
        assertThat(order.getTotal()).isEqualTo(100.0);
    }

    @Test
    void process_multipleItems_oneUnavailable_returnsNull() {
        cart.addItem(widget, 2);
        cart.addItem(gadget, 1);

        when(inventoryService.isAvailable(widget, 2)).thenReturn(true);
        when(inventoryService.isAvailable(gadget, 1)).thenReturn(false);

        Order order = orderProcessor.process("customer-1", cart);

        assertThat(order).isNull();
        verify(paymentGateway, never()).charge(anyString(), anyDouble());
    }
}