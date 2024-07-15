package com.tedu.element;

import java.awt.Graphics;

import com.tedu.manager.GameLoad;

public class DeathAnimation extends ElementObj {

    private long timecount = 0;
    private int initialW = 40; // 初始宽度
    private int initialH = 40; // 初始高度
    private int centerX; // 中心点X坐标
    private int centerY; // 中心点Y坐标

    @Override
    public void showElement(Graphics g) {
        this.setIcon(GameLoad.imgMap.get("boom"));
        g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), this.getW(), this.getH(), null);
    }

    @Override
    public ElementObj createElement(String str) {
        String[] split = str.split(",");
        for (String str1 : split) {
            String[] split2 = str1.split(":");
            switch (split2[0]) {
                case "x":
                    centerX = Integer.parseInt(split2[1]);
                    break;
                case "y":
                    centerY = Integer.parseInt(split2[1]);
                    break;
            }
        }
        this.setH(initialH);
        this.setW(initialW);
        this.setX(centerX);
        this.setY(centerY);
        return this;
    }

    @Override
    protected void updateImage(long gametimes) {
        if (timecount > 40) {
            super.setLive(false);
            timecount = 0;
        } else {
            int newW = initialW + (int)(timecount * 0.5); // 新宽度随时间增加
            int newH = initialH + (int)(timecount * 0.5); // 新高度随时间增加
            this.setX(centerX-(int)(timecount * 0.25)); // 更新X坐标
            this.setY(centerY-(int)(timecount * 0.25)); // 更新Y坐标
            this.setW(newW); // 更新宽度
            this.setH(newH); // 更新高度
        }
        
        timecount++;
    }
}