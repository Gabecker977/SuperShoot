package Entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Main.Game;
import Main.Sounds;
import World.Camera;
import World.World;

public class Boss extends Entity{
	//public static String bossState = "Normal";
	private int life = 10;
	private boolean isDameged = false;
	boolean killed=false;
	private int damegeFrames = 10;//, damegeCurret = 0;
	private int speed = 1;
	private static int cont=0;
	private double dx,dy;
	private int maskx, masky,maskw,maskh;
	private int frames = 0, maxFrame = 5, index = 0, maxIdex = 2, dframes=0,dfframes=30,dindex;
	private BufferedImage[] sprites;
	private BufferedImage[] death;
	private BufferedImage boss;
	public Boss(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		sprites = new BufferedImage[3];
		sprites[0] = Game.spritesheet.getSprint(0, 16*6, 16, 16);
		sprites[1] = Game.spritesheet.getSprint(16, 16*6, 16, 16);
		sprites[2] = Game.spritesheet.getSprint(32, 16*6, 16, 16);
		death = new BufferedImage[9];
		for(int x1 = 0; x1 < 9; x1++) {
			death[x1] = Game.spritesheet.getSprint((16*x1), 16*7, 16, 16);
		}
		boss = Game.spritesheet.getSprint(16*5, 16*5, 32, 32);
		/*boss = new BufferedImage[3];
		for(int x1=0;x1<3;x++) {
			boss[x1] = Game.spritesheet.getSprint(16*6, 16*6, 32, 32);
		}*/
	}
	public void tick() {
		depth = 0;
		 maskx = 5;
		 masky = 5;
		 maskw = 10;
		 maskh = 10;
		 
		 if(this.calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY())<160 && killed == false) {
			 if(isColiddingWithPlayer() == false) {
			if(x<Game.player.getX() && World.isFree((int)(x+speed),this.getY()) && 
					!isColidding((int)(x+speed),this.getY())) {
				x+=speed;
			}else if (x>Game.player.getX() && World.isFree((int)(x-speed),this.getY()) && 
					!isColidding((int)(x-speed),this.getY())){
				x-=speed;
			}
			 if(y<Game.player.getY()  && World.isFree(this.getX(),(int)(y+speed))&& 
						!isColidding(this.getX(),(int)(y+speed))) {
				y+=speed;
			}else if (y>Game.player.getY() && World.isFree(this.getX(),(int)(y-speed)) && 
					!isColidding(this.getX(),(int)(y-speed))) {
				y-=speed;
				}
			 }else {
				//dano
				 if(Game.rand.nextInt(100) < 10 && Game.player.isJumping == false) {
					 Game.player.life--;
					 Sounds.hurtSound.play();
					 Game.player.isDameged = true;
					 //System.out.println(Game.player.life);
				 	}
				 }
			 frames++;
				if(frames == maxFrame) {
					frames = 0;
					index++;
					if(index > maxIdex) {index = 0;}
			 
		 }else {dx = Game.rand.nextInt(101);
			 		dy = Game.rand.nextInt(101);
			 		
			 		for(int d = 0; d < 3; d++){
			 			if(dx < 50 && World.isFree((int)(x+speed),this.getY()) && 
							!isColidding((int)(x+speed),this.getY())) {
						x+=speed*0.1;
					}else if (dx >= 50 && World.isFree((int)(x-speed),this.getY()) && 
							!isColidding((int)(x-speed),this.getY())){
						x-=speed*0.1;
					}
					 if(dy < 50 && World.isFree(this.getX(),(int)(y+speed))&& 
								!isColidding(this.getX(),(int)(y+speed))) {
						y+=speed*0.1;
					}else if (dy >= 50 && World.isFree(this.getX(),(int)(y-speed)) && 
							!isColidding(this.getX(),(int)(y-speed))) {
						y-=speed*0.1;}
					 }}
				}
							 //Game.player.life+=100;
						 
						 //bossState = "sleeping";
		 
		 coliddingBullet();
		 coliddingSpell();
	 if(life <= 0) {
		 killed = true;
		 //bossState = "reborn";
		 dframes++;
			if(dframes == dfframes) {
				dframes = 0;
				dindex++;
				if(dindex > 8) {dindex = 0;
				this.kill();}}
		 //Sounds.deathEnemy.play();
		 
		 }
	if(isDameged) {
			damegeFrames++;
			if(damegeFrames == 18) {damegeFrames = 0;
			isDameged = false;
					}
				}
			}
	public void kill() {
		Game.bosses.remove(this);
		Game.entities.remove(this);
		cont++;
		
	}
	
	public void coliddingBullet() {
		for(int i = 0; i < Game.shooties.size(); i++) {
		Entity e = Game.shooties.get(i);
		if (e instanceof Shoot) { 
			if(Entity.isColidding(this, e)){
				isDameged = true;
				//Sounds.hitEnemy.play();
				life-=5;
				Game.shooties.remove(i);
				return;
				}
			}
		}
	}
	public void coliddingSpell() {
		for(int i = 0; i < Game.spells.size(); i++) {
		Entity e = Game.spells.get(i);
		if (e instanceof Magical) { 
			if(Entity.isColidding(this, e)){
				isDameged = true;
				//Sounds.hitEnemy.play();
					life-=10;
					Entities.Magical.life--;
				return;}}}}

public boolean isColiddingWithPlayer(){
	Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() +masky,maskw,maskh);
	Rectangle player = new Rectangle(Game.player.getX(),Game.player.getY(),16,16);
	return enemyCurrent.intersects(player);
}
	

public void rander(Graphics g) {
	
	if(killed) {
		Graphics2D g2 =(Graphics2D)g;
		//g2.translate(Game.player.getX(), Game.player.getY());
		//g2.rotate(cont, dindex, dy);
		g2.drawImage(death[dindex],this.getX() - Camera.x,this.getY() - Camera.y, null);
	}
	else {
	if(!isDameged) {
	g.drawImage(sprites[index],this.getX() - Camera.x,this.getY() - Camera.y, null);
		}
	else {g.drawImage(Entity.EnemyFeed,this.getX() - Camera.x,this.getY() - Camera.y, null);}
	}
	/*else if(bossState == "Reborn") {
		g.drawImage(boss,this.getX() - Camera.x,this.getY() - Camera.y, null);
	}*/
	/*if(Game.gameState == "cutscene") {
		Graphics2D g2 =(Graphics2D)g;
		g2.translate(Game.player.getX(), Game.player.getY());
		g2.rotate(cont, dindex, dy);
		g2.drawImage(death[1], Game.WIDTH*Game.SCALE/2,Game.HEIGHT*Game.SCALE/2, null);
	}*/
	//g.setColor(Color.blue);
	//g.fillRect(this.getX() + maskx- Camera.x, this.getY() + masky - Camera.y,maskw,maskh);
}
}
		
