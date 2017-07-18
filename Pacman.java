import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
 
public class Pacman extends Polkadot
{
   private String direction = "left"; //By default, face left
   
       // constructors
   public Pacman()     //default constructor
   {
      super();
   }
   public Pacman(double x, double y, double d, Color c)
   {
      super(x, y, d, c);
   
   }
   public String getDirection(){
      return direction;
   }
   public void draw(Graphics myBuffer, int bottomEdge) 
   {
      
      String file_name = "pacman_" + direction+".png"; //Filenames are in format pacman_direction.png (e.g. pacman_left.png)
      
      ImageIcon pacman = new ImageIcon(file_name);
      myBuffer.drawImage(pacman.getImage(), (int)getX(), (int)getY(), (int)getDiameter(), (int)getDiameter(), null); 
   }

   
   public void move(String dir, BufferedImage bi, int maxX, int maxY){ //Move pacman in appropriate direction
      double oldY = getY();
      double oldX = getX();
      direction = dir;
      if (dir.equals("up")){
         setY(getY() - 5);
      }
      else if (dir.equals("down")){
            //System.out.println("hi");
         setY(getY() + 5);
      }
      else if (dir.equals("right")){
            //System.out.println("hi");
         setX(getX() + 5);
      }
      else if (dir.equals("left")){
            //System.out.println("hi");
         setX(getX() - 5);
      }
      if (getX() > maxX){
         setX(maxX - getX());
      }
      else if (getX() < 0){
         setX(getX() + maxX);
      }
      
   
      /*** Wall Detection (Using colour of pixels on img) ***/
      for (int x = (int)getX(); x<= (int)(getX()+getDiameter()); x++){
         for (int y = (int)getY(); y<= (int)(getY()+getDiameter()); y++){
            if (x < maxX && y < maxY && x >= 0 && y>= 0){
               int clr=  bi.getRGB(x,y);  //Converts buffered img to rgb int, then converted into red, green, and blue values (0-255)
               int  red   = (clr & 0x00ff0000) >> 16;
               int  green = (clr & 0x0000ff00) >> 8;
               int  blue  =  clr & 0x000000ff;
               // if (bi.getRGB(x, y) > -16777216){
               if (blue > 247){ //Found through testing, varies based on img 
                  setY(oldY);
                  setX(oldX);
                  break;
               }
            }
         
         }
      }
   
      
   }
   
      
}