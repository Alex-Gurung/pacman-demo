import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class Driver
{
    public static void main(String[] args)
   { 
      JFrame frame = new JFrame("Pacman Demo");
      frame.setSize(808, 838);   
      frame.setLocation(0, 0);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      DemoPanel p = new DemoPanel();
      frame.setContentPane(p);
      p.requestFocus();
      frame.setVisible(true); 
   }
}
