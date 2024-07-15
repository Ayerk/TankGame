package com.tedu.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tedu.element.ElementObj;

/**
 * *
 * 
 * @说明 本类是元素管理器，专门存储所有的元素， 同时，提供方法给予视图控制和获取数据
 * @author HAHA
 * @问题一：存储所以元素数据，怎么存放？ list map set
 * @问题二：管理器是视图和控制要访问，管理器就必须只有一个，单例模式
 */

public class ElementManager {
	/*
	 * String 作为key 匹配所有的元素 play->List<Object> listPlay enemy->List<Object> 枚举类型
	 * 当做map的key用来区分不一样的资源，用于获取资源 List中元素的泛型应该是 元素 基类
	 * 所有元素都可以存放到map集合中，显示模块只要获取到这个map就可以 显示是有的界面需要显示的元素（调用元素基类的showElement（）方法）
	 */

	private Map<GameElement, List<ElementObj>> gameElements;

	public Map<GameElement, List<ElementObj>> getGameElements() {
		return gameElements;
	}

//  添加元素（多半由加载器调用）
	public void addElement(ElementObj obj, GameElement ge) {
//		List<ElementObj>list =gameElements.get(ge); 
		// hash map.get()方法，获取key值对应的元素
//		list.add(obj);
		gameElements.get(ge).add(obj);// 添加对象到集合中，按key值进行存储
	}

//	依据key 返回list集合，取出某一类元素
	public List<ElementObj> getElementsByKey(GameElement ge) {
		return gameElements.get(ge);
	}

	/**
	 * 单例模式：内存中有且只用一个实例。 饿汉模式-启动就自动加载实例 饱汉模式-需要使用的时候才加载实例
	 * 
	 * 编写方式： 
	 * 1.需要一个静态的属性（定义一个static常量）单例的引用 
	 * 2.提供一个静态的方法（返回这个实例）return单例的引用
	 * 3.一般为防止其他人自己使用（类是可以实例化），所以会私有化构造方法
	 */

	private static ElementManager EM;// 引用
	// synchronized线程锁->保证本方法执行中只有一个线程

	public static synchronized ElementManager getManager() {
		if (EM == null) {// 空值判定
			EM = new ElementManager();
		}
		return EM;
	}

	private ElementManager() {// 构造方法私有化
		init();// 实例化方法
	}

//	static {//饿汉实例化对象 //静态语句块是在类被加载的时候直接执行
//		EM =new ElementManager(); //只会执行一次
//		
//	}
	/**
	 * 本方法是为了 将来可能出现的功能扩展，重写init方法准备的
	 * 
	 */
	public void init() {// 实例化在这里完成
		// hash散列
		gameElements = new HashMap<GameElement, List<ElementObj>>();
		// 将每种元素集合都放到map中；
//		gameElements.put(GameElement.PLAY, new ArrayList<ElementObj>());
//		gameElements.put(GameElement.MAPS, new ArrayList<ElementObj>());
//		gameElements.put(GameElement.ENEMY, new ArrayList<ElementObj>());
//		gameElements.put(GameElement.BOSS, new ArrayList<ElementObj>());
		// 道具，子弹，爆炸效果，死亡效果。。。
		for(GameElement ge:GameElement.values()) {//通过循环读取枚举类型的方式添加集合
			gameElements.put(ge,new ArrayList<ElementObj>());
		}
	}

};
