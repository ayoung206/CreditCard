/**
 * This class is designed to deal with transactions.
 *
 * Reference: Textbook, Stackoverflow.
 */

import java.util.ArrayList;

/**
 * Provides a capability to purchase items and shows a summary of monthly transactions. {@link
 * #transactions} list will be empty whenever {@link #closeMonth()} is called.
 *
 * @author Yong Hoon Do, yhdo@ucsd.edu
 * @author A Kim, ayk037@ucsd.edu
 * @since Oct 20, 2017
 */
public class CreditCard {

  /**
   * Types of status for each transaction process. It gives a failure message when the transaction
   * denied.
   */
  private enum TransactionProcess {
    OK(""),
    LIMIT_EXCEED("Account Limit Exceeds."),
    INCORRECT_ACCOUNT("The given transaction does not math with the card number.");

    private String message;

    TransactionProcess(String m) {
      message = m;
    }

    public String getMessage() {
      return message;
    }
  }

  private String cardNumber;
  private String accountHolderName;
  private double limit;
  private double currentBalance;
  private double overDraftAmount;
  private double rebateRate;
  private ArrayList<Transaction> transactions;

  /**
   * A monthly purchased amount so that it resets to zero whenever the credit card closes month. It
   * updates at {@link #processTransaction(Transaction)}, and resets the value as zero at {@link
   * #closeMonth()}.
   */
  private double monthylTotalPurchasedAmount;

  /**
   * Gives a monthly summary of purchasing activities and refreshes the current transaction list so
   * that gets ready to track of the next month transaction.
   *
   * @return A monthly summary of the purchasing activities.
   */
  public String closeMonth() {
    StringBuilder builder = new StringBuilder();
    double rebatedAmount = monthylTotalPurchasedAmount * rebateRate;
    currentBalance -= rebatedAmount;

    builder.append("Account: ").append(accountHolderName).append(" ").append(cardNumber.substring(cardNumber.length() - 4)).append("\n");
    builder.append("-----------------------------------------------------").append("\n");
    for (Transaction t : transactions) {
      builder.append(t).append("\n");
    }
    if (transactions.isEmpty()) {
      builder.append("NO TRANSACTIONS FOUND\n");
    }
    builder.append("-----------------------------------------------------").append("\n");

    builder.append("Rebate received: $").append(rebatedAmount).append("\n");
    builder.append("Current Balance: $").append(currentBalance).append("\n");

    if (currentBalance - limit > 0) {
      builder.append("ACCOUNT OVERDRAWN BY: $").append(currentBalance - limit).append("\n");
    }

    // refreshes monthly tracked values.
    transactions.clear();
    monthylTotalPurchasedAmount = 0d;

    return builder.toString();
  }

  /**
   * Initializes the instance variables with its default value.
   *
   * @param cardNumber A valid card number.
   * @param accountHolderName The holder of the credit card.
   */
  public CreditCard(String cardNumber, String accountHolderName) {
    this.cardNumber = cardNumber;
    this.accountHolderName = accountHolderName;
    limit = 5000d;
    currentBalance = 0d;
    rebateRate = .02d;
    overDraftAmount = 1000d;
    monthylTotalPurchasedAmount = 0d;
    transactions = new ArrayList<>();
  }

  /**
   * Updates the current balance and keeps track of the monthly purchased amount up to date as well
   * as current transaction list. It fails to accept the transaction only when the given card number
   * does not match with the CreditCard's instance card number, or it exceeds the limit.
   *
   * @param t A transaction containing a desired purchasing amount.
   * @return Success of the process.
   */
  public boolean processTransaction(Transaction t) {
    if (t == null) {
      return false;
    }

    TransactionProcess process = TransactionProcess.OK;
    if (!t.getCardNumber().equals(getCardNumber())) {
      process = TransactionProcess.INCORRECT_ACCOUNT;
    }

    boolean isExceedingLimit = (overDraftAmount + limit)
        - (currentBalance + t.getPurchaseAmount()) < 0;
    if (process == TransactionProcess.OK && isExceedingLimit) {
      process = TransactionProcess.LIMIT_EXCEED;
    }

    switch (process) {
      case OK:
        transactions.add(t);
        currentBalance += t.getPurchaseAmount();
        monthylTotalPurchasedAmount += t.getPurchaseAmount();
        return true;
      case LIMIT_EXCEED:
      case INCORRECT_ACCOUNT:
        t.denyTransaction(process.getMessage());
      default:
        return false;
    }
  }

  /**
   * The account number associated with the card.
   */
  public String getCardNumber() {
    return cardNumber;
  }

  /**
   * The name of the account holder.
   */
  public String getAccountHolder() {
    return accountHolderName;
  }

  /**
   * The current balance on the card.
   */
  public double getCurrentBalance() {
    return currentBalance;
  }

  /**
   * A reference to the ArrayList of Transactions since the last month was closed.
   */
  public ArrayList<Transaction> getCurrentTransactions() {
    return transactions;
  }

  public void setCurrentBalance(double amt) {
    currentBalance = amt;
  }

  @Override
  public String toString() {
    // This method will work once you implement the other methods
    return this.getClass().getSimpleName() + " " +
        getCardNumber() + " " + getAccountHolder();
  }
}
