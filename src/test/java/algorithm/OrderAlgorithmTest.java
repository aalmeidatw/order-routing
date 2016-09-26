package algorithm;

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
import strategy.NoneStrategy;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class OrderAlgorithmTest {
    private OrderAlgorithm orderAlgorithm;
    private RequestListMap requestListMap;
    private CapacityListMap capacityMap;

    @Before
    public void setUp() throws Exception {
        this.requestListMap = new RequestListMap();
        this.capacityMap = new CapacityListMap();

        this.orderAlgorithm = new OrderAlgorithm( new FilterShippingMethod(),
                new RequestListMap(),
                new CapacityListMap());

        this.capacityMap.updateCapacityQuantity("CANADA", 5);
    }

    @Test
    public void shouldReturnBrazilCountryWhenKeyboardProductIsPassed() throws Exception {
        Request request = new Request(asList(
                new InventoryItem("Brazil", "Keyboard", 2),
                new InventoryItem("France", "Mouse", 2)),
                new Repository().getWarehouseRepository(),
                ShippingMethod.DHL,
                asList( new OrderItem("Keyboard",2)),
                new NoneStrategy());

        Response expected = new Response(asList(new WarehouseFulfill("Brazil", "Keyboard" , 2)));

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
        Response expected = new Response(asList(new WarehouseFulfill("Brazil", "Mouse", 2), new WarehouseFulfill("France", "Monitor", 6)));

        assertThat(actual, is(expected));
    }

    @Test
    public void shouldSubtractQuantityNeededAndReturnOneValue() throws Exception {

        int quantityNeeded = 4;
        assertThat(orderAlgorithm.getNewCapacityValue(new InventoryItem("Canada", null, 0), capacityMap, quantityNeeded ), is (1));
    }

    @Test
    public void shouldReturnZeroWhenQuantityNeededIsMoreThanCapacityValue() throws Exception {

        int quantityNeeded = 7;
        assertThat(orderAlgorithm.getNewCapacityValue(new InventoryItem("Canada", null, 0), capacityMap, quantityNeeded ), is (0));
    }

    @Test(expected = ProductIsNotAvailableException.class)
    public void shouldReturnQuantityAvailableThenWarehouseCapacityIsMoreThanQuantityNeeded() throws Exception {

        Request request = new Request(asList(
                new InventoryItem("Canada", "Mouse", 2)),
                new Repository().getWarehouseRepository(),
                ShippingMethod.FEDEX,
                asList( new OrderItem("Mouse", 6)),
                new NoneStrategy());

        Response actual = orderAlgorithm.execute(request);
        Response expected = new Response(asList(new WarehouseFulfill("Canada", "Mouse", 2)));

        assertThat(actual, is(expected));
    }

    @Test(expected = ProductIsNotAvailableException.class)
    public void shouldThrowProductIsNotAvailableExceptionWhenOrderIsNotCompleted() throws Exception {

        Request request = new Request(asList(
                new InventoryItem("China", "Mouse", 4),
                new InventoryItem("Brazil", "Mouse", 3)),
                new Repository().getWarehouseRepository(),
                ShippingMethod.FEDEX,
                asList( new OrderItem("Mouse", 5)),
                new NoneStrategy());

        orderAlgorithm.execute(request);
    }
}