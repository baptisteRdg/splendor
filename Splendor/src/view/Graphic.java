package view;
import model.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;

public class Graphic {
	private final int  width;
	private final int height;
	//private final HashMap<String, Image> picture;
	private final ApplicationContext context;
	private final Font font;
	private final double player_point_start_x;
	private final double player_point_start_y;
	private final double player_point_width;
	private final double player_point_height;
	private final double player_point_text_x;
	private final double player_point_text_y;
	private final double player_name_start_x;
	private final double player_name_start_y;
	private final double player_name_width;
	private final double player_name_height;
	private final double player_name_text_x;
	private final double player_name_text_y;
	private final double player_money_x;
	private final double player_money_y;
	private final double player_money_width;
	private final double player_money_heigh;
	private final double player_advantage_x;
	private final double player_advantage_y;
	private final double player_advantage_width;
	private final double player_advantage_heigh;
	private final double bank_heigh;
	private final double bank_width;
	private final double bank_x;
	private final double bank_y1;
	private final double board_x;
	private final double board_y;
	private final double board_width;
	private final double board_heigh;
	private final double width_card;
	private final double height_card;
	private final List<Money> list_money;
	
	@SuppressWarnings("unused")
	public Graphic(ApplicationContext context) {
		
		Objects.requireNonNull(context);
		//Objects.requireNonNull(picture_name);
		var screenInfo=context.getScreenInfo();
		this.width=screenInfo.width();
		this.height=screenInfo.height();
		this.context=context;
		this.font=new Font("Arial", Font.BOLD, 12);
		player_point_start_x=width*0.87;
		player_point_start_y=height*0.03;
		player_point_width=width*0.10;
		player_point_height=height*0.05;
		player_point_text_x=width*0.89;
		player_point_text_y=height*0.065;
		player_name_start_x=width*0.03;
		player_name_start_y=height*0.03;
		player_name_width=width*0.1;
		player_name_height=height*0.05;
		player_name_text_x=width*0.05;
		player_name_text_y=height*0.065;
		player_money_heigh=height*0.20;
		player_money_width=width*0.20;
		player_money_x=width*0.25;
		player_money_y=height*0.75;
		player_advantage_heigh=height*0.20;
		player_advantage_width=width*0.20;
		player_advantage_x=width*0.45;
		player_advantage_y=height*0.75;
		bank_heigh=height*0.60;
		bank_width=width*0.10;
		bank_x=width*0.1;
		bank_y1=height*0.2;
		board_x=width*0.20;
		board_y=height*0.20;
		board_heigh=height*0.70;
		board_width=width*0.70;
		width_card=(board_width-board_x)/4;
		height_card=(board_heigh-board_y)/4;
		list_money= List.of(Money.DIAMOND,Money.EMERALD,Money.ONYX,Money.RUBY,Money.SAPPHIRE,Money.GOLD);
		//manque les images
		
	}
	
	private void drawFilledRectangle(double x,double y,double size_x,double size_y,Color color) {
		context.renderFrame(graphics->{
			graphics.setColor(color);
			graphics.fillRect((int) x,(int)y,(int)size_x,(int)size_y);
		});
	}
	
	private void drawText(double x,double y,String s,Font font,Color color) {
		context.renderFrame(graphics->{
			graphics.setColor(color);
			graphics.setFont(font);
			graphics.drawString(s, (int)x, (int)y);
		});
	}
	
	private void  drawCircle(double x,double y,double rayon,Color color) {
		context.renderFrame(graphics->{
			graphics.setColor(color);
			graphics.fillOval((int)x, (int)y, (int)rayon,(int) rayon);
		});
	}
	public void drawPoint(Player P) {
		var pts= P.getPts();
		drawFilledRectangle(player_point_start_x, player_point_start_y,player_point_width,player_point_height, Color.DARK_GRAY);
		drawText(player_point_text_x,player_point_text_y,new String("Point: "+pts),font,Color.BLACK);		
	}
	
	
	public void drawName(Player P) {
		var name= P.getName();
		drawFilledRectangle(player_name_start_x,player_name_start_y ,player_name_width ,player_name_height , Color.DARK_GRAY);
		drawText(player_name_text_x, player_name_text_y, new String("Player "+name), font, Color.black);
	}
	
	public void drawMoney(Map<Money,Integer > money,double x,double y,double size_x,double size_y,Color color_rect,Color color_font,String header) {
		int size=money.size();
		drawFilledRectangle(x, y, size_x, size_y, color_rect);
		drawText(x+size_x*0.05, y+size_y*0.05, header, font, color_font);
		if(size<=0) {
			return;
		}
		int little_height=(int)(size_y-size_y*0.2)/size;
		y+=size_y*0.20;
		
		for(var k:list_money) {
			drawCircle(x+size_x*0.075,y, size_x*0.05, k.toColor());
			drawText(x+size_x*0.15,y , new String(" : "+money.get(k)), font, color_font);//voir comment afficher les gens
			y+=little_height;
		}
	}
	
