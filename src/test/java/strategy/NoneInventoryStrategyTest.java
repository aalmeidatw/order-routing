package strategy;

import model.InventoryItem;
import org.junit.Before;
import org.junit.Test;
import repository.Repository;

import java.util.List;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class NoneInventoryStrategyTest {
    private Repository repository;
    private NoneInventoryStrategy noneInventoryStrategy;
    private  final InventoryItem BRAZIL_KEYBOARD_2 = new InventoryItem("Brazil", "Keyboard", 2);
    private  final InventoryItem FRANCE_MOUSE_2 = new InventoryItem("France", "Mouse", 2);

    @Before
    public void setUp() throws Exception {
        this.repository = new Repository();
        this.noneInventoryStrategy = new NoneInventoryStrategy();
    }

    @Test
    public void shouldReturnNoOrderedList() throws Exception {

        List<InventoryItem> actual = noneInventoryStrategy.executeStrategy(asList(
                BRAZIL_KEYBOARD_2,
                FRANCE_MOUSE_2),
                repository.getWarehouseRepository());

        List<InventoryItem> expected = asList(BRAZIL_KEYBOARD_2, FRANCE_MOUSE_2);

        assertThat(actual, is(expected));
    }
}