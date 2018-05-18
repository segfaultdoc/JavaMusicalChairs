import java.util.*;
import java.util.concurrent.locks.*;

/*
 *
 * The infamous chairs, that all 
 * threads so greatly desire.
 *
 */

public class Chair {
  private String name;
  private int num;
  private Player p;
  private ReadWriteLock lock = new ReentrantReadWriteLock();
  private boolean taken = false;

  public Chair(String name, int num){
    this.name = name;
    this.num = num;
  }
 
  public ReadWriteLock getLock(){
    return this.lock;
  }
  public String getName(){
    this.lock.readLock().lock();
    try{
      return this.name;
    }
    finally{
      this.lock.readLock().unlock();
    }
  }

  public int getNum(){
    this.lock.readLock().lock();
    try{
      return this.num;
    }
    finally{
      this.lock.readLock().unlock();
    }
  }

  public void setPlayer(Player p){
    this.p = p;
  }
  
  public Player getPlayer(){
    return this.p;
  }

  /*
   * This was originally causing a deadlock, 
   * because it needed a writelock, but the object 
   * calling this already held lock.
   *
   */
  public String sit(Player p){
      this.p = p; 
      this.taken = true;
      return this.name;
    
  }

  public void dontSit(){ 
  
      this.p = null;
      this.taken = false;
  }


  public boolean isTaken(){
    return this.taken;
  }
}
