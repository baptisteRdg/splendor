package view;
import model.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;

public class Graphic {
	private final int  width;
	private final int height;
	//private final HashMap<String, Image> picture;
	private final ApplicationContext context;
	private final Font font;
	
	@SuppressWarnings("unused")
	public Graphic(ApplicationContext context) {
		
		Objects.requireNonNull(context);
		//Objects.requireNonNull(picture_name);
		var screenInfo=context.getScreenInfo();
		this.width=screenInfo.width();
		this.height=screenInfo.height();
		this.context=context;
		this.font=new Font("Arial", Font.BOLD, 12);
	
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
		drawFilledRectangle(width*0.87, height*0.03,width*0.10,height*0.05, Color.DARK_GRAY);
		drawText(width*0.89,height*0.065,new String("Point: "+pts),font,Color.BLACK);		
	}
	
	
	public void drawName(Player P) {
		var name= P.getName();
		drawFilledRectangle(width*0.03, height*0.03, width*0.1, height*0.05, Color.DARK_GRAY);
		drawText(width*0.05, height*0.065, new String("Player "+name), font, Color.black);
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
		
		for(var k:money.keySet()) {
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
		drawMoney(P.getMoney(), width*0.25, height*0.75, width*0.20, height*0.20, Color.DARK_GRAY, Color.black, new String("Porte-monnaie"));
		drawMoney(P.getAdventage(), width*0.45, height*0.75, width*0.20, height*0.20, Color.DARK_GRAY, Color.black, new String("Advantage"));
		
	}
	
	public void drawBank(Board b) {
		drawMoney(b.getJetons(), width*0.03, height*0.20, width*0.1, height*0.60,Color.DARK_GRAY, Color.black, new String("Banque"));
	}
	
	public void drawCards(Board b) {
		var x_first=width*0.20;
		var y_first=height*0.20;
		var heigh_card=(height*0.70-y_first)/4;
		var width_card=(width*0.70-x_first)/4;
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
}
