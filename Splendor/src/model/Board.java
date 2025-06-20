package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


import view.Ter;



public class Board {
	private final int number_of_joueur;
	private final ArrayList<ArrayList<Card>> pioches;
	private final ArrayList<ArrayList<Card>> grille;
	private final List<Player> joueurs;
	private final Bank jetons = new Bank();
	
	public Card nextCard(int level) {
		if(pioches.get(level-1).isEmpty()) return null;
		
		var index=(int)(Math.random()*pioches.get(level-1).size());
		var returned=pioches.get(level-1).get(index);
		pioches.get(level-1).remove(index);
		return returned;
		
	}
	public List<Card> masterPosibility(Player a) {
		var retour=new ArrayList<Card>();
		
		for(var card:grille.get(3)) {
			
			if(a.toJoin(card)) {
				retour.add(card);
			}
		}
		return retour;
	}
	private void firstMoney(){
		var tmp=new HashMap<Money, Integer>();
		int retired=0;
		if(number_of_joueur==2) {
			retired=2;
		}
		if(number_of_joueur==3) {
			retired=3;
		}
		tmp.put(Money.EMERALD, 7-retired);
		tmp.put(Money.DIAMOND,7-retired);
		tmp.put(Money.GOLD,5);
		tmp.put(Money.ONYX,7-retired);
		tmp.put(Money.RUBY,7-retired);
		tmp.put(Money.SAPPHIRE,7-retired);
		
		jetons.add(tmp);
	}
	
	public Board(int number_player,List<Player> joueur,List<Card> cards){
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(cards);
		
		pioches = new ArrayList<ArrayList<Card>>();
		grille = new ArrayList<ArrayList<Card>>();
		firstMoney();
		
		if(number_player<2 || number_player>4) throw new IllegalArgumentException("nombre de joueur invalid");

		number_of_joueur=number_player;
		this.joueurs=List.copyOf(joueur);
		for(int i=0;i<4;i+=1) {
			pioches.add(new ArrayList<Card>());
		}
		for(var a:cards) {
			pioches.get(a.level()-1).add(a);
		}
		
		for(int i=0;i<4;i+=1) {
			grille.add(new ArrayList<Card>());
		}
		for(int j=1;j<=3;j+=1) {
			for(int i=0;i<5;i+=1) {
				grille.get(j-1).add(nextCard(j));
			}}
		
		var number_master=(number_of_joueur==2)?3:(number_of_joueur==3)?4:5;
		for(int i=0;i<number_master;i+=1) {
			grille.get(3).add(nextCard(4));
		}
	}
	
	
	public Bank getJetons() {
		return jetons;
	}
	
	public List<Player> getJoueurs() {
		return joueurs;
	}
	
	public ArrayList<ArrayList<Card>> getGrille(){
		return grille;
	}
	
	public ArrayList<ArrayList<Card>> getGrilleUpdate(boolean limited){
		var list = new ArrayList<ArrayList<Card>>(grille);
		list.remove(3);
		if(limited) {list.remove(2); list.remove(1); } // que les cartes lvl1 pour la V1
		return list;
	}
	
	
	public List<Card> masterPossibility(Player p){
		var tmp=new ArrayList<Card>();
		for(var a:grille.get(3)) {
			if(p.toJoin(a)) {
				tmp.add(a);
			}
		}
		return tmp;
	}
	
	public void printGrille() {
		var msg = new StringBuilder().append("Voici le plateau de jeu\n");
		for(int i=0;i< grille.size();i++) {
			msg.append("Ligne ").append(i).append(" | ");
			for(int j=0;j< grille.get(i).size();j++) {
				msg.append(" ").append(j).append(" (").append(grille.get(i).get(j)).append(")");
			}
			msg.append("\n");
		}
		Ter.ln(msg);
	}
	
	public void printGrilleBuy(boolean limited) {
		var msg = new StringBuilder().append("Sélectionner les coordonnées de la carte à acheter\n");
		var list = getGrilleUpdate(limited);
		for(int i=0;i< list.size();i++) {
			msg.append("Ligne ").append(i).append(" | ");
			for(int j=0;j< grille.get(i).size();j++) {
				msg.append(" ").append(j).append(" (").append(list.get(i).get(j)).append(")");
			}
			msg.append("\n");
		}
		Ter.ln(msg);
	}
	
	public void printGrilleForReservation() {
		var msg = new StringBuilder().append("\nVoici les cartes à réservées, vous pouvez aussi réservé des cartes mystères :\n");
		int i;
		for(i=0;i< grille.size()-1;i++) {
			msg.append("  Ligne ").append(i).append(" | ");
			for(int j=0;j< grille.get(i).size();j++) {
				msg.append(" ").append(j).append(" (").append(grille.get(i).get(j)).append(")");
			}
			msg.append("\n");
		}
		msg.append("  Pioches mystère | Carte de niveau 1, Carte de niveau 2, Carte de niveau 3\n");
		Ter.ln(msg);
	}
			
	
	
	public Card  getCard(int lig,int col) {
		if(lig >=5 || lig<0 || col>=5 || col <0) {
			throw new IllegalArgumentException("les coordonnés ne sont pas valide");
		}
		var returned=grille.get(lig).get(col);
		Objects.requireNonNull(returned);
		grille.get(lig).remove(col);
		grille.get(lig).add(col, nextCard(returned.level() ));
		return returned;
	}
	
	public void printWinner() {
		var winner = new ArrayList<Player>();
		var loser = new ArrayList<Player>();
		
		for(var i:joueurs) {
			if(i.getPts() > 0) winner.add(i);
			else loser.add(i);
		}
        Collections.sort(winner, Comparator.comparingInt(Player::getPts));
        Collections.sort(loser, Comparator.comparingInt(Player::getPts));

        var msg = new StringBuilder().append("Liste des gagnants :\n");
        var cpt =1;
        for(var i:winner) {
        	msg.append("  - N°").append(cpt).append("  ").append(i.getName()).append("  pts:").append(i.getPts()).append("\n");cpt++;
        }
        msg.append("\nListe des perdants :\n");
        for(var i:loser) {
        	msg.append("  - N°").append(cpt).append("  ").append(i.getName()).append("  pts:").append(i.getPts()).append("\n");cpt++;

        }
		Ter.space();
        Ter.ln(msg);
	}
	
	public void removeCard(int lig,int col) {
		if(lig >=5 || lig<0 || col>=5 || col <0)throw new IllegalArgumentException("[Error] removeCard les coordonnés ne sont pas valide");
		grille.get(lig).set(col, nextCard(grille.get(lig).get(col).level()));
	}
	
	public void removeMaster(Card card) {
		Objects.requireNonNull(card);
		grille.get(3).remove(card);
		grille.get(3).add(nextCard(card.level()));
	}
}
