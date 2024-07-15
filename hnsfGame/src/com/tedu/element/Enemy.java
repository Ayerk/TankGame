package com.tedu.element;

import java.awt.Graphics;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameMainJPanel;

public class Enemy extends ElementObj {

	
	//@author chenbaolin 大作业可以不用再定义坦克类型，都需要实现上下左右运动，待修改
	// 定义敌方坦克类型，type1只会左右移动，type2能上下左右移动

	//@author chenbaolin 敌人移动不需要键盘监听 方向设置使用fx即可
//	private boolean botleft = false; // 左
//	private boolean botup = false; // 上
//	private boolean botright = false;// 右
//	private boolean botdown = false;// 下

	
	private int moveNum=2;//move一次的偏移量
	
	private int moveTimeGap = 40;//@author chenbaolin 改变运动方向的时间间隔,时间差为+0到+10
	
	private long lastestMoveTime=0L;//@author chenbaolin 用于记录上一次改变运动方向时的时间
	
	private String fx = "botdown";// 上下左右 使用bot+"direction"的格式

	private boolean pkType = false;

	public String getFx() {
		return fx;
	}

	public void setFx(String fx) {
		this.fx = fx;
	}

	@Override
	public void showElement(Graphics g) {
		g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), this.getW(), this.getH(), null);
	}

	@Override
	public ElementObj createElement(String str) {
		String[] split = str.split(",");
		//error
		this.setX(Integer.parseInt(split[0]));
		this.setY(Integer.parseInt(split[1]));
		ImageIcon icon2 = GameLoad.imgMap.get(split[2]);
		this.setW(40);
		this.setH(40);
		this.setIcon(icon2);

		return this;
	}

	@Override
	protected void updateImage(long gametimes) {
//		ImageIcon icon =GameLoad.imgMap.get(fx);
//		System.out.println(icon.getIconHeight());
//		如果高度是小于等于0说明图片路径有问题
		this.setIcon(GameLoad.imgMap.get(fx));
	}


	//只检测敌人和玩家、地形的碰撞
	private boolean checkCollision() {
		ElementManager em = ElementManager.getManager();
//		Map<GameElement, List<ElementObj>> all = em.getGameElements();
		List<ElementObj> plays =em.getElementsByKey(GameElement.PLAY);
		List<ElementObj> maps =em.getElementsByKey(GameElement.MAPS);
		List<ElementObj> play2s = em.getElementsByKey(GameElement.PLAY2);
		for(ElementObj play:plays) {
			if(this.pk(play)) {
				return true;
			}
		}
		for (ElementObj play2 : play2s) {
			if (this.pk(play2)) {
				return true;
			}
		}
		for(ElementObj map:maps) {
			//是地形元素但不是草
			if(!map.name.equals("GRASS") && this.pk(map)){
				return true;
			}
		}
		return false;
	}
	
	//检测敌人和敌人的重叠
	private boolean checkEnemyOverlap() {
		ElementManager em = ElementManager.getManager();
//		Map<GameElement, List<ElementObj>> all = em.getGameElements();
		List<ElementObj> enemys =em.getElementsByKey(GameElement.ENEMY);
		for(ElementObj enemy:enemys) {
			//enemy!=this 排除检测自己与自己重合的情况
			if(enemy!=this  && this.pk(enemy)) {
				return true;
			}
			
		}
		return false;
	}

	@Override
	protected void move(long gameTime) {
		//敌人出生时有重叠情况则暂时不需要进行碰撞检测，防止出生时两个敌人重叠卡死
		boolean needCheckConllision=true;
		//如果用于判定需不需要进行其他物体的碰撞检测
		boolean nextDectect =true;
		
		
		//先尝试改变运动位置再进行判断，判断重叠则取消此次运动
		Random random=new Random();
		//每 40<=Gap<=50 改变一次方向 40-50的概率不均等，因为每次move（）都会改变一次Gap
		int Gap =random.nextInt(11)+moveTimeGap;
		if(gameTime-lastestMoveTime>=Integer.toUnsignedLong(Gap)) {
			int changeNum =random.nextInt(9);
			//上左右各有两个数对应，下有三个数对应，以此实现方向权重分配，让机器人偏下运动
			if (changeNum>=0 && changeNum <=1) {
				setFx("botup");
			}else if (changeNum>=2 && changeNum<=3) {
				setFx("botright");
			}else if (changeNum>=4 && changeNum <=5) {
				setFx("botleft");
			}else {
				// 6，7，8的情况
				setFx("botdown");
			}
			//记录运动方向改变时间，用于下一次比较能否改变方向
			lastestMoveTime=gameTime;
		}
		
		if (getFx().equals("botleft") && this.getX() > 0) {
			//敌人生成时直接进行敌人重叠的判定，如果重叠就不需要进行敌人与敌人之间的碰撞检测
			if(checkEnemyOverlap()) {
				needCheckConllision=false;
			}
			
			this.setX(this.getX() - moveNum);
			//如果一开始敌人没重叠且移动后敌人重叠，则不允许移动
			if(checkEnemyOverlap() && needCheckConllision) {
				this.setX(this.getX() + moveNum);
				//不用继续后面的其他的检测了
				nextDectect=false;
			}
			
			//如果一开始敌人就重叠也要进行敌人和玩家、地形等的判定，重叠则不允许移动
			if(checkCollision() && nextDectect) {
				this.setX(this.getX() + moveNum);
			}
		}
		
		if (getFx().equals("botup") && this.getY() > 0) {
			//敌人生成时直接进行敌人重叠的判定，如果重叠就不需要进行敌人与敌人之间的碰撞检测
			if(checkEnemyOverlap()) {
				needCheckConllision=false;
			}
			
			this.setY(this.getY() - moveNum);
			//如果一开始敌人没重叠且移动后敌人重叠，则不允许移动
			if(checkEnemyOverlap() && needCheckConllision) {
				this.setY(this.getY() + moveNum);
				//不用继续后面的其他的检测了
				nextDectect=false;
			}
			
			//如果一开始敌人就重叠也要进行敌人和玩家、地形等的判定，重叠则不允许移动
			if(checkCollision() && nextDectect) {
				this.setY(this.getY() + moveNum);
			}
		}
		
		if (getFx().equals("botright")  && this.getX() < 800 - this.getW()) {
			//敌人生成时直接进行敌人重叠的判定，如果重叠就不需要进行敌人与敌人之间的碰撞检测
			if(checkEnemyOverlap()) {
				needCheckConllision=false;
			}
			
			this.setX(this.getX() + moveNum);
			//如果一开始敌人没重叠且移动后敌人重叠，则不允许移动
			if(checkEnemyOverlap() && needCheckConllision) {
				this.setX(this.getX() - moveNum);
				//不用继续后面的其他的检测了
				nextDectect=false;
			}
			
			//如果一开始敌人就重叠也要进行敌人和玩家、地形等的判定，重叠则不允许移动
			if(checkCollision() && nextDectect) {
				this.setX(this.getX() - moveNum);
			}
			
		}
		
		if (getFx().equals("botdown") &&this.getY() < 580 - this.getH()) {
			//敌人生成时直接进行敌人重叠的判定，如果重叠就不需要进行敌人与敌人之间的碰撞检测
			if(checkEnemyOverlap()) {
				needCheckConllision=false;
			}
			
			this.setY(this.getY() + moveNum);
			//如果一开始敌人没重叠且移动后敌人重叠，则不允许移动
			if(checkEnemyOverlap() && needCheckConllision) {
				this.setY(this.getY() - moveNum);
				//不用继续后面的其他的检测了
				nextDectect=false;
			}
			
			//如果一开始敌人就重叠也要进行敌人和玩家、地形等的判定，重叠则不允许移动
			if(checkCollision() && nextDectect) {
				this.setY(this.getY() - moveNum);
			}
		}
	}

	private long filetime = 0;
	@Override
	protected void add(long gameTime) {
		filetime=filetime+new Random(this.getX()+this.getY()).nextInt(3);
		//计数器，如果时间到了就发射 随机种子为当前tank坐标和
		if (filetime > 100) {
			this.pkType = true;
			filetime = 0;
		}
		if (!this.pkType) {// 如果不发射,就退出
			return;
		}
		ElementObj obj = GameLoad.getObj("enemyfile");
		ElementObj element = obj.createElement(this.toString());
		ElementManager.getManager().addElement(element, GameElement.ENEMYFILE);
//      如果控制子弹速度等等......还有代码编写

		this.pkType = false;

	}

	@Override
	public String toString() {// 偷懒 建议自己定义一个方法
		// {x:3,y:5,f:up} json格式
		int x = this.getX();
		int y = this.getY();
		switch (this.fx) { // 子弹在发射时候就已经给予了固定的位置。可以加上目标，修改json格式
		case "botup":
			x += 15;
			y -= 5;// 一般不会写数值，一般情况下图片大小就是显示大小；一般情况下可以使用图片大小参与运算
			break;
		case "botright":
			y += 15;
			x += 35;
			break;
		case "botdown":
			x += 15;
			y += 35;
			break;
		case "botleft":
			y += 15;
			x -= 5;
			break;
		}
		return "x:" + x + ",y:" + y + ",f:" + this.fx;
	}
	public int updateToolsX() {
		return this.getX();
	}
	public int updateToolsY() {
		return this.getY();
	}
	public String getBegin() {
		return "x:"+updateToolsX()+",y:"+updateToolsY();
	}
	@Override
	public void die() {
		Goals.setGoal(Goals.getGoal() + 100);//敌人死亡后获得100分 
		
		if(this.isLive() == false ) {
			System.out.println("敌人已被击杀");
			Random random = new Random();
			if(random.nextInt(3)<1) {
				ElementObj element= new Tools().createElement(this.getBegin()); 
				ElementManager.getManager().addElement(element, GameElement.TOOLS);				
			}
		}
		
		ElementObj obj = GameLoad.getObj("deathAnimation");
		ElementObj deathAnimation = obj.createElement(this.deathLocation());
		ElementManager.getManager().addElement(deathAnimation, GameElement.DEATHANIMATION);
	}
	public String deathLocation() {// 记录敌人死亡坐标
		// {x:3,y:5} json格式
		int x = this.getX();
		int y = this.getY();
		return "x:" + x + ",y:" + y ;
	}
}
