package model;

import java.util.HashMap;
import java.util.Objects;

public class Master implements Card {
	private int pts;
	private final HashMap<Money,Integer> cost;
	private String id;
	
	Master(int pts,String id){
		Objects.requireNonNull(id);
		if(pts<0) {
			throw new IllegalArgumentException("number of points is negatif");
		}
		this.pts=pts;
		this.id=id;
		this.cost=new HashMap<Money,Integer>();
	}
}
