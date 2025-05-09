package model;

import java.util.HashMap;
import java.util.Objects;

public class Upgrade implements Card{
	private final int pts;
	private final String id;
	private final Money advantage;
	private final int level;
	private final HashMap<Money,Integer> cost;
	
	
	Upgrade(int level,String id){
		Objects.requireNonNull(id);
		if (level>3 || level<1) {
			throw new IllegalArgumentException("value on level is out on scope");
		}
		this.level=level;
		this.pts=level;
		this.id=id;
		this.advantage=new Money();
		this.cost= new HashMap<Money,Integer>();
		
	}
}
