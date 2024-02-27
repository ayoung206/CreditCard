import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Yong Hoon Do, yhdo@ucsd.edu
 * @author A Kim, ayk037@ucsd.edu
 * @since Oct 20, 2017
 */
public class AccountManager {

  // Used by the methods we provide for reading in the transactions
  // from a file.  You don't need to worry about this, and please
  // do not change it.
  private static final String DATE_FORMAT = "yyyy-MM-dd";

  // The accounts managed by this AccountManager object.
  // This is public only to make grading your assignments easier.
  // It would be better design to make it private.
  public HashMap<String, CreditCard> accounts;

  /**
   * Construct a new, empty, AccountManager object. To load accounts, use the loadAccounts method.
   */
  public AccountManager() {
    accounts = new HashMap<>();
  }

  /**
   * Uses Luhn's Algorithm to validate card numbers.
   *
   * @param cardNum Card number to validate
   * @return True if card number is valid, false otherwise.
   */
  public static boolean validateCardNumber(String cardNum) {
    if (cardNum == null || cardNum.isEmpty()) {
      return false;
    }

    String[] arr = cardNum.split("");
    int[] cardNumber = new int[arr.length];
    for (int i = 0; i < arr.length; i++) {
      if (!Character.isDigit(arr[i].charAt(0))) {
        // we only parse a digit, but not parsing other characters.
        return false;
      }
      cardNumber[i] = Integer.parseInt(arr[i]);
    }

    for (int i = cardNumber.length - 2; i >= 0; i -= 2) {
      cardNumber[i] = 2 * cardNumber[i];
      if (cardNumber[i] >= 10) {
        cardNumber[i] = cardNumber[i] - 9;
      }
    }

    int sum = 0;
    for (int i = 0; i < cardNumber.length - 1; i++) {
      sum += cardNumber[i];
    }
    sum = sum * 9;
    sum = sum % 10;

    return cardNumber[cardNumber.length - 1] == sum;
  }

