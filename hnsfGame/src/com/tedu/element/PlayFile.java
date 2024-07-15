package com.tedu.element;

import java.awt.Color;
import java.awt.Graphics;


/**
 * @说明: 玩家子弹类,本类的实体对象是由玩家对象调用和创建
 * 
 * @子类的开发步骤:
 * 1.继承于元素基类 重写show方法
 * 2.按照需求选择性重写其他方法 例如:move
 * 3.思考并定义子类特有的属性
 */

public class PlayFile extends ElementObj {
	private int attack;// 攻击力
	private int moveNum = 3;// 移动速度值
	private String fx;
//  扩展子弹类型 :可以拓展出多种子弹 (玩家需要有子弹类型)

	public PlayFile() {// 一个空的默认的构造方法
	} 

	// 对创建这个对象的过程进行封装,外界只需要传输必要的约定参数,返回值就是对象实体
	@Override // {x:3,y:5,f:up}
	public ElementObj createElement(String str) {// 自定义字符串的封包
		String[] split = str.split(",");
		for (String str1 : split) {
			String[] split2 = str1.split(":");// 0下标是x,y,f 1下标是值
			switch (split2[0]) {
			case "x":
				this.setX(Integer.parseInt(split2[1]));
				break;
			case "y":
				this.setY(Integer.parseInt(split2[1]));
				break;
			case "f":
				this.fx = split2[1];
				break;
			}
		}
		this.setW(10);
		this.setH(10);
		return this;
	}

	@Override
	public void showElement(Graphics g) {
		g.setColor(Color.red);// new Color(255,255,255.)
		g.fillOval(this.getX(), this.getY(), this.getW(), this.getH());

	}

	@Override
	protected void move(long gameTime) {
		if (this.getX() < 0 || this.getX() > 800 || this.getY() < 0 || this.getY() > 620) {
			this.setLive(false);
			return;
		}

		switch (this.fx) {
		case "left":
			this.setX(this.getX() - this.moveNum);
			break;
		case "up":
			this.setY(this.getY() - this.moveNum);
			break;
		case "right":
			this.setX(this.getX() + this.moveNum);
			break;
		case "down":
			this.setY(this.getY() + this.moveNum);
			break;
		}

	}

	/**
	 * 对于子弹来说：1.出边界 2.碰撞 3.玩家放保险
	 * 处理方式是，当达到死亡的条件时，只进行修改死亡状态的操作。
	 * 
	 */
//	@Override
//	public void die() {
//		
//	}

//	/* 子弹变装 */
//	private long time = 0;
//
//	protected void updateImage(long gameTime) {
////		System.out.println("子弹变化1");
//		if (gameTime - time > 20) {
//			time=gameTime;//为下次变装做准备
//			this.setW(this.getW());
//			this.setH(this.getH());
////			System.out.println("子弹变化2");
//		}
//	}
	
	
}
