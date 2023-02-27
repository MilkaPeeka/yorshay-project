/*
counting time since game began
 */

import java.util.concurrent.TimeUnit;

public class TimerThread extends Thread 
{
	public GamePanel panel; 
	public double t;
	public int finaltime;
    protected double inTime; // the initial time of the bird

	
	public TimerThread(GamePanel panel1)
	{
		this.inTime =TimeUnit.NANOSECONDS.toSeconds(System.nanoTime());
		this.panel=panel1;
		start();
	}
	
	public void run()
	{
		while(true)
		  {
		  try {
			  if((this.panel.IsGamePaused==false)&&(this.panel.IsGameOver==false)&&(this.panel.IsGameWon==false))
			   {
			  this.t = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime()); 
			  this.finaltime= (int)(this.t-this.inTime);
			   }
			  else
			   {
				   System.out.println("boolbool");
					this.inTime=this.t;
			   }
			  Thread.sleep(1000);
			  }
		  
		   catch (InterruptedException e) {
				// TODO Auto-generated catch block
				 e.printStackTrace();
			    }
		  }
			   
	}
}
