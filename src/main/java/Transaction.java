/*
 * File: Transaction.java
 * A file that implements a Transaction class, used in
 * CSE 8B PA3.
 */
import java.util.Date;

/**
 * A class to represent credit card transactions
 * @author Gustavo Umbelino, Heitor Schueroff, Christine Alvarado
 *
 * YOU DO NOT NEED TO MODIFY THIS FILE
 */
public class Transaction {

  private String transactionNumber; // Transaction id
  private String cardNumber;        // Credit card account number
  private Date date;                // Date of the transaction
  private String vendor;            // The vendor
  private double purchaseAmount;    // How much was charged
  private boolean isValid;          // Was the transaction allowed?
  private String invalidReason;     // If transaction is invalid, the reason why

  /**
   * @param tn
   * @param cn
   * @param dt
   * @param vnd
   * @param pamt
   */
  public Transaction(String tn, String cn, Date dt, String vnd, double pamt) {
    this.transactionNumber = tn;
    this.cardNumber = cn;
    this.date = dt;
    this.vendor = vnd;
    this.purchaseAmount = pamt;
    this.isValid = true;  // Transactions are valid until they are denied
  }

  /**
   * Set this transaction to invalid
   *
   * @param reason The reason the transaction was denied
   */
  public void denyTransaction(String reason) {
    isValid = false;
    invalidReason = reason;
  }

  /**
   * @return Reason why this transaction was denied
   */
  public String getDenialReason() {
    if (isValid) {
      System.out.println("WARNING: Asked for a denial reason " +
          "on a valid transaction");
    }
    return invalidReason;
  }

  @Override
  public String toString() {
    return "$" + purchaseAmount + " purchase at " +
        vendor + " with card ending in " + cardNumber.substring(cardNumber.length() - 4);
  }

  /**
   * @return Number of the card used in this transaction
   */
  public String getCardNumber() {
    return cardNumber;
  }

  /**
   * @return Date of this transaction
   */
  public Date getDate() {
    return date;
  }

  /**
   * @return Number of this transaction
   */
  public String getTransactionNumber() {
    return transactionNumber;
  }

  /**
   * @return Vendor where transaction was made 
   */
  public String getVendor() {
    return vendor;
  }

  /**
   * @return Purchase amount of this transaction
   */
  public double getPurchaseAmount() {
    return purchaseAmount;
  }

  /**
   * @return True if transaction is valid, false otherwise
   */
  public boolean isValid() {
    return isValid;
  }
  
}
