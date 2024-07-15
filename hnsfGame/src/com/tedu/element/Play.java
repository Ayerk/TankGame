package com.tedu.element;

import java.awt.Graphics;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

public class Play extends ElementObj {

	/**
	 * @移动属性：
	 * 
	 * @1.单属性 配合方向枚举类型；一次只能移动一个方向
	 * @2.双属性 上下和左右 配合boolean值使用：例如true代表上，false为下 
	 * 需要另外一个变量来确定是否按下方向键
	 *        约定：0代表不动，1代表向上，2代表下
	 * @3.4属性 上下左右都可以，boolean值配合使用true代表移动，false不移动 
	 * 同时按上和下：后按会重置先按的
	 * @说明：以上3种方式是代码编写方式和判定方式不一样
	 * @说明：游戏种有非常多的判定，灵活使用判定属性，很多状态值也使用判定属性 多状态 
	 * @可以使用map<泛型，boolean>；set<判定对象> 判定对象有时间
	 * @问题
	 * @1.图片要读取到内存中：加载器 临时处理方式：手动编写存储到内存中
	 * @2.什么时候修改图片：图片是父类中的属性存储
	 * @3.图片应该使用什么集合存储
	 */

	private boolean left = false; // 左
	private boolean up = false; // 上
	private boolean right = false;// 右
	private boolean down = false;// 下
	private long ToolsEndTime =0;
	private int hp = 2;
	protected boolean isSpeedUp = false;
	protected boolean isMoreBullet = false;
	protected boolean isInvalid = false;
	protected boolean isMoreFx = false;
	
	
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public boolean isSpeedUp() {
		return isSpeedUp;
	}
	public void setSpeedUp(boolean isSpeedUp) {
		this.isSpeedUp = isSpeedUp;
	}
	public boolean isMoreBullet() {
		return isMoreBullet;
	}
	public void setMoreBullet(boolean isMoreBullet) {
		this.isMoreBullet = isMoreBullet;
	}
	public boolean isInvalid() {
		return isInvalid;
	}
	public void setInvalid(boolean isInvalid) {
		this.isInvalid = isInvalid;
	}
	public boolean isMoreFx() {
		return isMoreFx;
	}
	public void setMoreFx(boolean isMoreFx) {
		this.isMoreFx = isMoreFx;
	}


//	变量专门记录主角当前的方向，默认为是up
	private String fx = "up";
	
	
//  主角的移动速度
	private int movespeed =2;

//	发射状态
	private boolean pkType = false; // true 攻击状态 ;false 停止

	public Play() {

	}

	public Play(int x, int y, int w, int h, ImageIcon icon) {
		super(x, y, w, h, icon);
	}

	// 题外话 ：过时的方法能用吗？可以用，也能够用，jdk底层使用
	@Override
	public ElementObj createElement(String str) {
		String[] split = str.split(",");
		this.setX(Integer.parseInt(split[0]));
		this.setY(Integer.parseInt(split[1]));
		ImageIcon icon2 = GameLoad.imgMap.get(split[2]);
//		this.setW(icon2.getIconWidth());
//		this.setH(icon2.getIconHeight());
		this.setH(40);
		this.setW(40);
		this.setIcon(icon2);
		return this;
	}

