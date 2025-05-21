
package model;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardCsv {
	ArrayList<Card> cards=new ArrayList<Card>();
	
	public Money StringToMoney(String money) {
		switch (money) {
		case "white": return Money.DIAMOND;
		case "green" :return Money.EMERALD;
		case "red":return Money.RUBY;
		case "black" : return Money.ONYX;
		case "blue":return Money.SAPPHIRE;
		default : throw new IllegalArgumentException("Unexpected value: " + money);
		}
		
	}
	
	public List<Card> cardsList() throws IOException{
		var path= Paths.get("/home/paul/Documents/semestre 2/java/splendor/card2.csv");
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
			var pts=Integer.parseInt(data[2]);
			if(level<=3) {	
				cards.add(new Upgrade(level,"",StringToMoney(data[1]), cost,pts));
			}
			else cards.add(new Master(pts,"",(HashMap<Money, Integer>) cost));
		}
		return cards;
		
	}
	
	
}
