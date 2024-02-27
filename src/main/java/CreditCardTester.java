import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreditCardTester {

  private static final String DATE_FORMAT = "yyyy-MM-dd";

  // Card info
  private static final String CARD_NUMBER = "1234432156788765";
  private static final String ACCOUNT_HOLDER = "Jimmy Neutron";
  private static final double CURR_BALANCE = 123.01;

  // Transactions general info
  private static final String DATE = "2017-04-10";

  // Transaction 1 info
  private static final String TRANS_NUMBER_CHEAP = "1";
  private static final double VAL_CHEAP = 8.35;
  private static final String VND_CHEAP = "Dollar Tree";


  // Transaction 2 info
  private static final String TRANS_NUMBER_EXPNS = "2";
  private static final double VAL_EXPNS = 5870.55;
  private static final String VND_EXPNS = "Bookstore";

  // main function calls tester
  public static void main(String[] args) {
    testCreditCard();
  }

  private static void testCreditCard() {

    // Formatter to parse the date object
    DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    // Create new credit card instance, you will probably see a compile
    // error here if you have not implemented the constructor
    CreditCard card = new CreditCard(CARD_NUMBER, ACCOUNT_HOLDER);

    // Testing getCardNumber
    System.out.print("TEST getCardNumber: ");
    try {
      String num = card.getCardNumber();
      if (num.equals(CARD_NUMBER)) {
        System.out.println("PASSED!");
      } else {
        System.out.println("FAILED - Expected: " + CARD_NUMBER + ", Got: " + num);
      }
    } catch (NullPointerException e) {
      System.out.println("EXCEPTION! Make sure your CreditCard constructor is correct");
      //e.printStackTrace(); // uncomment to see stack trace
    }

    // Testing getAccountHolder
    System.out.print("TEST getAccountHolder: ");
    try {
      String acc = card.getAccountHolder();
      if (acc.equals(ACCOUNT_HOLDER)) {
        System.out.println("PASSED!");
      } else {
        System.out.println("FAILED - Expected: " + ACCOUNT_HOLDER + ", Got: " + acc);
      }
    } catch (NullPointerException e) {
      System.out.println("EXCEPTION! Make sure your CreditCard constructor is correct");
      //e.printStackTrace(); // uncomment to see stack trace
    }

    // Testing getCurrentBalance
    System.out.print("TEST getCurrentBalance: ");
    try {
      double balnc = card.getCurrentBalance();
      if (balnc == 0.0) {
        System.out.println("PASSED!");
      } else {
        System.out.println("FAILED - Expected: 0.0, Got: " + balnc);
      }
    } catch (NullPointerException e) {
      System.out.println("EXCEPTION! Make sure your CreditCard constructor is correct");
      //e.printStackTrace(); // uncomment to see stack trace
    }

    // Testing getCurrentBalance
    System.out.print("TEST getCurrentTransactions: ");
    try {
      ArrayList<Transaction> transactions = card.getCurrentTransactions();
      int sizet = transactions.size();
      if (sizet == 0) {
        System.out.println("PASSED!");
      } else {
        System.out.println("FAILED - Expected size: 0, Got: " + sizet);
      }
    } catch (NullPointerException e) {
      System.out.println("EXCEPTION! Make sure your CreditCard constructor is correct");
      //e.printStackTrace(); // uncomment to see stack trace
    }

    // Testing setCurrentBalance
    System.out.print("TEST setCurrentBalance: ");
    try {
      card.setCurrentBalance(CURR_BALANCE);
      double balnc = card.getCurrentBalance();
      if (balnc == CURR_BALANCE) {
        System.out.println("PASSED!");
      } else {
        System.out.println("FAILED - Expected: " + CURR_BALANCE + ", Got: " + balnc);
      }
    } catch (NullPointerException e) {
      System.out.println("EXCEPTION! Make sure your CreditCard constructor is correct");
      //e.printStackTrace(); // uncomment to see stack trace
    }

    // AccountManager validates transactions
    // At this points this transaction should be valid
    // Create new valid transaction that should go through
    Transaction trans_cheap = null;
    try {
      Date date = dateFormat.parse(DATE);
      trans_cheap = new Transaction(TRANS_NUMBER_CHEAP, CARD_NUMBER, date, VND_CHEAP, VAL_CHEAP);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    // Testing processTransaction1
    System.out.print("TEST processTransaction1: ");
    try {
      boolean succ = card.processTransaction(trans_cheap);
      if (succ) {
        System.out.println("PASSED!");
      } else {
        System.out.println("FAILED - Transaction of $" + VAL_CHEAP + " should have been approved");
      }

    } catch (Exception e) {
      //e.printStackTrace(); // uncomment to see stack trace
      System.out.println("EXCEPTION!");
    }

    // Create new valid transaction that should NOT go through
    Transaction trans_expns = null;
    try {
      Date date = dateFormat.parse(DATE);
      trans_expns = new Transaction(TRANS_NUMBER_EXPNS, CARD_NUMBER, date, VND_EXPNS, VAL_EXPNS);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    // Testing processTransaction2
    System.out.print("TEST processTransaction2: ");
    try {
      boolean succ = card.processTransaction(trans_expns);
      if (succ) {
        System.out.println("FAILED - Transaction of $" + VAL_EXPNS + " should have NOT been approved");
      } else {
        System.out.println("PASSED!");
      }

    } catch (Exception e) {
      System.out.println("EXCEPTION!");
      //e.printStackTrace(); // uncomment to see stack trace
    }

    // Testing processTransaction3
    System.out.print("TEST processTransaction3: ");
    try {
      ArrayList<Transaction> transactions = card.getCurrentTransactions();
      int sizet = transactions.size();
      if (sizet == 1) {
        System.out.println("PASSED!");
      } else {
        System.out.println("FAILED - Expected number of transactions: 1, Got: " + sizet);
      }

    } catch (Exception e) {
      System.out.println("EXCEPTION!");
      //e.printStackTrace(); // uncomment to see stack trace
    }

    // Visual test closeMonth output
    System.out.println("TEST closeMonth: does your string look correct?\n");
    try {
      System.out.println(card.closeMonth());
    } catch (Exception e) {
      System.out.println("EXCEPTION!");
      //e.printStackTrace(); // uncomment to see stack trace
    }
  }

}
