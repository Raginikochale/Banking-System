package banking_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Accounts {
	private Connection connection;
	private Scanner scanner;

	public Accounts(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

	public long open_account(String email) {
		if (!account_exist(email)) {
			String open_account_query = "INSERT INTO Accounts VALUES(?, ?, ?, ?, ?)";
			scanner.nextLine();
			System.out.println("Enter Full Name: ");
			String full_name = scanner.nextLine();
			System.out.println("Enter Initial Amount: ");
			double balance = scanner.nextDouble();
			scanner.nextLine();
			System.out.println("Enter Security Pin: ");
			int security_pin = scanner.nextInt();
			try {
				long account_number = generateAccountNumber();
				PreparedStatement ps = connection
						.prepareStatement(open_account_query);
				ps.setLong(1, account_number);
				ps.setString(2, full_name);
				ps.setString(3, email);
				ps.setDouble(4, balance);
				ps.setInt(5, security_pin);
				int rowsAffected = ps.executeUpdate();
				if (rowsAffected > 0) {
					return account_number;
				} else {
					System.out.println("Account Creation failed!!");
				}
			} catch (Exception e) {
				System.out.println("account.open_account(): " + e);
			}
		}
		throw new RuntimeException("Account Already Exist");
	}

	public long getAccount_number(String email) {
		String query = "SELECT account_number from Accounts WHERE email = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getLong("account_number");
			}
		} catch (Exception e) {
			System.out.println("account.getAccount_number");
		}
		throw new RuntimeException("Account Number Doesn't Exist!");
	}

	public long generateAccountNumber() {
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT account_number from Accounts ORDER BY account_number DESC");
			if (rs.next()) {
				long last_account_number = rs.getLong("account_number");
				return last_account_number + 1;
			} else {
				return 10000100;
			}
		} catch (Exception e) {
			System.out.println("account.generateAccountNumber():" + e);
		}
		return 10000100;
	}

	public boolean account_exist(String email) {
		String query = "SELECT account_number from Accounts WHERE email = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("account.account_exist(): " + e);
		}
		return false;

	}

}
