package model.map;

import model.Warehouse;
import org.junit.Before;
import org.junit.Test;
import repository.Repository;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CapacityListMapTest {
    private CapacityListMap capacityListMap;
    private static final int BRAZIL_NEW_CAPACITY = 10;
    private static final int BRAZIL_CAPACITY = 15 ;
    private static String BRAZIL_WAREHOUSE_NAME = "BRAZIL";

    @Before
    public void setUp() throws Exception {
        this.capacityListMap = new CapacityListMap();

        List<Warehouse> warehouseList = new Repository().getWarehouseRepository();

        capacityListMap.createCapacityMap(warehouseList);
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