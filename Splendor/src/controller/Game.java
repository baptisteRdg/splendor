package controller;

import java.io.IOException;
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
	private List<Card> initCard(int nbPlayer){
		var list = new ArrayList<Card>();
		try {
			list = CardCsv.cardsList();
		} 
		catch (IOException e) {e.printStackTrace();}
		return list;
	}
	
	/// Permet d'initialiser une liste de joueur
	private ArrayList<Player> initPlayer(int nbPlayer) {
	    var msg = "Saisir le nom du joueur ";
	    var list = new ArrayList<Player>();

	    for(int i = 0; i < nbPlayer; i++) {
	        var str = new StringBuilder().append(msg).append(i+1).append("/").append(nbPlayer).append(" :");
	        var name = (String) Ter.ln(str, 2, false);
	        if (name.isEmpty()) throw new IllegalArgumentException("[Erreur] initPlayer - Le nom du joueur ne peut pas être vide");
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
	
	private boolean moneyEvent(Player player) {
		if(player.numberMonney() >= 10) return false; // pas plus de 10 jetons
		
		var listeJetons = new ArrayList<Money>(board.getJetons().keySet());
		Ter.space();
		printMoney(listeJetons);
		
		var choix = 0;
		var mapMoney = new HashMap<Money,Integer>();
		while(choix != 1 && choix != 2) {
			choix = (int) Ter.ln("[Choix] Retour (0) Choisir Trois jetons différents (1) ou deux fois le même ?(2) :",1,false);
			if(choix==0) return false;
			if(choix==1) mapMoney.putAll(choseDifferentMoney(listeJetons));
			if(choix==2) mapMoney.putAll(choseOneMoney(listeJetons));
		}
		
		if(mapMoney.isEmpty())return false;
		
		player.addMoney(mapMoney);
		board.subMoney(mapMoney);
		Ter.ln(new StringBuilder().append("\n[] Vous avez pris les jetons : ").append(mapMoney));
		return true;
	}
	
	private Map<Money,Integer> choseOneMoney(ArrayList<Money> listeJetons){
		Ter.space();printMoney(listeJetons);
		
		var map = new HashMap<Money,Integer>();
		
		while(true) {
			var choix = (int) Ter.ln("Retour(-1), N° du jetons à prendre : ", 1, true);
			if(choix < -1 || choix > listeJetons.size())continue; // vérification si c'est une possibilité
			if(choix == -1)return map;

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
			
			var choix = (int) Ter.ln("[Choix] Retour (-1) Choisir les trois jetons à prendre :",1,false);
			if(choix < -1 || choix > listeJetons.size()-1) continue;
			if(choix == -1)return map;

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
	
	
	private boolean buyCardEvent(Player player) {
		var i = (int) Ter.ln("Ligne :",1,false);
		var j = (int) Ter.ln("Numéro carte:",1,false);
		var card = board.getGrille().get(i).get(j);
		
		Ter.ln("Carte sélectionné "+ card);
		if(!player.buy(card)) { // pas assez d'argent
			Ter.ln("\n[Refusé] Achat de la carte impossible\n");
			return false;
		}
		Ter.ln("\nAchat de la carte validé");
		player.addCard(card);// ajoute la carte dans les possessions du joueur
		board.removeCard(i, j); // retire et remplace la carte du plateau
		return true;
	}
	
	private boolean reserveCardEvent(Player player) {
		if(player.getReserved().size() > 2) { // si déjà trois cartes réservées
			Ter.ln("\n[Action refusé] Vous ne pouvez plus rajouter de réservation\n");
			return false;
		}
		// choix de l'utilisateur
		var i = (int) Ter.ln("Ligne :",1,false);
		var j = (int) Ter.ln("Numéro carte:",1,false);
		var card = board.getGrille().get(i).get(j);
		
		player.addReservedCard(card);// ajoute dans les cartes réservées du joueur
		board.removeCard(i, j);// retire et remplace la carte du plateau
		return true;
	}
	
	private boolean buyReservedCardEvent(Player player) {
		var list = new ArrayList<Card>(player.getReserved());
		
		if(list.size() < 1) {
			Ter.ln("\n[Refusé] Vous n'avez pas de carte réservé\n");return false;
		}
		var msg = new StringBuilder().append("\nVoici vos réservations :\n");
		for(int i=0; i<list.size();i++) {
			msg.append("n°").append(i).append("  ").append(list.get(i)).append("\n");
		}
		Ter.ln(msg);
		
		var choix = 0;
		while(choix<0 || choix > list.size()) {
			choix = (int) Ter.ln("[Action] Choisir la carte à acheter :",1,false);
		}
		var card = list.get(choix);
		if(!player.buy(card)) {
			Ter.ln("\n[Refusé] Achat de la carte impossible\n");
			return false;
		}
		player.addCard(card);
		player.getReserved().remove(card);
		return true;
	}
	
	public void runner() {
		var end = false;
		while(true && !end) {
			for(var i:board.getJoueurs()) {
				Ter.space();
				Ter.ln("Tour du joueur "+i.getName());
				i.printStat();
				
				var choix = 0;
				while(choix < 1 || choix > 4) {
					choix = (int)Ter.ln("\n[Choix] Prendre des jetons (1) Acheter une carte (2) Réserver une carte (3) Acheter une réservation (4): ",1,false);
					if(choix == 1) { // prendre des jetons
						if(!moneyEvent(i)) choix = 0;
					}
					if(choix == 2) {
						board.printGrille();
						if(!buyCardEvent(i)) choix = 0;
					}
					if(choix == 3) {
						board.printGrille();
						if(!reserveCardEvent(i)) choix = 0;
					}
					if(choix == 4) {
						if(!buyReservedCardEvent(i)) choix = 0;
					}
					
				}
				i.update();
				if(i.getPts() > 0) {
					Ter.ln("\n Victoire, nous terminons le tour \n");
					end = true;
				}

			}
			
			board.printWinner();
		}
	}
	
	
	
	
}
