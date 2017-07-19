# pacman-demo
Demo for Game Physics, Enemy Behaviour, and Game Completion for TJHSST Summer Computer Science 2017. This demo is a simplified Java version of Pacman, used to show possible implementations of enemy behaviour, collision detection, user controls, and game completion.

## Enemy Behaviour
Ghost in this version of pacman have a variety of possible behaviours, the current one being a random movement with momentum, meaning that once a direction has been chosen the ghost will continue in that direction until it collides with something. Other options include truly random movement and a follower that tries to align itself with the pacman while possible. (Behaviours are intentionally somewhat simple both for quick demonstration purposes and for ease of comprehension)

## Collision Detection
There are two types of collision in this demo: object to object and object to image (although the image _is_ an object).

Object to object works by comparing the two's coordinates to see if there is any overlap, as done below:
```java
boolean collided =  getX() < pac.getX() + pac.getDiameter() && getX() + getDiameter() > pac.getX() && getY() < pac.getY() + pac.getDiameter() && getY() + getDiameter() > pac.getY();
```
(It should be noted that the coordinate system places (0,0) at the top left, and that both the current class and pac are rectangles)

Object to image is a little more complicated. The pixels of the object in question, which could be either a pac-dot, pacman, or ghost, are iterated over and tested against the pixels of the `BufferedImage`. After getting the RGB value for that pixel, [bitwise operators](https://www.tutorialspoint.com/java/java_basic_operators.htm) are used to convert it into red, green, and blue values from 0 to 255. Testing showed that the walls had a blue of 248, so that value was used as a test case for collision.
##### `PacDot.java` (but similar collision methods are also found in `Ghost.java` and `Pacman.java`)
``` java
public boolean collide_with_wall(BufferedImage bi, int maxX, int maxY){ //Test if collided with a wall
      for (int x = (int)getX(); x<= (int)(getX()+getDiameter()); x++){ //Iterate over the dot's surface, test if the background image's pixels are from the wall
         for (int y = (int)getY(); y<= (int)(getY()+getDiameter()); y++){
            if (x < maxX && y < maxY && x >= 0 && y>= 0){
               int clr=  bi.getRGB(x,y);  //Converts buffered img to rgb int, then converted into red, green, and blue values (0-255)
               int  red   = (clr & 0x00ff0000) >> 16;
               int  green = (clr & 0x0000ff00) >> 8;
               int  blue  =  clr & 0x000000ff;
               if (blue > 247){ //Found through testing, varies based on img 
                  return true;
               }
            }
         
         }
      }
      return false;
   
   }
```

This implementation has two major benefits:`
1. It's (relatively) easy to do. Instead of creating a series of wall objects to represent each section you can just modify the background image to get the required effect. This also means that slightly different effects for different objects are easy to generate by switching out the image. For example, in this version of pacman the ghosts aren't allowed down the corridor. Instead of having to create a different set of objects, they simply use a different image! (in this case `maps/pacman_map_comp.png` is used by pacman and `maps/pacman_new_ghost_map` is used by ghosts).
2. It's quicker, both in creation/modification time and on run. If you were to create an array of wall objects you would have to iterate over each one and check if it was colliding with the pacman. The image method, in contrast, only has to check the values that the pacman itself covers, which is more intuitive from a programming perspective and faster.

## User Controls
User controls in this version of pacman are pretty simple. The arrow keys control pacman's movement, and all other events are controlled by a timer. In the case that the game has ended, the spacebar will restart. This is accomplished with Event Listeners, a sample of which is found below (all listener code is found in `DemoPanel.java`)
##### `DemoPanel.java`
``` java
public DemoPanel() {
    (...)
    addKeyListener(new keysListener()); //These 2 lines setup keyboard input
    setFocusable(true);
  }
(...)
private class keysListener extends KeyAdapter { //When a key is pressed
    public void keyPressed(KeyEvent e) {
        int key_code = e.getKeyCode(); //Get key code (makes rest of code more readable)
        (...)
    }
 }
```

## Game Completion
This version of Pacman is completed in two ways: either you run into a ghost, or you pick up all of the pac-dots. As there is a known number of pac-dots, that case can be tested for directly:
##### `DemoPanel.java`
``` java
(...)
if (score == MAX_SCORE) { //Maximum score
    game_ended = true;
    you_won = true;
    t.stop(); //Stop timer (prevents repainting)
} 
(...)
```
Ghost collisions are slightly more complex, but as we have access to their collision method from `DemoPanel.java` we can iterate over the `ghosts` array and test if any of them are currently colliding with pacman.
##### `DemoPanel.java`
``` java
for (int i = 0; i < ghosts.length; i++) { //Test if any of the ghosts collide with pacman
    if (ghosts[i].collide_with_pacman(pacman)) { //If so, end the game and stop the timer
        game_ended = true;
        t.stop();
        break;
    }
}
```

## What do these files do?
### Coded files
* [`DemoPanel.java`](https://github.com/Alex-Gurung/pacman-demo/blob/master/DemoPanel.java) - Actual display of the game, extends JPanel
    * Contains overall game logic and display, as well as listeners for user and timer based input
* [`Driver.java`](https://github.com/Alex-Gurung/pacman-demo/blob/master/Driver.java)
    * Standard driver, creates a JFrame and puts a DemoPanel inside
* [`Ghost.java`](https://github.com/Alex-Gurung/pacman-demo/blob/master/Ghost.java)
    * Contains the Ghost class, which extends polkadot
    * Ghost knows everything polkadot does, as well as its name, how to draw itself, how to move around, and how to test for wall and pacman collisions
* [`PacDot.java`](https://github.com/Alex-Gurung/pacman-demo/blob/master/PacDot.java)
    * Contains the PacDot class, which also extends polkadot
    * Very barebones additions, but has specific collision and drawing methods
* [`Pacman.java`](https://github.com/Alex-Gurung/pacman-demo/blob/master/Pacman.java)
    * Pacman class, extends polkadot
    * Similar to Ghost, but moves differently and draws itself with a different image (object collisions also aren't handled here, only image collisions)
* [`Polkadot.java`](https://github.com/Alex-Gurung/pacman-demo/blob/master/Polkadot.java)
    * You've seen this file before, basic class for drawing a polkadot
### Jar files
* [`Pacman.java`](https://github.com/Alex-Gurung/pacman-demo/blob/master/Pacman.java)
    * Standard gameplay, ghosts move randomly but continue in the same direction if unblocked
### Folders
* [`characters`](https://github.com/Alex-Gurung/pacman-demo/tree/master/characters)
    * Contains the image files for ghosts and pacman
* [`maps`](https://github.com/Alex-Gurung/pacman-demo/tree/master/characters)
    * Contains the different map files (the blue rectangles were added in different locations depending on use case)
### Miscellaneous files (don't worry about these, they aren't necessary)
* [`.gitignore`](https://github.com/Alex-Gurung/pacman-demo/blob/master/.gitignore)
    * For uploading (_"pushing"_) purposes, ignores `.class` files
* [`LICENSE`](https://github.com/Alex-Gurung/pacman-demo/blob/master/LICENSE)
    * Standard practice for github repositories, don't worry about it
* [`compile.bat`](https://github.com/Alex-Gurung/pacman-demo/blob/master/compile.bat)
    * For my purposes, on Windows this will compile every `.java` file in a directory