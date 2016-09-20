package acceptance;

import algorithm.OrderAlgorithm;
import model.InventoryItem;
import model.OrderItem;
import model.ShippingMethod;
import model.dto.Request;
import model.dto.Response;
import org.junit.Before;
import org.junit.Test;
import repository.Repository;
import strategy.NoneStrategy;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AcceptanceTests {
    private OrderAlgorithm orderAlgorithm;

    @Before
    public void setUp() throws Exception {
        this.orderAlgorithm = new OrderAlgorithm();
    }

    @Test
    public void standardCase() throws Exception {

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
    public void shippingMethodCase() throws Exception {

        Request request = new Request(asList(
                new InventoryItem("Brazil", "Mouse", 2),
                new InventoryItem("South Africa", "Mouse", 2)),
                new Repository().getWarehouseRepository(),
                ShippingMethod.UPS,
                asList( new OrderItem("Mouse",1)),
                new NoneStrategy());

        Response expected = new Response(asList(new InventoryItem("South Africa", "Mouse" , 1)));

        Response response = orderAlgorithm.execute(request);
        assertThat(response, is(expected));
    }
}
