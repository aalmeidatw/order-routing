package model.filter;

import model.InventoryItem;
import model.ShippingMethod;
import model.Warehouse;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class FilterShippingMethodTest {
    private ShippingMethod  shippingMethodUPS;
    private FilterShippingMethod filterShippingMethod;
    private InventoryItem inventoryItem;
    private Warehouse brazilWarehouse;
    private Warehouse chileWarehouse;


    @Before
    public void setUp() throws Exception {
        this.filterShippingMethod = new FilterShippingMethod();
        this.shippingMethodUPS = ShippingMethod.UPS;

        this.inventoryItem = new InventoryItem("BRAZIL", "Mouse", 2);
        this.brazilWarehouse = new Warehouse("BRAZIL", asList(ShippingMethod.UPS, ShippingMethod.FEDEX), 10);
        this.chileWarehouse = new Warehouse("Chile", asList(ShippingMethod.FEDEX, ShippingMethod.DHL), 10);
    }

    @Test
    public void shouldReturnBrazilInventoryItemWhenUpsShippingMethodIsPassed() throws Exception {

        List<InventoryItem> response = filterShippingMethod.getInventoryShippingMethodRequest(asList(inventoryItem), shippingMethodUPS, asList(brazilWarehouse, chileWarehouse));

        List<InventoryItem> actual = asList(inventoryItem);

        assertThat(actual, is(response));
     }

    @Test
    public void shouldTestIfIsShippingMethodSuportedPassedInventoryName() throws Exception {

        assertTrue(filterShippingMethod.isShippingMethodIsSuported(inventoryItem, shippingMethodUPS.UPS, asList(brazilWarehouse, chileWarehouse)));
    }
}