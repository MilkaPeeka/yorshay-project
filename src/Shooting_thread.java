/*
here main player creates rockets to shoot
 */

public class Shooting_thread extends Thread{
	GamePanel panel;
	
	public Shooting_thread(GamePanel panel)
	{
		this.panel=panel;
		start();
	}
	
	public void run()
	{
		while(true)
		{
		
		   try {
				if((this.panel.players[0].space==true)&&!(this.panel.IsGamePaused)&&!(this.panel.IsGameOver)&&!(this.panel.IsGameWon))
				{
					panel.Rocketlist.add(new Rocket(this.panel.players[0].x+55, this.panel.players[0].y-15,panel));
				}
			Thread.sleep(200);
		      } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
		    }
			panel.repaint();
		}	
	}

}