	/*
	 * 对象自己的事情自己做
	 */
	@Override
	public void showElement(Graphics g) {
		g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), this.getW(), this.getH(), null);
	}

	/**
	 * 重写方法: 重写的要求：方法名称和参数类型序列必须和父类的方法一样
	 * 
	 * @说明 监听需要改变状态值
	 */
	public void keyClick(boolean bl, int key) {
		if (bl) {// 按下
			switch (key) {
			case 37:// 左
//			case 65:// 左 a
				this.up = false;
				this.down = false;
				this.right = false;
				this.left = true;
				this.fx = "left";
				break;
			case 38:// 上
//			case 87:// 上 w
				this.right = false;
				this.down = false;
				this.left = false;
				this.up = true;
				this.fx = "up";
				break;
			case 39:// 右
//			case 68:// 右 d
				this.down = false;
				this.up = false;
				this.left = false;
				this.right = true;
				this.fx = "right";
				break;
			case 40:// 下
//			case 83:// 下 s
				this.right = false;
				this.left = false;
				this.up = false;
				this.down = true;
				this.fx = "down";
				break;
//			case 32://空格 开启攻击状态
			case 10://回车
				this.pkType = true;
				break; 
			}
		} else {
			switch (key) {
			case 37:// 左
//			case 65:// 左 a
				this.left = false;
				break;
			case 38:// 上
//			case 87:// 上 w
				this.up = false;
				break;
			case 39:// 右
//			case 68:// 右 d
				this.right = false;
				break;
			case 40:// 下
//			case 83:// 下 s
				this.down = false;
				break;
//			case 32:
			case 10:
				this.pkType = false;// 关闭攻击状态
				break;
			}
		}
	}

	@Override
	public void move(long gameTime) {
		// 道具加速
		if(isSpeedUp) movespeed = 3;
		else movespeed = 2;
		
		if (this.left && this.getX() > 0) {
			this.setX(this.getX() - this.movespeed);
			if (checkCollision()) {
                this.setX(this.getX() + this.movespeed);
            }
		}
		if (this.up && this.getY() > 0) {
			this.setY(this.getY() - this.movespeed);
			if (checkCollision()) {
                this.setY(this.getY() + this.movespeed);
            }
		}
		if (this.right && this.getX() < 800 - this.getW()) {// 系统边界
			this.setX(this.getX() + this.movespeed);
			if (checkCollision()) {
                this.setX(this.getX() - this.movespeed);
            }
		}
		if (this.down && this.getY() < 580 - this.getH()) {
			this.setY(this.getY() + this.movespeed);
			if (checkCollision()) {
                this.setY(this.getY() - this.movespeed);
            }
		}
	}

	@Override
	protected void updateImage(long gametimes) {
//		ImageIcon icon =GameLoad.imgMap.get(fx);
//		System.out.println(icon.getIconHeight());
//		如果高度是小于等于0说明图片路径有问题
		this.setIcon(GameLoad.imgMap.get(fx));
	}
	public long getToolsEndTime() {
		return ToolsEndTime;
	}
	public void setToolsEndTime(long toolsEndTime) {
		ToolsEndTime = toolsEndTime;
	}

	/**
	 * @问题 1.重写方法的访问修饰符能否修改
	 *      2.add方法是否可以自动抛出异常
	 * @重写规则:
	 * 1.重写方法的方法的名称和返回值 必须和父类的一样
	 * 2.重写方法的传入参数类型序列和父类一样
	 * 3.重写方法访问修饰符 只能比父类更宽泛
	 *   比方说:父类的方法是受保护的,但是现在需要再非子类调用
	 *   可以直接子类继承,重写并super.父类方法,public方法
	 *   帮助父类公开方法
	 * 4.抛出的异常不能比父类更宽泛
	 * 子弹的添加需要发射者的坐标位置和发射者的方向,如果可以变换子弹,还要获取发射者的状态
	 */
	private long filetime = 0;
