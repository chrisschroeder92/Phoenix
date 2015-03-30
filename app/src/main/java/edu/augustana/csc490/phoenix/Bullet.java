package edu.augustana.csc490.phoenix;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by cschroeder on 3/30/15.
 */
public class Bullet {

    private Point bullet;
    private int bulletSpeed;
    private int bulletVelocityY;
    private int bulletRadius;
    private boolean bulletOnScreen;
    private Paint bulletPaint;

    public Bullet(Point gunPoint, int screenHeight){

        // set Point
        bullet = new Point();
        bullet.x = gunPoint.x;
        bullet.y = gunPoint.y - (screenHeight / 10);

        bulletRadius = screenHeight / 100;
        bulletSpeed = screenHeight / 2;
        bulletVelocityY = (-bulletSpeed);
        bulletOnScreen = true;

        // set Paint
        bulletPaint = new Paint();
        bulletPaint.setColor(Color.RED);
    }

    public Point getBullet(){
        return bullet;
    }

    public void updateBullet(double interval){
        bullet.y += interval * bulletVelocityY;
    }

    public boolean checkOnScreen(){
        if (bullet.y - bulletRadius < 0) {
            bulletOnScreen = false;
            return bulletOnScreen;
        } else {
            return true;
        }
    }

    public int getBulletRadius(){
        return bulletRadius;
    }

    public Paint getBulletPaint(){
        return bulletPaint;
    }
}
