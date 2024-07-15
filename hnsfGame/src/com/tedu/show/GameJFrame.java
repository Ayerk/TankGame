package com.tedu.show;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @说明 游戏窗体 主要实现的功能：关闭，显示，最大最小化
 * @author HAHA @功能说明： 需要嵌入面板，启动主线程
 * @窗体说明 swing awt 窗体大小（记录用户上次使用软件的窗体样式）
 * 
 * @分析： 1.面板绑定到窗体 
 *         2.监听绑定 
 *         3.游戏主线程启动
 *         4.显示窗体
 */
public class GameJFrame extends JFrame {
//	public static int GameX = 815;// 一般情况下GAMEX
//	public static int GameY = 617;
	private JPanel jPanel = null;//正在显示的面板
	private KeyListener keyListener = null;//键盘监听
	private MouseMotionListener mouseMotionListener= null;//鼠标监听
	private MouseListener mouseListener = null;
	private Thread thead = null; //游戏主线程

	public GameJFrame() {
		init();
	}

	public void init() {
//		this.setSize(GameX, GameY);// 设置窗口大小
		this.setTitle("坦克大战");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置退出并关闭
//		this.setLocationRelativeTo(null);// 屏幕居中显示
	
	}
	
	/*窗体布局：可以写存档，读档，button，拓展*/
	public void addButton() {
//		this.setLayout(manager); //布局格式，可以添加控件
	} 	
	/**
	 * 启动方法
	 */
	public void start() {
		if(jPanel!=null) {
			this.add(jPanel);
		}
		if(keyListener!=null) {
			this.addKeyListener(keyListener);
		}
		if(mouseListener!=null) {
			this.addMouseListener(mouseListener);
		}
		if(thead!=null) {
			thead.start();//启动线程
		}
//		界面刷新
		this.setVisible(true);//显示界面
//		如果jPanel是runnable的子类实体对象
		if(this.jPanel instanceof Runnable) {
//			已做类型判断,强制类型转化不会出错
//			new Thread((Runnable)this.jPanel).start();
			Runnable run = (Runnable)this.jPanel;
			Thread th = new Thread(run);
			th.start();
		}
		
	}
	
	
	

	/*set注入：通过set方法注入配置文件中读取的数据，将配置文件中的数值赋值为类的属性
	 * 构造注入：需要配合构造方法
	 * */
	public void setjPanel(JPanel jPanel) {
		this.jPanel = jPanel;
	}

	public void setKeyListner(KeyListener keyListener) {
		this.keyListener = keyListener;
	}

	public void setMouseMotionListener(MouseMotionListener mouseMotionListener) {
		this.mouseMotionListener = mouseMotionListener;
	}

	public void setMouseListener(MouseListener mouseListener) {
		this.mouseListener = mouseListener;
	}

	public void setThead(Thread thead) {
		this.thead = thead;
	}
	
	
	
	

}
