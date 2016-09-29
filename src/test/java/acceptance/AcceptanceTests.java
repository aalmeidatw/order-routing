package acceptance;

import algorithm.OrderAlgorithm;
import exception.ProductIsNotAvailableException;
import model.InventoryItem;
import model.OrderItem;
import model.ShippingMethod;
import model.WarehouseFulfill;
import model.dto.Request;
import model.dto.Response;
import model.filter.FilterShippingMethod;
import model.map.CapacityListMap;
import model.map.RequestListMap;
import org.junit.Before;
import org.junit.Test;
import repository.Repository;
import strategy.LargestCapacityStrategy;
import strategy.LargestInventoryStrategy;
import strategy.NoneInventoryStrategy;
import strategy.ShortestInventoryStrategy;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AcceptanceTests {
    private OrderAlgorithm orderAlgorithm;

    @Before
    public void setUp() throws Exception {
        this.orderAlgorithm = new OrderAlgorithm(
                new FilterShippingMethod(),
                new RequestListMap(),
                new CapacityListMap());
    }

    @Test
    public void standardCase() throws Exception {
        Request request = new Request
                (asList(
                    new InventoryItem("Brazil", "Keyboard", 2),
                    new InventoryItem("France", "Mouse", 2)),
                new Repository().getWarehouseRepository(),
                ShippingMethod.DHL,
                singletonList( new OrderItem("Keyboard",2)),
                        new NoneInventoryStrategy());

        Response expected = new Response(singletonList(new WarehouseFulfill("Brazil", "Keyboard" , 2)));

        Response response = orderAlgorithm.execute(request);
        assertThat(response, is(expected));
      }

    @Test
    public void shippingMethodCase() throws Exception {
        Request request = new Request(
                asList(
                    new InventoryItem("Brazil", "Mouse", 2),
                    new InventoryItem("South Africa", "Mouse", 2)),
                new Repository().getWarehouseRepository(),
                ShippingMethod.UPS,
                singletonList( new OrderItem("Mouse",1)),
                        new NoneInventoryStrategy());

        Response expected = new Response(singletonList(new WarehouseFulfill("South Africa", "Mouse" , 1)));

        Response response = orderAlgorithm.execute(request);
        assertThat(response, is(expected));
    }

    @Test
    public void capacityCase() throws Exception {
        Request request = new Request(
                asList(
                    new InventoryItem("Canada", "Mouse", 4),
                    new InventoryItem("Canada", "Keyboard", 3),
                    new InventoryItem("France", "Keyboard", 2)),
                new Repository().getWarehouseRepository(),
                ShippingMethod.FEDEX,
                asList(
                        new OrderItem("Mouse",4),
                        new OrderItem("Keyboard", 3)),
                new NoneInventoryStrategy());

        Response expected = new Response(
                asList( new WarehouseFulfill("Canada", "Mouse", 4),
                        new WarehouseFulfill("Canada", "Keyboard", 1),
                        new WarehouseFulfill("France", "Keyboard", 2)));

        Response response = orderAlgorithm.execute(request);
        assertThat(response, is(expected));
    }

    @Test
    public void largestInventoryCase() throws Exception {
         Request request = new Request(
                 asList(
                    new InventoryItem("China", "Mouse", 4),
                    new InventoryItem("Brazil", "Mouse", 3),
                    new InventoryItem("Brazil", "Keyboard", 3),
                    new InventoryItem("France", "Mouse", 2),
                    new InventoryItem("France", "Keyboard", 2)),
                new Repository().getWarehouseRepository(),
                ShippingMethod.DHL,
                asList(
                        new OrderItem("Mouse",1),
                        new OrderItem("Keyboard", 1)),
                new LargestInventoryStrategy());

        Response expected = new Response(
                asList( new WarehouseFulfill("Brazil", "Mouse", 1),
                        new WarehouseFulfill("Brazil", "Keyboard", 1)));

        Response response = orderAlgorithm.execute(request);
        assertThat(response, is(expected));
    }

    @Test
    public void shortestInventoryCase() throws Exception {
        Request request = new Request(
                asList(
                    new InventoryItem("China", "Mouse", 4),
                    new InventoryItem("Brazil", "Mouse", 3),
                    new InventoryItem("Brazil", "Keyboard", 3),
                    new InventoryItem("France", "Keyboard", 2)),
                new Repository().getWarehouseRepository(),
                ShippingMethod.DHL,
                asList(
                        new OrderItem("Mouse",1),
                        new OrderItem("Keyboard", 1)),
                new ShortestInventoryStrategy());

        Response expected = new Response(
                asList(new WarehouseFulfill("China", "Mouse", 1),
                new WarehouseFulfill("France", "Keyboard", 1)));

        Response response = orderAlgorithm.execute(request);
        assertThat(response, is(expected));
    }

    @Test
    public void largestCapacityCase() throws Exception {
        Request request = new Request(
                asList(
                    new InventoryItem("China", "Mouse", 4),
                    new InventoryItem("Brazil", "Mouse", 3),
                    new InventoryItem("Brazil", "Keyboard", 3),
                    new InventoryItem("France", "Keyboard", 2)),
                new Repository().getWarehouseRepository(),
                ShippingMethod.DHL,
                asList(
                        new OrderItem("Mouse",1),
                        new OrderItem("Keyboard", 1)),
                new LargestCapacityStrategy());

        Response expected = new Response(
                asList(
                        new WarehouseFulfill("China", "Mouse", 1),
                new WarehouseFulfill("Brazil", "Keyboard", 1)));

        Response response = orderAlgorithm.execute(request);
        assertThat(response, is(expected));
    }

    @Test
    public void manyProductsCase() throws Exception {
        Request request = new Request(
                asList(
                    new InventoryItem("Canada", "Mouse", 2),
                    new InventoryItem("Brazil", "Mouse", 2),
                    new InventoryItem("Brazil", "Keyboard", 3),
                    new InventoryItem("France", "Keyboard", 2),
                    new InventoryItem("South Africa", "Monitor", 4),
                    new InventoryItem("South Africa", "Camera", 1),
                    new InventoryItem("South Africa", "Mouse", 2)),
                new Repository().getWarehouseRepository(),
                ShippingMethod.FEDEX,
                asList( new OrderItem("Mouse", 6),
                        new OrderItem("Keyboard", 3),
                        new OrderItem("Monitor", 3),
                        new OrderItem("Camera", 1)),
                new NoneInventoryStrategy());

        Response expected = new Response(
                asList(
                    new WarehouseFulfill("Canada", "Mouse", 2),
                    new WarehouseFulfill("Brazil", "Mouse", 2),
                    new WarehouseFulfill("South Africa", "Mouse", 2),
                    new WarehouseFulfill("Brazil", "Keyboard", 3),
                    new WarehouseFulfill("South Africa", "Monitor", 3),
                    new WarehouseFulfill("South Africa", "Camera", 1)));

        Response response = orderAlgorithm.execute(request);
        assertThat(response, is(expected));
    }

    @Test(expected = ProductIsNotAvailableException.class)
    public void invalidRequest() throws Exception {
        Request request = new Request(
                asList(
                    new InventoryItem("China", "Mouse", 4),
                    new InventoryItem("Brazil", "Mouse", 3)),
                new Repository().getWarehouseRepository(),
                ShippingMethod.FEDEX,
                singletonList(
                        new OrderItem("Mouse", 5)),
                new NoneInventoryStrategy());

        orderAlgorithm.execute(request);
    }
}
