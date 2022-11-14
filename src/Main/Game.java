package Main;


import java.awt.Canvas;
import java.awt.image.DataBufferInt;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Entities.Boss;
import Entities.Enemy;
import Entities.Entity;
import Entities.LifePack;
import Entities.Magical;
import Entities.Player;
import Entities.Shoot;
import Graphics.BossSprites;
import Graphics.Spritesheet;
import Graphics.UI;
import World.Camera;
import World.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	/*Janela*/
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;
	//public static final int WS = Toolkit.getDefaultToolkit().getScreenSize().width;
	//public static final int HS = Toolkit.getDefaultToolkit().getScreenSize().height;
	private int CUR_LEVEL = 1, MAX_LEVEL = 3;
	private int index=0;
	private BufferedImage image;
	private int cutframes = 0;
	public int indexcut=0;
	
	public static List<Magical> spells;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Boss> bosses;
	public static Spritesheet spritesheet;
	public static List<Shoot> shooties;
	public static World world;
	public static Player player;
	public static Random rand;
	public Menu menu;
	
	public static int[] pixels;
	public BufferedImage lightMap;
	public static int[] lightPixels;
	public static int[] minimapPixels; 

	public static BufferedImage minimap;
	
	public UI ui;
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelartfont.ttf");
	public static Font newfont;
	public static int mx,my;
	
	public static String gameState = "Menu";
	private boolean msgGameOver = true;
	private int GameOverFrames = 0;
	private boolean restart = false;
	public boolean saveGame = false;
	public Game() {
		//Sounds.stoptime.loop();
		ui = new UI();
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		//Fullscreem
		//setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
		intitFrame();
		//objetos
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		try {
			lightMap = ImageIO.read(getClass().getResource("/Lightmap.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		lightPixels = new int[lightMap.getWidth()* lightMap.getHeight()];
		lightMap.getRGB(0,0, lightMap.getWidth(), lightMap.getHeight(), lightPixels, 0, lightMap.getWidth());
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bosses = new ArrayList<Boss>();
		shooties = new ArrayList<Shoot>();
		spells = new ArrayList<Magical>();
		spritesheet = new Spritesheet("/spritesheet.png");
		try {
			newfont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(30f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		player = new Player(0,0,16,16,spritesheet.getSprint(32, 0, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		
		minimap = new BufferedImage(World.WIDTH,World.HEIGHT,BufferedImage.TYPE_INT_RGB);
		minimapPixels = ((DataBufferInt)minimap.getRaster().getDataBuffer()).getData();
		
		menu = new Menu();
	}
	public void intitFrame() {
		frame = new JFrame("JOJO"); //Título
		frame.add(this);
		//frame.setUndecorated(true);
		frame.pack();
		//
		frame.setResizable(false);
		Image imagem = null;
		try{
			imagem = ImageIO.read(getClass().getResource("/icon.png"));
		}catch(IOException e){e.printStackTrace();
		}
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage(getClass().getResource("/icon.png"));
		Cursor c = toolkit.createCustomCursor(image, new Point(0,0),"img");
		
		frame.setCursor(c);
		frame.setIconImage(imagem);
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	public synchronized void stop() {
		try {
			isRunning = false;
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static void main(String args[]) {
		Game game = new Game();
		game.start();
		
	}
	public void tick() {
		if(gameState == "Normal") {
			if(this.saveGame) {
				this.saveGame = false;
				String[] opt1 = {"level","life"};
				int[] opt2 = {this.CUR_LEVEL, (int)player.life};
				menu.saveGame(opt1, opt2, 10);
				//System.out.println("Jogo salvo");
			}
			restart = false;
		for(int i = 0; i < entities.size(); i++) {
		Entity e = entities.get(i);
		e.tick();
		}
		for(int i = 0; i < shooties.size();i++) {
			shooties.get(i).tick();
			
		} for(int i = 0; i < spells.size();i++) {
			spells.get(i).tick();
			
		}
		}
		if(gameState == "cutscene") {
			cutframes++;
			if(cutframes >= 60) {
				cutframes = 0;
				indexcut++;}
			if(indexcut >= 12) {
				indexcut=0;
				gameState = "Normal";
				}
		}
		if(gameState == "Stop") {
			if(index == 500)
				for(int c=0;c<3;c++)
				player.tick();
			else { if(rand.nextInt(100)<30) {
					
		for(int i = 0; i < entities.size(); i++) {
		Entity e = entities.get(i);
		e.tick();
		}
		for(int i = 0; i < spells.size();i++) {
			spells.get(i).tick();
				} }
			}
		}
		
		if(enemies.size() == 0 && CUR_LEVEL < MAX_LEVEL) {
			CUR_LEVEL++;
			String newWorld = "level"+CUR_LEVEL+".png";
			World.restart(newWorld);
		}else if(CUR_LEVEL == MAX_LEVEL && entities.size() == 1) {
			gameState = "cutscene";
			
		}
		//GameOver
		 else if(gameState == "Game Over") {
			this.GameOverFrames++;
				if(this.GameOverFrames == 30) {
					this.GameOverFrames = 0;
					if(msgGameOver) 
						msgGameOver = false;
						else {msgGameOver = true;}
					}
				if(restart) {
				restart = false;
				gameState = "Normal";
				CUR_LEVEL = 1;
				String newWorld = "level"+CUR_LEVEL+".png";
				World.restart(newWorld);}
		} 
		 else if(gameState == "Menu") {
				menu.tick();}
				}
	public void applyLight() {
		for(int xx = 0; xx < Game.WIDTH; xx++) {
			for(int yy = 0; yy < Game.HEIGHT; yy++) {
				if(lightPixels[xx+(yy*Game.WIDTH)] == 0xffffffff) {
					pixels[xx+(yy*Game.WIDTH)] = 0;
				}
			}
		}
	}
	
	public void rander() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//objetos graficos
		/*g.setColor(Color.BLACK);
		g.fillRect(50, 50, 10, 10);
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString("OLA MUNDO", 60, 80);
		
		Graphics2D g2 = (Graphics2D) g;*/
		world.rander(g);
		Collections.sort(entities, Entity.nodeSorter);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.rander(g);}
			for(int i = 0; i < shooties.size();i++) {
				shooties.get(i).rander(g);
			} for(int i = 0; i < spells.size();i++) {
				spells.get(i).rander(g);
			}
			//applyLight();
			
			ui.rander(g);
			//BossSprites.rander(g);
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE,   HEIGHT*SCALE, null);
		g.setFont(new Font("arial", Font.BOLD, 15));
		g.drawString("Ammo: "+ Game.player.ammo, 30, 60);
		if(gameState == "Stop") {
			int r = rand.nextInt(150);
			int y = rand.nextInt(150);
			int s = rand.nextInt(150);
				index+=3;
				if(index >=500) {
					index=500;
					r=70;
					y=60;
					s=100;}
			//System.out.println(index);
					Graphics2D g2 = (Graphics2D) g;
					g2.setColor(new Color(r,y,240,s));
					g2.fillOval((player.getX()-Camera.x)*2+16-(index*World.WIDTH/10)/2,(player.getY()-Camera.y)*2+24-(index*World.HEIGHT/10)/2,  index*World.WIDTH/10,  index*World.HEIGHT/10);
		}else index =0;
		if(gameState == "Game Over") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0,0,  WIDTH*SCALE,  HEIGHT*SCALE);
			g.setFont(new Font("arial", Font.BOLD, 30));
			g.drawString("Rebecca Over", WIDTH*SCALE/3,  HEIGHT*SCALE/2);
			g.setColor(new Color(255,255,255));
			g.setFont(new Font("arial", Font.BOLD, 20));
			if(msgGameOver) {
			g.drawString("Press enter to restart", WIDTH*SCALE/3 - 15,  HEIGHT*SCALE/2 + 30);
		}}else if(gameState == "Menu") {
			menu.rander(g);
			
			//Graphics2D g2 = (Graphics2D) g;
			//double angle = Math.atan2(90+50 - my, 90+50 - mx);
			//g2.rotate(angle, 90+50, 90+50);
			//g.setColor(Color.BLUE);
			//g.fillRect(mx-50,my-50, 100, 100);
		}else if(gameState == "cutscene") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,200/12*indexcut));
			g2.fillRect(0,0, WIDTH*SCALE, HEIGHT*SCALE);
			if(cutframes >= 59){
			for(int i=0;i<rand.nextInt(4);i++) {
			g2.setColor(Color.WHITE);
			g2.fillRect(0,0, WIDTH*SCALE, HEIGHT*SCALE);
			Sounds.hurtSound.play();
				}
			}
			g.setColor(Color.BLACK);
			g.drawRect(player.getX()-Camera.x, player.getY()-Camera.y,16,16);
		}
		//MiniMap
		World.randerMiniMap();
		g.drawImage(minimap,540,0,100,100,null);
		bs.show();
	}
	public void run() {
		
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning = true) {
			long now = System.nanoTime();
			delta += (now - lastTime)/ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				rander();
				frames++;
				delta--;
			} if (System.currentTimeMillis() - timer >= 1000) {System.out.println("FPS: "+frames);
			frames = 0;
			timer += 1000;
			}
			
		} stop();
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			//Andar para direita
			player.right = true;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			//Andar para esquerda
			player.left = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			//Pular
			player.up = true;
			if(gameState == "Menu") {
				menu.up = true;
			}
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			//Agachar
			player.down = true;
			if(gameState == "Menu") {
				menu.down = true;
				//System.out.println("f");
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_I) {
			player.transforme = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_F) {
			player.shoot = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restart = true;
		if(gameState == "Menu") {
			menu.enter = true;
		}
	}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "Menu";
			Menu.pause = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.jump = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_K) {
			if(gameState == "Normal")
			this.saveGame = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_J) {
			if(gameState == "Normal") {
			gameState = "Stop";
			Sounds.stoptime.play();}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			//Andar para direita
			player.right = false;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			//Andar para esquerda
			player.left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			//Pular
			player.up = false;
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			//Agachar
			player.down = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_I) {
			player.transforme = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_F) {
			player.shoot = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_J) {
			gameState = "Normal"
					;}
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		Game.player.mouseShoot = true;
		Game.player.mx = (e.getX()/SCALE)+ Camera.x;
		Game.player.my = (e.getY()/SCALE)+ Camera.y;
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		Game.player.mouseShoot = false;
		//vg
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		this.mx = e.getX();
		this.my = e.getY();
	} 

}


