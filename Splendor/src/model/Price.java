package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Price {

	private final HashMap<Money,Integer> price;
	
	public Price(Map<Money,Integer> map) {
		Objects.requireNonNull(map);
		price = new HashMap<Money,Integer>(map);
	}
	
	public Map<Money,Integer> get(){
		return price;
	}
	
	/// permet depuis un prix ajouter une banque d'avantages pour avoir un nouveau prix
	public Price reduction(Bank advantages) {
		Objects.requireNonNull(advantages);
		var newPrice = new HashMap<Money,Integer>();
		
		price.forEach((money,value)->{
			var reduc = advantages.get(money);
			var newValue = value - reduc;
			if(newValue<0)newValue=0;// pas de négatif possible
		
			if(newPrice.put(money, newValue) != null) {
				throw new IllegalArgumentException("pas possible d'avoir deux fois la même monnaie");
			}
		});
		
		return new Price(newPrice);
	}
	
	
	
	@Override
	public String toString() {
		var msg = new StringBuilder();
		
		price.forEach((money,value)->{
			msg.append(" ").append(value).append(money).append(" ");
		});
		return msg.toString();
	}
}
