import io.restassured.response.Response;
import order.Order;
import order.OrderClient;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class OrderTest {
    Order order;
    OrderClient orderClient;

    @Before
    public void setup() {
        orderClient = new OrderClient();
    }

    @Test
    public void getIngredients() {
        ArrayList<String> listOfIng = new ArrayList<>(orderClient.getIngredient());
        System.out.println(listOfIng.size());
    }
}
