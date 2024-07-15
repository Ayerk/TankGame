package com.tedu.element;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Instruction extends ElementObj {

	@Override
	public void showElement(Graphics g) {
		g.setColor(new Color(112,221,255));
		Font font = new Font(null, Font.BOLD, 20);
		g.setFont(font);
		g.drawString("操作说明：", 200, 480);
		font = new Font(null, Font.BOLD, 18);
		g.setFont(font);
		g.drawString("玩家1：黄色坦克，键盘方向键移动，回车键射击", 200, 510);
		g.drawString("玩家2：紫色坦克，wasd移动，空格键射击", 200, 540);
		g.drawString("按下“ESC”键可以暂停游戏", 200, 570);

	}

	@Override
	public ElementObj createElement(String str) {

		return this;
	}

}
