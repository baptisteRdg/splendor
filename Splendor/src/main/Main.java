package main;
import java.util.Objects;

import controller.Game;
import controller.graphicAction;
import view.Ter;


public class Main {

	public static void main(String[] args) {
		Objects.requireNonNull(args);
		var display = true;
	    if(args[0].equals("--text")) display = false;
	    
		if(display) {var screen = new graphicAction(); screen.runner();}
		else {
			var choix = 0;
			while(choix > 2 || choix < 1) {
				choix = Ter.sc("\n⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜\n	\n\n	-:-:- Bienvenue sur Splendor -:-:-\n"
						+ "\n\n sélectionné votre mode de jeu :"
						+ "\n	1) Version classique"
						+ "\n	2) Version réduite"
						+ "\n->");
			}
			 
			switch(choix) {
			case 1 ->{var jeu = new Game(false); jeu.runner();break;}
			case 2 ->{var jeu = new Game(true); jeu.runnerLimited();break;}
			}
		}
		
	}
	
	

}
