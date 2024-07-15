package com.tedu.element;

import java.awt.Graphics;
import java.util.Random;

import javax.swing.ImageIcon;

public class Tools extends ElementObj{
	private String type = "";
	private Random random = new Random();
	
	public String getType() {
		return type;
	}
	private String[] allType = {"Invalid","SpeedUp","MoreHp"};
	public Tools() {}
	@Override
	public void showElement(Graphics g) {
		g.drawImage(this.getIcon().getImage(), 
				this.getX(), 
				this.getY(), 
				this.getW()-10, 
				this.getH()-10, null);
	}
	@Override
	public ElementObj createElement(String string) {
		String[] split = string.split(",");
		for (String str1:split) {
			String[] split2 = str1.split(":");
			switch (split2[0]) {
			case "x": this.setX(Integer.parseInt(split2[1]));break;
			case "y": this.setY(Integer.parseInt(split2[1]));break;
			}
		}
		this.setW(50);
		this.setH(50);
		
		int index= random.nextInt(3)+1;
		this.type = allType[index-1];
		
		this.setIcon(new ImageIcon("image/tool/0"+index+".png"));
		System.out.println("已生成道具"+allType[index-1]);
		return this;
	}

}
