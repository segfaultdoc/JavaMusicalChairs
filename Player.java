import java.util.*;
import java.util.concurrent.atomic.*;

/*
 * This class extends thread, and 
 * tries sitting in chairs
 *
 */

public class Player extends Thread {
  // player name
  private String name;
  // player number
  private int num;
  // false as long as the thread is alive
  // and playing
  private  boolean out = false;

  private GameChairs chairs;
  // I use a random number generator to randomly have thread occupy
  // a chair resource
  private Random r = new Random();
  // keeps track of all chairs the player sits in throughout the game
  private ArrayList <Chair> chairHistory = new ArrayList<Chair> ();
  
  public Player(String name, int num, GameChairs c){
    this.name = name;
    this.num = num;
    this.chairs = c;
  }
 
  public ArrayList <Chair> getChairHistory(){
    return new ArrayList<Chair> (this.chairHistory);

  }
  public void run(){
    // while the player has not lost the game
    // the thread will continue to look for chairs
    // during rounds
    while(out == false && chairs.getNumChairs() > 0){
      
      Chair c = null;
      
      boolean avail = chairs.available();
      // while a chair is still available and 
      // the player has not sat yet, then the 
      // player will continue to look for a chair
      // until all chairs are taken
      while(avail == true && c == null){
        try{
          c = chairs.sitInChair(this, r.nextInt(chairs.getNumChairs()+1));
          
        } catch(Exception e){ break; }
        // updates the chair availibility
        avail = chairs.available();
      }
      // if no chairs are available and the player ended with no chair
      // then he is out
      if(c == null){
        out = true;
        // lock the GameChairs object
        synchronized(this){
          // since one player loses at a time, this 
          // is when a chair is removed. Otherwise multiple
          // chairs would be removed simultaneously
          this.chairs.removeChair();
        }
     
      }
      // If the player sat, then the chair is added to the history
      else if(c != null){
        chairHistory.add(new Chair(c.getName(), c.getNum()));
        try{
          // this essentially represents the music is on
          this.sleep(150);
        } catch(Exception e){ e.printStackTrace(); }
      } 
    }
  }

  public String get_name(){
    return this.name;
  }

  public void set_name(String name){
    this.name = name;
    super.setName(name);
  }

  public int getNum(){
    return this.num;
  }

  public void setNum(int num){
    this.num = num;
  }
}
