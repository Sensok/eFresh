package efresh.desktop;

import efresh.service.*;
import efresh.system.*;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

import javax.imageio.ImageIO;

import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.io.FileOutputStream;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
/**
 * Holds the data for the program
 * @author Adam Harper
 */
public class Data
{
   /**
    * Map of the filename and name
    */
   public static HashMap fileMap;
   private String mPathSep;
   private String mUserName;
   /**
    * Map of the class name and the people in the class
    */
   public static HashMap<String, ClassParser> personMap;

   /**
    * Map of the game name and the game contents
    */
   public static HashMap<String, GameParser> gameMap;

   public List studentList;

   /**
    * Creates a new Data object.
    */
   public Data()
   {
      gameMap = new HashMap<String, GameParser>();
      personMap = new HashMap<String, ClassParser>();
      fileMap = new HashMap<String, File>();
      studentList = new ArrayList<String>();
      mPathSep = System.getProperty("file.separator");

   }

   /**
    * Add to the filename map
    *
    * @param pName name of the file
    * @param pFile the file to pass in
    */
   public void addToMap(String pName, File pFile)
   {
      fileMap.put(pName, pFile);
   }

   /**
    * Add to the person map
    *
    * @param pName class name
    * @param pClass class contents
    */
   public void addToPerson(String pName, ClassParser pClass)
   {
      personMap.put(pName, pClass);
      update(true, true, pName);
   }

   /**
    * Add to the game map
    *
    * @param pName name of the game
    * @param pGame contents of the game
    */
   public void addToGame(String pName, GameParser pGame)
   {
      gameMap.put(pName, pGame);
      update(true, false, pName);
   }

   /**
    * Removes from the game
    *
    * @param pName name of which to remove
    */
   public void removeFromGame(String pName)
   {
      gameMap.remove(pName);
      fileMap.remove(pName);
      update(false, false, pName);
      Shell mShell = new Shell();

   }

   /**
    * Removes from the person map
    *
    * @param pName name of the class to be removed
    */
   public void removeFromPerson(String pName)
   {
      int value = studentList.indexOf(pName);
      StudentInfo.removeClass(value);
      studentList.remove(value);
      personMap.remove(pName);
      new RemoveTempFiles((File)fileMap.get(pName));
      fileMap.remove(pName);

      update(false, true, pName);
   }

   /**
    * remove
    *
    * @param pName remove from all
    */
   public void remove(String pName)
   {
      if(pName.contains(" "))
      {
         removeFromGame(pName);
      }
      else
      {
         removeFromPerson(pName);
      }
   }

   /**
    * Updates the gui to the information that has been imported
    *
    * @param pAdd whether we are adding or removing
    * @param pPerson dealing with person or game
    * @param pName name of which we are adding or removing
    */
   public void update(Boolean pAdd, Boolean pPerson, String pName)
   {
      if (pPerson) // add to person
      {
         if (pAdd)
         {
            Random.addToBox(pName);
            StudentInfo.addClass(pName, convert(personMap.get(pName)));
            studentList.add(pName);
            ImpAndRem.addToBox(pName);
         }
         else
         {
            Random.removeFromBox(pName);
            ImpAndRem.removeFromBox(pName);
         }
      }
      else
      {
         if (pAdd)
         {
            Games.addToFileList(pName);
            ImpAndRem.addToBox(pName);
         }
         else
         {
            Games.removeFromList(pName);
            ImpAndRem.removeFromBox(pName);
         }
      }
   }

   /**
    * This will convert the data from the parser to an array
    * of strings
    */
   public String[] convert(ClassParser pData)
   {
      return pData.getNames();
   }


   /**
    * This will check to see if a name is in a
    * map
    */
   public boolean contains(String pName)
   {
      return fileMap.containsKey(pName);
   }

   /**
    * This will get the Hashmap for the file map
    */
   public HashMap getFileMap()
   {
      return fileMap;
   }

   public String getFileName(String pName)
   {
      File zipFile = (File)fileMap.get(pName);
      return zipFile.getName();
   }
   public File updateData(String pClassName, String pStudentName,
                          String pData[])
   {
      personMap.get(pClassName).addData(pStudentName, pData);
      File returnFile = writeNewCSV(pClassName);
      return returnFile;
   }

   public File writeNewCSV(String pClassName)
   {
      String path = System.getProperty("user.home") + mPathSep +
         "efresh-tmp" + mPathSep + Main.username
         + mPathSep +  getFileName(pClassName) + ".csv";

      File newCSV = new File(path);
      try
      {
         Collection<String[]> data = personMap.get(pClassName).getDataMap();

         FileOutputStream mStream = new FileOutputStream(newCSV);
         String lineToWrite =
            "\"Name,I-Number,\"E-mail address\",Major,\"Home City, "
            + "State\",\"null\"\n";
         byte[] content = lineToWrite.getBytes();
         mStream.write(content);
         for (String[] temp : data)
         {
            lineToWrite = "";
            for(String toPrint : temp)
            {
                  lineToWrite += "\"" + toPrint + "\",";
            }
            lineToWrite = lineToWrite.substring(0,lineToWrite.length() - 1);
            lineToWrite += "\n";
            if (!lineToWrite.contains("Name,I-Number"))
            {
               content = lineToWrite.getBytes();
               mStream.write(content);
            }
         }
         mStream.close();

      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      return newCSV;
   }
   public File newImageFile(String pClassName)
   {
      String path = System.getProperty("user.home") + mPathSep +
         "efresh-tmp" + mPathSep + Main.username
         + mPathSep + getFileName(pClassName) + ".pdf";

      File updatedPDF = new File(path);
      try
      {
         int x = 0;
         int y = 0;
         Collection<java.awt.Image> images =
            personMap.get(pClassName).getImageMap();

         FileOutputStream mStream = new FileOutputStream(path);
         Document newPDF = new Document(PageSize.A0);
         PdfWriter writer = PdfWriter.getInstance(newPDF, mStream);
         newPDF.open();

         for(java.awt.Image image : images)
         {
            BufferedImage bi = (BufferedImage) image;
            Image itextImage =
               Image.getInstance(Toolkit.getDefaultToolkit().
                                 createImage(bi.getSource()), null);
            newPDF.add(itextImage);

         }
         newPDF.close();

      }

      catch (IOException ie)
      {
         System.out.println("ERROR IN CREATING FILE");
      }
      catch (DocumentException e)
      {
         System.out.println("ERROR IN CREATING DOCUMENT");
      }
      return updatedPDF;
   }
}
