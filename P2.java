import java.util.*;


public class P2 {
  static int N_Threads = 10;
  static boolean game = true;
  public static void main(String [] args){
    
    try{
      if(args[0] != null){
        N_Threads = Integer.parseInt(args[0]);
      }
    } catch(Exception e){ }
    ArrayList <Player> players = new ArrayList <Player> (N_Threads);

    /*for(int i=0; i < N_Threads; i++){
      
      Runnable r = () -> {
      

      };
    
      players[i] = new Thread(r);
      players[i].start();
    }*/
    
   /*for(int i = 0; i < N_Threads; i++){
      players.add(new Player("P" + (i+1), i+1)); 
    }*/
    /*GameChairs c = new GameChairs(N_Threads-1);
    Emcee e = new Emcee(players);
    while(game == true){
      game =false; 
      e.playMusic();
    }*/
    Emcee e = new Emcee(N_Threads, N_Threads-1);
    e.start();
   // System.out.println("Thread from " + Thread.currentThread().getName());
  }















}