  public ArrayList<String> loadAccounts(String filename) {
    ArrayList<String> invalid = new ArrayList<>();
    try (Scanner scanner = new Scanner(new File(filename))) {
      // Skip the header
      scanner.nextLine();

      // Read the credit card data
      while (scanner.hasNextLine()) {
        String[] data = scanner.nextLine().split(",");
        System.out.println("------------------------------------");
        String cardNumber = data[0];
        System.out.println("Card number: " + cardNumber);
        String accountHolder = data[1];
        System.out.println("Account holder: " + accountHolder);
        String bal = data[2];
        System.out.println("Balance: " + bal);
        double cardBalance = Double.parseDouble(bal);
        if (validateCardNumber(cardNumber)) {
          CreditCard card = new CreditCard(cardNumber, accountHolder);
          card.setCurrentBalance(cardBalance);
          accounts.put(cardNumber, card);
        } else {
          invalid.add(cardNumber);
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println("Accounts file not found");
      e.printStackTrace();
    }

    System.out.println("------------------------------------");
    return invalid;
  }

  /**
   * Process a list of transactions from a file. As each transaction is processed, the balance of
   * the corresponding credit card is adjusted if the transaction is allowed.  If the transaction is
   * not valid, the transaction is denied with the reason for the denial.
   *
   * @param transactionsFile The name of the file where the transactions are listed.
   * @return A list of denied Transactions, modified to indicate why they were denied.
   */
  public ArrayList<Transaction>
  processTransactionsFromFile(String transactionsFile) {
    ArrayList<Transaction> transactions = readTransactionsFromFile(transactionsFile);
    return processTransactions(transactions);
  }

  /**
   * Process a list of transactions. As each transaction is processed, the balance of the
   * corresponding credit card is adjusted if the transaction is allowed.  If the transaction is not
   * valid, the transaction is denied with the reason for the denial.
   *
   * @param transactions A list of transactions.
   * @return A list of denied Transactions, modified to indicate why they were denied.
   */
  private ArrayList<Transaction>
  processTransactions(ArrayList<Transaction> transactions) {
    // The transactions that are denied
    ArrayList<Transaction> denied = new ArrayList<>();
    for (Transaction t : transactions) {
      if (validateCardNumber(t.getCardNumber())) {
        // Get the card object corresponding to the card number
        CreditCard c = accounts.getOrDefault(t.getCardNumber(), null);
        if (c != null && !c.processTransaction(t)) {
          denied.add(t);
        }
      } else {
        t.denyTransaction("Invalid account number " + t.getCardNumber());
        denied.add(t);
      }
    }
    return denied;
  }

  /**
   * Read transactions from a file.
   *
   * YOU DO NOT NEED TO AND SHOULD NOT CHANGE THIS METHOD
   *
   * @param transactionsFilename Name of transactions data file
   * @return A list of transactions from the file.
   */
  private ArrayList<Transaction>
  readTransactionsFromFile(String transactionsFilename) {
    ArrayList<Transaction> transactions = new ArrayList<>();
    try { // Read data
      Scanner scanner = new Scanner(new File(transactionsFilename));

      // Use comma as the delimiter
      scanner.useDelimiter(",|\n");

      // Skip header
      scanner.nextLine();

      // Formatter to parse the date object
      DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

      while (scanner.hasNext()) {
        String transactionNumber = scanner.next();
        String cardNumber = scanner.next();
        Date date = dateFormat.parse(scanner.next());
        String vendor = scanner.next();
        double purchaseAmount = scanner.nextDouble();
        transactions.add(new Transaction(
            transactionNumber, cardNumber, date, vendor, purchaseAmount));
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      System.err.println("Could not read from transaction file");
    } catch (InputMismatchException e) {
      System.err.println("Purchase amount is not a double type");
      System.err.println("Skipping the rest of the file");
    } catch (ParseException e) {
      System.err.println("Bad date format: " + e.getMessage());
      System.err.println("Skipping the rest of the file");
    }
    return transactions;
  }

  /**
   * This method traverses the list of credit cards, produces for each their end-of-month statement
   * by calling their `closeMonth()` method, and records each report to the file named `filename`.
   * Statements are separated by a long string of asterisks.
   *
   * @param filename Name of the file on which to write the statements.
   */
  public void generateStatements(String filename) {
    try {
      PrintWriter statements = new PrintWriter(new File(filename));
      statements.write("******************************************************\n");
      for (String cardNum : accounts.keySet()) {
        CreditCard card = accounts.get(cardNum);
        String statement = card.closeMonth(); // closeMonth generates a statement
        statements.write(statement);
        statements.write("******************************************************\n");
      }
      statements.flush();
      statements.close();
    } catch (IOException e) {
      System.err.println("There was a problem opening the statements file");
    }
  }

  /**
   * Implement the program described in PA3
   *
   * @param args The three command line arguments: The filename that contains the account
   * information The filename that contains the transactions The filename where the account
   * statements will be written
   */
  public static void main(String[] args) {
    // YOU DO NOT NEED TO CHANGE THIS METHOD.

    if (args.length < 3) {
      System.err.println("Invalid number of arguments");
      System.exit(1);
    }

    String creditCardsFilename = args[0];
    String transactionsFilename = args[1];
    String statementsFilename = args[2];

    AccountManager accountManager = new AccountManager();
    System.out.println("Loading accounts...");
    ArrayList<String> invalidAccounts =
        accountManager.loadAccounts(creditCardsFilename);

    if (invalidAccounts.size() > 0) {
      System.out.println("The following " + invalidAccounts.size() +
          " account numbers are invalid. No accounts created:");
      for (String acct : invalidAccounts) {
        System.out.println("\t" + acct);
      }
    }

    ArrayList<Transaction> denied =
        accountManager.processTransactionsFromFile(transactionsFilename);

    if (denied.size() > 0) {
      System.out.println("The following transactions were denied: ");
      for (Transaction t : denied) {
        System.out.println("-----------------------------------------------------");
        System.out.println(t.toString());
        System.out.println("Reason: " + t.getDenialReason());
      }
      System.out.println("-----------------------------------------------------");
    }

    System.out.println("Writing statements to " + statementsFilename + " ...");
    accountManager.generateStatements(statementsFilename);
  }
}
