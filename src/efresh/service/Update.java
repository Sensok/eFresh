package efresh.service;

//TODO: Update the dropbox account with the new information

import com.dropbox.core.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
//from the session i.e. files that have been removed/deleted

/**
 *
 * @author Adam Harper and Jordan Jensen
 */
public class Update
{

    private String mPathSep;
    public Update(DbxClient dropBox) {

    }

    public Update(DbxClient mClient, String username, List <File> pFilesToDelete) throws FileNotFoundException {
       mPathSep = System.getProperty("file.separator");
      InputStream is;
      String path = System.getProperty("user.home") + mPathSep +
         "efresh-tmp" + mPathSep + username + mPathSep;
      File folder = new File(path);
      File[] listFiles = folder.listFiles();
 
      for(File file : listFiles){          
              InputStream inputStream;
              inputStream = new FileInputStream(file);
              try {
                DbxEntry.File uploadedFile = mClient.uploadFile("/" + username + "/" + file.getName(),
                DbxWriteMode.force(), file.length(), inputStream);
                inputStream.close();
                }

               catch (Exception e) {
                e.printStackTrace();
                }       
      }
        for(File file: pFilesToDelete) { 
              try {
                  mClient.delete("/" + username + "/" + file.getName());
              }
              
              catch (Exception e){
                  e.printStackTrace();
              }
          }
    }
}
