import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class DemoPanel extends JPanel {
  private static final int FRAME1 = 224; //X and Y frame sizes, found from background img dimensions
  private static final int FRAME2 = 256;

  private BufferedImage myImage;
  private BufferedImage image = null; //Background img
  private BufferedImage comp_image = null; //Img for ghost collisions
  private BufferedImage dots_image = null; //Img to determine dot placement
  private Graphics myBuffer;
  private Pacman pacman;

  private Ghost clyde;
  private Ghost blinky;
  private Ghost inky;
  private Ghost pinky;

  private final Ghost[] ghosts = new Ghost[4]; //Arrays make for easier iteration
  private PacDot[] pacdots = new PacDot[192];
  private int score = 0;
  private boolean game_ended = false; //Game condition test
  private boolean you_won = false;



  private Timer t;
  //constructor   
  public DemoPanel() {
    myImage = new BufferedImage(FRAME1, FRAME2, BufferedImage.TYPE_INT_RGB);
    myBuffer = myImage.getGraphics();
    try { //Tries to find the images and instantiate the objects, the exception is in case the files aren't found
      image = ImageIO.read(new File("pacman_map.png"));
      comp_image = ImageIO.read(new File("pacman_new_ghost_map.png"));
      dots_image = ImageIO.read(new File("pacman_map_dots_3.png"));
    } catch (IOException i) {
      System.out.println("couldn't get map");
    }

    addKeyListener(new keysListener()); //These 2 lines setup keyboard input
    setFocusable(true);

    resetGame(); //Deal with the rest of setup, resetGame() can be called to restart game

  }
  public void resetGame() { //Method to instantiate objects and generally set up game 
    pacman = new Pacman(FRAME1 / 2 - 5, FRAME2 / 2 + 15, 10, Color.BLACK);

    //GHOSTS
    // clyde = new Ghost(FRAME1 / 2 -20, FRAME2 / 2 -10, 10, Color.BLACK, "clyde"); //Inside of box
    // inky = new Ghost(FRAME1 / 2 - 5, FRAME2 / 2-10, 10, Color.BLACK, "inky");
    // pinky = new Ghost(FRAME1 / 2+10, FRAME2 / 2-10, 10, Color.BLACK, "pinky");
    clyde = new Ghost(FRAME1 / 2 - 20, FRAME2 / 2 - 33, 10, Color.BLACK, "clyde"); //Inside of box
    inky = new Ghost(FRAME1 / 2 - 5, FRAME2 / 2 - 33, 10, Color.BLACK, "inky");
    pinky = new Ghost(FRAME1 / 2 + 10, FRAME2 / 2 - 33, 10, Color.BLACK, "pinky");
    //Depending on algorithm, the commented clyde, inky, and pinky statements can be used for more traditional setup

    blinky = new Ghost(FRAME1 / 2 - 5, FRAME2 / 2 - 33, 10, Color.BLACK, "blinky"); //Outside of box

    ghosts[0] = clyde;
    ghosts[1] = inky;
    ghosts[2] = pinky;
    ghosts[3] = blinky;
    score = 0;
    game_ended = false;
    setUpDots(); //Create dot objects
    t = new Timer(15, new Listener()); //Create a new one
    t.start(); //Start it
  }

  public void setUpDots() { //Set up pac-dots
    int count = 0; //Count used to know where in array we are
    for (int y = 5; y < FRAME2; y += 10) { //Iterate over background image, jumping by 10 to minimise overlap&duplication
      for (int x = 5; x < FRAME1; x += 10) {
        if (count < pacdots.length) { //Make sure we haven't exceeded cap
          pacdots[count] = new PacDot(x + 5, y + 5); //Create a new PacDot
          if (!pacdots[count].collide_with_wall(dots_image, FRAME1, FRAME2)) { //Test if it collides with a wall
            boolean already_there = false;
            for (int i = 0; i < count; i++) { //See if a pacdot is already in the area
              if (pacdots[i] != null) {
                if (pacdots[count].collide_with_pacman(pacdots[i])) {
                  already_there = true;
                  break;
                }
              }
            }
            if (!already_there) { //If not, increase count (if count isn't increased the index will be overwritten)
              count++;
            }
          }

        }
      }
    }

  }

  public void paintComponent(Graphics g) {
    g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
  }

  private class Listener implements ActionListener { //Called with the Timer
    public void actionPerformed(ActionEvent e) {
      if (score == 192) { //Maximum score
        game_ended = true;
        you_won = true;
        t.stop();
      } else {
        for (int i = 0; i < ghosts.length; i++) { //Test if any of the ghosts collide with pacman
          if (ghosts[i].collide_with_pacman(pacman)) { //If so, end the game and stop the timer
            game_ended = true;
            t.stop();
            break;
          }
        }
      }

      if (!game_ended) { //If the game hasn't been ended
        if (image != null) { //Make sure the background image isn't null (it shouldn't ever be if the file is there)
          myBuffer.drawImage(image, 0, 0, (int) FRAME1, (int) FRAME2, null);
        }
        for (int i = 0; i < ghosts.length; i++) { //Move all of the ghosts (move method in ghost class)
          ghosts[i].move(comp_image, FRAME1, FRAME2);
        }
        for (int i = 0; i < pacdots.length; i++) { //Draw all of the pac-dots, and check for collisions with pacman
          if (pacdots[i] != null) {
            boolean collided = pacdots[i].collide_with_pacman(pacman);
            if (collided) {
              score++;
            }
            pacdots[i].draw(myBuffer, FRAME2);
          }
        }
        pacman.draw(myBuffer, FRAME2); //Draw pacman and the ghosts

        clyde.draw(myBuffer, FRAME2);
        blinky.draw(myBuffer, FRAME2);
        inky.draw(myBuffer, FRAME2);
        pinky.draw(myBuffer, FRAME2);



        myBuffer.setFont(new Font("Lucida Bright", Font.PLAIN, 9)); //Display some basic information
        myBuffer.setColor(Color.WHITE);
        myBuffer.drawString("P a c m a n", 10, 19); //Letter spacing is for legibility
        myBuffer.drawString("S c o r e : " + score, (int) FRAME1 / 2 + 10, 19);

      } else { //The game has been ended, display some information (like score and how to replay)
        myBuffer.setColor(Color.BLACK);
        myBuffer.clearRect(0, 0, FRAME1, FRAME2);
        myBuffer.setFont(new Font("Lucida Bright", Font.PLAIN, 10));
        myBuffer.setColor(Color.WHITE);
        if (you_won) {
          myBuffer.drawString("Y o u  W o n ! ! ! ", 10, 50); //Letter spacing is for legibility
        } else {
          myBuffer.drawString("Y o u  L o s t :( ", 10, 50); //Letter spacing is for legibility
        }
        myBuffer.drawString("S c o r e : " + score, (int) FRAME1 / 2 + 10, 50);
        myBuffer.drawString("T o  R e p l a y , p r e s s  S p a c e", (int) FRAME1 / 6, FRAME2 / 2);
      }
      repaint();
    }
  }

  private class keysListener extends KeyAdapter { //When a key is pressed

    public void keyPressed(KeyEvent e) {
      int key_code = e.getKeyCode(); //Get key code (makes rest of code more readable)
      if (!game_ended) {
        if (key_code == KeyEvent.VK_UP) { //If up arrow, move pacman up (just changes their coord)
          pacman.move("up", image, FRAME1, FRAME2);
        } else if (key_code == KeyEvent.VK_DOWN) { //If down arrow, move pacman down
          pacman.move("down", image, FRAME1, FRAME2);
        } else if (key_code == KeyEvent.VK_RIGHT) { //etc.
          pacman.move("right", image, FRAME1, FRAME2);
        } else if (key_code == KeyEvent.VK_LEFT) { //etc.
          pacman.move("left", image, FRAME1, FRAME2);
        }
      }
      if (game_ended && key_code == KeyEvent.VK_SPACE) { //If the game has ended, and spacebar has been pressed, reset everything
        resetGame();
      }
    }

  }

}