import java.util.*;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class GameChairs{
  private ArrayList <Chair> chairs;
  // keeps track of the number of chairs left in the game
  private int numChairs;
  // maps players sitting in chairs
  private ConcurrentHashMap <Player, Chair> pcMap = new ConcurrentHashMap <Player, Chair> ();
  // mapps all chairs that exist in the game to their number
  private ConcurrentHashMap <Integer, Chair> nChairMap = new ConcurrentHashMap <Integer, Chair> ();
  private ReadWriteLock lock = new ReentrantReadWriteLock();

  public GameChairs(int numChairs){
    this.numChairs = numChairs;
    chairs = new ArrayList <Chair> (numChairs);
    for(int i =0; i < numChairs; i++){
      nChairMap.put(new Integer(i+1), new Chair("C"+(i+1), (i+1)));
    }
  }

  public void removeChair(){
    this.lock.writeLock().lock();

    try{
      if(this.numChairs > 0){
        // removes the chair from the map
        nChairMap.remove(this.numChairs);
        // clears the chairs from players sitting
        pcMap.clear();

        for(Chair c: nChairMap.values()){
          c.dontSit();  
        }
        numChairs--; 
      }
    }
    finally{
      this.lock.writeLock().unlock();
    }
  }

  public int getNumChairs(){
    this.lock.readLock().lock();
    try{
      return this.numChairs;
    }
    finally{
      this.lock.readLock().unlock();
    }
  }

  public boolean available(){
    this.lock.readLock().lock();
    try{
      if(this.numChairs == 0){ return false; }
      else 
      { return (this.pcMap.size() < this.numChairs); }
    }
    finally{
      this.lock.readLock().unlock();
    }
  }

  public synchronized Chair sitInChair(Player p, int n){
      
    Chair c = nChairMap.get(n);
     if(c == null || this.numChairs== 0 ){ return null; } 
      c.getLock().writeLock().lock();
      try{  
        if(c.isTaken() == false){
          c.sit(p);
          // maps this player to the chair
          this.pcMap.put(p, c);
          return c;
        }
        else{ return null; }
      }
      finally{
        c.getLock().writeLock().unlock(); 
      }
  }
}
