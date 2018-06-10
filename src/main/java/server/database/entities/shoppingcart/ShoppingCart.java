package server.database.entities.shoppingcart;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import server.database.entities.Order;


@Setter
@Getter
public class ShoppingCart implements Iterable<Order> {

	private Set<Order> orders;

	public ShoppingCart() {
		orders = new HashSet<>();
	}

	public void addOrder(Order newOrder) {

		if (!orders.contains(newOrder)) {
			orders.add(newOrder);
		} else {
			orders.remove(newOrder);
			orders.add(newOrder);
		}
	}

	public void removeOrder(Order removedOrder) {
		orders.remove(removedOrder);
	}

	public void clearCart() {
		orders.clear();
	}

	@Override
	public Iterator<Order> iterator() {
		// TODO Auto-generated method stub
		return orders.iterator();
	}

	public int getSize() {
		return orders.size();
	}



	

	
	

}
