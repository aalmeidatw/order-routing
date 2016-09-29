package model.filter;

import model.InventoryItem;
import model.ShippingMethod;
import model.Warehouse;
import model.dto.Request;
import org.junit.Before;
import org.junit.Test;
import strategy.NoneInventoryStrategy;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FilterShippingMethodTest {
    private FilterShippingMethod filterShippingMethod;
    private static final InventoryItem BRAZIL_MOUSE_2 = new InventoryItem("BRAZIL", "Mouse", 2);
    private static final Warehouse BRAZIL_WAREHOUSE = new Warehouse("BRAZIL", asList(ShippingMethod.UPS, ShippingMethod.FEDEX), 10);
    private static final Warehouse CHILE_WAREHOUSE = new Warehouse("CHILE", asList(ShippingMethod.FEDEX, ShippingMethod.DHL), 10);

    @Before
    public void setUp() throws Exception {
        this.filterShippingMethod = new FilterShippingMethod();

    }

    @Test
    public void shouldReturnBrazilInventoryItemWhenUpsShippingMethodIsPassed() throws Exception {

        Request request = Request.builder()
                .inventoryItems(singletonList(BRAZIL_MOUSE_2))
                .warehouseList(asList(BRAZIL_WAREHOUSE, CHILE_WAREHOUSE))
                .shippingMethodMethod(ShippingMethod.UPS)
                .orderItemsList(singletonList(null))
                .strategy(new NoneInventoryStrategy()).build();

        List<InventoryItem> response = filterShippingMethod.getInventoryListFiltredByShippingMethodRequest(request);
        List<InventoryItem> actual = singletonList(BRAZIL_MOUSE_2);

        assertThat(actual, is(response));
     }
}