package model.map;

import model.InventoryItem;
import model.OrderItem;
import model.ShippingMethod;
import model.dto.Request;
import org.junit.Before;
import org.junit.Test;
import repository.Repository;
import strategy.NoneInventoryStrategy;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class CapacityListMapTest {
    private CapacityListMap capacityListMap;
    private Request request;
    private static final int BRAZIL_NEW_CAPACITY = 10;
    private static final int BRAZIL_CAPACITY = 15 ;
    private static String BRAZIL_WAREHOUSE_NAME = "BRAZIL";

    @Before
    public void setUp() throws Exception {
        this.capacityListMap = new CapacityListMap();

        this.request = new Request(
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
                asList(
                        new OrderItem("Mouse", 6),
                        new OrderItem("Keyboard", 3),
                        new OrderItem("Monitor", 3),
                        new OrderItem("Camera", 1)),
                new NoneInventoryStrategy());

        capacityListMap.createCapacityMap(request);
    }

    @Test
    public void shouldReturnTrueWhenCapacityIsMoreThanZero() throws Exception {
        assertTrue(capacityListMap.isMoreThanZero(BRAZIL_WAREHOUSE_NAME));
    }

    @Test
    public void shouldReturnMaxCapacityWhenWarehouseNameIsPassed() throws Exception {
        assertThat(capacityListMap.getProductQuantity(BRAZIL_WAREHOUSE_NAME), is(BRAZIL_CAPACITY));
    }

    @Test
    public void shouldInsertNewValueInCapacityMapWhenWarehouseNameIsPassed() throws Exception {
        capacityListMap.updateCapacityQuantity(BRAZIL_WAREHOUSE_NAME, BRAZIL_NEW_CAPACITY);
        assertThat(capacityListMap.getProductQuantity(BRAZIL_WAREHOUSE_NAME), is(BRAZIL_NEW_CAPACITY));
    }
}