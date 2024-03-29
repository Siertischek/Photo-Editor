/*
 *Hunter Lloyd
 * Copyrite.......I wrote, ask permission if you want to use it outside of class. 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;
import java.util.prefs.Preferences;

class IMP implements MouseListener{
   JFrame frame;
   JPanel mp;
   JButton start;
   MyPanel redPanel;
   MyPanel greenPanel;
   MyPanel bluePanel;
   JScrollPane scroll;
   JMenuItem openItem, exitItem, resetItem;
   Toolkit toolkit;
   File pic;
   ImageIcon img;
   int colorX, colorY;
   int [] pixels;
   int [] results;
   //Instance Fields you will be using below
   
   //This will be your height and width of your 2d array
   int height=0, width=0;
   
   //your 2D array of pixels
    int picture[][];

    /* 
     * In the Constructor I set up the GUI, the frame the menus. The open pulldown 
     * menu is how you will open an image to manipulate. 
     */
   IMP()
   {
      toolkit = Toolkit.getDefaultToolkit();
      frame = new JFrame("Image Processing Software by Hunter");
      JMenuBar bar = new JMenuBar();
      JMenu file = new JMenu("File");
      JMenu functions = getFunctions();
      frame.addWindowListener(new WindowAdapter(){
            @Override
              public void windowClosing(WindowEvent ev){quit();}
            });
      openItem = new JMenuItem("Open");
      openItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ handleOpen(); }
           });
      resetItem = new JMenuItem("Reset");
      resetItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ reset(); }
           });     
      exitItem = new JMenuItem("Exit");
      exitItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ quit(); }
           });
      file.add(openItem);
      file.add(resetItem);
      file.add(exitItem);
      bar.add(file);
      bar.add(functions);
      frame.setSize(600, 600);
      mp = new JPanel();
      mp.setBackground(new Color(0, 0, 0));
      scroll = new JScrollPane(mp);
      frame.getContentPane().add(scroll, BorderLayout.CENTER);
      JPanel butPanel = new JPanel();
      butPanel.setBackground(Color.black);
      start = new JButton("start");
      start.setEnabled(false);
      start.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ 
            redPanel.drawing();
            greenPanel.drawing();
            bluePanel.drawing();
           }
           });
      butPanel.add(start);
      frame.getContentPane().add(butPanel, BorderLayout.SOUTH);
      frame.setJMenuBar(bar);
      frame.setVisible(true);      
   }
   
   /* 
    * This method creates the pulldown menu and sets up listeners to selection of the menu choices. If the listeners are activated they call the methods 
    * for handling the choice, fun1, fun2, fun3, fun4, etc. etc. 
    */
   
  private JMenu getFunctions()
  {
     JMenu fun = new JMenu("Functions");
     
     JMenuItem firstItem = new JMenuItem("MyExample - fun1 method");
    
     firstItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){fun1();}
           });

      JMenuItem rotateItem = new JMenuItem("Rotate 90");
    
     rotateItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){rotate();}
           });

      JMenuItem GSItem = new JMenuItem("GreyScale");
    
     GSItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){greyscale();}
           });
   
      JMenuItem BlurItem = new JMenuItem("Blur");
    
     BlurItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){blur();}
           });

      JMenuItem EdgeItem = new JMenuItem("Edge");
    
     EdgeItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){edge();}
           });

      JMenuItem HistoItem = new JMenuItem("Histogram");
    
     HistoItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){histogram();}
           });

      JMenuItem EqualItem = new JMenuItem("Equalize");
    
     EqualItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){equalization();}
           });
           
   
           
      fun.add(firstItem);
      fun.add(rotateItem);
      fun.add(GSItem);
      fun.add(BlurItem);
      fun.add(EdgeItem);
      fun.add(HistoItem);
      fun.add(EqualItem);

     
      return fun;   

  }
  
  /*
   * This method handles opening an image file, breaking down the picture to a one-dimensional array and then drawing the image on the frame. 
   * You don't need to worry about this method. 
   */
    private void handleOpen()
  {  
     img = new ImageIcon();
     JFileChooser chooser = new JFileChooser();
      Preferences pref = Preferences.userNodeForPackage(IMP.class);
      String path = pref.get("DEFAULT_PATH", "");

      chooser.setCurrentDirectory(new File(path));
     int option = chooser.showOpenDialog(frame);
     
     if(option == JFileChooser.APPROVE_OPTION) {
        pic = chooser.getSelectedFile();
        pref.put("DEFAULT_PATH", pic.getAbsolutePath());
       img = new ImageIcon(pic.getPath());
      }
     width = img.getIconWidth();
     height = img.getIconHeight(); 
     
     JLabel label = new JLabel(img);
     label.addMouseListener(this);
     pixels = new int[width*height];
     
     results = new int[width*height];
  
          
     Image image = img.getImage();
        
     PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width );
     try{
         pg.grabPixels();
     }catch(InterruptedException e)
       {
          System.err.println("Interrupted waiting for pixels");
          return;
       }
     for(int i = 0; i<width*height; i++)
        results[i] = pixels[i];  
     turnTwoDimensional();
     mp.removeAll();
     mp.add(label);
     
     mp.revalidate();
  }
  
  /*
   * The libraries in Java give a one dimensional array of RGB values for an image, I thought a 2-Dimensional array would be more usefull to you
   * So this method changes the one dimensional array to a two-dimensional. 
   */
  private void turnTwoDimensional()
  {
     picture = new int[height][width];
     for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
          picture[i][j] = pixels[i*width+j];
      
     
  }
  /*
   *  This method takes the picture back to the original picture
   */
  private void reset()
  {
        for(int i = 0; i<width*height; i++)
             pixels[i] = results[i]; 
       Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width)); 
       turnTwoDimensional();
       resetPicture();

      JLabel label2 = new JLabel(new ImageIcon(img2));    
       mp.removeAll();
       mp.add(label2);
     
       mp.revalidate(); 
    }
  /*
   * This method is called to redraw the screen with the new image. 
   */
  private void resetPicture()
  {
       for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
          pixels[i*width+j] = picture[i][j];
      Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width)); 

      JLabel label2 = new JLabel(new ImageIcon(img2));    
       mp.removeAll();
       mp.add(label2);
     
       mp.revalidate(); 
   
    }
    /*
     * This method takes a single integer value and breaks it down doing bit manipulation to 4 individual int values for A, R, G, and B values
     */
  private int [] getPixelArray(int pixel)
  {
      int temp[] = new int[4];
      temp[0] = (pixel >> 24) & 0xff;
      temp[1]   = (pixel >> 16) & 0xff;
      temp[2] = (pixel >>  8) & 0xff;
      temp[3]  = (pixel      ) & 0xff;
      return temp;
      
    }
    /*
     * This method takes an array of size 4 and combines the first 8 bits of each to create one integer. 
     */
  private int getPixels(int rgb[])
  {
         int alpha = 0;
         int rgba = (rgb[0] << 24) | (rgb[1] <<16) | (rgb[2] << 8) | rgb[3];
        return rgba;
  }
  
  public void getValue()
  {
      int pix = picture[colorY][colorX];
      int temp[] = getPixelArray(pix);
      System.out.println("Color value " + temp[0] + " " + temp[1] + " "+ temp[2] + " " + temp[3]);
    }
  
  /**************************************************************************************************
   * This is where you will put your methods. Every method below is called when the corresponding pulldown menu is 
   * used. As long as you have a picture open first the when your fun1, fun2, fun....etc method is called you will 
   * have a 2D array called picture that is holding each pixel from your picture. 
   *************************************************************************************************/
   /*
    * Example function that just removes all red values from the picture. 
    * Each pixel value in picture[i][j] holds an integer value. You need to send that pixel to getPixelArray the method which will return a 4 element array 
    * that holds A,R,G,B values. Ignore [0], that's the Alpha channel which is transparency, we won't be using that, but you can on your own.
    * getPixelArray will breaks down your single int to 4 ints so you can manipulate the values for each level of R, G, B. 
    * After you make changes and do your calculations to your pixel values the getPixels method will put the 4 values in your ARGB array back into a single
    * integer value so you can give it back to the program and display the new picture. 
    */
  private void fun1()
  {
     
    for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
       {   
          int rgbArray[] = new int[4];
         
          //get three ints for R, G and B
          rgbArray = getPixelArray(picture[i][j]);
         
        
           rgbArray[1] = 0;
           //take three ints for R, G, B and put them back into a single int
           picture[i][j] = getPixels(rgbArray);
        } 
     resetPicture();
  }

  private void rotate()
  {
      int rotatedpic[][];
      rotatedpic = new int[width][height];
      for(int i=0; i<height; i++)
         for(int j=0; j<width; j++)
         {
            rotatedpic[j][i] = picture[height-i-1][j];

         }
      int temp = width;
      width = height;
      height = temp;
      picture = rotatedpic;
      resetPicture();
      height = width;
      width = temp;
  }

  private void greyscale()
  {
      for(int i=0; i<height; i++)
         for(int j=0; j<width; j++)
         {   
          int rgbArray[] = new int[4];
         
          //get three ints for R, G and B
          rgbArray = getPixelArray(picture[i][j]);

            rgbArray[1] = (int)((rgbArray[1] * 0.21)+(rgbArray[2] * 0.72)+(rgbArray[3] * 0.07));
            rgbArray[2] = (int)((rgbArray[1] * 0.21)+(rgbArray[2] * 0.72)+(rgbArray[3] * 0.07));
            rgbArray[3] = (int)((rgbArray[1] * 0.21)+(rgbArray[2] * 0.72)+(rgbArray[3] * 0.07));

            picture[i][j] = getPixels(rgbArray);
         }
      resetPicture();
  }

  private void blur()
  {
      greyscale();

      int blurpic[][] = new int[height][width];
      for(int i=0; i<height; i++)
         for(int j=0; j<width; j++)
         {
            if((height-1)>i && i>0)
            {
               if((width-1)>j && j>0)
               {
                  int blurred[] = new int[4];
                  int rgbArray[] = new int[4];
         
                  rgbArray = getPixelArray(picture[i-1][j-1]);
                  blurred[0] = rgbArray[0];
                  blurred[1] = blurred[1] + rgbArray[1];
                  blurred[2] = blurred[2] + rgbArray[2];
                  blurred[3] = blurred[3] + rgbArray[3];
                  rgbArray = getPixelArray(picture[i-1][j]);
                  blurred[1] = blurred[1] + rgbArray[1];
                  blurred[2] = blurred[2] + rgbArray[2];
                  blurred[3] = blurred[3] + rgbArray[3];
                  rgbArray = getPixelArray(picture[i-1][j+1]);
                  blurred[1] = blurred[1] + rgbArray[1];
                  blurred[2] = blurred[2] + rgbArray[2];
                  blurred[3] = blurred[3] + rgbArray[3];
                  rgbArray = getPixelArray(picture[i][j-1]);
                  blurred[1] = blurred[1] + rgbArray[1];
                  blurred[2] = blurred[2] + rgbArray[2];
                  blurred[3] = blurred[3] + rgbArray[3];
                  rgbArray = getPixelArray(picture[i][j+1]);
                  blurred[1] = blurred[1] + rgbArray[1];
                  blurred[2] = blurred[2] + rgbArray[2];
                  blurred[3] = blurred[3] + rgbArray[3];
                  rgbArray = getPixelArray(picture[i+1][j-1]);
                  blurred[1] = blurred[1] + rgbArray[1];
                  blurred[2] = blurred[2] + rgbArray[2];
                  blurred[3] = blurred[3] + rgbArray[3];
                  rgbArray = getPixelArray(picture[i+1][j]);
                  blurred[1] = blurred[1] + rgbArray[1];
                  blurred[2] = blurred[2] + rgbArray[2];
                  blurred[3] = blurred[3] + rgbArray[3];
                  rgbArray = getPixelArray(picture[i+1][j+1]);
                  blurred[1] = blurred[1] + rgbArray[1];
                  blurred[2] = blurred[2] + rgbArray[2];
                  blurred[3] = blurred[3] + rgbArray[3];

                  blurred[1] = blurred[1]/8;
                  blurred[2] = blurred[2]/8;
                  blurred[3] = blurred[3]/8;

                  blurpic[i][j] = getPixels(blurred);
               }
            }
         }

      for(int i=0; i<height; i++)
         for(int j=0; j<width; j++)
         {
            picture[i][j] = blurpic[i][j];
         }

      resetPicture();
      
  }

  private void edge()
  {
      greyscale();

      int bw[][] = new int[height][width];
      for(int i=0; i<height; i++)
         for(int j=0; j<width; j++)
         {
            int mask = 0;
            if((height-2)>i && i>1)
            {
               if((width-2)>j && j>1)                           
               {
                  mask = mask + picture[i-2][j-2]*-1;
                  mask = mask + picture[i-2][j-1]*-1;
                  mask = mask + picture[i-2][j]*-1;
                  mask = mask + picture[i-2][j+1]*-1;
                  mask = mask + picture[i-2][j+2]*-1;

                  mask = mask + picture[i-1][j-2]*-1;
                  mask = mask + picture[i-1][j-1];
                  mask = mask + picture[i-1][j];
                  mask = mask + picture[i-1][j+1];
                  mask = mask + picture[i-1][j+2]*-1;

                  mask = mask + picture[i][j-2]*-1;
                  mask = mask + picture[i][j-1];
                  mask = mask + picture[i][j]*16;
                  mask = mask + picture[i][j+1];
                  mask = mask + picture[i][j+2]*-1;

                  mask = mask + picture[i+1][j-2]*-1;
                  mask = mask + picture[i+1][j-1];
                  mask = mask + picture[i+1][j];
                  mask = mask + picture[i+1][j+1];
                  mask = mask + picture[i+1][j+2]*-1;

                  mask = mask + picture[i+2][j-2]*-1;
                  mask = mask + picture[i+2][j-1]*-1;
                  mask = mask + picture[i+2][j]*-1;
                  mask = mask + picture[i+2][j+1]*-1;
                  mask = mask + picture[i+2][j+2]*-1;

                  
               }
            }


            if(mask > 25)
            {
               int white[] = {255,255,255,255};
               bw[i][j] = getPixels(white);
            }
            else
            {
               int black[] = {0,0,0,0};
               bw[i][j] = getPixels(black);
            }
         }

      for(int i=0; i<height; i++)
         for(int j=0; j<width; j++)
         {
            picture[i][j] = bw[i][j];
         }

      resetPicture();
  }
  
  private void histogram()
  {
      int red[] = new int[256];
      int green[] = new int[256];
      int blue[] = new int[256];
      int max = 0;
      for(int i=0; i<height; i++)
         for(int j=0; j<width; j++)
         {
            int rgbArray[] = new int[4];
         
            rgbArray = getPixelArray(picture[i][j]);

            red[rgbArray[1]]+=1;
            green[rgbArray[2]]+=1;
            blue[rgbArray[3]]+=1;
         }
      
      for(int c = 0; c < 255; c++)
      {
         if(red[c] > max)
         {
            max = red[c];
         }
         if(green[c] > max)
         {
            max = green[c];
         }
         if(blue[c] > max)
         {
            max = blue[c];
         }
      }
      JFrame redFrame = new JFrame("Red");
      redFrame.setSize(305, 600);
      redFrame.setLocation(800, 0);
      JFrame greenFrame = new JFrame("Green");
      greenFrame.setSize(305, 600);
      greenFrame.setLocation(1150, 0);
      JFrame blueFrame = new JFrame("blue");
      blueFrame.setSize(305, 600);
      blueFrame.setLocation(1450, 0);
      redPanel = new MyPanel(red,max);
      greenPanel = new MyPanel(green,max);
      bluePanel = new MyPanel(blue,max);
      redFrame.getContentPane().add(redPanel, BorderLayout.CENTER);
      redFrame.setVisible(true);
      greenFrame.getContentPane().add(greenPanel, BorderLayout.CENTER);
      greenFrame.setVisible(true);
      blueFrame.getContentPane().add(bluePanel, BorderLayout.CENTER);
      blueFrame.setVisible(true);
         start.setEnabled(true);
  }

  private void equalization()
  {
      int red[] = new int[256];
      int green[] = new int[256];
      int blue[] = new int[256];
      int redsum = 0;
      int greensum = 0;
      int bluesum = 0;
      for(int i=0; i<height; i++)
         for(int j=0; j<width; j++)
         {
            int rgbArray[] = new int[4];
         
            rgbArray = getPixelArray(picture[i][j]);

            red[rgbArray[1]]+=1;
            green[rgbArray[2]]+=1;
            blue[rgbArray[3]]+=1;
         }

      for(int i=0; i<height; i++)
         for(int j=0; j<width; j++)
         {
            int rgbArray[] = new int[4];
         
            rgbArray = getPixelArray(picture[i][j]);

            redsum = 0;
            greensum = 0;
            bluesum = 0;

            for(int c = 0; c < rgbArray[1]; c++)
            {
               redsum = redsum + red[c];
            }
            for(int c = 0; c < rgbArray[2]; c++)
            {
               greensum = greensum + green[c];
            }
            for(int c = 0; c < rgbArray[3]; c++)
            {
               bluesum = bluesum + blue[c];
            }

            int colors[] = {rgbArray[0],Math.round(((255*redsum-1)/((height*width)-1))),Math.round(((255*greensum-1)/((height*width)-1))),Math.round(((255*bluesum-1)/((height*width)-1)))};

            picture[i][j] = getPixels(colors);
         }

      resetPicture();
  }
  
  
  private void quit()
  {  
     System.exit(0);
  }

    @Override
   public void mouseEntered(MouseEvent m){}
    @Override
   public void mouseExited(MouseEvent m){}
    @Override
   public void mouseClicked(MouseEvent m){
        colorX = m.getX();
        colorY = m.getY();
        System.out.println(colorX + "  " + colorY);
        getValue();
        start.setEnabled(true);
    }
    @Override
   public void mousePressed(MouseEvent m){}
    @Override
   public void mouseReleased(MouseEvent m){}
   
   public static void main(String [] args)
   {
      IMP imp = new IMP();
   }
 
}