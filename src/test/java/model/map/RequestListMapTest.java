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

public class RequestListMapTest {
    private RequestListMap requestListMap;
    private static String MOUSE_PRODUCT = "Mouse";
    private static String CAMERA_PRODUCT = "Camera";
    private static int MOUSE_QUANTITY = 6;
    private static int MOUSE_NEW_QUANTITY = 10;
    private static int NONE_QUANTITY = 0;
    private static int QUANTITY_ONE = 1;

    @Before
    public void setUp() throws Exception {
        Request request = Request.builder()
                .inventoryItems(asList(new InventoryItem("Canada", "Mouse", 2),
                        new InventoryItem("Brazil", "Mouse", 2),
                        new InventoryItem("Brazil", "Keyboard", 3),
                        new InventoryItem("France", "Keyboard", 2),
                        new InventoryItem("South Africa", "Monitor", 4),
                        new InventoryItem("South Africa", "Camera", 1),
                        new InventoryItem("South Africa", "Mouse", 2)))
                .warehouseList(new Repository().getWarehouseRepository())
                .shippingMethodMethod(ShippingMethod.FEDEX)
                .orderItemsList(asList(new OrderItem("Mouse",6),
                        new OrderItem("Camera", 1)))
                .strategy(new NoneInventoryStrategy()).build();

        this.requestListMap = new RequestListMap();
        this.requestListMap.createRequestMap(request.getOrderItemsList());
    }

    @Test
    public void shouldReturnTrueWhenQuantityNeededIsMoreThanZero() throws Exception {
        assertTrue(requestListMap.isMoreThanZero(MOUSE_PRODUCT));
    }

    @Test
    public void shouldReturnQuantityNeededWhenProductNameIsPassed() throws Exception {
       assertThat(requestListMap.getProductQuantity(MOUSE_PRODUCT), is(MOUSE_QUANTITY));
    }

    @Test
    public void shouldInsertNewValueInCapacityMapWhenWarehouseNameIsPassed() throws Exception {
        requestListMap.updateProductQuantity(MOUSE_PRODUCT, MOUSE_NEW_QUANTITY);
        assertThat(requestListMap.getProductQuantity(MOUSE_PRODUCT), is(MOUSE_NEW_QUANTITY));
    }

    @Test
    public void shouldReturnTrueWhenMapListIsCompleted() throws Exception {
        requestListMap.updateProductQuantity(MOUSE_PRODUCT, NONE_QUANTITY);
        requestListMap.updateProductQuantity(CAMERA_PRODUCT, NONE_QUANTITY);
        assertTrue(requestListMap.isMapCompleted());
    }
    @Test
    public void shouldReturnFalseWhenMapListIsNotCompleted() throws Exception {
        requestListMap.updateProductQuantity(MOUSE_PRODUCT, NONE_QUANTITY);
        requestListMap.updateProductQuantity(CAMERA_PRODUCT, QUANTITY_ONE);
        assertFalse(requestListMap.isMapCompleted());
    }
}