
package model;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public final class CardCsv {
	
	public static Money StringToMoney(String money) {
		switch (money) {
		case "white": return Money.DIAMOND;
		case "green" :return Money.EMERALD;
		case "red":return Money.RUBY;
		case "black" : return Money.ONYX;
		case "blue":return Money.SAPPHIRE;
		default : throw new IllegalArgumentException("Unexpected value: " + money);
		}
		
	}
	
	public static ArrayList<Card> cardsList() throws IOException{
		var cards = new ArrayList<Card>();
		var path= Paths.get(".//card2.csv");
		var is= Files.newInputStream(path);
		var buffer=new BufferedReader(new InputStreamReader(is));
		String line=buffer.readLine();//suppression du header
		while((line= buffer.readLine())!=null) {
			var data=line.split(",");
			var level=Integer.parseInt(data[0]);
			var cost=new HashMap<Money, Integer>();
					cost.put(Money.DIAMOND,Integer.parseInt(data[3]));
					cost.put(Money.SAPPHIRE,Integer.parseInt(data[4]));
					cost.put(Money.EMERALD,Integer.parseInt(data[5]));
					cost.put(Money.RUBY,Integer.parseInt(data[6]));
					cost.put(Money.ONYX,Integer.parseInt(data[7]));
			var price = new Price(cost);
			var pts=Integer.parseInt(data[2]);
			if(level<=3) {	
				cards.add(new Upgrade(level,"",StringToMoney(data[1]), pts,price));
			}
			else cards.add(new Master(pts,"",price));
		}
		return cards;
		
	}
	
	
}
