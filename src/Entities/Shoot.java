package Entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Main.Game;
import World.Camera;

public class Shoot extends Entity{
	BufferedImage shoot;
	private double dx,dy;
	private int life = 70,curlife = 0;
	private double spd = 4;
	public Shoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}
	public void tick() {
		x+=dx*spd;
		y+=dy*spd;
		curlife++;
		if(curlife == life) {
			Game.shooties.remove(this);}
		return;
		
	}
	public void rander(Graphics g) {
		/*Graphics2D g2 = (Graphics2D) g;
		double angle = Math.atan2(getY()-Camera.y+8- Game.my, getX()-Camera.x+8- Game.mx);
		g2.rotate(angle*-1, getX() - Camera.x +8,getY() - Camera.y + 8);*/
		g.drawImage(Shoot, getX() - Camera.x, getY() - Camera.y, null);
	}
}
