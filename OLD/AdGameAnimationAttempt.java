/*
package com.amaz1n.adheroes2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class AdGame extends ApplicationAdapter {
	SpriteBatch batch;

	//1024x800

	Sprite spr1;
	Sprite spr2;
	player p1;
	player p2;
	Texture map;

	float INCG,MAXG;

	int LEFT,FORWARD,RIGHT;
	int IDLE,SHOOT,MELEE,WALK,JUMP;

	@Override
	public void create () {
		INCG = 0.25f;MAXG = -6.25f;//increment 0.25 is 1/25th of maximum 6.25

		LEFT = 0;FORWARD = 1;RIGHT = 2;
		IDLE = 0;SHOOT = 1;MELEE = 2;WALK = 0;JUMP = 1;
		batch = new SpriteBatch();
		Texture[][] slimeFrames = charFrames("slime1");
		Animation[][] slimeMoves = charMoves("slime1");
		p1 = new player("Slime",200,4,4,slimeFrames,slimeMoves);//create player 1
		spr1 = new Sprite(p1.getCurrentFrame());
		spr1.setPosition(p1.getX(),p1.getY());
		spr1.setScale(0.75f);//reduce size

		Texture[][] binFrames = charFrames("bin1");
		Animation[][] binMoves = charMoves("bin1");
		p2 = new player("Recycle Bin",696,8,8,binFrames,binMoves);//equal distance
		spr2 = new Sprite(p2.getCurrentFrame());
		spr2.setPosition(p2.getX(),p2.getY());
		spr2.setScale(0.75f);

		map = new Texture("map_slimeFields.png");
	}

	public Texture[][] charFrames(String s){//load frames for each character
		Texture l0 = new Texture(s+"_IL.png");
		//Texture l1;
		//Texture l2;
		Texture[] lFrames = new Texture[]{l0};
		Texture f0 = new Texture(s+"_IF.png");////Only one frame
		Texture[] fFrames = new Texture[]{f0};///////////////front-facing
		Texture r0 = new Texture(s+"_IR.png");
		//Texture r1;
		//Texture r2;
		Texture[] rFrames = new Texture[]{r0};

		return new Texture[][]{lFrames,fFrames,rFrames};//return 2D array
	}
	public Animation[][] charMoves(String s){
		Texture l1 = new Texture(s+"_WL_1.png");
		Texture l2 = new Texture(s+"_WL_2.png");
		Texture l3 = new Texture(s+"_WL_3.png");
		Texture l4 = new Texture(s+"_WL_4.png");
		Texture l5 = new Texture(s+"_WL_5.png");
		Texture l6 = new Texture(s+"_WL_6.png");
		Texture[] lwTextures = new Texture[]{l1,l2,l3,l4,l5,l6};
		TextureRegion[] lwFrames = new TextureRegion[6];
		for(int i=0;i<6;i++){
			TextureRegion tR = new TextureRegion(lwTextures[i]);
			lwFrames[i]=tR;
		}
		Animation<TextureRegion> lWalk = new Animation<>(1,lwFrames);//make animation
		//Texture[] ljFrames = new Texture[]{};
		Animation[] lMoves = new Animation[]{lWalk};
		Texture r1 = new Texture(s+"_WR_1.png");
		Texture r2 = new Texture(s+"_WR_2.png");
		Texture r3 = new Texture(s+"_WR_3.png");
		Texture r4 = new Texture(s+"_WR_4.png");
		Texture r5 = new Texture(s+"_WR_5.png");
		Texture r6 = new Texture(s+"_WR_6.png");
		Texture[] rwTextures = new Texture[]{r1,r2,r3,r4,r5,r6};
		TextureRegion[] rwFrames = new TextureRegion[6];
		for(int i=0;i<6;i++){
			TextureRegion tR = new TextureRegion(rwTextures[i]);
			rwFrames[i]=tR;
		}
		Animation<TextureRegion> rWalk = new Animation<>(1,rwFrames);
		Animation[] rMoves = new Animation[]{rWalk};
		return new Animation[][]{lMoves,rMoves};
	}

	public void moveLimits(player p){//falling, borders, ground, etc.
		//Vertical
		if(p.getY()<=100){//on or below ground
			p.setY(100);//confirm ground level ************add static variable for ground levels
			p.setFalling(false);//set falling to false
		}
		else{//not on the ground
			p.setFalling(true);//falling is true
		}

		if(!p.getFalling()){//if falling is false
			p.setVY(0);//stop velocity
		}
		else{//if falling is true
			if(p.getVY()>=MAXG){//travelling up or not at full gravity
				p.changeVY(-INCG);//subtract an increment
			}
			else{//reached or surpassed max gravity
				p.setVY(MAXG);//confirm max gravity
			}
		}
		//Horizontal
		if(p.getX()<=-25){//to account for differing texture borders,
			p.setX(-25);//some sprites may clip into the walls
		}
		if(p.getX()>=920){//1024-0.75(128)-8
			p.setX(920);
		}
		p.changeX(p.getVX());//move player
		p.changeY(p.getVY());
	}

	public void slimebounce(player p){////////////////////////////////////TEMPORARY TEST METHOD/////Success
		if(p.getY()<=100){
			p.setVY(10);
			p.changeX(p.getVX());
			p.changeY(p.getVY());
		}
	}

	public void binwalk(player p){///////////////////////////////////////TEMPORARY TEST METHOD/////Failure
		if(p.getY()<=100){
			p.setVX(-2);
			Animation a = p.getAnimation(WALK);
			a.setPlayMode(Animation.PlayMode.LOOP);
			//************************************************USE SPRITEBATCH (check bounce.java)
			p.changeX(p.getVX());
			p.changeY(p.getVY());
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(map,0,0);

		spr1.setTexture(p1.getCurrentFrame());
		spr1.draw(batch);
		moveLimits(p1);
		slimebounce(p1);/////////////////////////TEMPORARY
		spr1.setPosition(p1.getX(),p1.getY());

		spr2.setTexture(p2.getCurrentFrame());
		spr2.draw(batch);
		moveLimits(p2);
		binwalk(p2);/////////////////////////////TEMPORARY
		spr2.setPosition(p2.getX(),p2.getY());

		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}


*/
/*
slime:         FR 4, DMG 4
recycle bin:   FR 8, DMG 8
fast food man: FR12, DMG12
witch cat:     FR16, DMG16
dumbbell rack: FR20, DMG20
robot:         FR24, DMG24
*//*



*/
/*Old python code for gravity
if pr[VY]<=0:#going up
		pr[GODOWN]=False
		elif pr[VY]>=0:#going down
		pr[GODOWN]=True
		pr[Y]+=pr[VY]
		if pr[Y]>=520:#ground level
		pr[Y]=520#stay
		pr[VY]=0#no y velocity
		pr[GODOWN]=True#going down
		pr[DOUBLE]=True#double jump available
		pr[VY]+=0.35#velocity in y
*//*


class player{
	private String name;
	private int x,y;//position for hit box
	private float vx,vy;//velocity
	private int hp,dmg,fireRate;
	private Texture[][] frames;
	private Animation<Texture>[][] anims;
	private int currentDirect,currentFrame;
	private boolean falling,dblJump;

	public player(String n,int startX,int damage,int rate,Texture[][] f,Animation<Texture>[][] a){
		name = n;
		x = startX; y = 800;//starting positions will be in the air to account for differing ground levels
		vx = 0; vy = 0;//initial velocity is 0
		hp = 150;
		dmg = damage;
		fireRate = rate;
		frames = f;
		anims = a;
		currentDirect = 1;//forward
		currentFrame = 0;//idle
		falling = true;
		dblJump = true;
	}

	public String getName(){return name;}
	public int getX(){return x;}
	public int getY(){return y;}
	public float getVX(){return vx;}
	public float getVY(){return vy;}
	public int getHP(){return hp;}
	public int getDMG(){return dmg;}
	public int getRate(){return fireRate;}
	//public boolean getStun(){return stun;}
	public Texture getCurrentFrame(){return frames[currentDirect][currentFrame];}
	public Animation getAnimation(int i){
		return anims[currentDirect][i];
	}
	public boolean getFalling(){return falling;}
	public boolean get2Jump(){return dblJump;}

	public void changeVY(float f){vy+=f;}//vx doesn't need this (only for gravity)
	public void changeX(float f){x+=f;}
	public void changeY(float f){y+=f;}

	public void setX(int i){x=i;}
	public void setY(int i){y=i;}
	public void setVX(float f){vx=f;}
	public void setVY(float f){vy=f;}
	public void setHP(int i){hp=i;}//DMG, fire rate don't change
	//***melee stun will be put off until core elements established
	//public void setStun(){stun = !stun;}//toggles false to true and vice versa
	public void setCurrentFrame(int d,int f){
		currentDirect = d;
		currentFrame = f;
	}
	public void setFalling(boolean b){falling=b;}
	public void set2Jump(boolean b){dblJump=b;}
}
*/
