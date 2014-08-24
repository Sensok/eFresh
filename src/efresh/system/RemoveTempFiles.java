package efresh.system;

import java.io.File;

/**
 * Will remove temp files when the data is removed from the program
 *
 * @author Jordan Jensen
 */
public class RemoveTempFiles
{
   /**
    *The users encrypted name
    */
   private String mName;

   /**
    * The incoming file
    */
   private File mIncFile;

   /**
    *   This will take the source file and the user name
    *   to copy the .zip or pdf into the users directory
    *   for eFresh.
    *   @param pSource - the source file
    *   @param pName - the users encrypted user name
    */
   public RemoveTempFiles(File pSource)
   {
      try
      {
         Shell mShell = new Shell();

         mShell.remove(pSource);
      }
      catch (Exception e)
      {
         System.out.println("ERROR REMOVE");
      }
   }
}
