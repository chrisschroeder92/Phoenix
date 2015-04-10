package edu.augustana.csc490.phoenix;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by cschroeder on 3/26/15.
 */
public class PhoenixView extends SurfaceView implements SurfaceHolder.Callback {

    public PhoenixThread phoenixThread;
    private Activity activity;

    SharedPreferences highScore;

    private static final String TAG = "Phoenix"; // for Log.w(TAG, ...)

    private boolean dialogIsDisplayed = false;
    private boolean gameOver;

    // game timing
    private double totalElapsedTime;
    private double bulletFiredTime;

    // game elements
    private Enemies enemies;
    private ArrayList<Bullet> bullets;
    private Gun gun;
    private Random rand; // random number for bullet generation

    // game parameters
    private int screenWidth;
    private int screenHeight;
    private Paint backgroundPaint;


    public PhoenixView(Context context, AttributeSet attrs) {

        super(context, attrs);
        activity = (Activity) context;

        getHolder().addCallback(this);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);

    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
        newGame();
    }

    public void newGame() {

        // initilize elements
        bullets = new ArrayList<>();
        gun = new Gun(screenWidth, screenHeight);
        enemies = new Enemies(new Point(screenWidth / 2, screenHeight / 4), screenWidth / 20);
        enemies.setVelocity(screenHeight / 30);
        rand = new Random();
        setBulletFireTiming();

        highScore = getContext().getSharedPreferences("HighScore", Context.MODE_PRIVATE);

        if (gameOver) {
            gameOver = false;
            phoenixThread = new PhoenixThread(getHolder());
            phoenixThread.start();
        }
    }

    private void updatePositions(double elapsedTime) {

        double interval = elapsedTime / 1000.0;

        for (int i=0; i<bullets.size(); i++){
            Bullet bullet = bullets.get(i);
            bullets.get(i).updateBullet(interval);
            if (!bullet.checkOnScreen(gun.getRadius())){
                bullets.remove(i);
            }
        }

        for (int i=0; i<bullets.size(); i++) {
            if (!bullets.get(i).isEnemyBullet()){
                if (enemies.isHit(bullets.get(i).getBullet(), bullets.get(i).getBulletRadius())) {
                    bullets.remove(i);
                }
                if (enemies.areAllHit()){
                    stopGame();
                    showGameOverDialog(true);
                    Log.e(TAG, Double.toString(totalElapsedTime));
                    gameOver = true;
                }
            } else{
                if (gun.isHit(bullets.get(i).getBullet(), bullets.get(i).getBulletRadius())){
                    stopGame();
                    showGameOverDialog(false);
                    gameOver = true;
                }
            }
        }

        if (!enemies.updatePosition(interval, screenHeight) ){
            stopGame();
            showGameOverDialog(false);
            gameOver = true;
        }
    }

    public void fireRandomBullet(int num){
        if (5 == num){
            int enemyNum = rand.nextInt(enemies.getSize());
            bullets.add(new Bullet(enemies.getPosition(enemyNum), screenHeight, true));
        }
    }

    public void setBulletFireTiming(){

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (enemies.getSize()==0){ // prevents error when game is over -- will have to change so when the game is over, the timer stops.
                    return;
                }
                fireRandomBullet(rand.nextInt(20));
            }}, 100, 100);
    }

    public void fireBullet() {

        double timeBetweenBullets = totalElapsedTime - bulletFiredTime;  // currently set to only 2 bullet firings a second by the user

        if (timeBetweenBullets > 0.5) {
            bullets.add(new Bullet(gun.getGunPoint(), screenHeight, false));
            bulletFiredTime = totalElapsedTime;
        }else{
            return;
        }
    }

    public void moveGun(MotionEvent event) {

        Point touchPoint = new Point((int) event.getX(), (int) event.getY());  // get the touch point out of the event
        gun.setGunPoint(touchPoint.x,gun.getGunPoint().y); // set only the x-coordinate to move
    }

    public void drawGameElements(Canvas canvas) {

        // clear the background
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(),
                backgroundPaint);

        // draw gun
        canvas.drawCircle(gun.getX(), gun.getY(), gun.getRadius(), gun.getPaint());

        // draw gun end
        canvas.drawRect(gun.getX() - (screenWidth / 30), gun.getY(), gun.getX() + (screenWidth / 30), gun.getY() - (screenHeight * 3 / 20), gun.getPaint());

        // draw enemies
        for (int i=0; i<enemies.getSize(); i++) {
            canvas.drawCircle(enemies.getPosition(i).x, enemies.getPosition(i).y, enemies.getEnemySize(), enemies.getEnemyPaint());
        }
        // draw bullets
        for (int i=0; i<bullets.size(); i++){
            Bullet bullet = bullets.get(i);
            canvas.drawCircle(bullet.getBullet().x,bullet.getBullet().y, bullet.getBulletRadius(), bullet.getBulletPaint());
        }
    }

    // Mostly Copied from CannonGame
    private void showGameOverDialog(boolean won) {

        final int points;
        final boolean loser;
        boolean highScorer = false;

        int highScoreInt = highScore.getInt("HighScore", 0);

        if(won){
            points = calculatePoints();
            if (points > highScoreInt){
                highScore.edit().putInt("HighScore", points).commit();
                highScorer = true;
            }
            loser = false;
        } else{
            points = 0;
            loser = true;
        }

        final boolean highScorer1 = highScorer; // need to do something better here;

        final DialogFragment gameResult = new DialogFragment() {

            public Dialog onCreateDialog(Bundle bundle) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                if (loser){
                    builder.setTitle(getResources().getString(R.string.lost));
                } else if (highScorer1) {
                    builder.setTitle(getResources().getString(R.string.topScore));
                } else{
                    builder.setTitle(getResources().getString(R.string.won));
                }

                builder.setMessage("Total Points: " + points + '\n' + "High Score: " + highScore.getInt("HighScore", 0));
                builder.setPositiveButton(R.string.reset_game,
                        new DialogInterface.OnClickListener() {
                            // called when "Reset Game" Button is pressed
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialogIsDisplayed = false;
                                newGame(); // set up and start a new game
                            }
                        } // end anonymous inner class
                );
                return builder.create();
            }
        };
        // in GUI thread, use FragmentManager to display the DialogFragment
        activity.runOnUiThread(
                new Runnable() {
                    public void run()
                    {
                        dialogIsDisplayed = true;
                        gameResult.setCancelable(false); // modal dialog
                        gameResult.show(activity.getFragmentManager(), "results");
                    }
                } // end Runnable
        ); // end call to runOnUiThread
    }

    private int calculatePoints(){

        return (int) ((100.0 - totalElapsedTime) * 1000);
    }

    public void stopGame() {
        if (phoenixThread != null) {
            phoenixThread.setRunning(false);
        }
    }

    public void releaseResources() {

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (!dialogIsDisplayed) {
            phoenixThread = new PhoenixThread(holder);
            phoenixThread.setRunning(true);
            phoenixThread.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // ensure that thread terminates properly
        boolean retry = true;
        phoenixThread.setRunning(false);

    }

    public boolean onTouchEvent(MotionEvent e) {
        // get int representing the type of action which caused this event
        int action = e.getAction();

        // the user user touched the screen or dragged along the screen
        if (action == MotionEvent.ACTION_DOWN ||
                action == MotionEvent.ACTION_MOVE) {
            moveGun(e); // move the gun FIRST to touchpoint
            fireBullet(); // fire the bullet
        }

        return true;
    }

    private class PhoenixThread extends Thread {

        private SurfaceHolder surfaceHolder;
        private boolean threadIsRunning = true;

        public PhoenixThread(SurfaceHolder holder) {
            surfaceHolder = holder;
            setName("PhoenixThread");
        }

        public void setRunning(boolean running) {
            threadIsRunning = running;
        }

        public void run() {
            Canvas canvas = null;
            long previousFrameTime = System.currentTimeMillis();

            while (threadIsRunning) {
                try {
                    // get Canvas for exclusive drawing from this thread
                    canvas = surfaceHolder.lockCanvas(null);

                    // lock the surfaceHolder for drawing
                    synchronized (surfaceHolder) {
                        long currentTime = System.currentTimeMillis();
                        double elapsedTimeMS = currentTime - previousFrameTime;
                        totalElapsedTime += elapsedTimeMS / 1000.0;
                        updatePositions(elapsedTimeMS); // update game state
                        drawGameElements(canvas); // draw using the canvas
                        previousFrameTime = currentTime; // update previous time

                    }
                    Thread.sleep(10); // if you want to slow down the action...
                } catch (InterruptedException ex) {
                    Log.e(TAG, ex.toString());
                } finally  // regardless if any errors happen...
                {
                    // make sure we unlock canvas so other threads can use it
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

    }

}