package ch01;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;


public class GamePanel extends JPanel implements Runnable
{
    private static int pwidth;
    private static int pheight;
    
    private Thread animator;
    private volatile boolean running = false;
    private volatile boolean gameOver = false;
    private volatile boolean isPaused = false;
    
    
    private static final int NO_DELAYS_PER_YIELD = 16;
    private static final int MAX_FRAME_SKIPS = 5;
    
    private Graphics dbg;
    private Image dbImage = null;
    
    // Requested period between frames, determines FPS.
    private static final int period = 10;
    
    private static final Random rng = new Random();
    private int colint;
    
    private static int x = 100;
    private static int y = 100;
    private static int ovalx = 375;
    private static int ovaly = 200;
    private static Color ovalColor = Color.BLUE;
    private static boolean flip = false;
    
    
    // Stats
    private static long MAX_STATS_INTERVAL = 1000L;
    
    private static int NUM_FPS = 10;
    
    private long statsInterval = 0L
    private long prevStatsTime = 0L;
    private long totalElapsedTime = 0L;
    
    private long frameCount = 0;
    private double fpsStore[];
    private long statsCount = 0;
    private double averageFPS = 0.0;
    
    private DecimalFormat df = new DecimalFormat("0.##");
    private DecimalFormat timedf = new DecimalFormat("0.####");
    
    public GamePanel(int w, int h){
      pwidth = w;
      pheight = h;
      setPreferredSize(new Dimension(pwidth, pheight));
      setBackground(Color.WHITE);
      
      setFocusable(true);
      requestFocus();
      readyForTermination(); 
      readyForMouseClicks();
    }
    
    private void readyForTermination(){
      addKeyListener(new KeyAdapter() {
        public void keyPressed(KeyEvent e){
          int keyCode = e.getKeyCode();
          if((keyCode == KeyEvent.VK_ESCAPE) ||
            (keyCode == KeyEvent.VK_Q) ||
            (keyCode == KeyEvent.VK_END) ||
            ((keyCode == KeyEvent.VK_C) && (e.isControlDown()))){
            
              running = false;
          }
          
          if(keyCode == KeyEvent.VK_P){
            if (isPaused) isPaused = false;
            else isPaused = true;
          }
        }
      });
    }
    
    private void readyForMouseClicks(){
      addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e){
            testPress(e.getX(), e.getY());    
          }
      });
    }
    
    private void testPress(int mousex, int mousey){
       if(!isPaused && !gameOver){
           
         if (((mousex >= ovalx) && (mousex <= ovalx+50)) && ((mousey >= ovaly) && (mousey <= ovaly+50))){
           System.out.println("You got me!!!");  
           if(ovalColor == Color.BLUE) ovalColor = Color.RED;
           else if(ovalColor == Color.RED) ovalColor = Color.GREEN;
           else if(ovalColor == Color.GREEN) ovalColor = Color.BLUE;
         }
         System.out.println("Mouse x: "+mousex+" y: "+mousey);    
       }
    }
    
    public void pauseGame(){
      isPaused = true;
    }
    
    public void resumeGame(){
      isPaused = false;
    }
    
    @Override
    public void paintComponent(Graphics g){
      super.paintComponent(g);
      // if(dbImage != null)
        // g.drawImage(dbImage, 0, 0, null);
    }
    
    @Override
    public void addNotify(){
      super.addNotify();
      startGame();
    }
    
    private void startGame(){
      if(animator == null || !running){
        animator = new Thread(this);
        animator.start();
      }
    }
    
    public void stopGame(){
      running = false;   
    }
    
    public void run(){
      running = true;
      
      int counter = 0;
      long sumTime = 0;
      long beforeTime, afterTime, timeDiff, sleepTime, tempTime;
      long overSleepTime = 0L;
      int noDelays = 0;
      long excess = 0L;
      
      beforeTime = System.nanoTime();
      prevStatsTime = beforeTime;
      
      while(running){
          
          
        gameUpdate();
        gameRender();
        //repaint();
        paintScreen();
        
        afterTime = System.nanoTime();
        timeDiff = afterTime - beforeTime;
        sleepTime = (period * 1_000_000L) - timeDiff - overSleepTime;
        
        if(sleepTime > 0 ){
        
          try{
            Thread.sleep(sleepTime/1_000_000);
            //System.out.println(sleepTime/1_000_000);
            
          }
          catch(InterruptedException iex){}
          overSleepTime = System.nanoTime() - afterTime - sleepTime;
        } else {
          overSleepTime = 0L;
          excess -= sleepTime;  // Accumulate the negative sleep time
          
          if(++noDelays >= NO_DELAYS_PER_YIELD){
              Thread.yield();
              noDelays = 0;
          }
        }
        
        // tempTime = beforeTime;
        beforeTime = System.nanoTime();
        
        int skips = 0;
        while((excess > (period * 1_000_000L)) && ( skips < MAX_FRAME_SKIPS)){
          excess -= (period * 1_000_000L);
          gameUpdate();
          skips++;
        }
        
        reportStats();
        // Frame Rate Display
        //
        // if(counter < (1000/period)){
            // sumTime += (beforeTime - tempTime);
            
            // counter++;
        // }else {
           // System.out.println("Frame Rate: "+Double.toString(((double)sumTime)/(period*1_000_000L)));
           // sumTime = 0;
           // counter = 0;
        // }    
      }
      System.exit(0);
    }
    
    private void gameUpdate(){
        if(!isPaused && !gameOver){
            
        }
    }
    
    private void gameRender(){
      if(dbImage == null){
        dbImage = createImage(pwidth, pheight);
        if(dbImage == null){
          System.out.println("dbImage is null");
          return;
        } 
        dbg = dbImage.getGraphics(); 
      } 
         
        
        
        // colint = rng.nextInt(2);
        // if(colint == 0)
          // dbg.setColor(Color.BLUE);
        // else dbg.setColor(Color.RED);
        Graphics2D dbg2 = (Graphics2D) dbg;
        dbg2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        dbg.setColor(Color.WHITE);
        dbg.fillRect(0,0,pwidth,pheight);
        
        dbg.setColor(ovalColor);
        
        dbg.fillOval(ovalx, ovaly, 50, 50);
        if(ovaly == 200) flip = true;
        if (ovaly == 400) flip = false;
        if (flip) ovaly++; else ovaly--;
        
        if(gameOver)
          gameOverMessage(dbg);
      
    }
    
    
    
    private void gameOverMessage(Graphics g)
    { if (x > pwidth) {
        x = 0; 
        if (y > pheight) y = 0; 
        else y +=20;
      }
      else x +=100;
      
      String msg = "You lose!";
      
      g.setColor(Color.RED);
      g.drawString(msg, x, y);   
    }
    
    private void paintScreen(){
        Graphics g;
        try{
           g = this.getGraphics();
           if((g != null) && (dbImage != null)){
             g.drawImage(dbImage, 0, 0, null);
           }
           Toolkit.getDefaultToolkit().sync();
           g.dispose();
        } catch (Exception e){
          System.out.println("Graphics context error: " + e);
        }
    }
    
    private void reportStats(){
      
        frameCount++;
        statsInterval += period;
        
        if(statsInterval >= MAX_STATS_INTERVAL){
          long timeNow = System.nanoTime();
          
          long realElapsedTime = timeNow - prevStatsTime;
        }
        
    }
}
