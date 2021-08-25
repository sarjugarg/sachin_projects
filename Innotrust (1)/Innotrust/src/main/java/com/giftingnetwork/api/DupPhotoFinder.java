package com.giftingnetwork.api;


import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;

import javax.imageio.ImageIO;

public class DupPhotoFinder {

    public static void main(String[] args) {
        System.out.println("sdfs");
try {
    File a = new File("/Users/sachinyadav/Downloads/a.jpeg");
    File b = new File("/Users/sachinyadav/Downloads/c.jpeg");
    System.out.println("result " +  compareImage(a,b));
      
    
} catch (Exception e) {
    System.out.println(e );
}
      

    }

    public static boolean compareImage(File fileA, File fileB) {

        try {

            // take buffer data from botm image files //
            BufferedImage biA = ImageIO.read(fileA);
            DataBuffer dbA = biA.getData().getDataBuffer();
            int sizeA = dbA.getSize();
            System.out.println("Image1 " + sizeA);
            BufferedImage biB = ImageIO.read(fileB);
            DataBuffer dbB = biB.getData().getDataBuffer();
            int sizeB = dbB.getSize();
             System.out.println("Imagee2 " + sizeB);

            if (sizeA == sizeB) {
                for (int i = 0; i < sizeA; i++) {
                    if (dbA.getElem(i) != dbB.getElem(i)) {
                        System.out.println("Image is Different ");
                        return false;
                    }
                }
                System.out.println("Image is same ");
                return true;

            } else {
                System.out.println("Image is Different ");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Failed to compare image files ...");
            return false;
        }
    }

}