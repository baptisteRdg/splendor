package controller;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import view.Graphic;
import view.Ter;
import model.*;
public class graphicAction {
	

	public CoordClic getClic(Graphic graph) {
		var event = graph.getContext().pollOrWaitEvent(1000000000);
		if(event==null){return null;}// Il existe d'autre type wait mais je ne m'en rappel plus
		switch(event) {
		case KeyboardEvent _ :return null;
		  // Recup la touche du clavier
		case PointerEvent pe:
		  var location = pe.location();
		  return new CoordClic(location.x(), location.y());
		default :
		  return null;
		}
	}
	
	
	public List<Player> players(Graphic graph){
		int nb_player=0;
		while(nb_player==0) {
			var clic_coord=getClic(graph);
			if(clic_coord==null) {
				continue;
			}
			nb_player=graph.clicToNbPeople(clic_coord);
		}
		switch(nb_player){
		case 2: return List.of(new Player("P1"),new Player("P2"));
		case 3: return List.of(new Player("P1"),new Player("P2"),new Player("P3"));
		case 4: return List.of(new Player("P1"),new Player("P2"),new Player("P3"),new Player("P4"));
		default: throw new IllegalArgumentException("problem init list people");
		}
	}
	
	public List<Card> cards(){
		try {
			var cards=CardCsv.cardsList();
			return cards;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Board board(Graphic graph) {
		var players=players(graph);
		var cards=cards();
		var board=new Board(players.size(), players, cards);
		return board;
	}
	public int buyOrMoneyOrReserved(Graphic graph,Board b,Player P){
		graph.drawBoard(b);
		graph.drawPlayer(P);
		var list_choice=List.of(new String("Buy"),new String("Money"),new String("Reserved"));
		graph.drawChoice(list_choice);
		int choice=-1;
		while(choice==-1) {
			var clic=getClic(graph);
			if(clic==null) {
				continue;
			}
			choice=graph.clicToChoice(clic, list_choice);
		}
		return choice;
	}
	public int PlateOrMyster(Graphic graph) {
		var list_choice=List.of(new String("PLate"),new String("Myster"));
		graph.drawChoice(list_choice);
		int choice=-1;
		while(choice==-1) {
			var clic=getClic(graph);
			if(clic==null) {
				continue;
			}
			choice=graph.clicToChoice(clic, list_choice);
		}
		return choice;
	}
	public int levelCard(Graphic graph){
		var list_choice=List.of(new String("Level 1"),new String("Level 2"),new String("Level 3"));
		graph.drawChoice(list_choice);
		int choice=-1;
		while(choice==-1) {
			var clic=getClic(graph);
			if(clic==null) {
				continue;
			}
			choice=graph.clicToChoice(clic, list_choice);
		}
		return choice;
	}
	public int sameOrDifferentMoney(Graphic graph){
		var list_choice=List.of(new String("2 same money"),new String("3 different money"));
		graph.drawChoice(list_choice);
		int choice=-1;
		while(choice==-1) {
			var clic=getClic(graph);
			if(clic==null) {
				continue;
			}
			choice=graph.clicToChoice(clic, list_choice);
		}
		return choice;
	}
	
	public boolean reservedCard(Board b,Player P,Graphic graph) {
		if(P.getReserved().size() > 2) { // si déjà trois cartes réservées
			
			return false;
		}
		var choice=PlateOrMyster(graph);
		if(choice==0) {
			var coord = clicToCard(b, graph);
			var card=b.getCard(coord.y(), coord.x());
			P.addReservedCard(card);
			b.removeCard(coord.y(), coord.x());
		}
		if(choice==1) {
			var level=levelCard(graph);
			var card = b.nextCard(level);
			P.addReservedCard(card);
			
		}
		if(b.getJetons().get(Money.GOLD)>0) {
			var oneGold = new HashMap<Money,Integer>();
			oneGold.put(Money.GOLD, 1);
			b.getJetons().sub(oneGold);
			P.addMoney(oneGold);
		}
		return true;
	}
	
	public CoordClic clicToCard(Board b,Graphic graph) {
		CoordClic coord=null;
		var listCard = b.getGrilleUpdate();
		while(coord==null) {
			var clic=getClic(graph);
			if(clic==null) {
				continue;
			}
			coord=graph.clicToCoordCard(clic);
			var i = coord.y();
			var j = coord.x();
			if(i < 0 || i > listCard.size()-1) {coord=null;continue;}
			if( j < 0 || j > listCard.get(i).size()-1){coord=null;continue;}
		}
		return coord;
	}
	
	public Map<Money,Integer> takeDifferentMoney(Board b,Graphic graph) {
		var map = new HashMap<Money,Integer>();
		while(true) {
			if(map.size() == 3)return map;
			
			CoordClic coord=null;
			Money money=null;
			while(coord==null|| money==null) {
				coord=getClic(graph);
				if(coord==null) {
					continue;
				}
				money=graph.clicToMoneyBank(coord, b);
			}

			if(b.getJetons().get(money) > 0) { // au moins un jeton
				map.putIfAbsent(money, 1);
			}
		}
	}
	
	public Map<Money,Integer> sameMoney(Board b,Graphic graph){
		var map = new HashMap<Money,Integer>();
		while(true) {
			if(map.size() == 1)return map;
			
			CoordClic coord=null;
			Money money=null;
			while(coord==null) {
				coord=getClic(graph);
				
				if(coord==null) {
					continue;
				}
				money=graph.clicToMoneyBank(coord, b);
				if(money==null) {
					coord=null;
					continue;
				}
			}

			if(b.getJetons().get(money) > 3) { // au moins un jeton
				map.putIfAbsent(money, 2);
			}
		}
	}
	
	
	public boolean buyCard(Board b,Player P,Graphic graph){
		
		var coord=clicToCard(b, graph);
		var card=b.getCard(coord.y(), coord.x());
		System.out.println(card);
		if(!P.buy(card)) { // pas assez d'argent
			return false;
		}
		P.addCard(card);// ajoute la carte dans les possessions du joueur
		b.removeCard(coord.y(), coord.x()); // retire et remplace la carte du plateau
		return true;
	}
	
	public boolean getMoney(Board b,Player P,Graphic graph) {
		int choice=sameOrDifferentMoney(graph);
		var chosenTokens = new HashMap<Money,Integer>();
		if(choice==0) {
			var tmp=sameMoney(b, graph);
			chosenTokens.putAll(tmp);
		}
		if(choice==1) {
			var tmp = takeDifferentMoney(b,graph);
			chosenTokens.putAll(tmp);
		}
		P.addMoney(chosenTokens);
		b.getJetons().sub(chosenTokens);
		return true;
	}
	
	public void runner() {
		Application.run(Color.WHITE, context ->{
			var graph=new Graphic(context);

			graph.drawMenu();
			var board=board(graph);
			//System.out.println(board);
			graph.clear();
			var end=false;
			while(!end) {
				for(var i:board.getJoueurs()) {
					graph.clear();
					graph.drawPlayer(i);
					var great=false;
					while(!great) {
						int choice=buyOrMoneyOrReserved(graph,board,i);
						System.out.println(choice);
						switch(choice) {
							case 0 : great=buyCard(board, i, graph);break;
							case 1 : great=getMoney(board, i, graph);break;
							case 2 : great=reservedCard(board, i, graph);break;
							}
						}
					
				i.update();
				if(i.getPts() > 9) {
					end = true;
				}
				//gestionNoble(i);
				}
				
				}
			
		});
	}
	
}
