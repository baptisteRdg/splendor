package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


import view.Ter;

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
	
	private final HashMap<Money, Integer> adventage = new HashMap<Money, Integer>();
	
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
	
	public int numberMonney() {
		return money.values().stream().mapToInt(s->s).sum();
	}
	
	public HashMap<Money, Integer> getAdventage(){
		return adventage;
	}
	
	public void addAdventage(Map<Money, Integer> upgradeMap) {
		Objects.requireNonNull(upgradeMap);
		upgradeMap.forEach((money, value) -> adventage.merge(money, value, Integer::sum));
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
			
			money.merge(key,(0-value),Integer::sum);
		}
	}
	
	public boolean buy(Card carte) {
		Objects.requireNonNull(carte);
		Ter.ln(carte.cost().toString());
		Ter.ln(money.toString());
		for(Map.Entry<Money, Integer> i:carte.cost().entrySet()) {
			if(money.getOrDefault(i.getKey(),0) < i.getValue())return false; // si pas assez
		}
		
		subMoney(carte.cost());
		return true;
	}
	
	public Set<Card> getPossession() {
		return possession;
	}
	
	/// Permet d'ajouter une carte dans les possessions du joueur
	public void addCard(Card carte) {
		Objects.requireNonNull(carte);
		if(!possession.add(carte))throw new IllegalArgumentException("Carte déjà présente carte:"+carte);
	}
	
	public Set<Card> getReserved() {
		return reserved;
	}
	
	public void addReservedCard(Card card) {
		Objects.requireNonNull(card);
		reserved.add(card);
	}
	
	public boolean toJoin(Card a2) {
		Objects.requireNonNull(a2);
		for(var sou: a2.cost().keySet()) {
			if(a2.cost().get(sou)>adventage.getOrDefault(money,0)) {
				return false;
			}
		}
		return true;
	}
	
	public void deleteReservedCard(Card card) {
		Objects.requireNonNull(card);
		reserved.remove(card);
	}
	
	
	public String getName() {
		return name;
	}
	
	public void printStat() {
		Ter.ln(new StringBuilder().append("Points :").append(pts)
				.append("\nJetons :").append(money)
				.append("\nPossessions :").append(possession)
				.append("\nRéservé :").append(reserved)
				);
	}
	
	
}
