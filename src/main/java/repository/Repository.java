package repository;


import lombok.Value;
import model.Shipping;
import model.Warehouse;
import java.util.List;
import static java.util.Arrays.asList;

@Value
public class Repository {
    private static List<Shipping> BRAZIL_SHIPPING = asList(new Shipping("DHL"), new Shipping("FEDEX"));
    private static List<Shipping> FRANCE_SHIPPING = asList(new Shipping("DHL"), new Shipping("FEDEX"), new Shipping("UPS"));
    private static List<Shipping> SOUTH_AFRICA_SHIPPING = asList(new Shipping("DHL"));
    private static List<Shipping> CHINA_SHIPPING = asList(new Shipping("DHL"));
    private static List<Shipping> CANADA_SHIPPING = asList(new Shipping("FEDEX"));

    private static Warehouse BRAZIL_WAREHOUSE = new Warehouse("BRAZIL", BRAZIL_SHIPPING, 15);
    private static Warehouse FRANCE_WAREHOUSE = new Warehouse("BRAZIL", FRANCE_SHIPPING, 10);
    private static Warehouse SOUTH_AFRICA_WAREHOUSE = new Warehouse("SOUTH AFRICA", SOUTH_AFRICA_SHIPPING, 10);
    private static Warehouse CHINA_WAREHOUSE = new Warehouse("SOUTH AFRICA", CHINA_SHIPPING, 20);
    private static Warehouse CANADA_WAREHOUSE = new Warehouse("CANADA", CANADA_SHIPPING, 5);


    public static List<Warehouse> getWarehouseRepository(){
        return asList(BRAZIL_WAREHOUSE, FRANCE_WAREHOUSE, SOUTH_AFRICA_WAREHOUSE, CHINA_WAREHOUSE, CANADA_WAREHOUSE);
    }
}
