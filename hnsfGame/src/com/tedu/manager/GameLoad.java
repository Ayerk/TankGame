package com.tedu.manager;

import java.io.IOException;
import java.io.InputStream;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.ImageIcon;
import com.tedu.controller.GameThread;
import com.tedu.element.ElementObj;
import com.tedu.element.Enemy;
import com.tedu.element.Goals;
import com.tedu.element.Instruction;
import com.tedu.element.MapObj;

/**
 * @说明 加载器（工具类：用于读取配置文件的工具，大多提供的是static方法
 */

public class GameLoad {
//	得到资源管理器
	private static ElementManager em = ElementManager.getManager();

	public static int DefaultEnemyNum = 0;

//	图片集合 使用map来存储  枚举类型配合移动（扩展）
	public static Map<String, ImageIcon> imgMap = new HashMap<>();
	public static Map<String, List<ImageIcon>> imgMaps;

//		Collections 用于集合排序的工具类 可以为所有的对象类型的 记录进行排序
//		排序只能用于Collection 的子类

//	用户读取文件的类
	private static Properties pro = new Properties();

	/**
	 * @说明 传入地图id由加载方法依据文件规则自动生成地图文件名称，加载文件
	 * @param mapId 文件编号
	 */

	public static void MapLoad(int mapId) {

//		得到了文件路径
		String mapName = "com/tedu/text/" + mapId + ".map";
//		使用IO流来获取文件对象 得到类加载器
		ClassLoader classLoader = GameLoad.class.getClassLoader();
		InputStream maps = classLoader.getResourceAsStream(mapName);
//		System.out.println(maps);
		if (maps == null) {
			System.out.println("地图配置文件读取异常，请重新安装");
			return;
		}

		try {
//			以后用的是xml和json
			pro.clear();
			pro.load(maps);
			Enumeration<?> names = pro.propertyNames();
//			可以直接动态的获取所有的key，有key就可以获取value
			while (names.hasMoreElements()) {
				String key = names.nextElement().toString();
//				System.out.println(pro.getProperty(key));
//			System.out.println(names.nextElement());
//				可以自动创建和加载地图了
				String[] arrs = pro.getProperty(key).split(";");
				for (int i = 0; i < arrs.length; i++) {
					ElementObj element = new MapObj().createElement(key + "," + arrs[i]);
					em.addElement(element, GameElement.MAPS);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GoalLoad();
	}

	/**
	 * @说明 加载图片代码
	 * 加载图片 代码和图片之间差一个路径问题
	 */
	public static void loadImg() {// 可以带参数，不同的关需要不同的图片资源
		String texturl = "com/tedu/text/GameData.pro";// 重新进行命名
		ClassLoader classLoader = GameLoad.class.getClassLoader();
		InputStream text = classLoader.getResourceAsStream(texturl);

//		imgMap用于存放数据
		pro.clear();// 清除

		try {
//			System.out.println(text);
			pro.load(text);
			Set<Object> set = pro.keySet();// 是一个set集合
			for (Object o : set) {
				String url = pro.getProperty(o.toString());
				imgMap.put(o.toString(), new ImageIcon(url));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 加载玩家
	 * 
	 */
	public static void loadPlay() {
		String playStr = "300,540,up";
		ElementObj obj = getObj("play");
		ElementObj play = obj.createElement(playStr);
//		ElementObj play = new Play().createElement(playStr);
//		解耦，降低代码和代码之间的耦合度
		em.addElement(play, GameElement.PLAY);
	}
	
	public static void loadPlay2() {
		String playStr2 = "460,540,up";
		ElementObj obj2 = getObj("play2");
		ElementObj play2 = obj2.createElement(playStr2);
		em.addElement(play2, GameElement.PLAY2);
	}

	public static void loadEnemy(int mapId) {
		GameLoad.DefaultEnemyNum = 0;
//		得到了文件路径
//		com/tedu/text/level7enemycontrol.pro
		String controlUrlName = "com/tedu/text/DefaultEnemyLocation.pro";
//		使用IO流来获取文件对象 得到类加载器
		ClassLoader classLoader = GameLoad.class.getClassLoader();
		InputStream Ips = classLoader.getResourceAsStream(controlUrlName);
//		System.out.println(maps);
		if (Ips == null) {
			System.out.println("敌人控制配置文件读取异常，请重新安装");
			return;
		}
		try {
			pro.clear();
			pro.load(Ips);
			Enumeration<?> names = pro.propertyNames();
//			可以直接动态的获取所有的key，有key就可以获取value
			while (names.hasMoreElements()) {
				String key = names.nextElement().toString();
//				System.out.println(pro.getProperty(key));
//				//注意这里next又跳到下一个去了
//				System.out.println(names.nextElement());
//				可以自动创建和加载地图了
				String[] arrs = pro.getProperty(key).split(";");
				GameLoad.DefaultEnemyNum += arrs.length;
				for (int i = 0; i < arrs.length; i++) {
					ElementObj enemy = new Enemy().createElement(arrs[i]);// 目前只传入了x,y和图标
					em.addElement(enemy, GameElement.ENEMY);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void loadBase() {
		loadObj();
		String baseStr = "380,540";
		ElementObj obj = getObj("base");
		ElementObj base = obj.createElement(baseStr);
		em.addElement(base, GameElement.BASE);
	}

	public static ElementObj getObj(String str) {
		try {
			Class<?> class1 = objMap.get(str);
			if (class1 == null) {
				System.err.println("Class not found for key: " + str);
				return null;
			}
			Object newInstance = class1.newInstance();
			if (newInstance instanceof ElementObj) {
				return (ElementObj) newInstance; // 这个对象和new Play()等价
				// 直接从配置文件中取
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 扩展：使用配置文件实例化，通过固定的key（字符串）
	 * @param args
	 */

	private static Map<String, Class<?>> objMap = new HashMap<>();

	public static void loadObj() {
		String texturl = "com/tedu/text/obj.pro";// 重新进行命名
		ClassLoader classLoader = GameLoad.class.getClassLoader();
		InputStream text = classLoader.getResourceAsStream(texturl);
		pro.clear();

		try {
			pro.load(text);

			Set<Object> set = pro.keySet();
			for (Object o : set) {
				String classUrl = pro.getProperty(o.toString());
//				使用反射的方式直接获取类
				Class<?> forName = Class.forName(classUrl);
				objMap.put(o.toString(), forName);
				System.out.println("Loaded class for key: " + o.toString() + ", class: " + forName.getName());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @说明 用于加载”得分显示”元素
	 */
	public static void GoalLoad() {
		ElementObj goals = new Goals().createElement("");
		em.addElement(goals, GameElement.GOALS);

	}
	
	/**
	 * @说明 用于加载”操作指南”元素
	 */
	public static void InstructionLoad() {
		ElementObj Instruction = new Instruction().createElement("");
		em.addElement(Instruction, GameElement.INSTRUCTION);
		System.out.println("我");
	}

	public static void backgroundLoad() {
		ElementObj obj = GameLoad.getObj("UIbackground");
		ElementObj mainUI = obj.createElement("");
		em.addElement(mainUI, GameElement.UIBACKGROUND);
	}

	public static void beginButtonLoad() {
		ElementObj button_obj = GameLoad.getObj("beginButton");
		ElementObj button = button_obj.createElement("");
		em.addElement(button, GameElement.BEGINBUTTON);
	}

	public static void levelButtonLoad() {
		for (int i = 1; i <= 10; i++) {
			ElementObj levelButton = GameLoad.getObj("levelButton");
			levelButton = levelButton.createElement(i + "");
			em.addElement(levelButton, GameElement.LEVELBUTTON);
		}
	}

	/**
	 * @说明 添加关卡和相关的元素
	 * @param level
	 */
	public static void LevelLoad(int level) {
		GameThread.gameTime = 0;
		GameThread.killNum = 0;

		GameLoad.loadBase();
		GameLoad.MapLoad(level);// 可以变为变量，重新加载地图
//		// 加载主角
		GameLoad.loadPlay();// 可以带参数
		if (GameThread.isMultiPlayer) {
			GameLoad.loadPlay2();
		}
		
		GameLoad.hpShowLoad("type:Play,x:20,y:560");
		if(GameThread.isMultiPlayer) {
			GameLoad.hpShowLoad("type:Play2,x:740,y:560");
		}
		
		// 添加敌人NPC
		GameLoad.loadEnemy(level);// 通过level读取对应配置文件控制敌人生成数量位置和时间等

		GameThread.level = level;
	}

	public static void LevelOverLoad() {
		GameThread.killNum = 0;
		ElementObj levelOver = GameLoad.getObj("levelOver");
		levelOver = levelOver.createElement("");
		em.addElement(levelOver, GameElement.LEVELOVER);
	}

	public static void pauseLoad() {
		ElementObj pause = GameLoad.getObj("pause");
		pause = pause.createElement("");
		em.addElement(pause, GameElement.PAUSE);

		backButtonLoad();
		restartButtonLoad();
		resumeButtonLoad();
	}

	public static void backButtonLoad() { // 加载返回主页按钮元素
		GameThread.killNum = 0;
		ElementObj backButton = GameLoad.getObj("backButton");
		backButton = backButton.createElement("");
		em.addElement(backButton, GameElement.BACKBUTTON);
	}

	public static void resumeButtonLoad() { // 加载返回游戏按钮元素
		ElementObj backButton = GameLoad.getObj("resumeButton");
		backButton = backButton.createElement("");
		em.addElement(backButton, GameElement.RESUMEBUTTON);
	}

	public static void restartButtonLoad() { // 加载重新开始按钮元素

		ElementObj backButton = GameLoad.getObj("restartButton");
		backButton = backButton.createElement("");
		em.addElement(backButton, GameElement.RESTARTBUTTON);
	}

	public static void nextButtonLoad() { // 加载重新开始按钮元素
		ElementObj nextButton = GameLoad.getObj("nextButton");
		nextButton = nextButton.createElement("");
		em.addElement(nextButton, GameElement.NEXTBUTTON);
	}

	public static void mainBackButtonLoad() { // 加载重新开始按钮元素
		ElementObj mainBackButton = GameLoad.getObj("mainBackButton");
		mainBackButton = mainBackButton.createElement("");
		em.addElement(mainBackButton, GameElement.MAINBACKBUTTON);
	}

	public static void failGameLoad(String cause) {
		ElementObj failGame = GameLoad.getObj("failGame");
		failGame = failGame.createElement(cause);
		em.addElement(failGame, GameElement.FAILGAME);
		backButtonLoad();
		restartButtonLoad();
	}

	public static void multiPlayerButtonLoad() { // 加载双人模式按钮元素
		ElementObj multiPlayerButton = GameLoad.getObj("multiPlayerButton");
		multiPlayerButton = multiPlayerButton.createElement("");
		em.addElement(multiPlayerButton, GameElement.MULTIPLAYERBUTTON);
	}

	public static void singlePlayerButtonLoad() { // 加载单人模式按钮元素
		ElementObj singlePlayerButton = GameLoad.getObj("singlePlayerButton");
		singlePlayerButton = singlePlayerButton.createElement("");
		em.addElement(singlePlayerButton, GameElement.SINGLEPLAYERBUTTON);
		InstructionLoad();
	}
	public static void hpShowLoad(String str) {
		ElementObj hpShow = GameLoad.getObj("hpShow");
		hpShow = hpShow.createElement(str);
		em.addElement(hpShow, GameElement.HPSHOW);
	}
}
