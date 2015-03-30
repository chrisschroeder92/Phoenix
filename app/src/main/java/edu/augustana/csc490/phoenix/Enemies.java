package edu.augustana.csc490.phoenix;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by cschroeder on 3/29/15.
 */
public class Enemies {

    private ArrayList<Point> enemiesList;
    private boolean[] hitList;
    private int velocity;
    private int enemySize;
    private Paint enemyPaint;

    public void Enemies(int baseWidth, int baseHeight, int size){

        enemiesList = new ArrayList<Point>(1);
        enemiesList.add(new Point(baseWidth, baseHeight));
        /**   enemies.set(2, new Point(screenWidth / 2, screenHeight * 2/3));
         enemies.set(3, new Point(screenWidth * 3/2, screenHeight * 2/3));
         enemies.set(4, new Point(screenWidth / 3, screenHeight / 3));
         enemies.set(5, new Point(screenWidth, screenHeight / 3));
         enemies.set(6, new Point(screenWidth * 4/3, screenHeight / 3));
         **/
        hitList = new boolean[1];
        for (int i = 0; i<hitList.length; i++){
            hitList[i] = false;
        }

        enemyPaint = new Paint();
        enemyPaint.setColor(Color.GRAY);

        hitList = new boolean[1];

        enemySize = size;

    }

    public Enemies(Point p, int d){
        Enemies(p.x, p.y, d);
    }
    /**
     * ********ENEMY LAYOUT*******
     * ****|4|*****|5|*****|6|****
     * ***************************
     * *********|2|*****|3|********
     * ***************************
     * ************|1|************
     */

    public void setVelocity(int i){
        velocity = i;
    }

    public int getVelocity(){
        return velocity;
    }

    public boolean isHit(Point bullet, int bulletRadius){

        if (bullet != null){
            if (Math.abs(bullet.x - enemiesList.get(0).x) < (bulletRadius + enemySize) &&
                    Math.abs(bullet.y - enemiesList.get(0).y) < (bulletRadius + enemySize))
             {
                return true;

            } else{

                return false;
            }
        }
            return false;
    }

    public boolean areAllHit(Point bullet, int bulletRadius){


        return false;
    }

    public void updatePosition(double interval){

            int enemyY = enemiesList.get(0).y;
            int enemyX = enemiesList.get(0).x;
            enemyY += interval * velocity;
            enemiesList.set(0, new Point(enemyX, enemyY));
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
