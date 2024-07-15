package com.tedu.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tedu.element.BackButton;
import com.tedu.element.BeginButton;
import com.tedu.element.ElementObj;
import com.tedu.element.Goals;
import com.tedu.element.LevelButton;
import com.tedu.element.MainBackButton;
import com.tedu.element.MultiPlayerButton;
import com.tedu.element.NextButton;
import com.tedu.element.RestartButton;
import com.tedu.element.ResumeButton;
import com.tedu.element.SinglePlayerButton;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

/*
 * @说明 监听类,用于监听用户的操作 KeyListener
 * @
 * 
 */

public class GameListener implements KeyListener, MouseListener {
	private ElementManager em = ElementManager.getManager();

	/*
	 * 能否通过一个集合来记录所有按下的键，如果重复触发，就直接结束 同时，第1次按下，记录到集合中，第2次判定集合中是否有。 松开就直接删除集合中的记录。
	 * 
	 * set集合
	 */

	private Set<Integer> set = new HashSet<Integer>();// 只能存对象

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated m ethod stub

	}

	/**
	 * 按下: 左37 上38 右39 下40   按tab没反应
	 * 实现主角的移动 
	 */
	@Override
	public void keyPressed(KeyEvent e) {
//		拿到玩家集合
		int key = e.getKeyCode();
		if (set.contains(key)) {// 判定集合中是否已经存在，包含这个对象
			// 如果包含直接结束方法
			return;
		}
		set.add(key);
		List<ElementObj> play = em.getElementsByKey(GameElement.PLAY);
		for (ElementObj obj : play) {
			obj.keyClick(true, e.getKeyCode());
		}
		List<ElementObj> play2 = em.getElementsByKey(GameElement.PLAY2);
		for (ElementObj obj2 : play2) {
			obj2.keyClick(true, e.getKeyCode());
		}

//		System.out.println("按下"+e.getKeyCode());

//		System.out.println("keyPressed"+e.getKeyCode());
	}

	/**
	 * 松开
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (!set.contains(e.getKeyCode())) {
			return;
		}
		set.remove(e.getKeyCode());// 移除数据
		List<ElementObj> play = em.getElementsByKey(GameElement.PLAY);
		for (ElementObj obj : play) {
			obj.keyClick(false, e.getKeyCode());
//			System.out.println("松开");
		}

		List<ElementObj> play2 = em.getElementsByKey(GameElement.PLAY2);
		for (ElementObj obj2 : play2) {
			obj2.keyClick(false, e.getKeyCode());
//			System.out.println("松开");
		}

//		System.out.println("keyReleased"+e.getKeyCode());

		// 检查其他按键的状态
		for (Integer pressedKey : set) {
			for (ElementObj obj : play) {
				obj.keyClick(true, pressedKey);
			}
		}
		for (Integer pressedKey : set) {
			for (ElementObj obj2 : play2) {
				obj2.keyClick(true, pressedKey);
			}
		}
		if (e.getKeyCode() == 27) {
			// 回到上级操作
			back();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO 自动生成的方法存根
		System.out.println("Mouse Clicked at (" + e.getX() + ", " + e.getY() + ")");

		// 检测开始游戏按钮是否被点击
		if (beginButton(e))
			return;

		// 检测选关按钮是否被点击
		if (levelButton(e))
			return;

		if (restartButton(e))
			return;

		if (resumeButton(e))
			return;

		if (backButton(e))
			return;

		if (nextButton(e))
			return;

		if (mainBackButton(e))
			return;

		if (multiPlayerButton(e))
			return;

		if (singlePlayerButton(e))
			return;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自动生成的方法存根
		// System.out.println("Mouse Pressed at (" + e.getX() + ", " + e.getY() + ")");
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自动生成的方法存根
		// System.out.println("Mouse Released at (" + e.getX() + ", " + e.getY() + ")");
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自动生成的方法存根
		// System.out.println("Mouse Entered the panel");
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自动生成的方法存根
		// System.out.println("Mouse Exited the panel");
	}

	private boolean beginButton(MouseEvent e) {
		if (em.getElementsByKey(GameElement.BEGINBUTTON).size() > 0) { // 如果“开始游戏”按钮存在
			BeginButton button = (BeginButton) em.getElementsByKey(GameElement.BEGINBUTTON).get(0);
			// 如果点击到了按钮
			if (button.contains(e.getX() - 7, e.getY() - 30)) { // 执行对应操作
				em.init(); // 清空元素资源
				System.out.println("点击了开始游戏按钮");
				GameLoad.backgroundLoad();
//				GameLoad.levelButtonLoad();
				GameLoad.mainBackButtonLoad();
				GameLoad.multiPlayerButtonLoad();
				GameLoad.singlePlayerButtonLoad();
				return true;
			}
		}
		return false;
	}

	private boolean backButton(MouseEvent e) {
		if (em.getElementsByKey(GameElement.BACKBUTTON).size() > 0) { // 如果“开始游戏”按钮存在
			BackButton button = (BackButton) em.getElementsByKey(GameElement.BACKBUTTON).get(0);
			// 如果点击到了按钮
			if (button.contains(e.getX() - 7, e.getY() - 30)) { // 执行对应操作
				em.init(); // 清空元素资源
				System.out.println("点击了返回页面按钮");
				GameLoad.backgroundLoad();
				GameLoad.levelButtonLoad();
				GameLoad.mainBackButtonLoad();
				GameThread.level = 0;
				GameThread.killNum = 0;
				Goals.setGoal(0);
				cancelPause();
				return true;
			}
		}
		return false;
	}

	private boolean restartButton(MouseEvent e) {
		if (em.getElementsByKey(GameElement.RESTARTBUTTON).size() > 0) { // 如果“重新开始游戏”按钮存在
			RestartButton button = (RestartButton) em.getElementsByKey(GameElement.RESTARTBUTTON).get(0);
			// 如果点击到了按钮
			if (button.contains(e.getX() - 7, e.getY() - 30)) { // 执行对应操作
				em.init(); // 清空元素资源
				System.out.println("点击了重新开始按钮");
				GameLoad.LevelLoad(GameThread.level);
				GameThread.killNum = 0;
				Goals.setGoal(0);
				cancelPause();
				return true;
			}
		}
		return false;
	}

	private boolean resumeButton(MouseEvent e) {
		if (em.getElementsByKey(GameElement.RESUMEBUTTON).size() > 0) { // 如果“恢复游戏”按钮存在
			ResumeButton button = (ResumeButton) em.getElementsByKey(GameElement.RESUMEBUTTON).get(0);
			// 如果点击到了按钮
			if (button.contains(e.getX() - 7, e.getY() - 30)) { // 执行对应操作

				cancelPause();
				return true;
			}
		}
		return false;
	}

	private boolean levelButton(MouseEvent e) {
		if (em.getElementsByKey(GameElement.LEVELBUTTON).size() > 0) {
			// System.out.println("选择关卡按钮存在");
			for (int i = 0; i < em.getElementsByKey(GameElement.LEVELBUTTON).size(); i++) {
				LevelButton button = (LevelButton) em.getElementsByKey(GameElement.LEVELBUTTON).get(i);
				if (button.contains(e.getX() - 7, e.getY() - 30)) { // 按下后进行操作
					em.init();
					System.out.println("点击了第" + button.getLevel() + "关");
					GameLoad.LevelLoad(button.getLevel());
					return true;
				}
			}
		}
		return false;
	}

	private boolean nextButton(MouseEvent e) {
		if (em.getElementsByKey(GameElement.NEXTBUTTON).size() > 0) {
			// System.out.println("选择关卡按钮存在");
			for (int i = 0; i < em.getElementsByKey(GameElement.NEXTBUTTON).size(); i++) {
				NextButton button = (NextButton) em.getElementsByKey(GameElement.NEXTBUTTON).get(i);
				if (button.contains(e.getX() - 7, e.getY() - 30)) { // 按下后进行操作
					cancelPause();
					em.init();
					GameLoad.LevelLoad(++GameThread.level);
					return true;
				}
			}
		}
		return false;
	}

	private boolean mainBackButton(MouseEvent e) {
		if (em.getElementsByKey(GameElement.MAINBACKBUTTON).size() > 0) {
			// System.out.println("选择关卡按钮存在");
			for (int i = 0; i < em.getElementsByKey(GameElement.MAINBACKBUTTON).size(); i++) {
				MainBackButton button = (MainBackButton) em.getElementsByKey(GameElement.MAINBACKBUTTON).get(i);
				if (button.contains(e.getX() - 7, e.getY() - 30)) { // 按下后进行操作
					em.init();
					GameLoad.loadObj();
					GameLoad.backgroundLoad();
					GameLoad.beginButtonLoad();
					return true;
				}
			}
		}
		return false;
	}

	private boolean multiPlayerButton(MouseEvent e) {
		if (em.getElementsByKey(GameElement.MULTIPLAYERBUTTON).size() > 0) { // 如果“多人游戏”按钮存在
			MultiPlayerButton button = (MultiPlayerButton) em.getElementsByKey(GameElement.MULTIPLAYERBUTTON).get(0);
			// 如果点击到了按钮
			if (button.contains(e.getX() - 7, e.getY() - 30)) { // 执行对应操作
				em.init(); // 清空元素资源
				System.out.println("点击了多人模式按钮");

				GameLoad.backgroundLoad();
				GameLoad.levelButtonLoad();
				GameLoad.mainBackButtonLoad();
				GameThread.isMultiPlayer = true;
				return true;
			}
		}
		return false;
	}

	private boolean singlePlayerButton(MouseEvent e) {
		if (em.getElementsByKey(GameElement.SINGLEPLAYERBUTTON).size() > 0) { // 如果“多人游戏”按钮存在
			SinglePlayerButton button = (SinglePlayerButton) em.getElementsByKey(GameElement.SINGLEPLAYERBUTTON).get(0);
			// 如果点击到了按钮
			if (button.contains(e.getX() - 7, e.getY() - 30)) { // 执行对应操作
				em.init(); // 清空元素资源
				System.out.println("点击了单人模式按钮");

				GameLoad.backgroundLoad();
				GameLoad.levelButtonLoad();
				GameLoad.mainBackButtonLoad();
				GameThread.isMultiPlayer = false;
				return true;
			}
		}
		return false;
	}

	private void back() { // 根据不同情况，进行不同的回退操作
		if (GameThread.isGameOver) {//防止在游戏结束状态下back键的错误跳转
			return;
		}
		// 情况一： 需要游戏暂停
		if (GameThread.level != 0 && em.getElementsByKey(GameElement.PAUSE).size() < 1) {
			pause();
			// 情况二： 取消游戏暂停
		} else if (GameThread.level != 0 && em.getElementsByKey(GameElement.PAUSE).size() > 0) {
			cancelPause();
			// 情况三： 从选关页面回退到主页面（开始游戏页面）
		} else if (em.getElementsByKey(GameElement.LEVELBUTTON).size() > 0
				|| em.getElementsByKey(GameElement.LEVELOVER).size() > 0
				|| em.getElementsByKey(GameElement.SINGLEPLAYERBUTTON).size() > 0) {
			em.init();
			GameLoad.loadObj();
			GameLoad.backgroundLoad();
			GameLoad.beginButtonLoad();
			Goals.setGoal(0);
		}
	}

	private void pause() { // 暂停游戏
		GameThread.isPause = true;
		GameLoad.pauseLoad();
	}

	private void cancelPause() { // 取消暂停
		GameThread.isPause = false;
		GameThread.isGameOver = false;
		em.getElementsByKey(GameElement.PAUSE).clear();
		em.getElementsByKey(GameElement.BACKBUTTON).clear();
		em.getElementsByKey(GameElement.RESTARTBUTTON).clear();
		em.getElementsByKey(GameElement.RESUMEBUTTON).clear();
		em.getElementsByKey(GameElement.LEVELOVER).clear();
		em.getElementsByKey(GameElement.NEXTBUTTON).clear();
	}
}
