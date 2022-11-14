package Entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Main.Game;
import World.Camera;

public class Magical extends Entity{
	BufferedImage shoot;
	private double dx,dy;
	public static int life = 3;
	public int curlife = 0;
	private double spd = 1;
	public static boolean loaded = false;
	public Magical(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}
	
	
	public void tick() {
		
		loaded = false;
		x+=dx*spd;
		y+=dy*spd;
		curlife++;
		if(life <= 0||curlife>=200) {
			Game.spells.remove(this);
			life = 3;
			//System.out.println("Piu");
			}
		return;
		
	}
	public void rander(Graphics g) {
		//Graphics2D g2 = (Graphics2D) g;
		//double angle = Math.atan2((this.getY() - Camera.y)-Game.my,((this.getX()- Camera.x) + 8) - Game.mx);
		//g2.rotate(angle, (this.getY()-Camera.y -8)+8,(this.getX()- Camera.x) + 8);*/
		//g2.scale(0.1, 0.1);
		g.drawImage(Shoot_Staff, getX() - Camera.x, getY() - Camera.y - 8, null);
		
	}
	}


