package strategy;

import model.InventoryItem;
import org.junit.Test;
import java.util.List;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class NoneStrategyTest {
    private NoneStrategy noneStrategy = new NoneStrategy();
    private  final InventoryItem BRAZIL_KEYBOARD_2 = new InventoryItem("Brazil", "Keyboard", 2);
    private  final InventoryItem FRANCE_MOUSE_2 = new InventoryItem("France", "Mouse", 2);

    @Test
    public void shouldReturnSomeInventoryList() throws Exception {

        List<InventoryItem> actual = noneStrategy.executeStrategy(asList(BRAZIL_KEYBOARD_2,
                FRANCE_MOUSE_2));

        List<InventoryItem> expected = asList(BRAZIL_KEYBOARD_2, FRANCE_MOUSE_2);

        assertThat(actual, is(expected));
    }
}