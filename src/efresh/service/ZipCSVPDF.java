package efresh.service;

import efresh.system.Shell;
import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.*;

public class ZipCSVPDF
{
   private File mCSV;
   private File mPDF;
   private String mZipName;
   private String mPath;
   private String mPathSep;
   private String mName;

   public ZipCSVPDF(File pCSV, File pPDF, String pName)
   {
      mName = pName;
      mPathSep = System.getProperty("file.separator");
      mCSV = pCSV;
      mPDF = pPDF;
      mZipName = mPDF.getName().
         substring(0,mPDF.getName().indexOf('.'));
      mPath = System.getProperty("user.home") + mPathSep +
         "efresh-tmp" + mPathSep + mName + mPathSep + mZipName + ".zip";
      zipFiles();
   }

   private void zipFiles()
   {

      try
      {

         FileInputStream pdf = new FileInputStream(mPDF);

         FileInputStream csv = new FileInputStream(mCSV);
         FileOutputStream mStream = new FileOutputStream(mPath);

         ZipOutputStream out = new ZipOutputStream(mStream);
         // name the file inside the zip  file
         out.putNextEntry(new ZipEntry(mZipName + ".pdf"));

         byte[] b = new byte[10240000];

         int count1;

         while ((count1 = pdf.read(b)) > 0)
         {
            System.out.println();
            out.write(b, 0, count1);
         }

         out.putNextEntry(new ZipEntry(mZipName + ".csv"));

         int count2;

         while ((count2 = csv.read(b)) > 0)
         {
            System.out.println();
            out.write(b, 0, count2);
         }

         out.close();
         csv.close();
         pdf.close();
      }
      catch (IOException e)
      {
         System.out.println("Failed to create zip");
      }
      // try
      // {
      //    // Shell temp = new Shell();
      //    // temp.remove(mCSV);
      //    // temp.remove(mPDF);
      // }
      // catch (IOException e)
      // {
      //    System.out.println("No File to remove");
      // }

   }
   /**
    * This will zip the csv and pdf files together
    *
    * @return the zipped file
    */
   public File getZipFile()
   {
      return new File(mPath);
   }

}
