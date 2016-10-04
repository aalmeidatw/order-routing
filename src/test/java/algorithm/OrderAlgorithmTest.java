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
import strategy.LargestCapacityStrategy;
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

    @Mock
    FilterShippingMethod filterShippingMethodMock;

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

        this.orderAlgorithm = new OrderAlgorithm( new FilterShippingMethod(),
                    requestMapMock, new CapacityListMap());

        orderAlgorithm.updateRequestAndCapacityMap( new InventoryItem("Brazil", "Mouse", 2),
                                                    new OrderItem("Mouse", 0),
                                                    MOUSE_NEEDED, NEW_CAPACITY);

        verify(requestMapMock, times(1)).updateProductQuantity("Mouse", 2);
    }

    @Test(expected = ProductIsNotAvailableException.class)
    public void shouldCallFilterShippingMethod() throws Exception {

        OrderAlgorithm orderAlgorithmMock = new OrderAlgorithm(filterShippingMethodMock, new RequestListMap(), new CapacityListMap());

        Request request = Request.builder()
                .inventoryItems(asList(new InventoryItem("Brazil", "Keyboard", 2), new InventoryItem("France", "Mouse", 2)))
                .warehouseList(new Repository().getWarehouseRepository())
                .shippingMethodMethod(ShippingMethod.DHL)
                .orderItemsList(singletonList(new OrderItem("Keyboard",2)))
                .strategy(new LargestCapacityStrategy()).build();

        when(filterShippingMethodMock.getInventoryListFiltredByShippingMethodRequest(request)).thenReturn(anyListOf(InventoryItem.class));

        orderAlgorithmMock.execute(request);

        verify(filterShippingMethodMock, times(1)).getInventoryListFiltredByShippingMethodRequest(request);
    }

    @Test
    public void shouldCallUpdateCapacityMap() throws Exception {

        this.orderAlgorithm = new OrderAlgorithm( new FilterShippingMethod(),
                requestMapMock, capacityListMapMock);

        orderAlgorithm.updateRequestAndCapacityMap( new InventoryItem("Brazil", "Mouse", 2),
                                                    new OrderItem("Mouse", 0),
                                                    MOUSE_NEEDED, NEW_CAPACITY);

        verify(capacityListMapMock).updateCapacityQuantity("Brazil", 0);
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
        Response expected = new Response(singletonList(new WarehouseFulfillOrder("Canada", "Mouse", 2)));

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