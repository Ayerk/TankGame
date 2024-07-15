package com.tedu.element;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class MainBackButton extends ElementObj{
	
	@Override
	public void showElement(Graphics g) {
		// TODO 自动生成的方法存根
		Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int arcWidth = 3; // 圆角的水平直径
        int arcHeight = 3; // 圆角的垂直直径
        g.setColor(new Color(255, 204, 0));
        g2d.fillRoundRect(this.getX(), this.getY(), this.getW(), this.getH(), arcWidth, arcHeight);
        g.setColor(Color.white);
     // 设置文本的颜色
        g2d.setColor(Color.WHITE);

        // 设置字体
        g.setFont(new Font("微软雅黑", Font.BOLD, 17));
        
        // 计算文本的位置，使其居中
        String text = "返回";
        int textWidth = g2d.getFontMetrics().stringWidth(text);
        int textHeight = g2d.getFontMetrics().getHeight();
        int textX = this.getX() + (this.getW() - textWidth) / 2;
        int textY = this.getY() + (this.getH() - textHeight) / 2 + g2d.getFontMetrics().getAscent();

        // 绘制文本
        g2d.drawString(text, textX, textY);
	}
	
	@Override
	public ElementObj createElement(String str) {
		// TODO 自动生成的方法存根
		
		this.setW(100);
		this.setH(30);
		
		this.setX(25);
		this.setY(40);
		
		return this;
	}
	
	public boolean contains(int x,int y) {
		return x>this.getX() && x<this.getX()+this.getW()
				&& y>this.getY() && y<this.getY()+this.getH();
	}
}
