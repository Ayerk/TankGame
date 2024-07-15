package com.tedu.element;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import com.tedu.show.GameJFrame;
import com.tedu.show.GameMainJPanel;

public class LevelOver  extends ElementObj{
	
	
	
	@Override
	public void showElement(Graphics g) {
		// TODO 自动生成的方法存根
		Graphics2D g2d = (Graphics2D) g;
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	    // 设置半透明的黑色背景
	    Color transparentBlack = new Color(0, 0, 0, 128); // 128是透明度，范围是0-255
	    g2d.setColor(transparentBlack);
	    Rectangle2D backgroundRect = new Rectangle2D.Double(0, 0, GameMainJPanel.GameX, GameMainJPanel.GameY);
	    g2d.fill(backgroundRect);

	    // 设置文本的颜色和字体
	    g2d.setColor(Color.WHITE);
	    g2d.setFont(new Font("微软雅黑", Font.BOLD, 24));

	    // 计算文本的位置，使其居中显示
	    String text = "成功通关";
	    FontMetrics fm = g2d.getFontMetrics();
	    Rectangle2D textBounds = fm.getStringBounds(text, g2d);
	    int textX = (getW() - (int) textBounds.getWidth()) / 2;
	    int textY = (getH() - (int) textBounds.getHeight()) / 2 + fm.getAscent();
	    
		// 绘制文本
	    g2d.drawString(text, textX, textY);
	}
	
	@Override
	public ElementObj createElement(String str) {
		// TODO 自动生成的方法存根
		this.setH(GameMainJPanel.GameY);
		this.setW(GameMainJPanel.GameX);
		this.setX(0);
		this.setY(0);
		
		return this;
	}
}
