package com.tedu.game;

import com.tedu.controller.GameListener;
import com.tedu.controller.GameThread;
import com.tedu.show.GameJFrame;
import com.tedu.show.GameMainJPanel;

public class GameStart {

	/**
	 * 程序唯一入口
	 */

	public static void main(String[] args) {

		GameJFrame gj = new GameJFrame();
		/* 实例化面板,注入到jFrame中 */
		GameMainJPanel jp = new GameMainJPanel();
//		实例化监听
		GameListener listener = new GameListener();

		GameThread th = new GameThread();
		// 注入
//		gj.setjPanel(jp);
		gj.getContentPane().add(jp);
		gj.pack();
		gj.setLocationRelativeTo(null);
		gj.setKeyListner(listener);
		gj.setMouseListener(listener); //注入鼠标监听器
		gj.setThead(th);
		gj.setFocusable(true);
		gj.requestFocusInWindow();
		gj.start();
		
		new Thread(jp).start();
	}
}
