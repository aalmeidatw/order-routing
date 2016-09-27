package strategy;

import model.InventoryItem;
import org.junit.Before;
import org.junit.Test;
import repository.Repository;

import java.util.List;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class LargestInventoryStrategyTest {
    private LargestInventoryStrategy largestInventoryStrategy ;
    private Repository repository;
    private  final InventoryItem CHINA_MOUSE_4 = new InventoryItem("China", "Mouse", 4);
    private  final InventoryItem BRAZIL_MOUSE_3 = new InventoryItem("Brazil", "Mouse", 3);
    private  final InventoryItem BRAZIL_KEYBOARD_3 = new InventoryItem("Brazil", "keyboard", 3);
    private  final InventoryItem FRANCE_MOUSE_2 = new InventoryItem("France", "Mouse", 2);
    private  final InventoryItem FRANCE_KEYBOARD_2 = new InventoryItem("France", "Keyboard", 2);

    @Before
    public void setUp() throws Exception {
        this.repository = new Repository();
        this.largestInventoryStrategy = new LargestInventoryStrategy();

    }

    @Test
    public void shouldReturnOrderedListByLargeUnits() throws Exception {

        List<InventoryItem> actual = largestInventoryStrategy.executeStrategy(asList(CHINA_MOUSE_4,
                BRAZIL_MOUSE_3,
                BRAZIL_KEYBOARD_3,
                FRANCE_MOUSE_2,
                FRANCE_KEYBOARD_2),
                repository.getWarehouseRepository());

        List<InventoryItem> expected = asList(BRAZIL_MOUSE_3,
                BRAZIL_KEYBOARD_3,
                CHINA_MOUSE_4,
                FRANCE_MOUSE_2,
                FRANCE_KEYBOARD_2);

        assertThat(actual, is(expected));
    }


}