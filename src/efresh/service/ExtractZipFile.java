package efresh.service;

import java.io.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.zip.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This will extract zip files
 *
 * @author Adam Harper
 */
public class ExtractZipFile
{
   /**
    * file name to unzip
    */
   private String mFileName;

   /**
    * pdf file name
    */
   private String mPDFName;

   /**
    * Creates a new ExtractZipFile object.
    *
    * @param selectedFile the file to extract
    */
   public ExtractZipFile(File selectedFile)
   {
      mFileName = " ";
      mPDFName = " ";

      String fName = selectedFile.toString();
      byte[] buf = new byte[1024];

      try
      {
         ZipInputStream zinstream =
            new ZipInputStream(new FileInputStream(fName));
         ZipEntry zentry = zinstream.getNextEntry();

         while (zentry != null)
         {
            String entryName = zentry.getName();
            FileOutputStream outstream = new FileOutputStream(entryName);
            int n;

            while ((n = zinstream.read(buf, 0, 1024)) > -1)
            {
               outstream.write(buf, 0, n);
            }

            outstream.close();
            zinstream.closeEntry();

            if (entryName.endsWith(".csv"))
            {
               mFileName = entryName;
            }
            else if (entryName.endsWith(".pdf"))
            {
               mPDFName = entryName;
            }

            zentry = zinstream.getNextEntry();
         }

         zinstream.close();
      }
      catch (Exception e)
      {
         System.out.println(e);
      }
   }

   /**
    * Getter for the CSV file name
    *
    * @return returns the file name
    */
   public String getCSVFileName()
   {
      return mFileName;
   }

   /**
    * getter for the pdf file name
    *
    * @return pdf file name
    */
   public String getPDFFileName()
   {
      return mPDFName;
   }
}
