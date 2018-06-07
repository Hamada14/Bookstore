package server.database.entities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Person {
	
	private static final String NAME_REGEX = "^[a-zA-Z]++(?:[',.-][a-zA-Z]++)*+$";
	protected static final String ID_COL = "ID";
	
	protected String name;
	
	public Person(String name) {
		this.name = name;
	}
	
	public Person() {
		
	}
	
	protected static boolean validateName(String name) {
		Pattern pattern = Pattern.compile(NAME_REGEX);
		Matcher matcher = pattern.matcher(name);
		return matcher.matches();
	}
}
