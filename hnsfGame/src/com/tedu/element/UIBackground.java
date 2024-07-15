package com.tedu.element;

import java.awt.Graphics;

import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;
import com.tedu.show.GameMainJPanel;

public class UIBackground extends ElementObj{

	@Override
	public void showElement(Graphics g) {
		// TODO 自动生成的方法存根
		g.drawImage(this.getIcon().getImage(), 0, 0, this.getW(), this.getH(), null);
	}
	
	@Override
	public ElementObj createElement(String str) {
		// TODO 自动生成的方法存根
		this.setIcon(GameLoad.imgMap.get("UIbackground"));
		this.setW(GameMainJPanel.GameX);
		this.setH(GameMainJPanel.GameY);
		System.out.println(this.getW()+" "+this.getH());
		return this;
	}
}
