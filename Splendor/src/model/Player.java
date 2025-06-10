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
	
	/// Cartes que le joueur possède
	private final Set<Card> possession = new HashSet<Card>();
	
	/// Carte réservé par le joueur
	private final Set<Card> reserved = new HashSet<Card>();
	
	private final Bank advantages = new Bank();
	private final Bank bank = new Bank();
	
	public Player(String nom) {
		Objects.requireNonNull(nom);
		name = nom;
	}
	
	public Bank getBank() {
		return bank;
	}
	public Bank getAdvantages() {
		return advantages;
	}
	
	
	/// Permet de mettre à jour le nombre de point du joueur
	public void update() {
		pts = 0;
		for(var i:possession) {
			pts+= i.point();
		}
	}
	
	public void addAdvantage(Money money) {
		var adventage = new HashMap<Money,Integer>();
		adventage.put(money, 1);
		advantages.add(adventage);
	}

	
	public int getPts() {
		return pts;
	}
	
	
	/// Permet d'ajouter des jetons au joueur
	public void addMoney(Map<Money,Integer> newMoney) {
		Objects.requireNonNull(newMoney);	
		bank.add(newMoney);
	}
	
	
	public boolean buy(Card card) {
		Objects.requireNonNull(card);
		
		var price = card.getPrice();
		
		if(!bank.canBuy(price,advantages)) {
			if(bank.canBuyWithGold(price,advantages)) {// si possible uniquement avec de l'or
				var choix = Ter.sc("[Action] Vous ne pouvez pas acheter la carte, mais avec du gold c'est possible.\nVoulez-vous les utiliser ? (1 oui, autre non)");
				if(choix == 1) bank.buyWithGold(price, this);
				else return false;
			}else  return false;
		}
		else bank.buy(price, this);// réalise l'achat sur le compte du joueur
		
		addAdvantage(card.advantage());// ajoute l'avantage au joueur
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
	
	public boolean toJoin(Card card) {
		Objects.requireNonNull(card);
		
		return(advantages.canBuy(card.getPrice(), advantages));
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
				.append("\nJetons :").append(bank)
				.append("\nPossessions :").append(possession)
				.append("\nRéservé :").append(reserved)
				);
			}

		}
