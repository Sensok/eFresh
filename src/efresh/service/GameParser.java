package efresh.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

/**
 * This will parse the games for the games gui
 * @author Adam Harper
 */
public class GameParser
{
   /**
    * The file holder
    */
   private File mFile;

   /**
    * map for games
    */
   private Map<String, String> newMap;

   /**
    * Contains the words for the game
    */
   private ArrayList<String> mList;

   /**
    * buffered reader for files
    */
   private BufferedReader buff;

   /**
    * parsed line
    */
   private String parseLine;

   /**
    * Creates a new GameParser object.
    *
    * @param pFile file to parse
    *
    * @throws IOException cant find file
    */
   public GameParser(File pFile)
      throws IOException
   {
      mList = new ArrayList<String>();
      mFile = pFile;
      newMap = new HashMap<String, String>();
      parseData();
   }

   /**
    * parses the data
    *
    * @throws IOException cant find file
    */
   private void parseData()
      throws IOException
   {
      String part1 = "";
      String part2 = "";

      buff = new BufferedReader(new FileReader(mFile));
      parseLine = buff.readLine();
      parseLine = buff.readLine();

      while (true)
      {
         parseLine = buff.readLine();

         if (parseLine == null)
         {
            break;
         }

         convertData(parseLine);
      }
   }

   /**
    * Converts the data form the parse line
    *
    * @param parseLine the line from the file
    */
   private void convertData(String parseLine)
   {
      parseLine = parseLine.substring(5, parseLine.length());

      if (parseLine.charAt(parseLine.length() - 1) == '\"')
      {
         parseLine = parseLine.substring(0, parseLine.length() - 1);
      }

      String[] data = parseLine.split("\",\"");
      newMap.put(data[0], data[1]);
      mList.add(data[0]);
   }

   /**
    * getter for the map
    *
    * @return map - the map of games
    */
   public Map getMap()
   {
      return newMap;
   }

   public ArrayList<String> getList()
   {
      return mList;
   }
}
