/*
a class of the shooter
 */

import java.awt.*;
import javax.swing.*;

public class AircraftPlayer extends Thread  
{
	GamePanel panel;
	int size=100;
	int x=500, y=0;
	 
	Image aircraftImage;
	
	boolean isPaused=false;
	public boolean left=false,right=false,space=false;

	public AircraftPlayer(GamePanel panel)
	{
		this.panel=panel;
		this.y=this.panel.ScreenHeight-this.panel.ScreenWidth/10-this.size;
		ImageIcon ii=new ImageIcon("Aircraft1.png");
		aircraftImage=ii.getImage();
		start();
	} 
	
	public void run()
	{
		while(true)
		{
		   try {
			   if(!(this.panel.IsGamePaused)&&!(this.panel.IsGameOver)&&!(this.panel.IsGameWon))
			   {
			   if(this.right==true) {this.x+=1;}
				if(this.left==true) {this.x-=1;}
				if(this.x+this.size>=this.panel.ScreenWidth) {this.x=this.panel.ScreenWidth-this.size;}
				if(this.x<=0) {this.x=0;}
				
				///////////////////////////////////////////////////////
				for(int i=0;i<panel.ballslist.size();i++)
				{
						int x2=panel.ballslist.get(i).x;
						int y2=panel.ballslist.get(i).y;
						
						
						if((x2>=this.x)&&(x2<=this.x+this.size) && (y2>=this.y)&&(y2<=this.y+this.size) ||
							
							(x2+panel.ballslist.size()>=this.x) && (x2+panel.ballslist.size()<=this.x+this.size) &&(y2>=this.y)&&(y2<=this.y+this.size) )
						{
							this.panel.IsGameOver=true;
							System.out.println("player died!!!!!!!!!!!");
						}
					}
			Thread.sleep(2);
			   }
		      }
		   catch (InterruptedException e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
		    }
			panel.repaint();
		}
	}
	
	public void drawAircraft(Graphics g){
		g.drawImage(aircraftImage, x,y,size,size, null);
	}
}
