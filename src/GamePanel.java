/*
renders the game on screen
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;


public class GamePanel extends JPanel{
	public AircraftPlayer[] players ;
	Image backGroundImage;
	Image FloorImage;

	public boolean IsGamePaused=false;
	public boolean IsGameOver=false;
	public boolean IsGameWon=false;
	public boolean isMultiplayer=false;

	Shooting_thread shooting_thread;
	
	ArrayList<Ball> ballslist = new ArrayList<Ball>();
	ArrayList<Rocket> Rocketlist = new ArrayList<Rocket>();
	
	public int ScreenHeight;
	public int ScreenWidth;
	public GameManager gamemanager;
	
	public level arr[]=new level[10];
	public int levelindex=0;
	
	public TimerThread timerthread;


	// no arguments = single player
	public GamePanel(){
		init();
	}

	public void init(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.ScreenHeight=(int) screenSize.getHeight();
		this.ScreenWidth=(int) screenSize.getWidth();
		ImageIcon ii =new ImageIcon("mountainbackground.jpg");
		backGroundImage= ii.getImage();

		ImageIcon Floorii =new ImageIcon("floor.jpg");
		FloorImage=Floorii.getImage();

		players = new AircraftPlayer[2];
		players[0] = new AircraftPlayer(this);
		if (isMultiplayer)
			players[1] = new AircraftPlayer(this);
		shooting_thread= new Shooting_thread(this);

		this.timerthread=new TimerThread(this);

		int maxballs=3;
		int ballscapacity=4;

		for(int i=0;i<10;i++)
		{
			this.arr[i]=new level(maxballs,ballscapacity);
			ballscapacity++;
		}
		gamemanager=new GameManager(this,this.arr[levelindex].maxballs,this.arr[levelindex].ballscapacity);


		addKeyListener(new KL (this));
		setFocusable(true);
	}

	// string argument = multiplayer, being either the server or the client
	public GamePanel(String identifier){
		if (identifier.equals("SERVER")){
//			startServer();
		}

		else {
			String ip;
			int port;
		}

		init();
	}

	public void paintComponent(Graphics g)
	{

		super.paintComponent(g);
		g.drawImage(backGroundImage,0,0,getWidth(),getHeight(), null);
		for(int i=0;i<10;i++)
		{
			g.drawImage(FloorImage,i*this.ScreenWidth/10,this.ScreenHeight-this.ScreenWidth/10,this.ScreenWidth/10,this.ScreenWidth/10, null);
		}

		for (AircraftPlayer player : players)
			if (player != null)
				player.drawAircraft(g);

		for(int i=0 ; i<ballslist.size(); i++)
		{
			  if (ballslist.get(i).isAlive())
			  {
				  ballslist.get(i).drawBall(g);
			  }
		}
		
		for(int i=0;i<Rocketlist.size();i++)
		{
			if(Rocketlist.get(i)!=null && Rocketlist.get(i).isAlive())
				Rocketlist.get(i).drawRocket(g);
		} 
		if(this.IsGamePaused)
		{
			Color c;
			c=new Color(100, 100, 100, 100);
			g.setColor(c);
			g.fillRect(0, 0, this.ScreenWidth, this.ScreenHeight);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g.setColor(Color.RED);
			g.drawString("CLICK O TO RESUME", this.ScreenWidth/2-100, this.ScreenHeight/2-100);
		}
		if(this.IsGameOver)
		{
			Color c;
			c=new Color(100, 100, 100, 100);
			g.setColor(c);
			g.fillRect(0, 0, this.ScreenWidth, this.ScreenHeight);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g.setColor(Color.RED);
			g.drawString("CLICK R TO RESTART", this.ScreenWidth/2-100, this.ScreenHeight/2-100);
		}
		if(this.IsGameWon)
		{
			Color c;
			c=new Color(100, 100, 100, 100);
			g.setColor(c);
			g.fillRect(0, 0, this.ScreenWidth, this.ScreenHeight);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g.setColor(Color.RED);
			g.drawString("CLICK N TO NEXT LEVEL", this.ScreenWidth/2-100, this.ScreenHeight/2-100);
		}
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.drawString("LEVEL: "+(this.levelindex+1),0,(int)(this.ScreenHeight*0.9));
		g.drawString("TIME: "+(this.timerthread.finaltime),(int)(this.ScreenWidth*0.5)-50,(int)(this.ScreenHeight*0.1));
		
	}


	class KL extends KeyAdapter
	{
		public GamePanel panel;
		public KL(GamePanel panel1)
		{
			this.panel=panel1;
		}
		
		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode();
			if ((code == KeyEvent.VK_RIGHT) && (players[0].right == false)) {
				players[0].right = true;
				System.out.println("right clicked");
				if (isMultiplayer) {
					if (isClient)
						client.sendClicked(1);
					else
						server.sendClicked(1);
				}
			} else {
				if ((code == KeyEvent.VK_LEFT) && (players[0].left == false)) {
					players[0].left = true;
					System.out.println("left clicked");
					if (isMultiplayer) {
						if (isClient)
							client.sendClicked(1);
						else
							server.sendClicked(1);
					}
				}

				if ((code == KeyEvent.VK_SPACE) && (players[0].space == false)) {
					players[0].space = true;
					System.out.println("space clicked");
					if (isMultiplayer) {
						if (isClient)
							client.sendClicked(1);
						else
							server.sendClicked(1);
					}
				}
				if ((code == KeyEvent.VK_P) && (IsGamePaused == false)) {
					IsGamePaused = true;
					System.out.println("game stop");
					if (isMultiplayer){
						if (isClient)
							client.sendStop();
						else
							client.sendStop();
					}

				}
				if ((code == KeyEvent.VK_O) && (IsGamePaused == true)) {
					IsGamePaused = false;
					System.out.println("game resume");
					if (isMultiplayer){
						if (isClient)
							client.sendResume();
						else
							client.sendResume();
					}
				}
				if ((code == KeyEvent.VK_R)/*&&(IsGameOver==true)*/) {
					this.panel.ballslist.clear();
					System.out.println("balllist size= " + ballslist.size());
					this.panel.gamemanager = new GameManager(this.panel, this.panel.arr[levelindex].maxballs, this.panel.arr[levelindex].ballscapacity);
					System.out.println("game start again");
					IsGameOver = false;
					IsGamePaused = false;
					IsGameWon = false;
				}
				if ((code == KeyEvent.VK_N) && (IsGameWon == true)) {
					this.panel.ballslist.clear();
					System.out.println("balllist size= " + ballslist.size());
					this.panel.levelindex++;
					this.panel.gamemanager = new GameManager(this.panel, this.panel.arr[levelindex].maxballs, this.panel.arr[levelindex].ballscapacity);
					System.out.println("game start again");
					IsGameWon = false;
				}
			}
		}
			
		public void keyReleased(KeyEvent e)
		{
			if(!IsGamePaused)
			{
				int code=e.getKeyCode();
			
			if((code==KeyEvent.VK_RIGHT)&&(players[0].right==true))
			{ 
				players[0].right=false;
				System.out.println("right released");
				if (isMultiplayer){
					if (isClient)
						client.sendReleased(1);
					else
						client.sendReleased(1);
				}
			}
			
			if((code==KeyEvent.VK_LEFT)&&(players[0].left==true))
			{ 
				players[0].left=false;
				System.out.println("left released");
				if (isMultiplayer){
					if (isClient)
						client.sendReleased(1);
					else
						client.sendReleased(1);
				}
			}
			
			
			if((code==KeyEvent.VK_SPACE)&&(players[0].space==true))
			{ 
				players[0].space=false;
				System.out.println("space released");
				if (isMultiplayer){
					if (isClient)
						client.sendReleased(1);
					else
						client.sendReleased(1);
				}
			}
		}
	}

	public void handleMultiplayer(byte action){
			switch (action){
				case 1: // space
					break;

				case 2: // right
					break;

				case 3: // left
					break;
			}
	}
}

	public void  hideMouseCursor(){
		//Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorimg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				cursorimg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JPanel.
		setCursor(blankCursor);	
	}

	public static void main(String[] args) {
		GamePanel bp;
		Scanner input = new Scanner(System.in);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame f=new JFrame("Chichen Invader Pre MS ver 0 2019 (c)");

		System.out.println("1 for multiplayer, 0 for singleplayer");
		if (input.nextInt() == 0)
			bp =new GamePanel();

		else {
			System.out.println("1 for being a server, 0 for being a client");
			if (input.nextInt() == 1){
				bp = new GamePanel("SERVER");
			}
			else {
				System.out.println("Enter IP:PORT");
				bp = new GamePanel(input.next());
			}
		}







		f.add(bp);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocation(0, 0);
		f.setSize((int)screenSize.getWidth(),(int)screenSize.getHeight());
		f.setResizable(false);
		f.setVisible(true);	
		f.setFocusable(false);
		bp.hideMouseCursor();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
}

}