package efresh.system;

import java.io.File;

/**
 *
 * @author Adam Harper and Jordan Jensen
 */
public class Starter
{
   /**
    *If there is already a directory, get the .zip files
    *and import them
    */
   private File mFile;

   /**
    *File separator based on system
    */
   private String mPathSep;

   /**
    * Creates a new Starter object.
    */
   public Starter()
   {
      mPathSep = System.getProperty("file.separator");
      if ((new File(System.getProperty("user.home") + mPathSep +
                    "efresh-tmp")).mkdir())
      {
         System.out.println("Created Folder");
      }
      else
      {
         System.out.println("Already Exists");
      }
   }

   /**
    * Creates a new Starter object.
    *
    * @param pName The user name
    */
   public Starter(String pName)
   {
      if ((new File(System.getProperty("user.home") + mPathSep +
                    "efresh-tmp" + mPathSep + pName)).mkdir())
      {
         System.out.println("Created Folder");
      }
      else
      {
         try
         {
            System.out.println("Already Exists");
         }
         catch (Exception e)
         {
         }
      }
   }
}
