package efresh.service;

import java.awt.Image;
import java.awt.image.*;

import java.io.*;

import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Random Queue for images
 *
 * @author Adam Harper
 */
public class RandomQueue
{
   /**
    * Sets a true or false on an image to make sure there are
    * no duplicates
    */
   private Boolean[] myBools;

   /**
    * Image for student pictures
    */
   private Image mImage;

   /**
    * Name for student
    */
   private String mName;

   /**
    * size of queue
    */
   private int size;

   /**
    * Count of how many we have been through
    */
   private int count;

   /**
    * List of images to use
    */
   private List<BufferedImage> listImages;

   /**
    * List of names for students
    */
   private List<String> listNames;

   /**
    * New random class
    */
   private Random randomNum;

   /**
    * Creates a new RandomQueue object.
    *
    * @param names List of names to use
    * @param images list of images to use
    */
   public RandomQueue(List<String> names, List<BufferedImage> images)
   {
      count = 0;
      size = names.size() - 1;
      myBools = new Boolean[size];
      initBools();
      listNames = names;
      listImages = images;
      randomNum = new Random();
      mImage = new BufferedImage(50, 50, 1);
   }

   /**
    * sets all bools to false except 0
    */
   public void initBools()
   {
      for (int i = 0; i < size; i++)
      {
         myBools[i] = false;
      }

      myBools[0] = true;
   }

   // create queue of random values
   /**
    * Creates a queue of random numbers based
    * on the number of students
    */
   public void random()
   {
      int randomInt = 0;

      while (myBools[randomInt])
      {
         if (count == (size - 1))
         {
            count = 0;
            initBools();
         }

         randomInt = randomNum.nextInt(size);
      }

      myBools[randomInt] = true;
      count++;
      mImage = listImages.get(randomInt - 1);
      mName = listNames.get(randomInt);
   }

   /**
    * creates a new random image for the student info
    * to take and use
    * @return file the image of student
    */
   public File getImage()
   {
      File outputFile = new File("random.png");

      try
      {
         ImageIO.write((RenderedImage) mImage, "png", outputFile);
      }
      catch (Exception e)
      {
      }

      return outputFile;
   }

   /**
    * get the name of the student
    *
    * @return mName - name of the student
    */
   public String getName()
   {
      return mName;
   }
}
