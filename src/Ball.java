/*
ball class
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;

public class Ball extends Thread {
	private boolean movingBottom=false,movingRight;
	private GamePanel panel;
	public int x,y,size=50;//size changes due to lvl.. size=50*lvl
	boolean isPaused=false;
	boolean aliveFlag=true;
	public int health;
	Image BallImage;
	
	private int level;
	
	 public static double gravity = 1500;

	    protected double velocity = 0; // the velocity of the bird in the y axis (velocity <= 0)

	    protected long inTime; // the initial time of the bird

	    public  double maxVelocity = -1300; // the max velocity of the bird

	    public boolean first = false; // first time to start the game

	    public int JUMP_SPEED = -1200;
	    
	    private double t;
	    
	    
	  

	
	public Ball(GamePanel panel,int level,int x,int y,boolean movingRight)
	{
		this.panel=panel;
		ImageIcon ii=new ImageIcon("ball.png");
		BallImage=ii.getImage();
		this.level=level;
		this.size=30*this.level;
		this.health=5*this.level;
		this.x=x;
		this.y=y;
		this.movingRight=movingRight;
		checkvelocity();
		start();
	}
	
	public void checkvelocity()
	{
		if(level==1)
		{
			this.maxVelocity=-1000;
			this.JUMP_SPEED=-900;
		}
	}
	
	public void run()
	{
		// 5 seconds timer
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while((aliveFlag))
		{
		   try {
			   if((this.panel.IsGamePaused==false)&&(this.panel.IsGameOver==false)&&(this.panel.IsGameWon==false))
			   {
			   /*X PART*/
			   if((this.movingRight)&&(this.x+this.size<this.panel.ScreenWidth))
			   {
				   this.x+=4;
			   }
			   if((this.movingRight)&&(this.x+this.size>=this.panel.ScreenWidth)) 
			   {
				   this.movingRight=false;
			   }
			   if((!this.movingRight)&&(this.x>=0))
			   {
				   this.x-=4;
			   }
			   if((!this.movingRight)&&(this.x<=0))
			   {
				   this.movingRight=true;
			   }
			   
			   /*Y PART:*/
			    this.t = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - this.inTime); // time since the last jump

		        this.inTime = System.nanoTime();
		        this.t *= 0.001;

		        if (!first) {
		            this.t = 0;
		            this.first = true;
		        }

		        this.y = (int) (this.y + (this.velocity * this.t) + (0.5 * this.gravity * Math.pow(this.t, 2)));

		        this.velocity = this.velocity + (this.gravity * this.t);

		        if (this.velocity < this.maxVelocity)
		            this.velocity = this.maxVelocity;

		        if (this.y < 0) {
		            this.y = 0;
		        }
		        if (!this.movingBottom) {
		            this.velocity = JUMP_SPEED;
		            this.movingBottom = true;
		        }
			   }
			   else
			   {
				   this.inTime = System.nanoTime();
			   }
		        /*CHECK FLOOR HIT:*/
		        if(this.y+this.size>=this.panel.ScreenHeight-(this.panel.ScreenWidth/10)) {this.bounceUp();}
		        
		        /*CHECK ROCKET HIT*/
		        
		    	// If a rocket hits this BALL
				for(int j=0;j<panel.Rocketlist.size();j++)
				{
					if(panel.Rocketlist.get(j) !=null && 
							panel.Rocketlist.get(j).isAlive())
					{
						int x2=panel.Rocketlist.get(j).x;
						int y2=panel.Rocketlist.get(j).y;
						
						
						if((x2>=this.x)&&(x2<=this.x+this.size) && (y2>=this.y)&&(y2<=this.y+this.size) ||
							
							(x2+this.size>=this.x) && (x2+this.size<=this.x+this.size) &&(y2>=this.y)&&(y2<=this.y+this.size) )
						{
							if(this.health==1)
							{
							    // kill the ball
								aliveFlag=false;
								checkdeadball();//split or dead!!!!!!!!
							}
							else
							{
								this.health--;
							}
							
						
								
								// kill the rocket when
								// rocket thread is in run state.
								panel.Rocketlist.get(j).exploteFlag=true;
								
								// give some time to draw explosion
						}
					}
				}
		        
		        Thread.sleep(20);
			   
			  
		    }
		       catch (InterruptedException e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
		    }
		   
		   boolean die=true;// important af!! killing zombie ball
		   for(int i=0 ; i<this.panel.ballslist.size(); i++)
			{
				  if (this.panel.ballslist.get(i)==this)
				  {
					 die=false;
				  }
			}
		   
		   if(die==true)
		   {
			   this.aliveFlag=false;
			   System.out.println("killed a dead man");
		   }
			panel.repaint();
		}	
	}
	public void checkdeadball()
	{
		if(this.level!=2)
		{
			this.panel.ballslist.add(new Ball(this.panel,this.level/2,this.x,this.y,true));
			this.panel.ballslist.add(new Ball(this.panel,this.level/2,this.x,this.y,false));
			
		}
		this.aliveFlag=false;
		this.panel.ballslist.remove(this);
	}
	
	public void drawBall(Graphics g){
		g.drawImage(BallImage, this.x,this.y,size,size, null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 30)); 
		g.drawString(this.health+"", this.x+this.size/2-6, this.y+this.size/2+6);
	}
	
	public void bounceUp() {
        movingBottom = false;
    }
	

}
