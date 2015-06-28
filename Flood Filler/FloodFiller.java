import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
 
@SuppressWarnings("serial")
public class FloodFiller extends JPanel {
   private static BufferedImage img;
   private final static Color replacementColor = new Color(0, 255, 0);
   private final static Color targetColor = new Color(0, 0, 0);
   
   public void turnBlacknWhite() {
    int x, y;
    int w = img.getWidth();
    int h = img.getHeight();
    // first compute the mean intensity
    int totintensity = 0;
    for (y = 0; y < h; y++) {
     for (x = 0; x < w; x++) {
      int rgb = img.getRGB(x, y);
      totintensity += (rgb >> 16) & 0xFF + (rgb >>  8) & 0xFF + rgb & 0xFF;
     }
    }
    int meanintensity = totintensity / (w * h);
    for (y = 0; y < h; y++) {
     for (x = 0; x < w; x++) {
      int rgb = img.getRGB(x, y);
      int intensity = (rgb >> 16) & 0xFF + (rgb >>  8) & 0xFF + rgb & 0xFF;
      if (intensity < meanintensity) {  // it's darker than mean intensity
       img.setRGB(x, y, 0);  // turn black
      } else {  // it's lighter
       img.setRGB(x, y, 0xFFFFFF);  // turn white
      }
     }
    }
   }

   public FloodFiller(String fileName) {
      URL imgUrl = getClass().getClassLoader().getResource(fileName);
      if (imgUrl == null) {
         System.err.println("Couldn't find file: " + fileName);
      } else {
         try {
            img = ImageIO.read(imgUrl);
         } catch (IOException ex) {
            ex.printStackTrace();
         }
      }

      turnBlacknWhite();

      setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
   }
 
   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      setBackground(Color.WHITE);
      g.drawImage(img, 0, 0, null);
   }

   public static void fill(int x, int y, Color targetColor, Color replacementColor) {
	   Color pixelColour = new Color(img.getRGB(x, y));
	   if (!pixelColour.equals(targetColor)) {
		   return;
	   }
	   img.setRGB(x, y, replacementColor.getRGB());
	   fill(x - 1, y, targetColor, replacementColor);
	   fill(x + 1, y, targetColor, replacementColor);
	   fill(x, y - 1, targetColor, replacementColor);
	   fill(x, y + 1, targetColor, replacementColor);
   }
 
   public static void main(String[] args) {
//      final String fileName = args[0];
      final String fileName = "image.jpg";
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            final JFrame frame = new JFrame("Flood Filler");
            frame.setContentPane(new FloodFiller(fileName));
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.getContentPane().addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent arg0) {
					fill(arg0.getX(), arg0.getY(), targetColor, replacementColor);
					frame.repaint();
				}
				
				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseClicked(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
         }
      });
   }
}
