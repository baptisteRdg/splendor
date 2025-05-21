package model;

import java.util.HashMap;
import java.util.Objects;

public non-sealed class Master implements Card {
	private final int pts;
	private final HashMap<Money,Integer> cost;
	private final String id;
	
	Master(int pts,String id,HashMap<Money,Integer> cost){
		Objects.requireNonNull(id);
		if(pts<0) {
			throw new IllegalArgumentException("number of points is negatif");
		}
		this.pts=pts;
		this.id=id;
		this.cost=cost;
	}
	
	public int point() {
		return pts;
	}
	
	public Money advantage() {
		return null;
	}
	
	public HashMap<Money,Integer> cost(){
		return cost;
	}
	
	public String id() {
		return id;
	}
	
	public int level() {
		return 4;
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
		var c=new StringBuilder().append("Nobles pts: "+pts+" cost:"+costString());
		
		
		return c.toString();
	}
	
}

