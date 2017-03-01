package kz.codingwolves.identicons;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class IdenticonGenerator {
	
	public static int height = 5;
	public static int width = 5;

	public static BufferedImage generate(String userName, boolean girl) {
		HashGenerator hashGenerator = new HashGenerator("MD5");
		byte[] hash = hashGenerator.generate(userName);

    	BufferedImage identicon = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = identicon.getRaster();

        //get byte values as unsigned ints
        //int r = hash[0] & 255;
        //int g = hash[1] & 255;
        //int b = hash[2] & 255;

        int [] background = new int[] {255, 255, 255, 0};
        
        int [] foreground;
        if (girl) {
        	foreground = new int[] {218, 89, 98, 255};
        } else {
        	foreground = new int[] {56, 195, 107, 255};
        }

        for(int x = 0; x < width; x++) {
        	int i = x < 3 ? x : 4 - x;
	    	for(int y = 0; y < height; y++) {
	    		int [] pixelColor;
	    		if((hash[i] >> y & 1) == 1) {
	    			pixelColor = foreground;
	    		} else {
	    			pixelColor = background;
	    		}
	    		raster.setPixel(x, y, pixelColor);
	    	}
        }
        return resize(identicon);
        //return (BufferedImage) identicon.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
	}
	
	private static BufferedImage resize(BufferedImage img) {
		Image tmp = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}
}