package efresh.system;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.RequestTokenPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.WebAuthSession;

import java.io.File;
import java.io.PrintWriter;

import java.util.Scanner;

/**
 *
 * @author Adam Harper and Jordan Jensen
 */
public class DropBoxApi
{
   /**
    * GERSII's app key
    */
   final static private String APP_KEY = "bwql4agsu6x0b8z";

   /**
    * GERSII's app secret
    */
   final static private String APP_SECRET = "utpzk3ce7v2jnrd";

   /**
    * Defines the access range that GERSII has
    */
   final static private AccessType ACCESS_TYPE = AccessType.DROPBOX;

   /**
    * The api to use to download files, grants access to dropbox
    * via web
    */
   public static DropboxAPI<WebAuthSession> mDBApi;

   /**
    * Creates a new DropBoxApi object.
    *
    * @param value the command
    *
    * @throws Exception - does not connect to drop box
    */
   public DropBoxApi(String value)
      throws Exception
   {
      // Initialize the goods/session
      AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
      WebAuthSession session = new WebAuthSession(appKeys, ACCESS_TYPE);

      // Initialize DropboxAPI object
      mDBApi = new DropboxAPI<WebAuthSession>(session);

      // Get ready for user input
      Scanner input = new Scanner(System.in);

      // Open file that stores tokens, MUST exist as a blank file
      File tokensFile = new File("resources/DropBoxAuth/TOKENS");

      String command = value; //input.next();

      if (command.equals("a"))
      {
         try
         {
            // Present user with URL to allow app access to Dropbox account on
            System.out.println("Please go to this URL and hit \"Allow\": " +
               mDBApi.getSession().getAuthInfo().url);

            AccessTokenPair tokenPair =
               mDBApi.getSession().getAccessTokenPair();

            // Wait for user to Allow app in browser
            System.out.println("Finished allowing?  Enter 'next' if so: ");

            if (input.next().equals("next"))
            {
               RequestTokenPair tokens =
                  new RequestTokenPair(tokenPair.key, tokenPair.secret);
               mDBApi.getSession().retrieveWebAccessToken(tokens);

               PrintWriter tokenWriter = new PrintWriter(tokensFile);
               tokenWriter.println(session.getAccessTokenPair().key);
               tokenWriter.println(session.getAccessTokenPair().secret);
               tokenWriter.close();
               System.out.println("Authentication Successful!");
            }
         }
         catch (DropboxException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      else if (command.equals("t"))
      {
         Scanner tokenScanner = new Scanner(tokensFile); // Initiate Scanner to read tokens from TOKEN file
         String ACCESS_TOKEN_KEY = tokenScanner.nextLine(); // Read key
         String ACCESS_TOKEN_SECRET = tokenScanner.nextLine(); // Read secret
         tokenScanner.close(); //Close Scanner

         //Re-auth
         AccessTokenPair reAuthTokens =
            new AccessTokenPair(ACCESS_TOKEN_KEY, ACCESS_TOKEN_SECRET);
         mDBApi.getSession().setAccessTokenPair(reAuthTokens);
         // System.out.println("Re-authentication Sucessful!");

         //Run test command
         System.out.println("Welcome, " + mDBApi.accountInfo().displayName);
      }
   }

   /**
    * getter for the api
    *
    * @return mDBApi the current web auth
    */
   public DropboxAPI getDBAPI()
   {
      return mDBApi;
   }
}
