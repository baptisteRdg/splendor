package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Bank {
	/// YEAH A BANK MAN !! ALL IN RED ITS LAS VEGAS HERE BABY
	private final HashMap<Money,Integer> account;
	
	public Bank() {
		account = new HashMap<Money,Integer>();
	}
	public Bank(Map<Money,Integer> map) {
		Objects.requireNonNull(map);
		account = new HashMap<Money,Integer>(map);
	}	
	
	
	public HashMap<Money,Integer> getAccount(){
		return account;
	}
	
	public HashMap<Money,Integer> getAccountWithoutGold(){
		var liste = new HashMap<Money,Integer>();
		
		account.forEach((money,value)->{
			if(!money.equals(Money.GOLD)) liste.put(money, value);
		});
		
		return liste;
	}
	
	/// permet de retourner de combien d'une monnaie un compte possède
	public Integer get(Money typeMoney) {
		return account.getOrDefault(typeMoney, 0);
	}
	
	
	public void add(Map<Money,Integer> map) {
		Objects.requireNonNull(map);
		map.forEach((key,value)-> account.merge(key,value, Integer::sum));
	}
	
	private void sub(Price price) {
	    Objects.requireNonNull(price);
	    var cost = price.get();
	    cost.forEach((money, value) -> account.merge(money, -value, Integer::sum));
	}
	
	public void sub(Map<Money,Integer> map) {
		Objects.requireNonNull(map);
	    map.forEach((money, value) -> account.merge(money, -value, Integer::sum));
	}
	
	
	/// Permet de savoir si un achat peut être fait sans gold
	public boolean canBuy(Price cost, Bank advantages) {
		Objects.requireNonNull(cost);
		Objects.requireNonNull(advantages);
		
		var price = cost.reduction(advantages);
		var map = price.get();
				
		for(Map.Entry<Money, Integer> entry : map.entrySet()) {
			var money = entry.getKey();// une monnaie du prix
			var costValue = entry.getValue();// nombre de cette monnaie
			var accountValue = account.getOrDefault(money, 0); // combien le joueur en possède
			
			if(accountValue < costValue)return false;
			
		}
		return true;
	}
	
	/// permet de savoir si un achat peut être fait avec du gold
	public boolean canBuyWithGold(Price cost,Bank advantages) {
		Objects.requireNonNull(cost);
		Objects.requireNonNull(advantages);

		var price = cost.reduction(advantages);
		var map = price.get();
	    var missing = 0; // total des jetons manquants

	    for (Map.Entry<Money, Integer> entry : map.entrySet()) {
	        var money = entry.getKey();
	        var costValue = entry.getValue();
	        var accountValue = account.getOrDefault(money, 0);

	        if (accountValue < costValue) { // si pas assez, regarde la différence
	            missing += costValue - accountValue;
	        }
	    }

	    var golds = account.getOrDefault(Money.GOLD, 0);// gold du compte

	    return golds >= missing;
	}

	
	/// réalise un achat
	/// @param Price le prix sans avantages
	/// @Player joueur pour l'achat'
	public boolean buy(Price cost, Player player) {
		Objects.requireNonNull(cost);
		Objects.requireNonNull(player);
		
		var advantages = player.getAdvantages();
		var price = cost.reduction(advantages);
		
		if(!canBuy(price,advantages))return false;// vérif si possible d'acheter 
		sub(price);
		return true;
	}
	
	/// Réalise un achat et complète avec l'or du joueur si possible
	/// @Param Price prix avant réduction
	/// @param Player joueur 
	public boolean buyWithGold(Price cost, Player player) {
		Objects.requireNonNull(cost);
		Objects.requireNonNull(player);
		
		var advantages = player.getAdvantages();
		var price = cost.reduction(advantages);
		var realPrice = new HashMap<Money,Integer>();// ce qu'il va falloir retirer au joueur à la fin
		var missing = 0;
		
		if(!canBuyWithGold(price,player.getAdvantages()))return false; // pas d'achat possible 
		
		// calcul combien de gold à fournir
	    for (Map.Entry<Money, Integer> entry : price.get().entrySet()) {
	        var money = entry.getKey();
	        var costValue = entry.getValue();
	        var accountValue = account.getOrDefault(money, 0);

	        if (accountValue < costValue) {
	        	missing += costValue - accountValue;
	        	realPrice.put(money, accountValue);
	        }else {
	        	realPrice.put(money, costValue);
	        }
	    }
	    realPrice.put(Money.GOLD, missing);
	    // retire les monnaies consommés au joueur 
	    sub(new Price(realPrice));
	    
		return true;
	}
	
	public int sommeAccount() {
		return account.values().stream().mapToInt(s->s).sum();

	}
	
	
	
	@Override
	public String toString() {
		var msg = new StringBuilder();
		
		account.forEach((money,value)->{
			msg.append(" ").append(value).append(money).append(" ");
		});
		return msg.toString();
	}
	

}