//最后实现	
// filetime 和传入的时间gameTime 进行比较，赋值等操作运算，控制子弹间隔

	@Override // 添加子弹
	public void add(long gameTime) {
		System.out.println(gameTime);

		if (!this.pkType) {// 如果不发射,就退出
//			System.out.println("不发射");
			return;
		}

///////////////////////////////////////两种发射模式		
		
//隔一段时间才能进行发射子弹，最后阶段取消注释即可
		if(gameTime-filetime<30) {
			return;
		}
		filetime = gameTime;
		
//		this.pkType = false;// 按一次发射一颗子弹
		
///////////////////////////////////////
		
//		new PlayFile();// 使用小工厂
//		将构造对象的多个步骤进行封装成一个方法,返回值直接是这个对象
//		传递一个固定格式 {x:3,y:5,f:up} json格式
		ElementObj obj = GameLoad.getObj("file");
		ElementObj file = obj.createElement(this.toString());
//		ElementObj element = new PlayFile().createElement(this.toString());
//		装入到集合中
		ElementManager.getManager().addElement(file, GameElement.PLAYFILE);
//      如果控制子弹速度等等......还有代码编写

	}

	@Override
	public String toString() {// 偷懒 建议自己定义一个方法
		// {x:3,y:5,f:up} json格式
		int x = this.getX();
		int y = this.getY();
		switch (this.fx) { // 子弹在发射时候就已经给予了固定的位置。可以加上目标，修改json格式
		case "up":
			x += 15;
			y -= 5;// 一般不会写数值，一般情况下图片大小就是显示大小；一般情况下可以使用图片大小参与运算
			break;
		case "right":
			y += 15;
			x += 35;
			break;
		case "down":
			x += 15;
			y += 35;
			break;
		case "left":
			y += 15;
			x -= 5;
			break;
		}
		return "x:" + x + ",y:" + y + ",f:" + this.fx;
	}
	
	
	public String deathLocation() {// 记录玩家死亡坐标
		// {x:3,y:5} json格式
		int x = this.getX();
		int y = this.getY();
		return "x:" + x + ",y:" + y ;
	}
	
	private boolean checkCollision() {
		ElementManager em = ElementManager.getManager();
		Map<GameElement, List<ElementObj>> all = em.getGameElements();
		List<ElementObj> enemys =em.getElementsByKey(GameElement.ENEMY);
		List<ElementObj> plays =em.getElementsByKey(GameElement.PLAY);
		List<ElementObj> maps =em.getElementsByKey(GameElement.MAPS);
		List<ElementObj> play2s =em.getElementsByKey(GameElement.PLAY2);

		for(ElementObj enemy:enemys) {
			if(this.pk(enemy)) {
				return true;
			}
			
		}
		for(ElementObj play:plays) {
			//play!=this 排除检测自己与自己重合的情况
			if(play!=this && this.pk(play)) {
				return true;
			}
		}
		for(ElementObj play2:play2s) {
			if(this.pk(play2)) {
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
	
	@Override
	public void die() {//玩家死亡时生成一个死亡动画
//		this.setIcon(GameLoad.imgMap.get("boom"));
		ElementObj obj = GameLoad.getObj("deathAnimation");
		ElementObj deathAnimation = obj.createElement(this.deathLocation());
		ElementManager.getManager().addElement(deathAnimation, GameElement.DEATHANIMATION);
//		System.out.println("玩家死亡");
	}
	
	public boolean isCrash(ElementObj obj) {
		return this.getRectangle().intersects(obj.getRectangle());
	}
	@Override
	public void setLive(boolean stats) {
		
		if(stats == false) {
			if(isInvalid) return; //无敌状态下不进行伤害判断
			if(this.hp <= 1) {
//				this.live = false;
				super.setLive(false);
			}else {
				this.hp--;
			}
//			System.out.println("玩家收到攻击,目前hp为"+this.hp+"，live状态为"+this.live);
			System.out.println("玩家收到攻击,目前hp为"+this.hp+"，live状态为"+this.isLive());
		}else {
//			this.live = true;
			super.setLive(true);
		}
	}
}



//try {
//Class<?> forName = Class.forName("com.tedu......");
//ElementObj element = forName.getDeclaredConstructor().newInstance().createElement("");
//} catch (InstantiationException e) {
//// TODO Auto-generated catch block
//e.printStackTrace();
//} catch (IllegalAccessException e) {
//// TODO Auto-generated catch block
//e.printStackTrace();
//} catch (IllegalArgumentException e) {
//// TODO Auto-generated catch block
//e.printStackTrace();
//} catch (InvocationTargetException e) {
//// TODO Auto-generated catch block
//e.printStackTrace();
//} catch (NoSuchMethodException e) {
//// TODO Auto-generated catch block
//e.printStackTrace();
//} catch (SecurityException e) {
//// TODO Auto-generated catch block
//e.printStackTrace();
//}
//以后的框架中会碰到
//会帮助你返回对象的实体,并初始化数据
