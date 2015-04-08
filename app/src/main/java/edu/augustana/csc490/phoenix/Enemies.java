package edu.augustana.csc490.phoenix;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

/**
 * ********ENEMY LAYOUT*******
 * ****|4|*****|5|*****|6|****
 * ***************************
 * *********|2|*****|3|********
 * ***************************
 * ************|1|************
 */

public class Enemies {

    private ArrayList<Point> enemiesList;
    private boolean[] hitList;
    private int velocity;
    private int enemySize;
    private Paint enemyPaint;

    public void Enemies(int baseWidth, int baseHeight, int size){

        enemiesList = new ArrayList<Point>(6);

        enemiesList.add(new Point(baseWidth, baseHeight));              // 1
        enemiesList.add(new Point(baseWidth * 5/8, baseHeight * 2/3));    // 2
        enemiesList.add(new Point(baseWidth * 11/8, baseHeight * 2/3));  // 3
        enemiesList.add(new Point(baseWidth / 4, baseHeight / 3));      // 4
        enemiesList.add(new Point(baseWidth, baseHeight / 3));          // 5
        enemiesList.add(new Point(baseWidth * 7/4, baseHeight / 3));    // 6

        hitList = new boolean[6];

        for (int i = 0; i<hitList.length; i++){
            hitList[i] = false;
        }

        enemyPaint = new Paint();
        enemyPaint.setColor(Color.GRAY);

        enemySize = size;

    }

    public Enemies(Point p, int d){
        Enemies(p.x, p.y, d);
    }

    public void setVelocity(int i){
        velocity = i;
    }

    public boolean isHit(Point bullet, int bulletRadius) {

        int bulletDiameter = bulletRadius * 2;
            for (int i = 0; i < enemiesList.size(); i++) {
                if (bullet != null) {
                    if (Math.abs(bullet.x - enemiesList.get(i).x) < (bulletDiameter + enemySize) &&
                            Math.abs(bullet.y - enemiesList.get(i).y) < (bulletDiameter + enemySize)) {
                        enemiesList.remove(i);
                        return true;
                    }
                }
            }
        return false;
    }

    public boolean areAllHit(){

        if (enemiesList.size() == 0){

            return true;

        } else {

            return false;
        }
    }

    public boolean updatePosition(double interval, int screenHeight){

        for(int i=0; i< enemiesList.size(); i++) {
            int enemyY = enemiesList.get(i).y;
            int enemyX = enemiesList.get(i).x;
            enemyY += interval * velocity;
            enemiesList.set(i, new Point(enemyX, enemyY));
            if (enemiesList.get(i).y > screenHeight){
                enemiesList.remove(i);
            }
            if (enemiesList.size() == 0){
                return false;
            }
        }

        return true;
    }

    public Point getPosition(int i){
        return enemiesList.get(i);
    }

    public int getEnemySize(){
        return enemySize;
    }

    public int getSize(){
        return enemiesList.size();
    }

    public Paint getEnemyPaint() { return enemyPaint;}

}
