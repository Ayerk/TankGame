package com.tedu.element;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import com.tedu.manager.GameLoad;

public class Base extends ElementObj {

	private int hp = 4;
	
	@Override
	public void showElement(Graphics g) {
		// TODO 自动生成的方法存根
		g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), this.getW(), this.getH(), null);
	}

	@Override
	public ElementObj createElement(String str) {
		// TODO 自动生成的方法存根
		String[] split = str.split(",");
		this.setX(Integer.parseInt(split[0]));
		this.setY(Integer.parseInt(split[1]));
		ImageIcon icon = GameLoad.imgMap.get("base_survive");
		this.setW(icon.getIconWidth());
		this.setH(icon.getIconHeight());
		this.setIcon(icon);
		return this;
	}
	
	@Override
	public Rectangle getRectangle() {
		// TODO 自动生成的方法存根
		return new Rectangle(this.getX(),this.getY(),this.getW(),this.getH());
	}
	
	@Override
	public void setLive(boolean live) {
		// TODO 自动生成的方法存根
		this.hp--;
		if (this.hp>0) {
			return;
		}
		super.setLive(live);
	}
	
	@Override
	public void die() {
		// TODO 自动生成的方法存根
		this.setIcon(GameLoad.imgMap.get("base_break"));
	}
}