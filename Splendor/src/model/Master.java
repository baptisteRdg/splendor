package model;

import java.util.Objects;

public non-sealed class Master implements Card {
	private final int pts;
	private final String id;
	private final Price price;
	
	Master(int pts,String id,Price price){
		Objects.requireNonNull(id);
		Objects.requireNonNull(price);
		
		if(pts<0) {
			throw new IllegalArgumentException("number of points is negatif");
		}
		this.pts=pts;
		this.id=id;
		this.price=price;
	}
	
	public int point() {
		return pts;
	}
	
	public Money advantage() {
		return null;
	}
	
	public Price getPrice(){
		return price;
	}
	
	public String id() {
		return id;
	}
	
	public int level() {
		return 4;
	}
	private String costString() {
		var ret=new StringBuilder().append("(");
		for(var a:price.get().keySet()) {
			if(price.get().get(a)>0) {
			ret=ret.append("").append(a.shortString()).append(":").append(price.get().get(a));
		}
	}
		return ret.append(")").toString();
}
	@Override
	public String toString() {
		var c=new StringBuilder().append("Nobles pts: "+pts+" cost:"+costString());
		
		for(var i=c.length();i<34;i++) {
			//System.out.println(c.length());
			c=c.append(" ");
		}
		return c.toString();
	}
	
}

