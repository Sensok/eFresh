package efresh.service;

import efresh.system.Shell;

import java.io.*;
import java.util.zip.*;

import org.pdfbox.cos.COSDocument;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDDocumentInformation;
import org.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class PDFTextParser {

   PDFParser parser;
   String parsedText;
   PDFTextStripper pdfStripper;
   PDDocument pdDoc;
   COSDocument cosDoc;
   PDDocumentInformation pdDocInfo;
   String mLineSep;
   String mPDFFile;
   String mDestCSV;
   String mPathSep;
   String mZipName;
   String mName;
// PDFTextParser Constructor
   public PDFTextParser(String pPDFFile, String pName, String pFileName)
   {
      mPathSep = System.getProperty("file.separator");
      mPDFFile = pPDFFile;
      mZipName = pFileName.substring(0,pFileName.indexOf('.'));
      mName = pName;
      mDestCSV = System.getProperty("user.home") + mPathSep
         + "efresh-tmp" + mPathSep + mName + mPathSep + mZipName + ".csv";
      String pdfText = pdftoText(mPDFFile);
      writeTexttoFile(pdfText, mDestCSV);
   }
// Extract text from PDF Document
   String pdftoText(String fileName) {

      File f = new File(fileName);

      if (!f.isFile()) {
         System.out.println("File " + fileName + " does not exist.");
         return null;
      }

      try {
         parser = new PDFParser(new FileInputStream(f));
      } catch (Exception e) {
         System.out.println("Unable to open PDF Parser.");
         return null;
      }

      try {
         parser.parse();
         cosDoc = parser.getDocument();
         pdfStripper = new PDFTextStripper();
         pdDoc = new PDDocument(cosDoc);
         parsedText = pdfStripper.getText(pdDoc);
         mLineSep = pdfStripper.getLineSeparator();
      } catch (Exception e) {
         System.out.println("An exception occured in parsing the PDF Document.");
         e.printStackTrace();
         try {
            if (cosDoc != null) cosDoc.close();
            if (pdDoc != null) pdDoc.close();
         } catch (Exception e1) {
            e.printStackTrace();
         }
         return null;
      }
      return parsedText;
   }

// Write the parsed text from PDF to a file
   void writeTexttoFile(String pdfText, String fileName)
   {

      String temp = "";

      StringTokenizer mtok = new StringTokenizer(pdfText, mLineSep);
      int firstThree = 0;
      int count = 0;
      while (mtok.hasMoreTokens())
      {
         if (firstThree < 3)
         {
            mtok.nextToken();
            firstThree++;
            if (firstThree == 3)
               temp += "\"Name\",\"I-Number\",\"E-mail address\","
                  +"\"Major\",\"Home City, State\"" + "\n";
         }
         if (firstThree >= 3)
         {
            String token = mtok.nextToken();
            if (token.length() > 1)
            {

               if (token.length() < 14 && count == 3
                   && !token.equals("Biology"))
                  temp += "\"" + token + " " + mtok.nextToken() + "\"";
               else
                  temp += "\"" + token + "\"";
               if (count < 4)
                  temp += ",";
               if (count == 4)
               {
                  count = 0;
                  temp += "\n";
               }
               else
                  count++;
            }
         }

      }
      try
      {
         PrintWriter pw = new PrintWriter(fileName);
         pw.print(temp);
         pw.close();
      }
      catch (Exception e)
      {
         System.out.println("An exception occured in writing the pdf text to file.");
         e.printStackTrace();
      }
   }

   public File getZipFile()
   {
      DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
      //get current date time with Date()
      Date date = new Date();
      String curDate = dateFormat.format(date);

      String path = System.getProperty("user.home") + mPathSep
         + "efresh-tmp" + mPathSep + mName + mPathSep +
         mZipName + ".zip";
      try
      {

         FileInputStream pdf = new FileInputStream(mPDFFile);

         FileInputStream csv = new FileInputStream(mDestCSV);
         FileOutputStream mStream = new FileOutputStream(path);

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
      try
      {
         Shell temp = new Shell();
         temp.remove(new File(mDestCSV));
      }
      catch (IOException e)
      {
         System.out.println("No File to remove");
      }

      return new File(path);
   }
}
