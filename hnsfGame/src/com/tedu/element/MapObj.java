package com.tedu.element;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class MapObj extends ElementObj {

	private int hp;
	private String mapObjName;//墙的type 使用枚举
	

	public String getMapObjName() {
		return mapObjName;
	}

	public void setMapObjName(String mapObjName) {
		this.mapObjName = mapObjName;
	}

	@Override
	public void showElement(Graphics g) {
		g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), this.getW(), this.getH(), null);
	}

	@Override // 如果可以传入 墙类型，x，y
	public ElementObj createElement(String str) {
//		System.out.println(str);
		String[] arr = str.split(",");
//		先写一个假的
		ImageIcon icon = null;
		switch (arr[0]) { // 设置图片信息 图片还没有加载到内存中
		case "GRASS":
			icon = new ImageIcon("image/wall/grass.png");
			this.mapObjName="GRASS";
			super.name="GRASS";
			break;
		case "BRICK":
			icon = new ImageIcon("image/wall/brick.png");
			this.mapObjName="BRICK";
			super.name="BRICK";
			break;
		case "RIVER":
			icon = new ImageIcon("image/wall/river.png");
			this.mapObjName ="RIVER";
			super.name="RIVER";
			break;
		case "IRON":
			icon = new ImageIcon("image/wall/iron.png");
			this.hp=4;
			this.mapObjName="IRON";
			super.name="IRON";
			break;
		}
		int x = Integer.parseInt(arr[1]);
		int y = Integer.parseInt(arr[2]);
		int w = icon.getIconWidth();
		int h = icon.getIconHeight();

		this.setH(20);
		this.setW(20);
		this.setX(x);
		this.setY(y);
		this.setIcon(icon);
		return this;
	}
	
	@Override //说明 这个设置扣血等的方法需要自己思考重新编写
	public void setLive(boolean live) {
//      被调用一次就减少一次血
		if("IRON".equals(name)) {//铁墙需要四下
			this.hp--;
			if (this.hp>0) {
				return;
			}
		}
		if ("RIVER".equals(name)) {
			return;
			
		}
		super.setLive(live);
	}
	
	@Override
	public Rectangle getRectangle() {
//		int x =this.getX()+1;
//		int y =this.getY()+1;
//		int w =this.getW()-2;
//		int h =this.getH()-2;
//		System.out.println("生成矩形：宽为"+ this.getX() + this.getY() +this.getW() +this.getH());
//		return new Rectangle(x,y,w,h);
		return new Rectangle(this.getX(),this.getY(),this.getW(),this.getH());
		
	}

}
