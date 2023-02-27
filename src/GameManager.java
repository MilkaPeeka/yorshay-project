/*
manager of the game - creates random balls
 */

import java.util.Random;

public class GameManager extends Thread {
	private static final int RAND_SEED = 123;
	private Random rand;
	private GamePanel panel;
	int maxballs;//=3;
	int ballscapacity;//=4;
	int ballsvaluecounter=0;

	public GameManager(GamePanel panel,int maxballs,int ballscapacity)
	{
		this.rand = new Random(RAND_SEED);
		this.panel=panel;
		this.panel.ballslist.clear();
		this.maxballs=maxballs;
		this.ballscapacity=ballscapacity;
		start();
	}
	
	public void run()
	{
		while(true)
		{
			 try {
		if((ballsvaluecounter<ballscapacity)&&(this.panel.ballslist.size()<maxballs))
		{
			int randsize= this.rand.nextInt(2) + 1;
			int randy=this.rand.nextInt(1000);
			int startdir=this.rand.nextInt(2) + 1;
			if(startdir==1)
			{
			this.panel.ballslist.add(new Ball(this.panel,randsize*2,-100,randy,true));
			}
			else
			{
				this.panel.ballslist.add(new Ball(this.panel,randsize*2,this.panel.ScreenWidth + 100,randy,false));	
			}
			this.ballsvaluecounter+=randsize;
		}
		if((this.ballsvaluecounter>=this.ballscapacity)&&(this.panel.ballslist.size()==0)&&(this.panel.IsGameWon==false))
		{
			this.panel.ballslist.clear();
			System.out.println("game won");
			this.panel.IsGameWon=true;
		}
		Thread.sleep(20);  
    }
 catch (InterruptedException e) {
	// TODO Auto-generated catch block
	 e.printStackTrace();
  }
	}
}
}
