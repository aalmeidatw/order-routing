package algorithm;

import exception.ProductIsNotAvailableException;
import model.*;
import model.dto.Request;
import model.dto.Response;
import model.filter.FilterShippingMethod;
import model.map.CapacityListMap;
import model.map.RequestListMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import repository.Repository;
import strategy.NoneInventoryStrategy;
import strategy.model.Strategy;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class OrderAlgorithmTest {
    private static final int MOUSE_NEEDED = 2;
    private static final int NEW_CAPACITY = 0;
    private OrderAlgorithm orderAlgorithm;
    private CapacityListMap capacityMap;

    @Mock
    Strategy strategyMock;

    @Mock
    RequestListMap requestMapMock;

    @Mock
    CapacityListMap capacityListMapMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        this.orderAlgorithm = new OrderAlgorithm( new FilterShippingMethod(),
                new RequestListMap(),
                new CapacityListMap());

        this.capacityMap = new CapacityListMap();
        this.capacityMap.updateCapacityQuantity("CANADA", 5);

        when(strategyMock.executeStrategy(anyListOf(InventoryItem.class), anyListOf(Warehouse.class) )).then(returnsFirstArg());
    }

    @Test
    public void shouldCallPrioritizationStrategy() throws Exception {

        Request request = Request.builder()
                .inventoryItems(asList(new InventoryItem("Brazil", "Keyboard", 2), new InventoryItem("France", "Mouse", 2)))
                .warehouseList(new Repository().getWarehouseRepository())
                .shippingMethodMethod(ShippingMethod.DHL)
                .orderItemsList(singletonList(new OrderItem("Keyboard",2)))
                .strategy(strategyMock).build();

        orderAlgorithm.execute(request);
        verify(strategyMock).executeStrategy(anyListOf(InventoryItem.class), anyListOf(Warehouse.class));
    }

    @Test
    public void shouldCallUpdateRequestMap() throws Exception {

        orderAlgorithm.updateRequestAndCapacityMap( requestMapMock, capacityListMapMock,
                                                    new InventoryItem("Brazil", "Mouse", 2),
                                                    new OrderItem("Mouse", 0),
                                                    MOUSE_NEEDED, NEW_CAPACITY);

        verify(requestMapMock, times(1)).updateProductQuantity("Mouse", 2);
    }

    @Test
    public void shouldCallUpdateCapacityMap() throws Exception {

        orderAlgorithm.updateRequestAndCapacityMap( requestMapMock, capacityListMapMock,
                                                    new InventoryItem("Brazil", "Mouse", 2),
                                                    new OrderItem("Mouse", 0),
                                                    MOUSE_NEEDED, NEW_CAPACITY);

        verify(capacityListMapMock, times(1)).updateCapacityQuantity("Brazil", 0);
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

        Request request = Request.builder()
                .inventoryItems(singletonList(new InventoryItem("Canada", "Mouse", 2)))
                .warehouseList(new Repository().getWarehouseRepository())
                .shippingMethodMethod(ShippingMethod.FEDEX)
                .orderItemsList(singletonList( new OrderItem("Mouse", 6)))
                .strategy(new NoneInventoryStrategy()).build();

        Response actual = orderAlgorithm.execute(request);
        Response expected = new Response(singletonList(new WarehouseFulfill("Canada", "Mouse", 2)));

        assertThat(actual, is(expected));
    }

    @Test(expected = ProductIsNotAvailableException.class)
    public void shouldThrowProductIsNotAvailableExceptionWhenOrderIsNotCompleted() throws Exception {

        Request request = Request.builder()
                .inventoryItems(asList(new InventoryItem("China", "Mouse", 4),  new InventoryItem("Brazil", "Mouse", 3)))
                .warehouseList(new Repository().getWarehouseRepository())
                .shippingMethodMethod(ShippingMethod.FEDEX)
                .orderItemsList(singletonList( new OrderItem("Mouse", 5)))
                .strategy(new NoneInventoryStrategy()).build();

        orderAlgorithm.execute(request);
    }
}