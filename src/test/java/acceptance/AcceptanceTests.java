package acceptance;

import algorithm.OrderAlgorithm;
import model.InventoryItem;
import model.OrderItem;
import model.ShippingMethod;
import model.dto.Request;
import model.dto.Response;
import model.filter.FilterShippingMethod;
import model.map.CapacityListMap;
import model.map.RequestListMap;
import org.junit.Before;
import org.junit.Test;
import repository.Repository;
import strategy.LargestStrategy;
import strategy.NoneStrategy;
import strategy.ShortestStrategy;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AcceptanceTests {
    private OrderAlgorithm orderAlgorithm;


    @Before
    public void setUp() throws Exception {
        this.orderAlgorithm = new OrderAlgorithm( new FilterShippingMethod(),
                new RequestListMap(),
                new CapacityListMap());
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

    @Test
    public void capacityCase() throws Exception {

        Request request = new Request(asList(
                new InventoryItem("Canada", "Mouse", 4),
                new InventoryItem("Canada", "Keyboard", 3),
                new InventoryItem("France", "Keyboard", 2)),
                new Repository().getWarehouseRepository(),
                ShippingMethod.FEDEX,
                asList( new OrderItem("Mouse",4), new OrderItem("Keyboard", 3)),
                new NoneStrategy());

        Response expected = new Response(asList(new InventoryItem("Canada", "Mouse", 4),
                    new InventoryItem("Canada", "Keyboard", 1),
                    new InventoryItem("France", "Keyboard", 2)));

        Response response = orderAlgorithm.execute(request);
        assertThat(response, is(expected));
    }

    @Test
    public void largestInventoryCase() throws Exception {

        Request request = new Request(asList(
                new InventoryItem("China", "Mouse", 4),
                new InventoryItem("Brazil", "Mouse", 3),
                new InventoryItem("Brazil", "Keyboard", 3),
                new InventoryItem("France", "Mouse", 2),
                new InventoryItem("France", "Keyboard", 2)),
                new Repository().getWarehouseRepository(),
                ShippingMethod.DHL,
                asList( new OrderItem("Mouse",1), new OrderItem("Keyboard", 1)),
                new LargestStrategy());

        Response expected = new Response(asList(new InventoryItem("Brazil", "Mouse", 1),
                new InventoryItem("Brazil", "Keyboard", 1)));

        Response response = orderAlgorithm.execute(request);
        assertThat(response, is(expected));
    }

    @Test
    public void shortestInventoryCase() throws Exception {

        Request request = new Request(asList(
                new InventoryItem("China", "Mouse", 4),
                new InventoryItem("Brazil", "Mouse", 3),
                new InventoryItem("Brazil", "Keyboard", 3),
                new InventoryItem("France", "Keyboard", 2)),
                new Repository().getWarehouseRepository(),
                ShippingMethod.DHL,
                asList( new OrderItem("Mouse",1), new OrderItem("Keyboard", 1)),
                new ShortestStrategy());

        Response expected = new Response(asList(new InventoryItem("China", "Mouse", 1),
                new InventoryItem("France", "Keyboard", 1)));

        Response response = orderAlgorithm.execute(request);
        assertThat(response, is(expected));
    }
}
