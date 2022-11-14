package Entities;


import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

import Main.Game;
import World.Camera;
import World.Node;
import World.Vector2i;

public class Entity {
	public static BufferedImage LIFE_PACK = Game.spritesheet.getSprint(16*6, 0, 16, 16);
	public static BufferedImage Enemy = Game.spritesheet.getSprint(16*7, 16, 16, 16);
	public static BufferedImage EnemyFeed = Game.spritesheet.getSprint(16, 16, 16, 16);
	public static BufferedImage BOW = Game.spritesheet.getSprint(16*7, 0, 16, 16);
	public static BufferedImage Arrow = Game.spritesheet.getSprint(16*6, 16, 16, 16);
	public static BufferedImage Gun_Left = Game.spritesheet.getSprint(16*9, 0, 16, 16);
	public static BufferedImage Gun_Right = Game.spritesheet.getSprint(16*8, 0, 16, 16);
	public static BufferedImage Shoot = Game.spritesheet.getSprint(0, 16*2, 16, 16);
	public static BufferedImage Shoot_Staff = Game.spritesheet.getSprint(0, 16*4, 16, 16);
	public static BufferedImage Staff = Game.spritesheet.getSprint(16, 16*2, 16, 16);
	public static BufferedImage Staff_Right = Game.spritesheet.getSprint(16, 16*2, 16, 16);
	public static BufferedImage Staff_Left = Game.spritesheet.getSprint(16, 16*4, 16, 16);
	public static BufferedImage Boss = Game.spritesheet.getSprint(0, 16*7, 16, 16);
	public static BufferedImage Boss2 = Game.spritesheet.getSprint(16*6, 16*6, 32, 32);
	
	protected double x;
	protected double y;
	protected int z;
	protected int width;
	protected int height;
	private BufferedImage sprite;
	protected List<Node> path;
	private int maskx,masky, maskw, maskh;
	public int depth;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.maskw = width;
		this.maskh = height;
		}
		
		public static Comparator<Entity> nodeSorter = new Comparator<Entity>() {
			@Override
			public int compare(Entity n0, Entity n1) {
				if(n1.depth < n0.depth) 
					return +1;
				if(n1.depth > n0.depth)
					return -1;
				return 0;
			}
		};
	
	public void setMask(int maskx, int masky, int maskw, int maskh) {
		this.maskx = maskx;
		this.masky = masky;
		this.maskw = maskw;
		this.maskh = maskh;
	}
	public void setX(int newX) {
		this.x = newX;
	}
	public void setY(int newY) {
		this.y = newY;
	}
	public int getX() {
		return (int)this.x;
	}
	public int getY() {
		return (int)this.y;
	}
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
	
	public void tick() {}
	public double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1 - x2)*(x1-x2)+(y1-y2)*(y1-y2));
	}
	
	public boolean isColidding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext +masky,maskw,maskh);
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if (e == this) 
				continue;
				Rectangle targetEnemy = new Rectangle(e.getX()+ maskx, e.getY()+masky,maskw,maskh);
			if(enemyCurrent.intersects(targetEnemy)){
				return true;
			}
		}
		for(int i = 0; i < Game.bosses.size(); i++) {
			Boss e = Game.bosses.get(i);
			if (e == this) 
				continue;
			Rectangle targetEnemy = new Rectangle(e.getX()+ maskx, e.getY()+masky,maskw,maskh);
			if(enemyCurrent.intersects(targetEnemy)){
				return true;
				}
			}
		return false;
	}
	
	public void followPath(List<Node> path) {
		if(path != null) {
			if(path.size() > 0) {
				Vector2i target = path.get(path.size()-1).tile;
				
				
				if(x < target.x * 16 && !isColidding(this.getX() + 1,this.getY())) {
					x++;
				}else if(x > target.x * 16 && !isColidding(this.getX() - 1,this.getY())) {
					x--;
				}//else caso eu queria que eles não andem em diagonal
				if(y < target.y * 16 && !isColidding(this.getX(),this.getY()+1)) {
					y++;
				}else if(y>target.y * 16 && !isColidding(this.getX(),this.getY()-1)) {
					y--;
				}
				//chegou ao destino
				if(x == target.x*16 && y == target.y*16) {
					path.remove(path.size()-1);
				}
			}
		}
	}
	
	public static boolean isColidding(Entity e1, Entity e2) {
		Rectangle e1mask = new Rectangle(e1.getX() + e1.maskx,e1.getY()+e1.masky,e1.maskw,e1.maskh);
		Rectangle e2mask = new Rectangle(e2.getX() + e2.maskx,e2.getY()+e2.masky,e2.maskw,e2.maskh);
		if(e1mask.intersects(e2mask) && e1.z == e2.z){return true;
		}
		return false;
	}
	public void rander(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		//g.setColor(Color.red);
		//g.fillRect(this.getX() - Camera.x + maskx, this.getY() - Camera.y + masky, maskw, maskh);
	}
}
