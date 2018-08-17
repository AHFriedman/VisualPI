/*
MIT License

Copyright (c) 2018 AHFriedman

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

import javax.swing.JFrame;
import java.util.TimerTask;
import java.util.Timer;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class VisualPIDriver extends TimerTask
{
   static VisualPI panel = new VisualPI();
   static JFrame frame;
   public void main(String[] args)
   {
      frame = new JFrame("Visual PI");
      frame.setSize(400,400);
      frame.setLocation(100,50);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(panel);
      frame.setVisible(true);
      frame.setFocusable(true);
      frame.setFocusable(true);
      Timer timer = new Timer();
      timer.schedule(this, 0, 0001);//0250);
      panel.setLayout(null);
      frame.addKeyListener(new listen());
   
   }
   public JFrame getFrame()
   {
      return frame;
   }
   public void run()
   {  
      //frame.requestFocusInWindow();
      panel.repaint();
      //panel.updateWorld();
   }
   
   public static class listen implements KeyListener 
   {
      
      public void keyTyped(KeyEvent e)
      {
      }
         
      public void keyPressed(KeyEvent e)
      {  
      }
      
      public void keyReleased(KeyEvent e)
      {
         panel.processUserInput(e.getKeyCode());
      }
   }

}