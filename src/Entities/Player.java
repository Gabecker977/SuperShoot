package Entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Graphics.Spritesheet;
import Main.Game;
import Main.Sounds;
import World.Camera;
import World.World;

public class Player extends Entity {
	public boolean right,left,up,down,transforme;
	public int dir_right = 0,dir_left = 1,dir_up = 2, dir_down = 3;
	public int dir = dir_right;
	private double speed = 1.5;
	public double life = 100;
	public static double lifeMax = 100;
	public int mx,my;
	public int ammo = 0;
	
	private int frames = 0, maxFrame = 20, index = 0, maxIdex = 4, tf = 0, tmax = 9;
	private boolean moved = false;
	public boolean jump = false;
	public int z, jumpFrames = 50, jumpCur = 0;
	public boolean isJumping = false;
	public boolean jumpUp = false, jumpDown = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] updownPlayer;
	private BufferedImage[] loadingPlayer;
	private BufferedImage monster;
	private BufferedImage damegedPlayer;
	
	private boolean hasGun = false;
	private boolean hasMagic = false;
	public boolean isDameged = false;
	private int damegedFrames = 0;
	public boolean shoot = false, mouseShoot = false;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		monster = Game.spritesheet.getSprint(112, 16, 16, 16);
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		updownPlayer = new BufferedImage[2];
		loadingPlayer = new BufferedImage[10];
		damegedPlayer = Game.spritesheet.getSprint(0,16,16,16);
		for(int i = 0; i < 4; i++) {
		rightPlayer[i] = Game.spritesheet.getSprint(32 + (16*i), 0, 16, 16);
		}
		for(int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprint(16*5 - (16*i), 16, 16, 16);
			}
		for(int c = 0; c < 2; c++) {
			updownPlayer[c] = Game.spritesheet.getSprint(32 + (16*c), 32, 16, 16);
			}
		for(int x1 = 0; x1 < 10; x1++) {
			loadingPlayer[x1] = Game.spritesheet.getSprint((16*x1), 16*3, 16, 16);
		}
		
	}
	public void tick() {
		depth  =1;
		if(jump) { if(isJumping == false) {
			//System.out.println("Pulando");
			jump = false;
			isJumping = true;
			jumpUp = true;
			}
		}
		if(isJumping) {
			 {
				if(jumpUp)
				jumpCur+=1;
				else if(jumpDown) {
					jumpCur-=1;
				}
				if(jumpCur <= 0) {
					jumpUp = false;
					jumpDown = false;
					isJumping = false;
				}
				z = jumpCur;
				if(jumpCur >= jumpFrames) {
					jumpUp = false;
					jumpDown = true;
					//isJumping = false;
					//System.out.println(z);
				}
			}
		}
		moved = false;
		if(right && World.isFree((int)(x+speed), this.getY())) {

			
			x+=speed;
			dir = dir_right;
			moved = true;
		}
		else if(left && World.isFree((int)(x- speed), this.getY())) {
			x-=speed;
			dir = dir_left;
			moved = true;

		}
		if (up && World.isFree(this.getX(), (int)(y-speed))) {
			y-=speed;
			moved = true;

		dir = dir_up;
		}
		else if(down && World.isFree(this.getX(),(int)(y+speed))) {
			y+=speed;
			dir = dir_down;
			moved = true;}
			
		if (moved) {frames++;
			if(frames == maxFrame) {
				frames = 0;
				index++;
				if(index >= maxIdex) {index = 0;}
			}}
		this.checkCollisionLifePack();
		this.checkCollisionAmmo();
		this.checkCollisionGun();
		this.checkCollisionStaff();
		
		if(isDameged) {
			damegedFrames++;
			if(damegedFrames == 8) {damegedFrames = 0;
			isDameged = false;}
		}
		if(hasMagic && shoot) { 
			 frames++;
			if(frames == maxFrame) {
				frames = 0;
				tf++;
				if(tf >= tmax) {
					tf = 0;
			Magical.loaded = true;
			//System.out.println("Piu");
			shoot = false;
			int dx = 0;
				
			if(dir == dir_right) {
				 dx = 1;
			}else {dx = -1;}
			for(int c=0;c<10;c++) {
			Magical spell = new Magical(this.getX(),this.getY()-2
					,16,16,null,dx,0);
			Game.spells.add(spell);}}
				}
		}
		
		if (shoot && hasGun && ammo > 0) {
			//atirar
			ammo--;
			Sounds.shoot.play();
			shoot = false;
			int dx = 0;
			//System.out.println("piu");
			if(dir == dir_right) {
				 dx = 1;
			}else {dx = -1;
			}
			Shoot bullet = new Shoot(this.getX(),this.getY() - 2,16,16,null,dx,0);
			Game.shooties.add(bullet);
		}
		if (mouseShoot) { if( hasGun && ammo > 0) {
			//atirar
			ammo--;
			Sounds.shoot.play();
			mouseShoot = false;
			double angle = (Math.atan2(my -(this.getY() - Camera.y),mx -(this.getX() - Camera.x)));
			//System.out.println(Math.toDegrees(angle));
			double dx = Math.cos(angle);
			double dy = Math.sin(angle);
			//System.out.println("piu");
			Shoot bullet = new Shoot(this.getX()-5,this.getY()-5 ,16,16,null,dx,dy);
			Game.shooties.add(bullet);}
		}
		if(life<=0) {
			//Game Over
			life = 0;
			Game.gameState = "Game Over";
		}
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2),0,World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2),0,World.HEIGHT*16 - Game.HEIGHT);
	}
	public void checkCollisionGun() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bow) {
				if(Entity.isColidding(this, atual)) {
					hasGun = true;
					//System.out.println("Pegou a arma");
					Game.entities.remove(atual);}
				}
			}
		}
	public void checkCollisionStaff() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Staff) {
				if(Entity.isColidding(this, atual)) {
					hasMagic = true;
					//System.out.println("Pegou a arma");
					Game.entities.remove(atual);}
				}
		}
	}
	public void checkCollisionAmmo() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Arrow) {
				if(Entity.isColidding(this, atual)) {
					ammo+=10;
					Game.entities.remove(atual);}
				}
			}
		}
	
	public void checkCollisionLifePack() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof LifePack) {
				if(Entity.isColidding(this, atual) && life < 100) {
					life+=Game.rand.nextInt((100-50)+50);
					Sounds.Heal.play();
					Game.entities.remove(atual);
					if(life > 100) {life = 100;}
				}
			}
		}
	}
	
	public void rander(Graphics g) {if(!isDameged) {
		if(transforme == true) {g.drawImage(monster, this.getX() - Camera.x, this.getY() - Camera.y - z, null);}
		else if(dir == dir_right) {g.drawImage(rightPlayer[index],this.getX() - Camera.x ,this.getY() - Camera.y - z, null);
				if(hasGun) {//renderizar arma para direita 
					g.drawImage(Gun_Right, this.getX() - Camera.x, this.getY() - Camera.y-z, null);
				}
				if(hasMagic) {//renderizar arma para direita 
					g.drawImage(Staff_Right, this.getX() - Camera.x +10, this.getY() - Camera.y-z, null);
					if(shoot) {g.drawImage(loadingPlayer[tf],this.getX() - Camera.x +18 ,this.getY() - Camera.y -8 -z, null);}
				}
				}	
		else if (dir==dir_left) {g.drawImage(leftPlayer[index],this.getX() - Camera.x ,this.getY() - Camera.y - z, null);
		if(hasGun) {//renderizar arma para esqueda
			g.drawImage(Gun_Left, this.getX() - Camera.x, this.getY() - Camera.y-z, null);
			}
		 if(hasMagic) {
			g.drawImage(Staff_Left, this.getX() - Camera.x -10, this.getY() - Camera.y-z, null);
			if(shoot) {g.drawImage(loadingPlayer[tf],this.getX() - Camera.x -18 ,this.getY() - Camera.y -10-z, null);}
		}
		} 
		if(transforme == true) {g.drawImage(monster, this.getX() - Camera.x, this.getY() - Camera.y-z, null);}
		else if (dir==dir_up) {g.drawImage(updownPlayer[1],this.getX() - Camera.x,this.getY() - Camera.y-z, null);}
		else if (dir==dir_down) {g.drawImage(updownPlayer[0],this.getX() - Camera.x ,this.getY() - Camera.y-z, null);
		}
	}else{ g.drawImage(damegedPlayer, this.getX() - Camera.x, this.getY() - Camera.y - z, null);
	if(hasGun) {if(dir == dir_right)
		{g.drawImage(Gun_Right, this.getX() - Camera.x, this.getY() - Camera.y-z, null);}
	else {g.drawImage(Gun_Left, this.getX() - Camera.x, this.getY() - Camera.y-z, null);}
	}
	 }
		if(isJumping) {g.setColor(Color.BLACK);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y + 8, 16,10);}}
			}
	
		