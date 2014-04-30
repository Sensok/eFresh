package efresh.desktop;

import efresh.service.*;
import efresh.system.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

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
      new RemoveTempFiles((File)fileMap.get(pName), Main.username);
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
}
