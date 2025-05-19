package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.*;
import view.Ter;

public class Game {

	private final Board board;
	
	
	
	public Game(){
		var msgNbPlayer = new StringBuilder().append("[Choix] Saisir le nombre de joueur (minimum 2 et maximum 4) : ");
		
		int nbPlayer = 0;
		while(nbPlayer < 2 || nbPlayer > 4) {
			nbPlayer = (int) Ter.ln(msgNbPlayer,1,false);
		}

		var listCard = initCard(nbPlayer);
		var listPlayer = initPlayer(nbPlayer);
		
		board = new Board(nbPlayer,listPlayer,listCard);
	}
	
	
	/// Permet d'initialiser une liste de carte
	private List<Card> initCard(int nbPlayer) {
		var list = new ArrayList<Card>();
		
		list.add(new Upgrade(1, "card 1", Money.DIAMOND));
		list.add(new Upgrade(1, "card 2", Money.EMERALD));
		list.add(new Upgrade(2, "card 3", Money.GOLD));
		list.add(new Upgrade(2, "card 4", Money.ONYX));
		list.add(new Upgrade(3, "card 5", Money.SAPPHIRE));

		return list;
	}
	
	/// Permet d'initialiser une liste de joueur
	private ArrayList<Player> initPlayer(int nbPlayer) {
	    var msg = "Saisir le nom du joueur ";
	    var list = new ArrayList<Player>();

	    for(int i = 0; i < nbPlayer; i++) {
	        var str = new StringBuilder().append(msg).append(i+1).append("/").append(nbPlayer).append(" :");
	        var name = (String) Ter.ln(str, 2, false);
	        if (name.isEmpty()) throw new IllegalArgumentException("[Error] initPlayer - Le nom du joueur ne peut pas être vide");
	        list.add(new Player(name));
	    }
	    return list;
	}

	
	
	private void printMoney(ArrayList<Money> liste) {
		var msg = new StringBuilder().append("Liste des jetons :\n");
		for(int i=0;i<liste.size();i++) {
			msg.append("[").append(i).append("]   ").append(liste.get(i)).append(" : ").append(board.getJetons().get(liste.get(i))).append("\n");
		}
		Ter.ln(msg);
	}
	
	private Map<Money,Integer> moneyEvent(int currentPlayer) {
		if(board.getJoueurs().get(currentPlayer).numberMonney() >= 10) return null; // pas plus de 10 jetons
		
		var listeJetons = new ArrayList<Money>(board.getJetons().keySet());
		Ter.space();
		printMoney(listeJetons);
		
		var choix = 0;
		while(choix != 1 && choix != 2) {
			choix = (int) Ter.ln("[Choix] Choisir Trois jetons différents (1) ou deux fois le même ?(2) :",1,false);
			if(choix==1) return choseDifferentMoney(listeJetons);
			if(choix==2) return choseOneMoney(listeJetons);
		}
		return null;
	}
	
	private Map<Money,Integer> choseOneMoney(ArrayList<Money> listeJetons){
		/// ici faire en soter de retirer trois jetons différents
		
		Ter.space();printMoney(listeJetons);
		
		var map = new HashMap<Money,Integer>();
		
		while(true) {
			var choix = (int) Ter.ln("Jetons à prendre (index) : ", 1, true);
			var money = listeJetons.get(choix);
			
			if(board.getJetons().get(money) > 3) {
				map.put(money, 2);
				return map;
			}
			Ter.ln("Pas assez de jeton");
		}
	}
	
	private Map<Money,Integer> choseDifferentMoney(ArrayList<Money> listeJetons) {
		Ter.space();printMoney(listeJetons);
		
		var map = new HashMap<Money,Integer>();
		
		while(true) {
			if(map.size() == 3)return map;
			
			var choix = (int) Ter.ln("[Choix] Choisir les trois jetons à prendre :",1,false);
			if(choix < 0 || choix > listeJetons.size()-1) continue;
			var money = listeJetons.get(choix);
			
			if(board.getJetons().get(money) > 0) { // au moins un jeton
				if(map.putIfAbsent(money, 1) == null) {// si pas déjà présent dans la liste
					Ter.ln("choix valide");
				}else {
					Ter.ln("Jeton déjà présent\n");
					continue;
				}
			}else Ter.ln("Pas assez de jeton du type\n");
			
		}
	}
	
	
	private Card buyCardEvent(int currentPlayer) {
		board.printGrille
		
		return board.takeCard();;
		
	}
	
	private Card reserveCardEvent(int currentPlayer) {
		return null;
	}
	
	public void runner() {
		var currentPlayer = 0;
		while(true) {
			for(var i:board.getJoueurs()) {
				Ter.space();
				Ter.ln("Tour du joueur "+board.getJoueurs().get(currentPlayer).getName());
				i.printStat();
				
				var choix = 0;
				while(choix < 1 || choix > 3) {
					choix = (int)Ter.ln("[\nChoix] Prendre des jetons (1) ou acheter une carte (2) ou réserver une carte (3) : ",1,false);
					if(choix == 1) { // prendre des jetons
						var mapMoney = moneyEvent(currentPlayer);
						Ter.ln(new StringBuilder().append("\nVous avez pris les jetons : ").append(mapMoney));
						i.addMoney(mapMoney);
						board.subMoney(mapMoney);						
					}
					if(choix == 2) { // acheter une carte
						var card = buyCardEvent(currentPlayer);
						i.addCard(card);
					}
					if(choix == 3) {
						var card = reserveCardEvent(currentPlayer);
						//i.addReservedCard(card);
						//board.subCard(card);
					}
					
				}

				currentPlayer++;
			}
			currentPlayer=0;
		}
	}
	
	
	
	
}
