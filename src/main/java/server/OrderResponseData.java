package server;



import lombok.Getter;
import lombok.Setter;
import server.database.entities.Order;

@Setter
@Getter
public class OrderResponseData extends ResponseData{
	private int oldQuantity;
	private Order order;
	
	
}
