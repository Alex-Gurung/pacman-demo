import java.awt.Color;
import java.awt.Graphics;





public class Bumper
{
  private int myX;
  private int myY;
  private int myXWidth;
  private int myYWidth;
  private Color myColor;
  
  public Bumper()
  {
    myX = 200;
    myY = 200;
    myXWidth = 25;
    myYWidth = 50;
    myColor = Color.BLUE;
  }
  
  public Bumper(int x, int y, int xWidth, int yWidth, Color c) {
    myX = x;
    myY = y;
    myXWidth = xWidth;
    myYWidth = yWidth;
    myColor = c;
  }
  

  public int getX()
  {
    return myX;
  }
  
  public int getY() {
    return myY;
  }
  
  public int getXWidth() {
    return myXWidth;
  }
  
  public int getYWidth() {
    return myYWidth;
  }
  
  public Color getColor() {
    return myColor;
  }
  

  public void setX(int x)
  {
    myX = x;
  }
  
  public void setY(int y) {
    myY = y;
  }
  
  public void setXWidth(int xWidth) {
    myXWidth = xWidth;
  }
  
  public void setYWidth(int yWidth) {
    myYWidth = yWidth;
  }
  
  public void setColor(Color c) {
    myColor = c;
  }
  
  
  public void draw(Graphics myBuffer)
  {
    myBuffer.setColor(getColor());
    myBuffer.fillRect(getX(), getY(), getXWidth(), getYWidth());
  }
  

  public boolean inBumper(Polkadot dot)
  {
    for (int x = getX(); x <= getX() + getXWidth(); x++)
      for (int y = getY(); y <= getY() + getYWidth(); y++)
        if (distance(x, y, dot.getX(), dot.getY()) <= dot.getRadius())
          return true;
    return false;
  }
  

  private double distance(double x1, double y1, double x2, double y2)
  {
    return Math.sqrt(Math.pow(x1 - x2, 2.0D) + Math.pow(y1 - y2, 2.0D));
  }
}