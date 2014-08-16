package efresh.service;

import org.apache.pdfbox.pdmodel.*;
import java.awt.Image;
import java.awt.image.*;
import org.apache.pdfbox.pdmodel.graphics.xobject.*;
import java.io.*;
import java.io.IOException;


import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * This helps parse the class information from the incoming files
 * @author Adam Harper
 */
public class ClassParser
{
   /**
    * The file to parse
    */
   private File mFile;

   /**
    * CSV to parse
    */
   private File mCSV;

   /**
    * PDF to parse
    */
   private File mPDF;

   /**
    * The map of data
    */
   private Map<String, String[]> mapData;

   /**
    * map of images
    */
   private Map<String, Image> mapImage;

   /**
    * array list of names to add to maps
    */
   private ArrayList<String> names;

   /**
    * list of images to add to map
    */
   private List<BufferedImage> mImages;

   /**
    * the buffer to read info from files
    */
   private BufferedReader buff;

   /**
    * The line to parse
    */
   private String parseLine;

   /**
    * Document to parse
    */
   private PDDocument mDocument;

   /**
    * Creates a new ClassParser object.
    *
    * @param pFile File to parse
    * @param pdfFile pdf to parse
    *
    * @throws IOException cant open file
    */
   public ClassParser(File pFile, File pdfFile)
      throws IOException
   {
      mFile = pFile;
      mPDF = pdfFile;
      mImages = new ArrayList<BufferedImage>();
      names = new ArrayList<String>();
      mapData = new HashMap<String, String[]>();
      mapImage = new HashMap<String, Image>();
      mDocument = new PDDocument();

      if (mFile.getName().endsWith(".csv"))
      {
         mCSV = mFile;
         parseCSV();
      }

      if (mPDF.getName().endsWith(".pdf"))
      {
         parsePDF();
      }

      setUpImageMap();
   }

   /**
    * Sets up the image map
    */
   private void setUpImageMap()
   {
      for (int i = 1; i < names.size(); i++)
      {
         mapImage.put(names.get(i), mImages.get(i - 1));
      }
   }

   /**
    * This will parse CSV
    *
    * @throws IOException cant open file
    */
   private void parseCSV()
      throws IOException
   {
      String part1 = "";
      String part2 = "";

      buff = new BufferedReader(new FileReader(mCSV));
      parseLine = buff.readLine();

      if (parseLine.contains("Class"))
      {
         parseLine = buff.readLine();
      }

      while (true)
      {
         if (parseLine == null)
         {
            break;
         }

         convertData(parseLine);
         parseLine = buff.readLine();
      }
   }

   /**
    * Parses the PDF
    */
   private void parsePDF()
   {
      try
      {
         //FileInputStream fileIn = new FileInputStream(mPDF);
         mDocument = PDDocument.load(mPDF.toString());
      }
      // catch (PDFException ex)
      // {
      //    System.out.println("Error parsing PDF document " + ex);
      // }
      // catch (PDFSecurityException ex)
      // {
      //    System.out.println("Error encryption not supported " + ex);
      // }
      catch (IOException ex)
      {
         System.out.println("Error handling PDF document " + ex);
      }

      // Get images
      List pageTree = mDocument.getDocumentCatalog().getAllPages();
      Iterator iter= pageTree.iterator();
      int i = 0;
      while(iter.hasNext())
      {
         Map mappy = null;
         PDPage page = (PDPage)iter.next();
         PDResources resources = page.getResources();
         try
         {
             mappy = resources.getImages();
         }
         catch (IOException e)
         {
            System.out.println("ERROR IMAGES: " + e);
         }

         Map<String,PDXObjectImage> images = new TreeMap<String, PDXObjectImage>(mappy);

         if( images != null )
         {
            for (Map.Entry<String, ?> entry: images.entrySet())
            {
               String key = "img" + i;
               System.out.println("Key is " + key );
               PDXObjectImage image = (PDXObjectImage)images.get( key );
               BufferedImage temp = null;
               try
               {
                  temp = image.getRGBImage();
               }
               catch (IOException e)
               {
                 System.out.println("ERROR RGB IMAGE: " + e);
               }
               if (temp != null)
               {
                  mImages.add(temp);
                  i++;
               }
            }
         }
      }
      try
      {
         mDocument.close();
      }
      catch(IOException e)
      {
         System.out.println("Error closing PDF document " + e);
      }
   }


   // /**
   //  * Extracts images from a Page and adds them to the image list.
   //  * @param pPage The page to extract images from.
   //  */
   // private void extractImages(Page pPage)
   // {
   //    List<Image> pageImages = pPage.getImages();

   //    for(Image temp : pageImages)
   //    {
   //       BufferedImage image = (BufferedImage) temp;

   //       if (image != null)
   //       {
   //          mImages.add(image);
   //       }
   //    }
   // }

   /**
    * This will get ride of unwanted things int the string
    *
    * @param mString string to parse and add
    */
   private void convertData(String mString)
   {
      if (parseLine.charAt(0) == '\"')
      {
         parseLine = parseLine.substring(1, parseLine.length());
      }

      if (parseLine.charAt(parseLine.length() - 1) == '\"')
      {
         parseLine = parseLine.substring(0, parseLine.length() - 1);
      }

      String[] data = parseLine.split("\",\"");
      mapData.put(data[0], data);
      names.add(data[0]);
   }

   /**
    * Getter for the names in the map
    *
    * @return StudentNames - name of students
    */
   public String[] getNames()
   {
      String[] studentNames = new String[names.size()];

      for (int i = 0; i < names.size(); i++)
      {
         studentNames[i] = names.get(i);
      }

      return studentNames;
   }

   /**
    * This is a getter for the images
    *
    * @return mImages - list of images
    */
   public List<BufferedImage> getImages()
   {
      return mImages;
   }

   /**
    * this will get the list of names
    *
    * @return names list of names
    */
   public List<String> getFullNames()
   {
      return names;
   }

   /**
    * getter for the data of a certain name
    *
    * @param pName name to get data for
    *
    * @return info for student at that index
    */
   public String[] getData(String pName)
   {
      return mapData.get(pName);
   }

   /**
    * This will get the image of a person based
    * on their name
    *
    * @param pName the name of the student
    *
    * @return file of students image
    */
   public File getPersonImage(String pName)
   {
      File outputFile = new File("random.png");

      try
      {
         ImageIO.write((RenderedImage) mapImage.get(pName), "png", outputFile);
      }
      catch (Exception e)
      {
      }

      return outputFile;
   }
}
