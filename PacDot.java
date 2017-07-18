import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
 
    
public class PacDot extends Polkadot
{
   private boolean visible = true;  
          // constructors
   public PacDot()     //default constructor (not technically needed)
   {
      super();
   }

   public PacDot(double x, double y) //Constructur actually utilised
   {
      super(x, y, 5, Color.WHITE);
   
   }
   public void draw(Graphics myBuffer, int bottomEdge) 
   {
       if (visible){ //Visible is a proxy for whether the dot has been 'eaten'
         super.draw(myBuffer, bottomEdge); //Call polkadot's draw (as there's no reason to rewrite)
      }
   }
      
   public boolean collide_with_pacman(Polkadot pac){ //Test if collided with pacman
      if (visible){
         boolean collided =  getX() < pac.getX() + pac.getDiameter() && getX() + getDiameter() > pac.getX() && getY() < pac.getY() + pac.getDiameter() && getY() + getDiameter() > pac.getY();
         if(collided){ //Is 'eaten' if collided, so set visible to false
            visible = false;
         }
         return collided; //Regardless, return the result
      } //If it's not visible, it can't collide
      return false;
   }
   
   public boolean collide_with_wall(BufferedImage bi, int maxX, int maxY){ //Test if collided with a wall (used in creation)
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
      
}