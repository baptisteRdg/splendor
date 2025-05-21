package model;

import java.util.HashMap;
import java.util.Objects;

public non-sealed class Upgrade implements Card{
	private final int pts;
	private final String id;
	private final Money advantage;
	private final int level;
	private final HashMap<Money,Integer> cost;
	
	

	Upgrade(int level,String id,Money money,HashMap<Money,Integer> cost,int pts){
		Objects.requireNonNull(id);
		if (level>3 || level<1) {
			throw new IllegalArgumentException("value on level is out on scope");
		}
		this.level=level;
		this.pts=pts;
		this.id=id;
		this.advantage= money;
		this.cost= cost;
	}
	
	public int point() {
		return pts;
	}
	
	public Money advantage() {
		return advantage;
	}
	public int level() {
		return level;
	}
	public HashMap<Money,Integer> cost(){
		return cost;
	}
	
	public String id() {
		return id;
	}
	private String costString() {
		var ret=new StringBuilder();
		for(var a:cost.keySet()) {
			ret=ret.append(" ").append(a.shortString()).append(": ").append(cost.get(a));
		}
		
		return ret.toString();
	}
	@Override
	public String toString() {
		var c=new StringBuilder().append("U: "+level+" P: "+pts+" A:"+advantage.shortString()+" cost:"+costString());
		
		
		return c.toString();
	}
}
