import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;


public class Ghost extends Polkadot {
      private String name = "clyde"; //Default for testing, can be anything
      private String current_direction = "up";
      // constructors
      public Ghost() //default constructor
      {
            super();
      }
      public Ghost(String my_name) //default constructor
      {
            super();
            name = my_name;

      }
      public Ghost(double x, double y, double d, Color c, String my_name) { //Constructor actually used
            super(x, y, d, c);
            name = my_name;

      }
      public String getName() { //Useful for debugging, but not necessary
            return name;
      }
      public void draw(Graphics myBuffer, int bottomEdge) {

            String file_name = "characters/" + name + ".png"; //image files follow name.png format

            ImageIcon ghost = new ImageIcon(file_name); //Create an image icon, and draw it
            myBuffer.drawImage(ghost.getImage(), (int) getX(), (int) getY(), (int) getDiameter(), (int) getDiameter(), null);
      }

      public boolean collide_with_pacman(Pacman pac) { // Test if colliding with a given pacman
            return getX() < pac.getX() + pac.getDiameter() && getX() + getDiameter() > pac.getX() && getY() < pac.getY() + pac.getDiameter() && getY() + getDiameter() > pac.getY();
      }
      // String[] directions = {"left", "right", "up"}
      // String direction = getShortestPath(directions);
      //Standard move used, somewhat random
      public void move(BufferedImage bi, int maxX, int maxY) { 
            double oldY = getY(); //Get current position and the way it was moving (try to preserve direction)
            double oldX = getX();
            String dir = current_direction;
            String[] directions = { //Possible directions
                  "left",
                  "up",
                  "down",
                  "right"
            };
            boolean not_moving = false;
            while (getX() == oldX && getY() == oldY) { //While the ghost hasn't moved (or has been prevented from moving)
                  if (dir.equals("up")) { //Move in appropriate directions (.equals() is for strings)
                        setY(getY() - 1);
                  } else if (dir.equals("down")) {
                        setY(getY() + 1);
                  } else if (dir.equals("right")) {
                        setX(getX() + 1);
                  } else if (dir.equals("left")) {
                        setX(getX() - 1);
                  }
                  if (getX() > maxX) { // Shouldn't happen depending on map, but overlap in x axis
                        setX(maxX - getX());
                  } else if (getX() < 0) {
                        setX(getX() + maxX);
                  }

                  not_moving = false;
                  /*** Wall Detection (Using colour of pixels on img) ***/
                  for (int x = (int) getX(); x <= (int)(getX() + getDiameter()); x++) {
                        for (int y = (int) getY(); y <= (int)(getY() + getDiameter()); y++) {
                              if (x < maxX && y < maxY && x >= 0 && y >= 0) {
                                    int clr = bi.getRGB(x, y); //Converts buffered img to rgb int, then converted into red, green, and blue values (0-255)
                                    int red = (clr & 0x00ff0000) >> 16;
                                    int green = (clr & 0x0000ff00) >> 8;
                                    int blue = clr & 0x000000ff;
                                    // if (bi.getRGB(x, y) > -16777216){
                                    if (blue > 247) { //Found through testing, varies based on img 
                                          setY(oldY);
                                          setX(oldX);
                                          not_moving = true; //Means the ghost hasn't moved
                                          break;
                                    }
                              }
                        }
                        if (not_moving) { //Don't want to continue testing as already found a collision
                              break;
                        }
                  }
                  if(not_moving){ dir = directions[(int)(Math.random() * directions.length)];} //If it couldn't move in that direction, randomly pick another one            }
            current_direction = dir;
            }
      }

