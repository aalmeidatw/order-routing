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
    private static List<ShippingMethod> brazilShippingMethod = asList(new ShippingMethod("DHL"), new ShippingMethod("FEDEX"));
    private static List<ShippingMethod> franceShippingMethod = asList(new ShippingMethod("DHL"), new ShippingMethod("FEDEX"), new ShippingMethod("UPS"));
    private static List<ShippingMethod> southAfricaShippingMethod = asList(new ShippingMethod("DHL"));
    private static List<ShippingMethod> chinaShippingMethod = asList(new ShippingMethod("DHL"));
    private static List<ShippingMethod> canadaShippingMethod = asList(new ShippingMethod("FEDEX"));

    private static Warehouse BRAZIL_WAREHOUSE = new Warehouse("BRAZIL", brazilShippingMethod, 15);
    private static Warehouse FRANCE_WAREHOUSE = new Warehouse("BRAZIL", franceShippingMethod, 10);
    private static Warehouse SOUTH_AFRICA_WAREHOUSE = new Warehouse("SOUTH AFRICA", southAfricaShippingMethod, 10);
    private static Warehouse CHINA_WAREHOUSE = new Warehouse("SOUTH AFRICA", chinaShippingMethod, 20);
    private static Warehouse CANADA_WAREHOUSE = new Warehouse("CANADA", canadaShippingMethod, 5);

    @Test
    public void shouldCreateWarehouseList() throws Exception {this.repository = new Repository();
        List<Warehouse> expected = asList(BRAZIL_WAREHOUSE, FRANCE_WAREHOUSE, SOUTH_AFRICA_WAREHOUSE, CHINA_WAREHOUSE, CANADA_WAREHOUSE);

        List<Warehouse> actual = repository.getWarehouseRepository();
        assertThat(actual, is(expected));
    }
}