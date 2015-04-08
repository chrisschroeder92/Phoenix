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
    private int bulletRadius;
    private boolean bulletOnScreen;
    private boolean enemyBullet;
    private Paint bulletPaint;

    public Bullet(Point gunPoint, int screenHeight, boolean enemy){

        // set Point
        bullet = new Point();

        enemyBullet = enemy;

        if (enemyBullet)
        {
            bullet.x = gunPoint.x;
            bullet.y = gunPoint.y;

            bulletRadius = screenHeight / 150;
            bulletSpeed = screenHeight / 3; // going down the screen
        } else
        {
            bullet.x = gunPoint.x;
            bullet.y = gunPoint.y - (screenHeight / 10);  // move it to near the top of the gun

            bulletRadius = screenHeight / 100;
            bulletSpeed = -screenHeight / 2; // going up the screen
        }
        bulletOnScreen = true;

        // set Paint
        bulletPaint = new Paint();
        bulletPaint.setColor(Color.RED);

    }

    public Point getBullet(){
        return bullet;
    }

    public void updateBullet(double interval){
        bullet.y += interval * bulletSpeed;
    }

    public boolean checkOnScreen(int gunRadius){
        if (bullet.y - bulletRadius - (gunRadius) <= 0) {
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

    public boolean isEnemyBullet(){
        return enemyBullet;
    }
}
