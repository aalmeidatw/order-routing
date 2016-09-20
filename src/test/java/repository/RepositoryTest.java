package repository;

import model.ShippingMethod;
import model.Warehouse;
import org.junit.Test;
import java.util.List;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


public class RepositoryTest {
    private Repository repository = new Repository();

    private static List<ShippingMethod> BRAZIL_SHIPPING_METHOD = asList(ShippingMethod.DHL, ShippingMethod.FEDEX);
    private static List<ShippingMethod> FRANCE_SHIPPING_METHOD = asList(ShippingMethod.DHL, ShippingMethod.FEDEX, ShippingMethod.UPS);
    private static List<ShippingMethod> SOUTH_AFRICA_SHIPPING_METHOD = asList(ShippingMethod.FEDEX, ShippingMethod.UPS);
    private static List<ShippingMethod> CHINA_SHIPPING_METHOD = asList(ShippingMethod.DHL);
    private static List<ShippingMethod> CANADA_SHIPPING_METHOD = asList(ShippingMethod.FEDEX);

    private static Warehouse BRAZIL_WAREHOUSE = new Warehouse("BRAZIL", BRAZIL_SHIPPING_METHOD, 15);
    private static Warehouse FRANCE_WAREHOUSE = new Warehouse("FRANCE", FRANCE_SHIPPING_METHOD, 10);
    private static Warehouse SOUTH_AFRICA_WAREHOUSE = new Warehouse("SOUTH AFRICA", SOUTH_AFRICA_SHIPPING_METHOD, 10);
    private static Warehouse CHINA_WAREHOUSE = new Warehouse("CHINA", CHINA_SHIPPING_METHOD, 20);
    private static Warehouse CANADA_WAREHOUSE = new Warehouse("CANADA", CANADA_SHIPPING_METHOD, 5);

    @Test
    public void shouldCreateWarehouseList() throws Exception {this.repository = new Repository();
        List<Warehouse> expected = asList(BRAZIL_WAREHOUSE, FRANCE_WAREHOUSE, SOUTH_AFRICA_WAREHOUSE, CHINA_WAREHOUSE, CANADA_WAREHOUSE);
        List<Warehouse> actual = repository.getWarehouseRepository();

        assertThat(actual, is(expected));
    }
}