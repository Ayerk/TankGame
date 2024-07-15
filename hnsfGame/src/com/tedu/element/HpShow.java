package com.tedu.element;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.lang.model.element.Element;
import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

public class HpShow extends ElementObj{
	private String type; //表示第几个玩家
	ElementManager em = ElementManager.getManager();
	
	@Override
	public void showElement(Graphics g) {
		// TODO 自动生成的方法存根
		g.setFont(new Font("微软雅黑", Font.BOLD, 17));
		if(this.type.equals("Play")) {
			//玩家1
			g.setColor(new Color(255, 180, 0));
			if(em.getElementsByKey(GameElement.PLAY).size()<1) {
				g.drawString("HP:"+0, getX(), getY());
			}else {
				Play play = (Play)em.getElementsByKey(GameElement.PLAY).get(0);
				g.drawString("HP:"+play.getHp(), getX(), getY());				
			}

		}
		if(this.type.equals("Play2")) {
			//玩家2
			g.setColor(new Color(181, 31, 255));
			if(em.getElementsByKey(GameElement.PLAY2).size()<1) {
				g.drawString("HP:"+0, getX(), getY());
			}else {
				Play play = (Play)em.getElementsByKey(GameElement.PLAY2).get(0);
				g.drawString("HP:"+play.getHp(), getX(), getY());				
			}

		}
	}
	
	
	@Override
	public ElementObj createElement(String string) {
		String[] split = string.split(",");
		for (String str1:split) {
			String[] split2 = str1.split(":");
			switch (split2[0]) {
			case "type": this.type = split2[1];break;
			case "x": this.setX(Integer.parseInt(split2[1]));break;
			case "y": this.setY(Integer.parseInt(split2[1]));break;
			}
		}
		System.out.println("hpShow创建:"+this.type);
		return this;
	}
}
