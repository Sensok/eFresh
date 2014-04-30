package efresh.desktop;

import efresh.service.*;

import efresh.system.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.nio.channels.FileChannel;

import java.util.Calendar;

import javafx.stage.Stage;

/**
 * Parses the data for the program
 * @author Adam Harper and Jordan Jensen
 */
public class DataParser
{
   /**
    * This will hold the PDF information
    */
   private File mPDFFile;

   /**
    * This will hold the CSV information
    */
   private File mCSVFile;

   /**
    * This will hold the info to parse the files
    */
   private BufferedReader buff;

   /**
    * Starts parsing the data
    * @param selectedFile current file to parse
    * @param pUserName - username of the current user
    */
   public DataParser(File selectedFile, String pUserName)
      throws FileNotFoundException, IOException, Exception
   {
      String newName = "";

      CopyFiles mCopy = new CopyFiles(selectedFile, pUserName);
      selectedFile = mCopy.getNewFile();

      if (selectedFile.getName().endsWith("zip"))
      {
         // pass in the zip
         // extract the 2 files and send both those files to the ClassParser
         newName = changeNameForPerson(selectedFile.getName(), "", selectedFile);


         if (Main.data.getFileMap().containsKey(newName))
         {
            Main.data.remove(newName);

            ClassParser test = new ClassParser(mCSVFile, mPDFFile);
            Main.data.addToPerson(newName, test);
            Main.data.addToMap(newName, selectedFile);
         }
         else if (! newName.contains(" "))
         {

            ClassParser test = new ClassParser(mCSVFile, mPDFFile);

            Main.data.addToPerson(newName, test);
            Main.data.addToMap(newName, selectedFile);
         }
         else
         {
            System.out.println("No Name");
         }
      }

      else
      {
         buff = new BufferedReader(new FileReader(selectedFile));

         String parseLine = buff.readLine();
         parseLine = buff.readLine();
         newName = changeNameForGame(parseLine);

         if (Main.data.contains(newName))
         {
            Main.data.remove(newName);
            Main.data.addToGame(newName, new GameParser(selectedFile));
         }
         else
         {
            Main.data.addToGame(newName, new GameParser(selectedFile));
         }

         Main.data.addToMap(newName, selectedFile);
      }
   }

   /**
    * This will change the String name to get ride of extra
    * characters
    * @param pName The name to parse
    *
    * @return String pName - The parsed name to use in the program
    */
   private String changeNameForGame(String pName)
   {
      return pName.replaceAll("[-,\"]", "");
   }

   /**
    * DOCUMENT ME!
    *
    * @param pName This will take a name and put it in the person map
    * @param parseLine This is the line to parse
    * @param pFile This is the file to parse
    *
    * @return String Parsed information
    */
   private String changeNameForPerson(String pName, String parseLine, File pFile)
   {
      ExtractZipFile newZip = new ExtractZipFile(pFile);
      mPDFFile = new File(newZip.getPDFFileName());
      mCSVFile = new File(newZip.getCSVFileName());

      if (mPDFFile.getName().contains(" ") || mCSVFile.getName().contains(" "))
      {
         showMessage("Zip File does not contain correct Files",
            "Need to enter a zip with 1 .csv and 1 .pdf");

         return " ";
      }
      else
      {
         try
         {
            BufferedReader buff = new BufferedReader(new FileReader(mCSVFile));
            parseLine = buff.readLine();
         }
         catch (Exception e)
         {
            System.out.println("ERROR" + parseLine);
         }

         if (parseLine.contains("Class"))
         {
            return parseLine.replaceAll("(^Class)|[,\"]", "");
         }
         else
         {
            return getSemester(mCSVFile.toString());
         }
      }
   }

   /**
    * This will get the current semester and turn it into a string
    * for renaming files.
    * @param pName The current name to parse into a semester
    *
    * @return String pName - the reformatted file name
    */
   private String getSemester(String pName)
   {
      Calendar mCalendar = Calendar.getInstance();
      int month = mCalendar.get(Calendar.MONTH);
      int year = mCalendar.get(Calendar.YEAR);
      pName = pName.substring(0, pName.length() - 6);

      if ((month >= 0) && (month < 3))
      {
         pName = pName + "WI-" + year;
      }
      else if ((month >= 3) && (month < 7))
      {
         pName = pName + "SP-" + year;
      }
      else
      {
         pName = pName + "FA-" + year;
      }

      return pName.replaceAll("[_,\"]", "");
   }

   /**
    * This will bring a pop up for message warnings
    *
    * @param pMsg1 message to display
    * @param pMsg2 message to display
    */
   private void showMessage(String pMsg1, String pMsg2)
   {
      Stage stage = new Alert(Main.mStage, pMsg1, pMsg2);
      stage.showAndWait();
   }
}
