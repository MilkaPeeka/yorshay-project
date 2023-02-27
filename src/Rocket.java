/*
this is the rocket that collides with the ball

 */

import javax.swing.*;
import java.awt.*;

public class Rocket extends Thread
{
	int x;
	int y;
	
	GamePanel panel;
	Image rocketImage;
	
	boolean exploteFlag=false;
	
	// same size for all the rockets.
	static int size=10;
	

	public Rocket(int x,int y,GamePanel panel)
	{
		this.x=x;
		this.y=y;
		ImageIcon ii=new ImageIcon("Bullet.png");
		rocketImage=ii.getImage();
		this.panel=panel;
		start();
	}
	
	public void run()
	{
		while(true)
		{
			
			
			try {
				 if(!(this.panel.IsGamePaused)&&!(this.panel.IsGameOver)&&!(this.panel.IsGameWon))
				 {
				if(!exploteFlag)
				{
					y-=10;
				}
				else
					break;
				Thread.sleep(10);
				 }
				} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
				if(y<=0)
					break;
			
				panel.repaint();
			
			// kill this rocket
			
		}
		this.panel.Rocketlist.remove(this);
		
	}
	
	public void drawRocket(Graphics g){
		if(!exploteFlag)
		{
			g.drawImage(rocketImage, x, y, size,size+30,null);	
		}
		else
		{
			 ImageIcon ii = new ImageIcon("Explosiongif.gif");
			 rocketImage=ii.getImage();
			 g.drawImage(rocketImage, x, y-50, size+30,size+30,null);
		}
	}
}
