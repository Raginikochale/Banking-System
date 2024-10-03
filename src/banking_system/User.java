package banking_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class User {
	private Connection connection;
	private Scanner scanner;

	public User(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

	public void register() {
		scanner.nextLine();
		System.out.println("Full Name: ");
		String full_name = scanner.nextLine();
		System.out.println("Email: ");
		String email = scanner.nextLine();
		System.out.println("Password: ");
		String password = scanner.nextLine();
		if (user_exist(email)) {
			System.out.println("User Already Exists for this Email Address!!");
			return;
		}
		String register_query = "INSERT INTO Users VALUES(?, ?, ?)";
		try {
			PreparedStatement ps = connection.prepareStatement(register_query);
			ps.setString(1, full_name);
			ps.setString(2, email);
			ps.setString(3, password);
			int affectedRow = ps.executeUpdate();
			if (affectedRow > 0) {
				System.out.println("Registration Successfull!");
			} else {
				System.out.println("Registration Failed!");
			}
		} catch (Exception e) {
			System.out.println("user.register(): " + e);
		}
	}

	public String login() {
		scanner.nextLine();
		System.out.println("Email: ");
		String email = scanner.nextLine();
		System.out.println("password: ");
		String password = scanner.nextLine();

		String login_query = "SELECT * FROM Users WHERE email = ? AND password = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(login_query);
			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return email;
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println("user.login(): " + e);
		}
		return null;
	}

	public boolean user_exist(String email) {
		String query = "SELECT * FROM Users WHERE email = ?";
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
			System.out.println("user.user_exist(): " + e);
		}
		return false;
	}

}
