package com.tedu.show;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.tedu.element.ElementObj;
import com.tedu.element.Play;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

/**
 * @说明 游戏的主要面板
 * @author HAHA
 * @功能说明 主要进行元素的显示，同时进行界面的刷新（多线程）
 * 
 * 
 * @题外话 java开发首先思考:做继承和接口实现
 * 
 * @多线程刷新 1.本类实现线程接口 2.本类中定义一个内部类来实现
 */

public class GameMainJPanel extends JPanel implements Runnable {
//      联动管理器

	private ElementManager em;
//	public static int GameX = 815;
//	public static int GameY = 617;
	public static int GameX = 800;
	public static int GameY = 580;//660

	public GameMainJPanel() {
		init();
//		测试代码
	}

	public void init() {
		em = ElementManager.getManager();// 得到元素管理器对象
		this.setPreferredSize(new Dimension(GameX, GameY));
	}

	/**
	 * paint 进行绘画元素 
	 * 固定顺序，先绘画在底层，后绘画的覆盖先画的 不排序的话会出现覆盖问题 
	 * 约定:本方法只执行一次,想实时刷新需要使用多线程
	 */
	@Override // 用于绘画的 Graphics 画笔 专门用于绘画
	public void paint(Graphics g) {
		super.paint(g);
//		map key-value key是无序不可重复的
//		set key-value 和key一样无需不可重复
		Map<GameElement, List<ElementObj>> all = em.getGameElements();
////		GameElement.values();// 隐藏方法 返回值是数组,数组顺序是定义枚举的顺序
		for (GameElement ge : GameElement.values()) {
			List<ElementObj> list = all.get(ge);
			for (int i = 0; i < list.size(); i++) {
				ElementObj obj = list.get(i);// 读取为基类
				obj.showElement(g);// 调用每个类自己的show方法完成自己的显示
			}
		}
		
	}

	@Override
	public void run() {// 接口实现
		while (true) {
			this.repaint();
//			一般情况下,多线程都会使用一个休眠,控制速度

			try {
				Thread.sleep(10);// 休眠10毫秒 1秒刷新100次
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
