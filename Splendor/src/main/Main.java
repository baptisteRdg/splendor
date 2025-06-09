package main;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.github.forax.zen.Application;

import controller.Game;
import controller.graphicAction;
import model.Board;
import model.CardCsv;
import model.Player;
import view.Graphic;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		var game = new Game();
		//game.runner();
		Application.run(Color.WHITE, context ->{
			var clic=new graphicAction(context);
			var P1=new Player("lala");
			var P2=new Player("lulu");
			Board board;
			try {
				board = new Board(2, List.of(P1,P2), CardCsv.cardsList());
				var graph =new Graphic(context);
				graph.drawBoard(board);
				graph.drawPlayer(P1);
				var gra=new graphicAction(context);
				System.out.println(graph.clicToMoneyBank(gra.getClic(),board));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});
	}

}
