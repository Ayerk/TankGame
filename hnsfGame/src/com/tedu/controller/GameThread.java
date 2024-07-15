package com.tedu.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.tedu.element.Base;
import com.tedu.element.ElementObj;
import com.tedu.element.Enemy;
import com.tedu.element.Goals;
import com.tedu.element.Play;
import com.tedu.element.Play2;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

/**
 * @说明 游戏的主线程，用于控制游戏加载，游戏关卡，游戏运行时自动化 游戏判定：游戏地图切换 资源释放和重新读取
 * 
 * @继承 使用继承的方式实现多线程（一般建议时使用接口实现）
 */

public class GameThread extends Thread {
	private ElementManager em;
	public static int level = 0; // 记录当前是哪个关卡
	public static boolean isPause = false;
	public static long gameTime = 0L;
	public static boolean isMultiPlayer = false;// 判断是否为双人模式 如果是是双人模式，设为true，默认为false
	public static int killNum = 0;
	public static int enemyNum = 0;
	public static boolean isGameOver =false;//用于判断游戏失败，true为游戏失败，默认为false

	public GameThread() {
		em = ElementManager.getManager();
	}

	@Override
	public void run() {// 游戏的run方法 主线程
		while (true) {// 扩展，可以将true变为一个变量用于控制结束

//		游戏开始前   读进度条，加载游戏资源（场景资源）
			gameLoad();

//		游戏进行时   游戏过程中
			gameRun();

//		游戏场景结束 游戏资源回收（场景资源）
			gameOver();

			try {
				sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void levelSelected() {
		GameLoad.backgroundLoad();
		GameLoad.beginButtonLoad();
	}

	/**
	 * 游戏的加载
	 */
	private void gameLoad() {

		GameLoad.loadImg();// 加载图片
		GameLoad.loadObj();// 通过类名创建获取类对象
		levelSelected();
	}

	/**
	 * @说明：游戏进行时
	 * @任务说明 游戏过程中需要做的事情：
	 * 1.自动化玩家的移动，碰撞，死亡
	 * 2.新元素的增加（NPC死亡后出现的道具）
	 * 3.暂停等等。。。。
	 * 
	 * 先实现主角的移动
	 */

	private void gameRun() {

		while (true) {// 预留扩展 true 可以变为变量，用于控制关卡结束等

			// 按gametime来生成敌人
			AutoLoadEnemies(GameThread.level, GameThread.gameTime);

			Map<GameElement, List<ElementObj>> all = em.getGameElements();
			List<ElementObj> enemys = em.getElementsByKey(GameElement.ENEMY);
			List<ElementObj> files = em.getElementsByKey(GameElement.PLAYFILE);
			List<ElementObj> maps = em.getElementsByKey(GameElement.MAPS);
			List<ElementObj> tools = em.getElementsByKey(GameElement.TOOLS);
			List<ElementObj> plays = em.getElementsByKey(GameElement.PLAY);
			List<ElementObj> enemyfiles = em.getElementsByKey(GameElement.ENEMYFILE);
			List<ElementObj> base = em.getElementsByKey(GameElement.BASE);
			List<ElementObj> play2 = em.getElementsByKey(GameElement.PLAY2);
			List<Play> newPlays = (List<Play>) (List<?>) plays;
			List<Play> newPlays2 = (List<Play>) (List<?>) play2;
			List<com.tedu.element.Tools> newTools = (List<com.tedu.element.Tools>) (List<?>) tools;

			synchronized (this) { // 添加同步块
				if (isPause) {
					try {// 把刷新时间加到continue前面防止程序刷新过快
						sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				}
			}

//			System.out.println(isMultiPlayer);

			// 碰撞检测在各个物体运动时进行 move（）内置checkCollision()判断
			moveAndUpdate(all, gameTime); // 游戏元素自动化方法

			ElementPk(enemys, files);
			ElementPk(maps, files);
			ElementPk(maps, enemyfiles);
			ElementPk(plays, enemyfiles);
			ElementPk(play2, enemyfiles);
			ElementPk(base, enemyfiles);
//			ElementPk(base, files); //用于测试
			takeTools(newPlays, newTools, gameTime);
			ToolsEnd(newPlays, gameTime);
			takeTools(newPlays2, newTools, gameTime);
			ToolsEnd(newPlays2, gameTime);

			if (maps.size() > 0 && killNum == enemyNum && playAlive() && level != 0 && isPause == false) { // 在关卡中且敌人被杀光
				System.out.println("关卡结束");
				GameLoad.LevelOverLoad();
				GameLoad.restartButtonLoad();
				GameLoad.backButtonLoad();
				if (level != 10) {
					GameLoad.nextButtonLoad();
				}
				isPause = true;
			}

//			if (maps.size() > 0 && !playAlive() && level != 0 && isPause == false) // 玩家死亡
//			{
//				System.out.println(plays.size());
//				isPause = true;
//			}

			// gameTime除了在暂停界面不自增，其余时候都自增
//			System.out.println("gameTime:"+gameTime);
			gameTime++;// 唯一的时间控制
			try {
				sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void AutoLoadEnemies(int level, long gameTime) {
		GameThread.enemyNum = 0;
		Properties pro = new Properties();
//		得到了文件路径
//		com/tedu/text/AutoCreatEnemyLevel7.pro		
		String controlUrlName = "com/tedu/text/AutoCreatEnemyLevel" + level + ".pro";
//		使用IO流来获取文件对象 得到类加载器
		ClassLoader classLoader = GameThread.class.getClassLoader();
		InputStream Ips = classLoader.getResourceAsStream(controlUrlName);
//		System.out.println(maps);
		if (Ips == null) {
//			System.out.println("动态生成敌人控制配置文件读取异常，请重新安装");
			return;
		}
		try {
			pro.clear();
			pro.load(Ips);
			Enumeration<?> names = pro.propertyNames();
//			可以直接动态的获取所有的key，有key就可以获取value
			while (names.hasMoreElements()) {
				String key = names.nextElement().toString();
				String[] arrs = pro.getProperty(key).split(";");
				GameThread.enemyNum += arrs.length;
				for (int i = 0; i < arrs.length; i++) {
					String[] split = arrs[i].split(",");
					if (split[3].equals(Long.toString(gameTime))) {
						// 如果有敌人子类的话，new Enemy（）需要改成getObj（"敌人子类"）
						ElementObj enemy = new Enemy().createElement(arrs[i]);// 目前只传入了x,y和图标
						em.addElement(enemy, GameElement.ENEMY);
					}
				}
			}
			GameThread.enemyNum += GameLoad.DefaultEnemyNum;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	游戏元素自动化方法
	public void moveAndUpdate(Map<GameElement, List<ElementObj>> all, long gameTime) {
//		Map<GameElement, List<ElementObj>> all = em.getGameElements();
//		GameElement.values();// 隐藏方法 返回值是数组,数组顺序是定义枚举的顺序
		for (GameElement ge : GameElement.values()) {
			List<ElementObj> list = all.get(ge);
//			编写直接操作数据集合数据的代码不建议使用迭代器
//			for (int i = 0; i < list.size(); i++) {
			for (int i = list.size() - 1; i >= 0; i--) {
				ElementObj obj = list.get(i);// 读取为基类
//				if (ge.equals(GameElement.PLAYFILE)) {
//					System.out.println("::::::::::::"+obj);
//				}//debug使用
				if (!obj.isLive()) {// 如果死亡删除
//					list.remove(i--); //可以使用这个方式
//					启动一个死亡方法（方法中可以做的事情例如：死亡，掉落装备）
					obj.die();
					if (obj instanceof Enemy) {
						GameThread.killNum++;
						System.out.println("击杀一名敌人，现已击杀数：" + killNum);
					}
					if (obj instanceof Base) {
						System.out.println("基地被摧毁");
						Goals.setGoal(0);
						isPause = true;
						isGameOver=true;
						GameLoad.failGameLoad("基地被摧毁");
					}

					if (!isMultiPlayer) {// 如果不是双人模式只进行一个玩家的死亡检测
						if (obj instanceof Play) {
							System.out.println("玩家被摧毁");
							Goals.setGoal(0);
							isPause = true;
							isGameOver=true;
							GameLoad.failGameLoad("玩家被摧毁");
						}
					}
					if (isMultiPlayer) {// 如果是双人模式，在其中一名玩家死亡时，进行另一个玩家的死亡检测
						if (!play2Alive()) {
							if (obj instanceof Play) {
								System.out.println("玩家被摧毁");
								Goals.setGoal(0);
								isPause = true;
								isGameOver=true;
								GameLoad.failGameLoad("所有玩家被摧毁");
							}
						}
						if (!playAlive()) {
							if (obj instanceof Play2) {
								System.out.println("玩家被摧毁");
								Goals.setGoal(0);
								isPause = true;
								isGameOver=true;
								GameLoad.failGameLoad("所有玩家被摧毁");
							}
						}

					}
					// 如果被销毁的元素不是基地，就移除
					// 基地需要更新图片，故不移除
					if (!(obj instanceof Base))
						list.remove(i);
					continue;
				}
				obj.model(gameTime);
				// 调用每个类自己的move方法完成自己的移动
			}
		}
	}

	public void ElementPk(List<ElementObj> listA, List<ElementObj> listB) {

//		使用双循环做一对一判定，如果为真，就设置两个对象的死亡状态
		for (int i = 0; i < listA.size(); i++) {
			ElementObj A = listA.get(i);
			for (int j = 0; j < listB.size(); j++) {
				ElementObj B = listB.get(j);
				if (A.name.equals("GRASS") || A.name.equals("RIVER")) {
					continue;
				}
				if (A.pk(B)) {
//					问题：如果是boss，也一枪一个吗
//					将setLive（false）变为一个受攻击方法，还可以传入另外一个攻击力
//					当受攻击方法里执行时，如果血量减为0，再进行设置生存为false
//					扩展
					A.setLive(false);
					B.setLive(false);
					break;
				}
			}
		}
	}

//	public void ElementCol(List<ElementObj> listA, List<ElementObj> listB) {
////		使用双循环做一对一判定，如果为真，就设置两个对象的不可移动状态
//		for (int i = 0; i < listA.size(); i++) {
//			ElementObj A = listA.get(i);
//			for (int j = 0; j < listB.size(); j++) {
//				ElementObj B = listB.get(j);
//				if (A.pk(B)) {
//					A.setMovable(false);
//					B.setMovable(false);
//					break;
//				}
//			}
//		}
//	}

	/**
	 * 游戏切换关卡
	 */
	private void gameOver() {
		// TODO Auto-generated method stub

		// @author chenbaolin @说明 关卡结束资源回收可以使用这个方法
//		em.init();
	}


	// 判断是否存在敌人元素
	private boolean areEnemysAllKilled() {
		return em.getElementsByKey(GameElement.ENEMY).size() < 1;
	}

	// 进入下一关
//	private void nextLevel() {
//		em.init();
//		GameLoad.LevelLoad(++GameThread.level);
//		GameLoad.restartButtonLoad();
//		GameLoad.backButtonLoad();
//		GameLoad.nextButtonLoad();
//	}

	private boolean playAlive() {
		return em.getElementsByKey(GameElement.PLAY).size() > 0;
	}

	private boolean play2Alive() {
		return em.getElementsByKey(GameElement.PLAY2).size() > 0;
	}

	public void takeTools(List<Play> Play, List<com.tedu.element.Tools> Tool, long gameTime) {
		// 双层循环，一对一判定
		for (int i = 0; i < Play.size(); i++) {
			for (int j = 0; j < Tool.size(); j++) {
				if (Play.get(i).pk(Tool.get(j))) {
					System.out.println("玩家拾取到道具");
					if (Tool.get(j).getType() == "SpeedUp") {
						System.out.println("道具效果：加速");
						Play.get(i).setSpeedUp(true);
					} else if (Tool.get(j).getType() == "MoreBullet") {
						System.out.println("道具效果：攻击间隔加快");
						Play.get(i).setMoreBullet(true);
					} else if (Tool.get(j).getType() == "MoreFx") {
						Play.get(i).setMoreFx(true);
					} else if (Tool.get(j).getType() == "Invalid") {
						System.out.println("道具效果：无敌");
						Play.get(i).setInvalid(true);
					} else if (Tool.get(j).getType() == "MoreHp") {
						System.out.println("增加血量");
						Play.get(i).setHp((Play.get(i).getHp() + 1) % 4);
					}
					Play.get(i).setToolsEndTime(gameTime);
//					System.out.println(gameTime);
					Tool.get(j).setLive(false);
				}
			}
		}
	}

	public void ToolsEnd(List<Play> Play, long gameTime) {
		for (int i = 0; i < Play.size(); i++) {
			if (gameTime - Play.get(i).getToolsEndTime() >= 500 && Play.get(i).getToolsEndTime() > 0) {
				Play.get(i).setSpeedUp(false);
				Play.get(i).setMoreBullet(false);
				Play.get(i).setMoreFx(false);
				Play.get(i).setInvalid(false);
				System.out.println("道具效果结束");
				Play.get(i).setToolsEndTime(0);
			}
		}
	}
}
