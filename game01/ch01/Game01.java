package ch01;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Game01
{
  public static void main(String[] args){
    JFrame f = new JFrame("Game 01");
    
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    f.setLayout(new FlowLayout());
    GamePanel gp = new GamePanel(800, 450);
    f.add(gp);
    f.addWindowListener(new WindowAdapter() {
       public void windowClosing(WindowEvent we){
         gp.stopGame();
         f.dispose();
       }
    });
    f.pack();
    f.setVisible(true);
  }
}
