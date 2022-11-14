package World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Main.Game;

public class Tile {
	
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprint(0, 0, 16, 16);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprint(16, 0, 16, 16);
	
	private BufferedImage sprint;
	private int x,y;
	
	public Tile(int x, int y, BufferedImage sprint){
		this.x = x;
		this.y = y;
		this.sprint = sprint;
	}
	public void rander(Graphics g) {
		g.drawImage(sprint, x - Camera.x , y - Camera.y, null);
	}
}
