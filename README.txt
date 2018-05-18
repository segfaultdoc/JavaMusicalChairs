compile - javac *.java
run - java P2 {num_players}


1. 
  
  a)
    The resource(s) that represent the music are lines 74 and 49 in the Player class. 
    There is an "avail" var in the conditional of the while loop, that queries the shared
    GameChairs class, asking if there are any available chairs. During this loop, the music
    is off and player(s) are looking for chairs to sit in. Once all chairs are filled, then
    the music is turned back on, and this is represented by line 74 in the Player class. All
    threads sleep for 150 milliseconds, meaning there is music playing for this duration, as
    no chairs can be contested over.
  
  b)
    Chairs are represented by the "Chair" class. Players never directly call any of the methods
    in here. They call methods in the GameChairs class, which is holds a collection of chairs.

2. 
  
  a)
    Players find empty chairs by having each thread generate a psuedorandom number. This player
    then calls the GameChairs' sitInChair method, passing in the generated number. The GameChairs
    method gets the Chair object mapped to this number, and asks it, if it is taken. If not taken
    then the player sits, and no longer looks for a chair. If the chair is taken, the player continues
    to try and sit, for as long as there are available chairs.

3.
  
  a)
    The biggest challenge, was designing the overall architecture. Things such as whether or not
    to create a player class, and how to print the information about each round at the hand, were some
    challenges I faced. There was defintely a need for cooperation. Emcee had to accept the GameChairs
    object from main() and pass it down to each player it instantiated. The threads were all fighting for
    resources, so any method the was getter/reader, I put a read lock on, to allow for multiple reads
    at a time. Any methods that modified data, were writelocked. The chair objects were up for stake,
    but I felt using a "middle-man", GameChairs class, the protection of resources was a bit easier.

4.

  a)
    The straightforward aspects were the locking of the objects. The hardest part figuring out where data
    races occured and actually creating the threads (implementing the run() method in Player).

5.
  
  a)
    I take CS475 right now, and to be quite honest, this project was quite simple compared to the concurrency
    programs from 475. But, it was different from 475, in the sense that for this class we acutally had to create
    the threads, whereas in 475 we are just required to write thread-safe code. Therefore, the biggest learning
    curve for me was figuring out how to implement the run() method, and where exactly threads should fight for 
    resources. I had an hour or two when I first started the assignment, where my code would deadlock after it
    got down to 2 chairs. This was a good sign, that I could make it almost to the end of the game! Then I would
    get down to zero chairs and my code would still deadlock. The way I was debgugging was through printing out
    the shared GameChairs object, from the Emcee thread. Since I use vim, print statements are my best friends.
    I realized the issue was that I was over synchronizing. Basically I would lock a chair object for write, which
    would then call a method that would try to obtain a read lock. Once I figured this out I was on my way to the
    finish line.

6.
  Good projects this semester, I feel smarter :) 





