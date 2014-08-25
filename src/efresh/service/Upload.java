package efresh.service;

import com.dropbox.core.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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
   public Upload(DbxClient mClient) throws FileNotFoundException
   {
      mPathSep = System.getProperty("file.separator");
      InputStream is;

      // Directory path here
      String path = System.getProperty("user.home") + mPathSep +
         "efresh-tmp" + mPathSep + "Login.info";

      File folder = new File(path);      
      
      InputStream inputStream;
       inputStream = new FileInputStream(path);
        try { 
              DbxEntry.File uploadedFile = mClient.uploadFile("/Login.info",
              DbxWriteMode.force(), folder.length(), inputStream);
              inputStream.close();
            }
        
         catch (Exception e) {
             e.printStackTrace();
          }
   }
}
