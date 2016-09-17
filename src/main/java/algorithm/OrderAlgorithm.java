package algorithm;


import model.InventoryItem;
import algorithm.dto.Request;
import algorithm.dto.Response;

import static java.util.Arrays.asList;

public class OrderAlgorithm {

    public Response execute(Request request){
        return new Response(asList(new InventoryItem("Brazil", "Keyboard" , 2)));
   }
}