      public void random_move(BufferedImage bi, int maxX, int maxY) { //Exact same as above, but doesn't preserve direction
            double oldY = getY();
            double oldX = getX();
            String dir = current_direction;
            String[] directions = {
                  "left",
                  "up",
                  "down",
                  "right"
            };
            boolean not_moving = false;
            while (getX() == oldX && getY() == oldY) {
                  if (dir.equals("up")) {
                        setY(getY() - 1);
                  } else if (dir.equals("down")) {
                        //System.out.println("hi");
                        setY(getY() + 1);
                  } else if (dir.equals("right")) {
                        //System.out.println("hi");
                        setX(getX() + 1);
                  } else if (dir.equals("left")) {
                        //System.out.println("hi");
                        setX(getX() - 1);
                  }
                  if (getX() > maxX) {
                        setX(maxX - getX());
                  } else if (getX() < 0) {
                        setX(getX() + maxX);
                  }
                  not_moving = false;

                  /*** Wall Detection (Using colour of pixels on img) ***/
                  for (int x = (int) getX(); x <= (int)(getX() + getDiameter()); x++) {
                        for (int y = (int) getY(); y <= (int)(getY() + getDiameter()); y++) {
                              if (x < maxX && y < maxY && x >= 0 && y >= 0) {
                                    int clr = bi.getRGB(x, y); //Converts buffered img to rgb int, then converted into red, green, and blue values (0-255)
                                    int red = (clr & 0x00ff0000) >> 16;
                                    int green = (clr & 0x0000ff00) >> 8;
                                    int blue = clr & 0x000000ff;
                                    // if (bi.getRGB(x, y) > -16777216){
                                    if (blue > 247) { //Found through testing, varies based on img 
                                          setY(oldY);
                                          setX(oldX);
                                          not_moving = true;
                                          break;
                                    }
                              }
                        }
                        if (not_moving) {
                              break;
                        }
                  }
                  dir = directions[(int)(Math.random() * directions.length)]; //Picks the next direction to be random regardless
            }
            current_direction = dir;

      }
      //Smarter move, but moves faster than pacman so almost unbeatable
      public void find_pac_move(BufferedImage bi, int pacX, int pacY, int maxX, int maxY) { 
            int oldY = (int)getY();
            int oldX = (int)getX();
            String[] preferred_directions = new String[4];
            String dir = current_direction;
            if (pacX >= getX()){ //Pick directions naively based on where pacman is (e.g. if to the left, go left)
                preferred_directions[0] = "right";
                preferred_directions[3] = "left";
            }
            else{
                 preferred_directions[0] = "left";
                preferred_directions[3] = "right"; 
            }
            if (pacY >= getY()){
                preferred_directions[1] = "down";
                preferred_directions[2] = "up";
            }
            else{
            preferred_directions[1] = "up";
                preferred_directions[2] = "down";
            }
            boolean not_moving = false;
            int direction_counter = 0;
            while (direction_counter< 4 &&(int)getX() == oldX && (int)getY() == oldY) {
                dir = preferred_directions[direction_counter];
                  if (dir.equals("up")) {
                        setY(getY() - 1);
                  } else if (dir.equals("down")) {
                        //System.out.println("hi");
                        setY(getY() + 1);
                  } else if (dir.equals("right")) {
                        //System.out.println("hi");
                        setX(getX() + 1);
                  } else if (dir.equals("left")) {
                        //System.out.println("hi");
                        setX(getX() - 1);
                  }
                  if (getX() > maxX) {
                        setX(maxX - getX());
                  } else if (getX() < 0) {
                        setX(getX() + maxX);
                  }
                  not_moving = false;

                  /*** Wall Detection (Using colour of pixels on img) ***/
                  for (int x = (int) getX(); x <= (int)(getX() + getDiameter()); x++) {
                        for (int y = (int) getY(); y <= (int)(getY() + getDiameter()); y++) {
                              if (x < maxX && y < maxY && x >= 0 && y >= 0) {
                                    int clr = bi.getRGB(x, y); //Converts buffered img to rgb int, then converted into red, green, and blue values (0-255)
                                    int red = (clr & 0x00ff0000) >> 16;
                                    int green = (clr & 0x0000ff00) >> 8;
                                    int blue = clr & 0x000000ff;
                                    // if (bi.getRGB(x, y) > -16777216){
                                    if (blue > 247) { //Found through testing, varies based on img 
                                          setY(oldY);
                                          setX(oldX);
                                          not_moving = true;
                                          break;
                                    }
                              }
                        }
                        if (not_moving) {
                              break;
                        }
                  }
                    direction_counter+=1;
            }
            current_direction = dir;

      }

}