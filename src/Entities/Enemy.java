package Entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import Main.Game;
import Main.Sounds;
import World.AStar;
import World.Camera;
import World.Vector2i;
import World.World;

public class Enemy extends Entity{
	private double dx,dy;
	//private int c;
	private int life = 10;
	private boolean isDameged = false;
	private int damegeFrames = 10;
	private int speed = 10;
	private int maskx, masky,maskw,maskh;
	private int frames = 0, maxFrame = 5, index = 0, maxIdex = 2;
	private BufferedImage[] sprites;
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[3];
		sprites[0] = Game.spritesheet.getSprint(112, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprint(128, 16, 16, 16);
		sprites[2] = Game.spritesheet.getSprint(144, 16, 16, 16);
		
	}
	
	public void tick() {
		depth = 0;
		 maskx = 5;
		 masky = 5;
		 maskw = 10;
		 maskh = 10;
		 
		 //algoritmo padrão
		 /*if(this.calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY())<160) {
		 if(Game.player.transforme == false) {
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
		 }
		 }else {dx = Game.rand.nextInt(101);
		 		dy = Game.rand.nextInt(101);
		 		
		 		for(int d = 0; d < 3; d++){
		 			if(dx < 50 && World.isFree((int)(x+speed),this.getY()) && 
						!isColidding((int)(x+speed),this.getY())) {
					x+=speed;
				}else if (dx >= 50 && World.isFree((int)(x-speed),this.getY()) && 
						!isColidding((int)(x-speed),this.getY())){
					x-=speed;
				}
				 if(dy < 50 && World.isFree(this.getX(),(int)(y+speed))&& 
							!isColidding(this.getX(),(int)(y+speed))) {
					y+=speed;
				}else if (dy >= 50 && World.isFree(this.getX(),(int)(y-speed)) && 
						!isColidding(this.getX(),(int)(y-speed))) {
					y-=speed;}
				 }}*/
			 
		 //if(Game.player.life == 0) {System.exit(1);}
		 if(!isColiddingWithPlayer()) {
		 if(path == null || path.size() == 0){
			 Vector2i start = new Vector2i((int)(x/16),(int)(y/16));
			 Vector2i end = new Vector2i((int)(Game.player.x/16),(int)(Game.player.y/16));
			 path = AStar.findPath(Game.world, start, end);
		 }
		 }else { //dano
			 if(Game.rand.nextInt(100) < 10 && Game.player.isJumping == false) {
				 Game.player.life--;
				 Sounds.hurtSound.play();
				 Game.player.isDameged = true;
			 	}
		 	}
		 
		 if(new Random().nextInt(100) < speed) {
			 speed++;
			 followPath(path);
			 if(speed >= 90)
				 speed = 50;}
		 if(new Random().nextInt(100) < 5) {
			 Vector2i start = new Vector2i((int)(x/16),(int)(y/16));
			 Vector2i end = new Vector2i((int)(Game.player.x/16),(int)(Game.player.y/16));
			 path = AStar.findPath(Game.world, start, end);
		 }
		 	frames++;
			if(frames == maxFrame) {
				frames = 0;
				index++;
				if(index > maxIdex) {index = 0;}}
	/*else{
			if(x>Game.player.getX() && World.isFree((int)(x+speed),this.getY()) && 
					!isColidding((int)(x+speed),this.getY())) {
				x+=speed;
			}else if (x<Game.player.getX() && World.isFree((int)(x-speed),this.getY()) && 
					!isColidding((int)(x-speed),this.getY())){
				x-=speed;
			}
			 if(y>Game.player.getY()  && World.isFree(this.getX(),(int)(y+speed))&& 
						!isColidding(this.getX(),(int)(y+speed))) {
				y+=speed;
			}else if (y<Game.player.getY() && World.isFree(this.getX(),(int)(y-speed)) && 
					!isColidding(this.getX(),(int)(y-speed))) {
				y-=speed;}}*/
		 
		
				//}
		//int angle = new Random().nextInt((175-30)+30);
		//dx = Math.cos(Math.toRadians(angle));
		//dy = Math.sin(Math.toRadians(angle));
	//x+=dx*speed;
	//y+=dy*speed;
	//if(x+(dx*speed) + width > Game.WIDTH) {
		//dx*=-1;
	//}else if(x+(dx*speed) < 0) {
		//dx*=-1;}}
		 coliddingBullet();
		 coliddingSpell();
		 if(life <= 0) {
			 Sounds.deathEnemy.play();
			 kill();
			 return;}
		if(isDameged) {
				damegeFrames++;
				if(damegeFrames == 18) {damegeFrames = 0;
				isDameged = false;}}
	}
	public void kill() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
		
	}
	public void coliddingBullet() {
		for(int i = 0; i < Game.shooties.size(); i++) {
		Entity e = Game.shooties.get(i);
		if (e instanceof Shoot) { 
			if(Entity.isColidding(this, e)){
				isDameged = true;
				Sounds.hitEnemy.play();
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
				Sounds.hitEnemy.play();
					life-=10;
					Entities.Magical.life--;
				return;}
				/*if(isColiddingWithExplosion()) {
					life-=5;
					System.out.println("b");}*/
			}
		}
	}
	public boolean isColiddingWithPlayer(){
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() +masky,maskw,maskh);
		Rectangle player = new Rectangle(Game.player.getX(),Game.player.getY(),16,16);
		return enemyCurrent.intersects(player);
	}
	/*public boolean isColiddingWithExplosion(){
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() +masky,maskw,maskh);
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if (e == this) 
				continue;
				Rectangle area = new Rectangle(e.getX(), e.getY(),90,90);
			if(enemyCurrent.intersects(area)){
				return true;
			}
		}
		return false;
	}*/
	
	
	public void rander(Graphics g) {
		
		if(!isDameged) {
		g.drawImage(sprites[index],this.getX() - Camera.x,this.getY() - Camera.y, null);}
		else {g.drawImage(Entity.EnemyFeed,this.getX() - Camera.x,this.getY() - Camera.y, null);}
		//g.setColor(Color.blue);
		//g.fillRect(this.getX() + maskx- Camera.x, this.getY() + masky - Camera.y,maskw,maskh);
	}
}
