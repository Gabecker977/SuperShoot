package Graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Main.Game;

public class BossSprites {
	private static BufferedImage boss;
	public static void rander(Graphics g) {
		int x=0;
		int y=0;
		while(x<300) {
			x++;
			y++;
			if(y>=300) {
				x=0;
			y=0;}}
			if(x>=300) {
				y=0;
				x=0;
		}
		boss = Game.spritesheet.getSprint(16*5, 16*5, 32, 32);
		Graphics2D g2 = (Graphics2D)g;
		/*g2.translate(Game.WIDTH*Game.SCALE/4+x, Game.HEIGHT*Game.SCALE/4+y);
		g2.drawImage(boss,0,0, null);
		g2.rotate(45);
		g2.drawImage(boss,0,0, null);
		g2.rotate(45);
		g2.drawImage(boss,0,0, null);
		g2.rotate(45);
		g2.drawImage(boss,0,0, null);
		g2.rotate(45);
		g2.drawImage(boss,0,0, null);*/
		//g2.dispose();
	}
}
