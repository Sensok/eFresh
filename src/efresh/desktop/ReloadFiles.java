package efresh.desktop;

import java.io.File;

/**
 * This will reload temporary files from the temp
 * file and add them back into the gui
 *
 * @author Jordan Jensen
 */
public class ReloadFiles
{
   /**
    * New file created from path
    */
   private File mFolder;

   /**
    * Creates a new ReloadFiles object.
    *
    * @param pPath The path to the file in the temp folder
    * @param pName The user name
    *
    * @throws Exception File not found
    */
   public ReloadFiles(String pPath, String pName)
      throws Exception
   {
      String files;

      mFolder = new File(pPath);

      File[] mFiles = mFolder.listFiles();


      for (int i = 0; i < mFiles.length; i++)
      {

         if (mFiles[i].isFile())
         {
            new DataParser(mFiles[i], pName);
         }
      }
   }
}
