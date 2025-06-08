package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import model.*;
import view.Ter;

public class Game {

	private final Board board;
	private final boolean limited;// 0 pour la V1 et 1 pour la V2
	
	public boolean limited() {
		return limited;
	}
	
	public Game(){		
		var nbPlayer = 0;var limitedChoice = -1;
		while(nbPlayer < 2 || nbPlayer > 4) {
			nbPlayer = Ter.sc("[Choix] Saisir le nombre de joueur (minimum 2 et maximum 4) : ");
		}
		while(limitedChoice != 0 && limitedChoice != 1) {
			limitedChoice = Ter.sc("[Choix Saisir la version du jeu (0 pour la V1 et 1 pour la V2) : ");
		}
		limited = (limitedChoice == 0) ? false : true;

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
	        var name = Ter.scS(str.toString());
	        if (name.isEmpty()) throw new IllegalArgumentException("[Erreur] initPlayer - Le nom du joueur ne peut pas être vide");
	        list.add(new Player(name));
	    }
	    return list;
	}
	private Card chooseNoble(Player player) {
		List<Card> lst=board.masterPossibility(player);
		if( lst.size()==1) {
			return lst.get(0);
		}
		var msg=new StringBuilder("Quelles cartes voulez vous ?\n");
		for(int i=0;i<lst.size();i++) {
			msg.append("[").append(i).append("]   ").append(lst.get(i)).append("\n");
		}
		int choice=-1;
		while(choice<0 || choice >=lst.size()){
			choice=Ter.sc(msg.toString());
		}
		return lst.get(choice);
	}
	
	private void gestionNoble(Player p) {
		if(board.masterPosibility(p).isEmpty()) {
			return ;
		}
		var card=chooseNoble(p);
		board.removeMaster(card);
		p.addCard(card);
	}
	
	private void printMoney(ArrayList<Money> liste) {
		var msg = new StringBuilder().append("Liste des jetons :\n");
		for(int i=0;i<liste.size();i++) {
			msg.append("[").append(i).append("]   ").append(liste.get(i)).append(" : ").append(board.getJetons().get(liste.get(i))).append("\n");
		}
		Ter.ln(msg);
	}
	
	private boolean moneyEvent(Player player) {
		if(player.getBank().sommeAccount() >= 10) {Ter.ln("\n Vous avez déjà au moins 10 jetons"); return false;} // pas plus de 10 jetons
		
		//var listeJetons = new ArrayList<Money>(board.getJetons().keySet());
		var tokensMap = board.getJetons().accountWithoutGold();
		var tokensList = new ArrayList<Money>(tokensMap.keySet());
		
		Ter.space();
		printMoney(tokensList);
		
		var choix = Integer.MIN_VALUE;
		var chosenTokens = new HashMap<Money,Integer>();
		while(choix < -1 || choix > 1) {
			choix = Ter.sc("[Choix] Retour (-1) Choisir Trois jetons différents (0) ou deux fois le même ?(1) :");
			if(choix==-1) return false;
			if(choix==0) {
				var tmp = choseDifferentMoney(tokensList);
				if(tmp == null)return false;
				chosenTokens.putAll(tmp);
			}
			if(choix==1) {
				var tmp = choseOneMoney(tokensList);
				if(tmp == null)return false;
				chosenTokens.putAll(tmp);
			}
		}
		
		if(chosenTokens.isEmpty())return false;
		
		player.addMoney(chosenTokens); // ajoute les jetons dans le compte du joueur
		board.getJetons().sub(chosenTokens); // retire les jetons de la pioche
		Ter.ln(new StringBuilder().append("\n[Event] Vous avez pris les jetons : ").append(chosenTokens));
		return true;
	}
	
	private Map<Money,Integer> choseOneMoney(ArrayList<Money> listeJetons){
		Ter.space();printMoney(listeJetons);
		
		var map = new HashMap<Money,Integer>();
		
		while(true) {
			var choix = Ter.sc("Retour(-1), N° du jetons à prendre : ");
			if(choix < -1 || choix >= listeJetons.size())continue; // vérification si c'est une possibilité
			if(choix == -1)return null;

			var money = listeJetons.get(choix);
			if(board.getJetons().get(money) > 3) {//regle du jeux specifique
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
			
			var choix = Ter.sc("[Choix] Retour (-1) Choisir les trois jetons à prendre :");
			if(choix < -1 || choix >= listeJetons.size()) continue;
			if(choix == -1)return null;

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
		Objects.requireNonNull(player);
		
		board.printGrilleBuy();
		
		var listCard = board.getGrilleUpdate();
		var i = Ter.sc("Ligne :");
		var j = Ter.sc("Numéro carte:");
		
		if(i < 0 || i > listCard.size()-1)return false;
		if( j < 0 || j > listCard.get(i).size()-1)return false;
		
		var card = listCard.get(i).get(j);
		
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
		Ter.space();
		board.printGrilleForReservation();
		if(player.getReserved().size() > 2) { // si déjà trois cartes réservées
			Ter.ln("\n[Action refusé] Vous ne pouvez plus rajouter de réservation\n");
			return false;
		}
		var choix = -2;
		while(choix < -1 || choix > 1) {
			choix = (int) Ter.sc("Réservé une carte mystère ou une du plateau ? Retour(-1)  Plateau(0)  Mystère(1) :");
			if(choix==-1)return false;
			if(choix==0) {
				var i = -1;
				var j = -1;
				while((i < 0 || i > 2) || j < 0 || j > 4) {
					 i = Ter.sc("Ligne :");
					 j = Ter.sc("Numéro carte:");
				}
				var card = board.getGrille().get(i).get(j);
				
				player.addReservedCard(card);// ajoute dans les cartes réservées du joueur
				board.removeCard(i, j);// retire et remplace la carte du plateau
				
				// donne si possible du gold au joueur :
				if(board.getJetons().get(Money.GOLD)>0) {
					var oneGold = new HashMap<Money,Integer>();
					oneGold.put(Money.GOLD, 1);
					board.getJetons().sub(oneGold);// je retire un jeton du plateau
					//board.subMoney(oneGold);
					player.addMoney(oneGold);// ajout du gold dans les money du joueur
					Ter.ln("\n[Event] Vous avez reçu un Gold pour la réservation d'une carte");
				}else Ter.ln("\n[Event] Vous n'avez pas reçu de gold en plus card il n'en reste plus");
				
				return true;
			}
			if(choix==1) {
				var i = 0;
				while(i<1 || i>3) {
					i = (int) Ter.sc("Niveau de la carte :");
				}
				var card = board.nextCard(i);
				player.addReservedCard(card);
				var gold=new HashMap<Money, Integer>();
				gold.put(Money.GOLD,1);
				player.addMoney(gold);
				return true;	
			}
		}
		
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
		
		var choix = -2;
		while(choix<-1 || choix > list.size()-1) {
			choix = Ter.sc("[Choix] Retour(-1) | Choisir le numéro de la carte à acheter :");
		}
		if(choix == -1) return false;
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
					choix = Ter.sc("\n[Choix] Prendre des jetons (1) Acheter une carte (2) Réserver une carte (3) Acheter une réservation (4): ");
					switch(choix) {
					case 1 -> {if(!moneyEvent(i)) 			choix = 0;	break;}
					case 2 -> {if(!buyCardEvent(i))			choix = 0; 	break;}
					case 3 -> {if(!reserveCardEvent(i)) 	choix = 0;	break;}
					case 4 -> {if(!buyReservedCardEvent(i))	choix = 0; 	break;}
					}
					
				}
				
				i.update();
				if(i.getPts() > 9) {
					Ter.ln("\n Victoire, nous terminons le tour \n");
					end = true;
				}
				gestionNoble(i);
			}
		}
		board.printWinner();
	}
	
	
	
	
}
