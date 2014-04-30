package efresh.service;

import java.math.BigInteger;

/**
 * Gets the square root of the function
 * @author Adam Harper
 */
public class SquareRoot
{
   /**
    * Square rooted value
    */
   private BigInteger yVal;

   /**
    * Creates a new SquareRoot object.
    *
    * @param x Big Integer to square root
    */
   public SquareRoot(BigInteger x)
   {
      if (x.compareTo(BigInteger.ZERO) < 0)
      {
         throw new IllegalArgumentException("Negative argument.");
      }

      if ((x == BigInteger.ZERO) || (x == BigInteger.ONE))
      {
         yVal = x;
      }

      BigInteger two = BigInteger.valueOf(2L);
      BigInteger y;

      for (y = x.divide(two); y.compareTo(x.divide(y)) > 0;
             y = ((x.divide(y)).add(y)).divide(two))
         ;

      yVal = y;
   }

   /**
    * Allows other classes to access the value
    *
    * @return square root value
    */
   BigInteger getValue()
   {
      return yVal;
   }
}
