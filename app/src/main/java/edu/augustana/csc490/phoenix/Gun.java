package edu.augustana.csc490.phoenix;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by cschroeder on 3/30/15.
 */
public class Gun {

    private int gunBaseRadius;
    private Point gunPoint;
    private Paint gunPaint;


    public Gun(int screenWidth, int screenHeight){

        gunPoint = new Point(screenWidth / 2, screenHeight);

        gunBaseRadius = screenWidth / 8;

        gunPaint = new Paint();
        gunPaint.setColor(Color.YELLOW);
        gunPaint.setStrokeWidth(screenWidth / 18f);

    }

    public Point getGunPoint(){
        return gunPoint;
    }

    public void setGunPoint(int x, int y){
        gunPoint.set(x, y);
    }

    public int getX(){
        return gunPoint.x;
    }

    public int getY(){
        return gunPoint.y;
    }

    public int getRadius(){
        return gunBaseRadius;
    }

    public Paint getPaint(){
        return gunPaint;
    }

    public boolean isHit(Point bullet, int bulletRadius) {
        if (Math.abs(bullet.x - gunPoint.x) < (bulletRadius + gunBaseRadius) &&
                Math.abs(bullet.y - gunPoint.y) < (bulletRadius + gunBaseRadius)) {
            return true;
        }
        return false;
    }

}
