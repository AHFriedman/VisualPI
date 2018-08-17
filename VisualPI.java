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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class VisualPI extends JPanel implements MouseListener, ActionListener, KeyListener, Runnable
{
   //https://en.wikipedia.org/wiki/Wireworld
   //x^2 + y^2 <= 1

   private static VisualPIDriver driver;
   private static ArrayList<Point2D.Double> InCircle;
   private static ArrayList<Point2D.Double> outOfCircle;
   private static ArrayList<Point2D.Double> circleCashe;
   private static ArrayList<Point2D.Double> outCashe;
   private static long startTime;
   private static float scale = 500;
   @SuppressWarnings("unused")
private boolean currentlyUpdating = false;
   @SuppressWarnings("unused")
private boolean haltUpdate = false;
   int ExcessiveNumberOfThreads = 100;
   double bestValue = 0;
   double bestPercent = 0;
   public VisualPI()
   {
      addMouseListener( this );
      this.setFocusable(true);
      this.requestFocus(); 
      this.addKeyListener(this);
      InCircle = new ArrayList<Point2D.Double>();
      outOfCircle = new ArrayList<Point2D.Double>();
      circleCashe = new ArrayList<Point2D.Double>();
      outCashe = new ArrayList<Point2D.Double>();
      startTime = System.currentTimeMillis();
      startThreads(ExcessiveNumberOfThreads);
   }
   
   public double percentDifference(double d, double pi)
   {
	   if(d > pi)
	   {
		   double ans = (d/pi)*100;
		   ans = ans % 10;
		   return 100-ans;
		   //return ((d)/pi) * 100;
	   }
	   else
		   return (((d)/pi)) * 100;
   }
   public void startThreads(int count)
   {
	   Thread[] threads = new Thread[count];
	   for(int i = 0; i < threads.length; i++)
	   {
		   threads[i] = new Thread(this);
		   threads[i].start();
	   }
   }
   public static void main(String[] args)
   {
      driver = new VisualPIDriver();
      driver.main(args);
      
   }
   
   public void updateWorld()
   {
      
      
       
   }
   
   public void paintComponent(Graphics g)
   {
	   //if(haltUpdate)
		   //return;
	   loadCashe();
	   currentlyUpdating = true;
	   startThreads(ExcessiveNumberOfThreads);
	   scale = getWidth()/4;
	  addPoint();
      g.setColor(new Color(220,220,220));
      g.fillRect(0,0,getWidth(),getHeight());
      g.setColor(Color.black);
      g.drawLine(10, 10, 10 , 10 + (int)scale);
      g.drawLine(10, 10 + (int) scale, 10 + (int)scale, 10 + (int) scale);
      
      g.setColor(Color.blue);
      for(int i = 0; i < InCircle.size(); i++)
      {
    	  Point2D.Double temp = InCircle.get(i);
    	  //System.out.println("temp: " + temp);
    	  if(temp == null)
    	  {
    		  continue;
    	  }
    	  double x = temp.x * scale;
    	  double y = temp.y * scale;
    	  g.drawLine((int)x + 10, (int)y+10, (int)x + 10, (int)y + 10); 
      }
      g.setColor(Color.red);
      for(int i = 0; i < outOfCircle.size(); i++)
      {
    	  Point2D.Double temp = outOfCircle.get(i);
    	  if(temp == null)
    		  continue;
    	  double x = temp.x * scale;
    	  double y = temp.y * scale;
    	  g.drawLine((int)x + 10, (int)y+10, (int)x + 10, (int)y + 10); 
      }
      double total = InCircle.size() + outOfCircle.size();
      double in = InCircle.size();
      g.setColor(Color.black);
      g.setFont(new Font("TimesRoman", Font.PLAIN, 14)); 
      g.drawString("Total/circle: " + (in/total)*4, 20 + (int)scale, 20);
      g.drawString("Sample Size: " + total, 20 + (int)scale, 40);
      long startSeconds = startTime/1000;
      long currSeconds = System.currentTimeMillis()/1000;
      g.drawString("Run time: " + (currSeconds-startSeconds), 20 + (int)scale, 60);
      currentlyUpdating = false;
      if(percentDifference((in/total)*4, Math.PI) >= bestPercent)
      {
    	  bestValue = (in/total)*4;
    	  bestPercent = percentDifference(bestValue, Math.PI);
      }
      String tempVal = "" + bestPercent;
      //tempVal.substring(0, 5);
      tempVal = tempVal + "%";
      g.drawString("Best Value: " + bestValue, 20 + (int)scale, 80);
      g.drawString("Best Percent: " + tempVal, 20 + (int)scale, 100);
      g.drawString("Curr Diff: " + percentDifference((in/total)*4, Math.PI),20 + (int)scale, 120);
   }
public void addPoint()
   {
	   double x = Math.random();
	   double y = Math.random();
	   Point2D.Double temp = new Point2D.Double(x,y);
	   double x2 = Math.pow(x, 2);
	   double y2 = Math.pow(y, 2);
	   if(x2+y2 <= 1)
		   circleCashe.add(temp);
	   else
		   outCashe.add(temp);
	   
   }
public void loadCashe()
   {
  		cloneAL(); 
   }
   
   public void cloneAL()
   {
	   //haltUpdate = true;
	   InCircle.clear();
	   outOfCircle.clear();
	   for(int i = 0; i < outCashe.size(); i++)
	   {
		   outOfCircle.add(outCashe.get(i));
	   }
	   for(int i = 0; i < circleCashe.size(); i++)
	   {
		   InCircle.add(circleCashe.get(i));
	   }
	   haltUpdate = false;
   }
@Override
public void keyPressed(KeyEvent arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void keyReleased(KeyEvent e) {
	// TODO Auto-generated method stub
	
}
@Override
public void keyTyped(KeyEvent e) {
	// TODO Auto-generated method stub
	
}
@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	
}
@Override
public void mouseClicked(MouseEvent e) {
	// TODO Auto-generated method stub
	
}
@Override
public void mouseEntered(MouseEvent e) {
	// TODO Auto-generated method stub
	
}
@Override
public void mouseExited(MouseEvent e) {
	// TODO Auto-generated method stub
	
}
@Override
public void mousePressed(MouseEvent e) {
	// TODO Auto-generated method stub
	
}
@Override
public void mouseReleased(MouseEvent e) {
	// TODO Auto-generated method stub
	
}
@Override
public void run() {
	//for(int i = 0; i < 50; i++)
	//if(!haltUpdate)
		addPoint();
	//	addPoint();
	
}

public void processUserInput(int keyCode) {
	//System.out.println(keyCode);
	if(keyCode == KeyEvent.VK_P)
		scale += 25;
	if(keyCode == KeyEvent.VK_O)
		scale -=25;
		
	
}
   
}