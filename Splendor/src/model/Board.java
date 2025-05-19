package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;



public class Board {
	private final int number_of_joueur;
	private final ArrayList<ArrayList<Card>> pioches;
	private final ArrayList<ArrayList<Card>> grille;
	private final List<Player> joueurs;
	private final HashMap<Money, Integer> jetons;
	
	private Card nextCard(int level) {
		if(pioches.get(level-1).isEmpty()) {
			return null;
		}
		var index=(int)(Math.random()*pioches.get(level-1).size());
		var returned=pioches.get(level-1).get(index);
		pioches.get(level-1).remove(index);
		return returned;
		
	}
	
	private HashMap<Money, Integer> firstMoney(){
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
		return tmp;
	}
	
	public Board(int number_player,List<Player> joueur,List<Card> cards){
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(cards);
		
		pioches = new ArrayList<ArrayList<Card>>();
		grille = new ArrayList<ArrayList<Card>>();
		jetons = firstMoney();
		
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
	
	
	public HashMap<Money, Integer> getJetons() {
		return jetons;
	}
	
	public List<Player> getJoueurs() {
		return joueurs;
	}
	
	public void subMoney(Map<Money, Integer> map) {
	    Objects.requireNonNull(map);
	    map.forEach((money, value) -> jetons.merge(money, -value, Integer::sum));
	}
			
	public Card  takeCard(int lig,int col) {
		if(lig >=5 || lig<0 || col>=5 || col <0) {
			throw new IllegalArgumentException("les coordonnés ne sont pas valide");
		}
		var returned=grille.get(lig).get(col);
		Objects.requireNonNull(returned);
		grille.get(lig).remove(col);
		grille.get(lig).add(col, nextCard(lig));
		return returned;
	}
}
