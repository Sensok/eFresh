package efresh.system;

import java.io.File;

/**
 * This will copy files from the user dir to the temp dir of
 * eFresh
 *
 * @author Jordan Jensen
 */
public class CopyFiles
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
    * String for file separator
    */
   private String mPathSep;
   /**
    *   This will take the source file and the user name
    *   to copy the .zip or pdf into the users directory
    *   for eFresh.
    *   @param pSource - the source file
    *   @param pName - the users encrypted user name
    */
   public CopyFiles(File pSource, String pName)
      throws Exception
   {
      mPathSep = System.getProperty("file.separator");
      Shell mShell = new Shell();

      mIncFile = new File(System.getProperty("user.home") + mPathSep +
                          "efresh-tmp" + mPathSep +
                          pName + mPathSep + pSource.getName());

      if (! mIncFile.getAbsolutePath().equals(pSource.getAbsolutePath()))
      {
         mShell.copy(pSource, mIncFile);
      }
   }

   /**
    * This will give the file map the path to the copied file
    *
    * @return The new file path for the file map to use
    */
   public File getNewFile()
   {
      return mIncFile;
   }
}
