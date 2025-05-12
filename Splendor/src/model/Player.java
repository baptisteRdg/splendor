package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Player {

	/// Nombre de points du joueur
	private int pts;  
	
	/// nom du joueur
	private final String name;
	
	/// Collection des jetons du joueur
	private final Map<Money, Integer> money = new HashMap<Money, Integer>();
	
	/// Cartes que le joueur possède
	private final Set<Card> possession = new HashSet<Card>();
	
	/// Carte réservé par le joueur
	private final Set<Card> reserved = new HashSet<Card>();
	
	public Player(String nom) {
		Objects.requireNonNull(nom);
		name = nom;
	}
	
	/// Permet de mettre à jour le nombre de point du joueur
	public void update() {
		pts = 0;
		for(var i:possession) {
			pts+= i.point();
		}
	}
	
	
	public int getPts() {
		return pts;
	}
	
	public Map<Money, Integer> getMoney() {
		return money;
	}
	
	/// Permet d'ajouter des jetons au joueur
	public void addMoney(Map<Money,Integer> newMoney) {
		Objects.requireNonNull(newMoney);
		
		for(Map.Entry<Money,Integer> i:newMoney.entrySet()) {
			var key = i.getKey();
			var value = i.getValue();
			
			if(value < 1) {
				throw new IllegalArgumentException("La valeur doit être positive money:"+i);
			}
			money.merge(key,value,Integer::sum);
		}		
	}
	
	
	private void subMoney(Map<Money,Integer> price) {
		Objects.requireNonNull(price);
		for(Map.Entry<Money, Integer> i:price.entrySet()) {
			var key = i.getKey();
			var value = i.getValue();
			
			if(value < 1) throw new IllegalArgumentException("La valeur doit être positive, money"+i);
			
			money.merge(key,(0-value),Integer::sum);
		}
	}
	
	public boolean buy(Card carte) {
		Objects.requireNonNull(carte);
		for(Map.Entry<Money, Integer> i:carte.cost().entrySet()) {
			if(!money.containsKey(i.getKey()))return false;
			if(money.get(i.getKey())< i.getValue()) return false;
		}
		
		subMoney(carte.cost());
		return true;
	}
	
	public Set<Card> getPossession() {
		return possession;
	}
	
	/// Permet d'ajouter une carte dans les possessions du joueur
	public void addPossession(Card carte) {
		Objects.requireNonNull(carte);
		if(!possession.add(carte))throw new IllegalArgumentException("Carte déjà présente carte:"+carte);
	}
	
	public Set<Card> getReserved() {
		return reserved;
	}
	
	
	public String getName() {
		return name;
	}
}
