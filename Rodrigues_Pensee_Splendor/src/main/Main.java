package main;
import controller.Game;
import controller.graphicAction;
import view.Ter;


public class Main {

	public static void main(String[] args) {
		var display = false;
		if(args.length == 0) {
			for (String arg : args) {
	            	if(arg.equals("--text")) display = false;
	        	}
		}
		if(display) {var screen = new graphicAction(); screen.runner();}
		else {
			var choix = 0;
			while(choix > 2 || choix < 1) {
				choix = Ter.sc("\n⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜\n	\n\n	-:-:- Bienvenue sur Splendor -:-:-\n"
						+ "\n\n sélectionné votre mode de jeu :"
						+ "\n	1) Version classique	(v2)"
						+ "\n	2) Version réduite 	(v1)"
						+ "\n->");
			}
			 
			switch(choix) {
			case 1 ->{var jeu = new Game(false); jeu.runner();break;}
			case 2 ->{var jeu = new Game(true); jeu.runnerLimited();break;}
			}
		}
		
	}
	
	

}
