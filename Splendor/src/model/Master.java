package model;

import java.util.HashMap;
import java.util.Objects;

public non-sealed class Master implements Card {
	private final int pts;
	private final HashMap<Money,Integer> cost;
	private final String id;
	
	Master(int pts,String id,Money money){
		Objects.requireNonNull(id);
		if(pts<0) {
			throw new IllegalArgumentException("number of points is negatif");
		}
		this.pts=pts;
		this.id=id;
		this.cost=new HashMap<Money,Integer>();
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
	
	@Override
	public String toString() {
		return "Noble: "+id+" pts:"+pts+" coute "+cost;
	}
	
}

