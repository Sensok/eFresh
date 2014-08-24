package efresh.system;

import com.dropbox.core.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
   public Download(DbxClient mClient) throws FileNotFoundException, DbxException, IOException
   {
      //this.client = new DropboxClient(this.config
      FileOutputStream outputStream = new FileOutputStream("Login.info");
        try {
              DbxEntry.File downloadedFile = mClient.getFile("/Login.info", null,outputStream);
            } 
        finally {
             outputStream.close();
            }
    
   }

    public Download() {
    }

   /**
    * get the file to download
    * use the users id to download all files ?
    * @param newFile the file name to download to
    * @param query temp string
    * @param mDBApi the main dropbox api to use
    */
   public void getFiles(String pUserName, DbxClient mClient)
   {
      FileOutputStream outputStream = null;
      String mPathSep = System.getProperty("file.separator");
      String filePath = System.getProperty("user.home") + mPathSep +
                     "efresh-tmp" + mPathSep + pUserName;
      try{
         File folder = new File(System.getProperty("user.home") + mPathSep +
                     "efresh-tmp" + mPathSep + pUserName);
         if(!folder.exists()){
             folder.mkdirs();
         }
         String path = "/" + pUserName;
         System.out.println(path);
         DbxEntry check = mClient.getMetadata(path);
        if (check == null){
             mClient.createFolder(path);
         }
        else{
            DbxEntry.WithChildren listing = mClient.getMetadataWithChildren(path);
            for (DbxEntry child : listing.children){
                System.out.print(child.name);
                mClient.getFile("/" + pUserName + "/" + child.name, null, new FileOutputStream(filePath + mPathSep + child.name));
            }
         }
      
      }
      catch(DbxException e){
          
      }
      catch(IOException e){
          System.out.println(e);
      }
   }

   /**
    * Need to finish... hahah... later
    *
    * @param query The files to get
    */
   public void getFile(String query)
   {
   }
}
