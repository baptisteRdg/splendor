package main;

import controller.Game;
import model.Board;
import model.Card;
import model.CardCsv;
import model.Money;
import model.Player;
import model.Upgrade;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import view.Graphic;
import com.github.forax.zen.Application;
import view.Ter;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//var game = new Game();
		//game.runner();
		Application.run(Color.WHITE, context ->{
			var b=new Graphic(context);
			var P=new Player("P1");
			b.drawEnd(P);
			
			
		});
	}

}
