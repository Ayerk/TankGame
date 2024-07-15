package com.tedu.element;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Goals extends ElementObj {
	private static int goal = 0;
	
	public static int getGoal() {
		return goal;
	}

	public static void setGoal(int goal) {
		Goals.goal = goal;
	}

	@Override
	public void showElement(Graphics g) {
		g.setColor(Color.red);
		Font goalfont = new Font(null, Font.BOLD, 20);
		g.setFont(goalfont);
		g.drawString("得分：" , 600, 570);
		g.setColor(Color.blue);
		goalfont = new Font(null, Font.BOLD, 18);
		g.drawString(Integer.toString(goal), 660, 570);
	}

	@Override
	public ElementObj createElement(String str) {

		return this;
	}
}
