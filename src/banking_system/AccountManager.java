package banking_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class AccountManager {
	private Connection connection;
	private Scanner scanner;

	public AccountManager(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

	public void credit_money(long account_number) throws Exception {
		scanner.nextLine();
		System.out.print("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter Security Pin: ");
		int security_pin = scanner.nextInt();

		try {
			connection.setAutoCommit(false);
			if (account_number != 0) {
				PreparedStatement ps = connection
						.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? and security_pin = ? ");
				ps.setLong(1, account_number);
				ps.setInt(2, security_pin);
				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
					PreparedStatement ps1 = connection
							.prepareStatement(credit_query);
					ps1.setDouble(1, amount);
					ps1.setLong(2, account_number);
					int rowsAffected = ps1.executeUpdate();
					if (rowsAffected > 0) {
						System.out.println("Rs." + amount
								+ " credited Successfully");
						connection.commit();
						connection.setAutoCommit(true);
						return;
					} else {
						System.out.println("Transaction Failed!");
						connection.rollback();
						connection.setAutoCommit(true);
					}
				} else {
					System.out.println("Invalid Security Pin!");
				}
			}
		} catch (Exception e) {
			System.out.println("accountManager.credit_money():" + e);
		}
		connection.setAutoCommit(true);
	}

	public void debit_money(long account_number) throws Exception {
		scanner.nextLine();
		System.out.print("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter Security Pin: ");
		int security_pin = scanner.nextInt();
		try {
			connection.setAutoCommit(false);
			if (account_number != 0) {
				PreparedStatement ps = connection
						.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? and security_pin = ? ");
				ps.setLong(1, account_number);
				ps.setInt(2, security_pin);
				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					double current_balance = rs.getDouble("balance");
					if (amount <= current_balance) {
						String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
						PreparedStatement ps1 = connection
								.prepareStatement(debit_query);
						ps1.setDouble(1, amount);
						ps1.setLong(2, account_number);
						int rowsAffected = ps1.executeUpdate();
						if (rowsAffected > 0) {
							System.out.println("Rs." + amount
									+ " debited Successfully");
							connection.commit();
							connection.setAutoCommit(true);
							return;
						} else {
							System.out.println("Transaction Failed!");
							connection.rollback();
							connection.setAutoCommit(true);
						}
					} else {
						System.out.println("Insufficient Balance!");
					}
				} else {
					System.out.println("Invalid Pin!");
				}
			}
		} catch (Exception e) {
			System.out.println("accountManager.depit_money(): " + e);
		}
		connection.setAutoCommit(true);
	}

	public void transfer_money(long sender_account_number) throws Exception {
		scanner.nextLine();
		System.out.print("Enter Receiver Account Number: ");
		long receiver_account_number = scanner.nextLong();
		System.out.print("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter Security Pin: ");
		int security_pin = scanner.nextInt();
		try {
			connection.setAutoCommit(false);
			if (sender_account_number != 0 && receiver_account_number != 0) {
				PreparedStatement ps = connection
						.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ? ");
				ps.setLong(1, sender_account_number);
				ps.setInt(2, security_pin);
				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					double current_balance = rs.getDouble("balance");
					if (amount <= current_balance) {

						// Write debit and credit queries
						String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
						String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";

						// Debit and Credit prepared Statements
						PreparedStatement creditPs = connection
								.prepareStatement(credit_query);
						PreparedStatement debitPs = connection
								.prepareStatement(debit_query);

						// Set Values for debit and credit prepared statements
						creditPs.setDouble(1, amount);
						creditPs.setLong(2, receiver_account_number);
						debitPs.setDouble(1, amount);
						debitPs.setLong(2, sender_account_number);
						int rowsAffected1 = debitPs.executeUpdate();
						int rowsAffected2 = creditPs.executeUpdate();
						if (rowsAffected1 > 0 && rowsAffected2 > 0) {
							System.out.println("Transaction Successful!");
							System.out.println("Rs." + amount
									+ " Transferred Successfully");
							connection.commit();
							connection.setAutoCommit(true);
							return;
						} else {
							System.out.println("Transaction Failed");
							connection.rollback();
							connection.setAutoCommit(true);
						}
					} else {
						System.out.println("Insufficient Balance!");
					}
				} else {
					System.out.println("Invalid Security Pin!");
				}
			} else {
				System.out.println("Invalid account number");
			}
		} catch (Exception e) {
			System.out.println("accountManager.transfer_Money():" + e);
		}
		connection.setAutoCommit(true);
	}

	public void getBalance_money(long account_number) {
		scanner.nextLine();
		System.out.print("Enter Security Pin: ");
		int security_pin = scanner.nextInt();
		try {
			PreparedStatement ps = connection
					.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ? AND security_pin = ?");
			ps.setLong(1, account_number);
			ps.setInt(2, security_pin);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				double balance = rs.getDouble("balance");
				System.out.println("Balance: " + balance);
			} else {
				System.out.println("Invalid Pin!");
			}
		} catch (Exception e) {
			System.out.println("accountManager.getBalance(): " + e);
		}
	}
}
