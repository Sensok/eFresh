package efresh.service;


/**
 * Splits up strings
 * @author Adam Harper
 */
public final class StringSplit
{
   /**
    * Value of the password and username combined
    */
   private String fullString;

   /**
    * Username of the current user
    */
   private String username;

   /**
    * Password of the current user
    */
   private String password;

   /**
    * Creates a new StringSplit object.
    *
    * @param pString fullstring of username and password combined
    */
   public StringSplit(String pString)
   {
      fullString = pString;
      splitter();
   }

   /**
    * Splits the full string into the username and password
    */
   public void splitter()
   {
      String[] splits = fullString.split(":::");

      int count = 0;

      for (String asset : splits)
      {
         if (count == 0)
         {
            username = asset;
         }
         else
         {
            password = asset;
         }

         count++;
      }
   }

   /**
    * Gets the value of username
    *
    * @return username
    */
   public String getUser()
   {
      return username;
   }

   /**
    * Gets the value of password
    *
    * @return password
    */
   public String getPass()
   {
      return password;
   }
}
