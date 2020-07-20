package ch01;



public class TimeChecker
{
    public static void main(String[] args){
      TimeChecker.sysTimeResolution();
      TimeChecker.sysNanoTimeResolution();
    }
    
    private static void sysTimeResolution(){
     
      long total, count1, count2;
      total = 0;
      
      for(int i = 0; i < 4; i++){
          count1 = System.currentTimeMillis();
          count2 = System.currentTimeMillis();
          while(count1 == count2)
          count2 = System.currentTimeMillis();
          
          System.out.println("run: " +i +" count2: "+count2);
          System.out.println("run: " +i +" count1: "+count1);
          System.out.println("run: " +i +" difference: "+(count2-count1));
          total += 1000 * (count2-count1);
      }
      
      System.out.println("System Time resolution: " + total/4 + " microsecs");
    }
    
    private static void sysNanoTimeResolution(){
     
      double total; 
      long count1, count2;
      total = 0.0;
      
      for(int i = 0; i < 4; i++){
          count1 = System.nanoTime();
          count2 = System.nanoTime();
          while(count1 == count2)
          count2 = System.nanoTime();
          
          System.out.println("run: " +i +" count2: "+count2);
          System.out.println("run: " +i +" count1: "+count1);
          System.out.println("run: " +i +" difference: "+(count2-count1));
          total +=  (count2-count1) / 1000.0;
      }
      
      System.out.println("System Time resolution: " + total/4.0 + " microsecs");
    }
}
