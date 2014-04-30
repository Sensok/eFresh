package efresh.service;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.RequestTokenPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.WebAuthSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;

import java.util.Scanner;

//TODO: Jordan - make sure that all the files get back into dropbox
/**
 * This will upload files to dropbox
 * @author Adam Harper
 */
public class Upload
{
   /**
    * String for file sep
    */
   private String mPathSep;
   /**
    * Creates a new Upload object.
    *
    * @param mDBApi Dropbox api to use to upload files to dropbox
    */
   public Upload(DropboxAPI mDBApi)
   {
      mPathSep = System.getProperty("file.separator");
      InputStream is;

      // Directory path here
      String path = System.getProperty("user.home") + mPathSep +
         "efresh-tmp" + mPathSep + "Login.info";

      File folder = new File(path);

      try
      {
         is = new FileInputStream(folder);

         mDBApi.putFileOverwrite(mPathSep +
                                 folder.getName(), is, folder.length(),
            null);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
}