	public void drawCard(Card C,double x,double y,double size_x,double size_y,Color background) {
		x=x+size_x*0.2;//creer nouvelle variable
		y= y+size_y*0.2;
		size_y=size_y-size_y*0.2;
		size_x=size_x-size_x*0.2;
		drawFilledRectangle(x, y, size_x, size_y, background);
		if(C.level()<=3) {drawText(x+size_x*0.05, y+size_y*0.15, new String("Card level:"+C.level()),font , Color.black);
		}else {
			drawText(x+size_x*0.05, y+size_y*0.15, new String("Master Card"),font , Color.black);
		}
		drawText(x+size_x*0.05,y+size_y*0.30 , new String("Points: "+C.point()), font, Color.black);
		if(C.advantage()!=null) {
			drawText(x+size_x*0.05,y+size_y*0.45,new String("Adventage:"), font, Color.black);
			drawCircle(x+size_x*0.85,y+size_y*0.4, size_y*0.1, C.advantage().toColor());
		}	
		drawMoney(C.cost(), x+size_x*0.05, y+size_y*0.5, size_x*0.95, size_y*0.5, background, Color.black, new String("Cost"));
		}
	
	public void drawPlayer(Player P){
		drawPoint(P);
		drawName(P);
		drawMoney(P.getMoney(),player_money_x, player_money_y, player_money_width, player_money_heigh, Color.DARK_GRAY, Color.black, new String("Porte-monnaie"));
		drawMoney(P.getAdventage(), player_advantage_x, player_advantage_y, player_advantage_width, player_advantage_heigh, Color.DARK_GRAY, Color.black, new String("Advantage"));
		
	}
	
	public void drawBank(Board b) {
		drawMoney(b.getJetons(), bank_x, bank_y1, bank_width, bank_heigh,Color.DARK_GRAY, Color.black, new String("Banque"));
	}
	
	public void drawCards(Board b) {
		var x_first=board_x;
		var y_first=board_y;
		var heigh_card=height_card;
		var width_card=this.width_card;
		var x=x_first;
		var y=y_first;
		for(var cards:b.getGrille()) {
			x=x_first;
			for(var card:cards) {
				drawCard(card,x,y, width_card,heigh_card,Color.gray );
				x+=width_card;
			}
			y+=heigh_card;
		}
	}
	
	public void drawNumPlayer(int number,double x,double y,double x_size,double y_size,Color color) {
		drawFilledRectangle(x, y, x_size, y_size,color);
		drawText(x+0.1*x_size, y+0.4*y_size, new String(number+" player"), font, Color.black);
	}
	
	public void drawBoard(Board B) {
		drawBank(B);
		drawCards(B);
		
	}
	
	public void drawMenu() {
		drawNumPlayer(2, 0.1*width, height*0.7, width*0.2, height*0.2,Color.DARK_GRAY );
		drawNumPlayer(3, 0.4*width, height*0.7, width*0.2, height*0.2,Color.DARK_GRAY );
		drawNumPlayer(4, 0.7*width, height*0.7, width*0.2, height*0.2,Color.DARK_GRAY );
		var title=new Font("Arial", Font.BOLD, 50);
		drawText(width*0.40, height*0.20, new String("SPLENDOR"),title , Color.BLACK);
	}
	
	public void drawEnd(Player winner) {
		drawFilledRectangle( 0.1*width, height*0.4, width*0.2, height*0.2,Color.DARK_GRAY );
		drawFilledRectangle( 0.4*width, height*0.4, width*0.2, height*0.2,Color.DARK_GRAY );
		drawFilledRectangle( 0.7*width, height*0.4, width*0.2, height*0.2,Color.DARK_GRAY );
		drawText(0.15*width, height*0.5, new String("REPLAY"),font , Color.BLACK);
		drawText( 0.45*width, height*0.5, new String("winner is"+winner.getName()),font , Color.BLACK);
		drawText(0.75*width, height*0.5, new String("QUIT"),font , Color.BLACK);
		
	}
	
	public CoordClic clicToCoordCard(CoordClic clic){
		if((clic.x()>= board_x && clic.x()<= board_x+board_width)||((clic.y()>= board_y && clic.y()<= board_y+board_heigh))) {
			return new CoordClic((int)((clic.x()-board_x)/width_card), (int)((clic.y()-board_y)/height_card));
		}
		return null;
	}
	
	public Money clicToMoneyBank(CoordClic clic, Board b) {
		int size=b.getJetons().size();
		double y= bank_y1+ bank_heigh*0.20;
		int little_height=(int)(bank_heigh*0.8)/size;
		if(!((clic.x()>= bank_x && clic.x()<= bank_x+bank_width)||((clic.y()>= y && clic.y()<= bank_y1+bank_heigh)))) {
			return null;
		}
		int cas=(int) ((clic.y()-y)/little_height);
		System.out.println(cas);
		if(cas==5) {
			return null;
		}
		return list_money.get(cas);
	}
}
