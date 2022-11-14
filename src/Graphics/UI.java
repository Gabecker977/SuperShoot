package Graphics;

import java.awt.Color;
import java.awt.Graphics;

import Entities.Player;
import Main.Game;
import World.Camera;

public class UI {
	//private boolean lastEnemy = false;
	//private int time = 0;
	public void rander(Graphics g){
		g.setColor(Color.red);
		g.fillRect(Game.player.getX() - Camera.x,Game.player.getY() - Camera.y - 4-Game.player.z,20,3);
		g.setColor(Color.green);
		g.fillRect(Game.player.getX() - Camera.x,Game.player.getY() - Camera.y - 4 - Game.player.z,(int)((Game.player.life/Player.lifeMax)*20),3);
		g.setColor(Color.red);
		g.fillRect(8,8,50,10);
		g.setColor(Color.green);
		g.fillRect(8,8,(int)((Game.player.life/Player.lifeMax)*50),10);
		g.setColor(Color.white);
		g.drawString((int)(Game.player.life)+"/"+(int)(Player.lifeMax), 10, 17);
		
		/*if(Game.enemies.size() <= 10){ lastEnemy = true;
		if(lastEnemy) {
			time++;
			g.setColor(Color.yellow);
			g.drawString(Game.enemies.size()+" inimigos restantes", Game.WIDTH/2 - 15, Game.HEIGHT/2);
			if(time >= 10) {lastEnemy = false;
			g.dispose();
			}
				}*/
		}
}
