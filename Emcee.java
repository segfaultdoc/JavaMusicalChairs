import java.util.*;


public class Emcee extends Thread {
  private ArrayList <Player> players = new ArrayList<Player> ();
  int pThreads;
  private int numChairs;
  private GameChairs chairs;
  
  public Emcee(int numPlayers, int numChairs){
    this.pThreads = numPlayers;
    this.numChairs = numChairs;
    this.chairs = new GameChairs (numChairs);
  }

  public void run(){ 

   System.out.println("Begin " + pThreads + " players"); 
    
    for(int i =0; i < pThreads; i++){
      players.add(new Player("P" + (i+1), (i+1), this.chairs));
    }
    for(Player p: this.players){
      p.start();
      
    }
    for(Player p: this.players){
      try{ p.join(); }
      catch(Exception e) { }
      
    }
    StringBuilder sb = new StringBuilder();
    
 
    Player winner = null;
    int round = 0;
    String [] lost = new String[this.players.size()-1];
      int count = 0;
    while(round < this.players.size()-1){
      System.out.println("Round "+ (round+1));
      for(Player p: this.players){
        ArrayList <Chair> chairArr = p.getChairHistory();
        if(chairArr.size() > round){
          System.out.println(p.get_name() + " sat in " + chairArr.get(round).getName());
          if(round == this.players.size()-2){ winner = p; }
        }
        else if(chairArr.size() == round){
          //System.out.println("");
          //System.out.println(p.get_name() + " lost");
          lost[count] = p.get_name() + " lost\n";
        }
        

      }
      System.out.println(lost[count]);
      count++;
      round++;
    }
    System.out.println(winner.get_name() + " wins!");
    //while(chairs.getNumChairs > 0){
     
    for(int i = 0; i < 10; i++){
      if(chairs.getNumChairs() == 0){ 
        System.out.println("END"); 
        //try{ this.join(); }
        //catch( Exception e) { e.printStackTrace(); }
        break;
      }
      System.out.println(this.chairs.getNumChairs()); 
        //break;
    }

    //}

    
  }

  //public void playMusic(){
    //this.start();
    
 // }



}
