package server;

import java.sql.PreparedStatement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderResponseData extends ResponseData{
	private int orderID;
	private PreparedStatement preparedStatement;
	
}
