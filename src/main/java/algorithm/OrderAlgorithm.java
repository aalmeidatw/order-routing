package algorithm;


import model.InventoryItem;
import model.dto.Request;
import model.dto.Response;

import static java.util.Arrays.asList;

public class OrderAlgorithm {

    public Response execute(Request request){
        return new Response(asList(new InventoryItem("Brazil", "Keyboard" , 2)));
   }
}
