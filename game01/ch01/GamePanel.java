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
    
    private Graphics dbg;
    private Image dbImage = null;
    
    private static final Random rng = new Random();
    private int colint;
    
    private static int x = 100;
    private static int y = 100;
    
    public GamePanel(int w, int h){
      pwidth = w;
      pheight = h;
      setPreferredSize(new Dimension(pwidth, pheight));
      setBackground(Color.WHITE);
    }
    
    @Override
    public void paintComponent(Graphics g){
      super.paintComponent(g);
      if(dbImage != null)
        g.drawImage(dbImage, 0, 0, null);
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
      while(running){
        gameUpdate();
        gameRender();
        repaint();
        
        try{
            Thread.sleep(20);
            
        }
        catch(InterruptedException iex){}
      }
      System.exit(0);
    }
    
    private void gameUpdate(){
        
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
        
        dbg.setColor(Color.WHITE);
        dbg.fillRect(0,0,pwidth,pheight);
        
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
}
