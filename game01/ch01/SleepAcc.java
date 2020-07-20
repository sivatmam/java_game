package ch01;

import java.text.DecimalFormat;

public class SleepAcc
{
  private static DecimalFormat df;
  
  public static void main(String[] args){
  
      df = new DecimalFormat("0.##");
      
      sleepTest(1000);
      sleepTest(500);
      sleepTest(200);
      sleepTest(100);
      sleepTest(50);
      sleepTest(20);
      sleepTest(10);
      sleepTest(5);
      sleepTest(1);
  }
  
  private static void sleepTest(int delay){
    long timeStart, timeEnd;
    
    timeStart = System.nanoTime(); 
    
    try{
        Thread.sleep(delay);
    } catch (InterruptedException ie){}
    
    timeEnd = System.nanoTime();
    
    double timeDiff =  ((double)(timeEnd - timeStart) / 1_000_000.0);
    double err = ((delay - timeDiff)/timeDiff) * 100;
    
    System.out.println("Slept: "+delay+" ms,   Time Difference: "+df.format(timeDiff)+" ms,   Error: "+df.format(err)+" %");
  }
}
