package strategy;

import model.InventoryItem;
import org.junit.Test;
import java.util.List;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


public class ShortestInventoryStrategyTest {

    private ShortestInventoryStrategy shortestInventoryStrategy = new ShortestInventoryStrategy();
    private  final InventoryItem CHINA_MOUSE_4 = new InventoryItem("China", "Mouse", 4);
    private  final InventoryItem BRAZIL_MOUSE_3 = new InventoryItem("Brazil", "Mouse", 3);
    private  final InventoryItem BRAZIL_KEYBOARD_3 = new InventoryItem("Brazil", "Keyboard", 3);
    private  final InventoryItem FRANCE_KEYBOARD_2 = new InventoryItem("France", "Keyboard", 2);


    @Test
    public void shouldReturnInventoryListThenBrazilIsLargeUnits() throws Exception {

        List<InventoryItem> actual = shortestInventoryStrategy.executeStrategy(asList(CHINA_MOUSE_4,
                BRAZIL_MOUSE_3,
                BRAZIL_KEYBOARD_3,
                FRANCE_KEYBOARD_2));

        List<InventoryItem> expected = asList(FRANCE_KEYBOARD_2,
                CHINA_MOUSE_4,
                BRAZIL_MOUSE_3,
                BRAZIL_KEYBOARD_3);

        assertThat(actual, is(expected));
    }

}