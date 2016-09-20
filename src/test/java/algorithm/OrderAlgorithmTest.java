package algorithm;

import model.InventoryItem;
import model.OrderItem;
import model.ShippingMethod;
import model.dto.Request;
import model.dto.Response;
import org.hamcrest.core.Is;
import org.junit.Test;
import repository.Repository;
import strategy.NoneStrategy;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


public class OrderAlgorithmTest {
    private OrderAlgorithm orderAlgorithm = new OrderAlgorithm();

    @Test
    public void shouldReturnBrazilCountryWhenKeyboardProductIsPassed() throws Exception {
        Request request = new Request(asList(
                new InventoryItem("Brazil", "Keyboard", 2),
                new InventoryItem("France", "Mouse", 2)),
                new Repository().getWarehouseRepository(),
                ShippingMethod.DHL,
                asList( new OrderItem("Keyboard",2)),
                new NoneStrategy());

        Response expected = new Response(asList(new InventoryItem("Brazil", "Keyboard" , 2)));

        Response response = orderAlgorithm.execute(request);
        assertThat(response, is(expected));
    }

    @Test
    public void shouldReturnBrazilAndArgentinaCountriesWhenMouseAndMonitorProductsIsPassed() throws Exception {

        Request request = new Request(asList(
                          new InventoryItem("Brazil", "Mouse", 2),
                          new InventoryItem("Chile", "Keyboard", 3),
                          new InventoryItem("France", "Monitor", 6)),
                new Repository().getWarehouseRepository(),
                ShippingMethod.DHL,
                asList( new OrderItem("Mouse", 2),
                        new OrderItem("Monitor", 6)),
                new NoneStrategy());

        Response actual = orderAlgorithm.execute(request);
        Response expected = new Response(asList(new InventoryItem("Brazil", "Mouse", 2), new InventoryItem("France", "Monitor", 6)));

        assertThat(actual, is(expected));
    }

}