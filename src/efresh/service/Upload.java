package efresh.service;

import com.dropbox.core.*;
import java.io.ByteArrayInputStream;

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
   public Upload(DbxClient mClient)
   {
      mPathSep = System.getProperty("file.separator");
      InputStream is;

      // Directory path here
      String path = System.getProperty("user.home") + mPathSep +
         "efresh-tmp" + mPathSep + "Login.info";

      File folder = new File(path);      
      
      InputStream inputStream;
       inputStream = new ByteArrayInputStream(path.getBytes());
        try { 
              DbxEntry.File uploadedFile = mClient.uploadFile("",
              DbxWriteMode.force(), folder.length(), inputStream);
              System.out.println("Uploaded: " + uploadedFile.toString());
              inputStream.close();
            }
        
         catch (Exception e) {
             e.printStackTrace();
          }
   }
}
