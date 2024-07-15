package com.tedu.element;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;


/**
 * @说明 所有元素的基类
 * @author HAHA
 * 
 * 
 */

public abstract class ElementObj {
	private int x;
	private int y;
	private int w;
	private int h;
	private ImageIcon icon;// 图片对象,获取高宽等属性
	// 还有.....状态值
	protected boolean live = true;// 生存状态 true 代表存在，false代表死亡
	// 可以使用枚举值来定义 （生存，死亡，隐身，无敌）
	
	//testing///用来区分草和水不与子弹pk
	public String name="";

	
//   注明：当重新定义一个用于判定状态的变量，需要思考：1.初始化 2.值的改变 3.值的判定
	public ElementObj() { // 这个构造没有作用,为了继承不报错

	}

	/**
	 * @说明 带参数构造方法;由子类传输数据到父类
	 * @param x    左上角x坐标
	 * @param y    左上角y坐标
	 * @param w    宽度
	 * @param h    高度
	 * @param icon 图片
	 */

	public ElementObj(int x, int y, int w, int h, ImageIcon icon) {
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.icon = icon;
	}

	/**
	 * @说明 抽象方法:显示元素
	 * 
	 * @param g 画笔 用于进行绘画
	 */
	public abstract void showElement(Graphics g);// 抽象方法必须实现

	/**
	 * @说明 使用父类定义接收键盘事件的方法
	 *       只有需要实现键盘监听的子类,重写这个方法(约定) 
	 * 
	 * @说明 方法2 使用接口的方式;使用接口方式需要在监听类进行类型转换
	 * 
	 * @题外话 约定 配置 大部分的java框架需要进行配置的
	 *         约定优于配置
	 *         
	 * @param  bl 点击的类型 true 代表按下  false代表松开
	 * @param  key     代表触发的键盘的code值
	 * @扩展   本方法分为2个方法,一个接收按下,一个接收松开        
	 */
	public void keyClick(boolean bl, int key) {// 不是强制重写的

	}

	/**
	 * @说明 移动方法：需要移动的子类，实现这个方法
	 */

	protected void move(long gameTime) {

	}

	/**
	 * @设计模式 模板模式；在模板模式中定义对象执行方法的先后顺序，由子类选择性重写方法
	 *  1.移动 2.换装 3.子弹发射
	 */
	public final void model(long gameTime) {// final 不能重写
//		先换装
		updateImage(gameTime);
//		再移动
		move(gameTime);
//		再发射子弹
		add(gameTime);
	}
//    long ... aaa 不定长的数组，可以向这个方法传输N个long类型的数据
	protected void updateImage(long gametimes) {
	}

	protected void add(long gameTime) {
	};

//  死亡方法
	public void die() { //死亡也是一个对象
	

	}

	public ElementObj createElement(String str) {
		return null;
	}

	/**
	 * @说明 本方法返回 元素的碰撞矩形对象（实时返回）
	 * @return
	 */
	public Rectangle getRectangle() {//碰撞检测用的hitbox
		return new Rectangle(x,y,w,h);
	}
	
	
	/**
	 * @说明 碰撞方法
	 *  一个是this对象，一个是传入值的obj
	 *  @param obj
	 *  @return boolean 返回true 说明有碰撞，返回false说明没有碰撞
	 */
	public boolean pk(ElementObj obj) {
		return this.getRectangle().intersects(obj.getRectangle());
	}
	
	
	
	/**
	 * 只要是VO类就要为属性生成get和set方法
	 * JAVA POJO 定义规范
	 * VO 值对象 view object (视图对象)界面需要显示的
	 */

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}


}
