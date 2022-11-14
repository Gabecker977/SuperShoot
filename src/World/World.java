package World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


import Entities.*;
import Graphics.Spritesheet;
import Main.Game;

public class World {
	public static Tile[] tiles;
	public static int WIDTH,HEIGHT;
	public static final int TILE_SIZE = 16;
	public World(String path) {
			try {
				BufferedImage map = ImageIO.read(getClass().getResource(path));
				int[] pixels = new int[map.getWidth()*map.getHeight()];
				WIDTH = map.getWidth();
				HEIGHT = map.getHeight();
				tiles = new Tile[map.getWidth()*map.getHeight()];
				map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels,0, map.getWidth());
				for(int xx = 0; xx < map.getWidth(); xx++) {for(int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx+(yy*map.getWidth())];
					tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16,yy*16, Tile.TILE_FLOOR);
					if(pixelAtual == 0xFF000000) {
						//piso
						tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16,yy*16, Tile.TILE_FLOOR);
						}else if ( pixelAtual == 0xFFFFFFFF) {
							//parede
							tiles[xx + (yy*WIDTH)] = new WallTile(xx*16,yy*16, Tile.TILE_WALL);
						}
						else if(pixelAtual == 0xFF0026FF) {
							//player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
						}
						else if(pixelAtual == 0xFFFF0000) {
							//enemy
							Enemy en = new Enemy(xx*16,yy*16,16,16,Entity.Enemy);
							Game.entities.add(en);
							Game.enemies.add(en);
						}
						else if(pixelAtual == 0xFF00FF21) {
							//Boss
							Boss en = new Boss(xx*16,yy*16,16,16,Entity.Boss);
							Game.entities.add(en);
							Game.bosses.add(en);
						}
						else if(pixelAtual == 0xFFFF6730) {
							//Bow
							Game.entities.add(new Bow(xx*16,yy*16,16,16,Entity.BOW));
						}
						else if(pixelAtual == 0xFF00EBEA) {
							//Staff
							Game.entities.add(new Staff(xx*16,yy*16,16,16,Entity.Staff));
						}
						else if(pixelAtual == 0xFFFFDD21) {
							//arrow
							Arrow ammo = new Arrow(xx*16,yy*16,16,16,Entity.Arrow);
							Game.entities.add(ammo);
							ammo.setMask(8, 8, 8, 8);
						}
						else if(pixelAtual == 0xFFFF00DC) {
							//LifePack
							LifePack pack = new LifePack(xx*16,yy*16,16,16,Entity.LIFE_PACK);
									pack.setMask(8, 8, 8, 8);
							Game.entities.add(pack);
						}
						
					}
				} 
				/*for(int i = 0; i < pixels.length; i++) {
					if(pixels[i] == 0xFFFF0000) {
						//vermelho
					}
				}*/
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

	}
	public static boolean isFree(int xNext, int yNext) {
		int x1 = xNext/TILE_SIZE;
		int y1 = yNext/TILE_SIZE;
		
		int x2 = (xNext + TILE_SIZE-2)/TILE_SIZE;
		int y2 = yNext /TILE_SIZE;
		
		int x3 = xNext/TILE_SIZE;
		int y3 = (yNext+ TILE_SIZE-2)/TILE_SIZE;
		
		int x4 = (xNext + TILE_SIZE-2)/TILE_SIZE;
		int y4 = (yNext + TILE_SIZE-2)/TILE_SIZE;
		
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				 (tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				 (tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				 (tiles[x4 + (y4*World.WIDTH)] instanceof WallTile)); 	
	}
	public static void restart(String level) {
		Game.entities.clear();
		Game.enemies.clear();
		Game.spells.clear();
		Game.shooties.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0,0,16,16,Game.spritesheet.getSprint(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		Game.minimap = new BufferedImage(World.WIDTH,World.HEIGHT,BufferedImage.TYPE_INT_RGB);
		Game.minimapPixels = ((DataBufferInt)Game.minimap.getRaster().getDataBuffer()).getData();
		
		return;
	}
	public void rander(Graphics g) {
		int xstart = Camera.x/16;
		int ystart = Camera.y/16;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.rander(g);
			}
		}
	}
	public static void randerMiniMap() {
		for(int i=0; i<Game.minimapPixels.length;i++) {
			Game.minimapPixels[i] = 0;
		}
		for(int xx=0;xx<WIDTH;xx++) {
			for(int yy=0;yy<HEIGHT;yy++) {
				if(tiles[xx + (yy*WIDTH)] instanceof WallTile) {
					Game.minimapPixels[xx + (yy*WIDTH)] = 0xff0000;
					
				}
			}
		}
		int Playerx = Game.player.getX()/16;
		int Playery = Game.player.getY()/16;
		Game.minimapPixels[Playerx + (Playery*WIDTH)] = 0x0000ff;
	}
}
