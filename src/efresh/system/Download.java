package efresh.system;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.*;
import com.dropbox.client2.DropboxAPI.DropboxFileInfo.*;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.RequestTokenPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.WebAuthSession;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Scanner;

/**
 * This will download things from the dropbox
 *
 * @author Jordan Jensen and Adam Harper
 */
public class Download
{
   /**
    * Creates a new Download object.
    *
    * @param mDBApi The api to download from
    */

   //TODO Download more than just the login info
   public Download(DropboxAPI mDBApi)
   {
      //this.client = new DropboxClient(this.config
      getFile("Login.info", "login", mDBApi);

      //for each loop
      // for(File newFile : All Files on DropBox)
      // {
      // getFile("string", "Login.user")
      // }
   }

   /**
    * get the file to download
    *
    * @param newFile the file name to download to
    * @param query temp string
    * @param mDBApi the main dropbox api to use
    */
   public void getFile(String newFile, String query, DropboxAPI mDBApi)
   {
      FileOutputStream outputStream = null;
      String mPathSep = System.getProperty("file.separator");
      try
      {
         File file =
            new File(System.getProperty("user.home") + mPathSep +
                     "efresh-tmp" + mPathSep + newFile);
         outputStream = new FileOutputStream(file);

         DropboxFileInfo info =
            mDBApi.getFile(mPathSep + newFile, null, outputStream, null);
      }
      catch (Exception e)
      {
         System.out.println("Something went wrong: " + e);
      }
      finally
      {
         if (outputStream != null)
         {
            try
            {
               outputStream.close();
            }
            catch (IOException e)
            {
               // intentionally left blank
            }
         }
      }
   }

   /**
    * Need to finish... hahah... later
    *
    * @param query The files to get
    */
   public void search(String query)
   {
   }
}
