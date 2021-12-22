package _CSI2132._labs;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeConsole {

	static Scanner scanner;
	static String DB="";
	static String USER="";
	static String PASS="";
	/**employee.userID*/
	static String empUID;
	/**employee.hotelID*/
	static String empHID;
	/**employee.name*/
	static String empName;

	public static void main(String[] args) {
		try {
			Class.forName("org.postgresql.Driver");
			Connection db = DriverManager.getConnection("jdbc:postgresql://web0.site.uottawa.ca:15432/"+DB,USER,PASS);
			System.out.println("Successfully Connected to the DB.");
			scanner = new Scanner(System.in);
			loginEmployee(db);
			while (true) {
				String option = "";
				while (option.length()<=0 || option.charAt(0)<'2' || option.charAt(0)>'8') {
					System.out.println();
					System.out.println("-----------------------------");
					System.out.println("Please choose an option (2-8)");
					System.out.println("2) Rent a room with booking");
					System.out.println("3) Rent a room without booking");
					System.out.println("4) Insert payment for customer");
					System.out.println("5) Display (currently) available rooms");
					System.out.println("6) Display (currently) reserved rooms");
					System.out.println("7) Display (all) booked rooms");
					System.out.println("8) Exit");
					System.out.println("-----------------------------");
					option = scanner.nextLine();
				}
				switch (option) {
					case "2" -> rentWithBooking(db);
					case "3" -> rentWithoutBooking(db);
					case "4" -> insertPayment(db);
					case "5" -> displayFreeRooms(db);
					case "6" -> displayOccuRooms(db);
					case "7" -> displayBookedRooms(db);
					case "8" -> System.exit(0);
				}

			}

		} catch (ClassNotFoundException | SQLException exception) {
			System.out.println("Connection Failed");
			exception.printStackTrace();
		}
		scanner.close();
	}

	private static void loginEmployee(Connection db) throws SQLException {
		//get employee userID
		System.out.print("Please enter you userID: ");
		empUID = scanner.nextLine();

		//get pw from DB
		Statement st = db.createStatement();
		st.execute("select password from \"Project Hotel\".employee where userid = " + empUID);
		ResultSet rs = st.getResultSet();
		String pw = "";
		if(rs.next()) {pw = rs.getString("password");}

		System.out.print("Please enter your password: ");
		String password = scanner.nextLine();

		if(password.equals(pw)) {
			System.out.println("Login successful");

			//get hotelID, name
			st = db.createStatement();
			st.execute("select hotelID, name from \"Project Hotel\".employee where userid = " + empUID);
			rs = st.getResultSet();
			if(rs.next()) {
				empHID = rs.getString("hotelID");
				empName = rs.getString("name");
			}

			System.out.println("Welcome back, " + empName);
		} else {
			System.out.println("Login failed");
			System.exit(0);
		}
	}

	private static void rentWithBooking(Connection db) throws SQLException {
		//find userIDs of those who have booking
		System.out.println("Here are the users that booked a room: ");
		Statement st = db.createStatement();
		st.execute("SELECT distinct(userID)\n" +
				"FROM \"Project Hotel\".reservationcurrent\n" +
				"WHERE isrented = false AND hotelID = " + empHID);
		ResultSet rs = st.getResultSet();

		//list userIDs
		ArrayList<String> users = new ArrayList<>();
		while (rs.next()) { users.add(rs.getString("userID"));}
		for (String user : users) { System.out.print(user + ", ");}
		System.out.println();

		//select userID
		String selectedUser = "";
		while (!users.contains(selectedUser)) {
			System.out.print("Please select a userID from the list to reserve their room: ");
			selectedUser = scanner.nextLine();
		}

		//list booked rooms
		st = db.createStatement();
		st.execute("SELECT distinct(roomID)\n" +
			"FROM \"Project Hotel\".reservationcurrent\n"
			+ "WHERE isrented = false AND hotelid = " + empHID +" AND userid =" + selectedUser+"\n"
			+ "AND roomID IN (SELECT roomIDoccu(LOCALTIMESTAMP,LOCALTIMESTAMP,"+empHID+"))"); //!! HEURISTIC
		rs = st.getResultSet();
		ArrayList<String> rooms = new ArrayList<>();
		if(rooms.isEmpty()) {System.out.println("!! There are no rooms ready to book yet (did you arrive early?)"); return;}
		while (rs.next()) { rooms.add(rs.getString("roomID"));}
		for (String room : rooms) { System.out.print(room + ", ");}
		System.out.println();

		//select room
		String selectedRoom = "";
		while (!rooms.contains(selectedRoom)) {
			System.out.print("Please select a roomID from the list of booked rooms: ");
			selectedRoom = scanner.nextLine();
		}

		System.out.print("Please enter the number of occupants: ");
		String occupants = scanner.nextLine();

		//set rented status of room
		st = db.createStatement();
		st.execute("set search_path=\"Project Hotel\";");
		st.execute("UPDATE \"Project Hotel\".reservationcurrent\n"+
			"SET isrented = true, occupants = "+occupants+"\n"+
			"WHERE ("+
			"roomID = "+selectedRoom+" AND "+
			"userID = "+selectedUser+" AND "+
			"roomID IN (SELECT roomIDoccu(LOCALTIMESTAMP,LOCALTIMESTAMP,"+empHID+")))"); //!! HEURISTIC

		System.out.println("Successfully updated the rental status.");
	}

	private static void rentWithoutBooking(Connection db) throws SQLException {
		//list userIDs
		Statement st = db.createStatement();
		st.execute("select userid\n from \"Project Hotel\".customer");
		ResultSet rs = st.getResultSet();
		ArrayList<String> users = new ArrayList<>();
		while (rs.next()) { users.add(rs.getString("userID"));}

		//select userID
		System.out.print("Please enter the customer userID: ");
		String userID = scanner.nextLine();
		while (!users.contains(userID)) {
			System.out.println("The userID could not be found.");
			System.out.print("Please enter the customer userID: ");
			userID = scanner.nextLine();
		}

		//select date
		String dateBeg = "";
		String dateEnd = "";
		while (dateEnd == "") {
			System.out.print("Please enter the end date (format YYYY-MM-DD): ");
			dateBeg = (new Timestamp(System.currentTimeMillis())).toString();
			dateEnd = scanner.nextLine()+" 00:00:00.000";
			System.out.println("dateBeg: " + dateBeg);
			System.out.println("dateEnd: " + dateEnd);
			try{
			if(!Timestamp.valueOf(dateEnd).after(Timestamp.valueOf(dateBeg))) {
				System.out.println("!! Make sure date is after current time !!");
				dateEnd = "";
			}}catch(Exception err){
				err.printStackTrace();
				System.out.println("!! Make sure date is in format: YYYY-MM-DD !!");
				dateEnd = "";
			}
		}

		//find rooms available from dateBeg to dateEnd
		st = db.createStatement();
		st.execute("SET search_path='Project Hotel';");
		st.execute("SELECT roomIDfree('"+dateBeg+"', '"+dateEnd+"', "+empHID+") AS roomID;");
		rs = st.getResultSet();

		//list available rooms
		ArrayList<String> rooms = new ArrayList<>();
		while (rs.next()) {rooms.add(rs.getString("roomID"));}
		if(rooms.isEmpty()){System.out.println("!! no rooms available (try booking a different time-interval)"); return;}
		for (int i = 0; i < rooms.size(); i++) {
			if (i % 10 == 0) { System.out.println();}
			System.out.print(rooms.get(i) + ", ");
		}
		System.out.println();

		//select room
		String selectedRoom = "";
		while (!rooms.contains(selectedRoom)) {
			System.out.print("Please select a roomID from the list of free rooms: ");
			selectedRoom = scanner.nextLine();
		}

		//create reservation
		st = db.createStatement();
		st.execute("set search_path =\"Project Hotel\"");
		st = db.createStatement();
		st.execute("insert into \"Project Hotel\".reservationcurrent (userid,hotelid,roomid,isrented,bookingnum,datebeg,occupants,dateend) " +
				"values(" + userID + "," + "(SELECT hotelID FROM \"Project Hotel\".Room WHERE (roomID =" + selectedRoom + "))"
				+ "," + selectedRoom + "," + true + "," + 1 + ",'" + dateBeg + "'," + 1 + ",'" + dateEnd + "')");
		System.out.println("Successfully reserved the room " + selectedRoom + " for customer " + userID + ".");
	}

	private static void insertPayment(Connection db) throws SQLException {
		//get userIDs with payment due
		Statement st = db.createStatement();
		st.execute("SELECT distinct(userID)\n"
				+ "FROM \"Project Hotel\".reservationcurrent\n"
				+ "WHERE paymentDue > 0\n"
				+ "AND hotelid = "+empHID);
		ResultSet rs = st.getResultSet();

		//list users
		ArrayList<String> users = new ArrayList<>();
		while (rs.next()) { users.add(rs.getString("userID"));}
		if(users.isEmpty()){System.out.println("No users with payment due!!"); return;}
		for (String user : users) { System.out.print(user + ", ");}
		System.out.println();

		//select user
		String selectedUser = "";
		while (!users.contains(selectedUser)) {
			System.out.print("Please select a userID from the list to pay the bills: ");
			selectedUser = scanner.nextLine();
		}

		//list booking numbers
		st = db.createStatement();
		st.execute("SELECT bookingNum,dateBeg,dateEnd,paymentDue\n"
				+ "FROM \"Project Hotel\".reservationcurrent\n"
				+ "WHERE paymentDue > 0\n"
				+ "AND userid = "+selectedUser+"\n"
				+ "AND hotelid = "+empHID);
		rs = st.getResultSet();
		ArrayList<String> bnums = new ArrayList<>();
		while (rs.next()) {
			bnums.add(rs.getString("bookingNum"));
			System.out.print(bnums.get(bnums.size()-1)
				+",\t"+rs.getString("dateBeg")
				+",\t"+rs.getString("dateEnd")
				+",\t"+rs.getString("paymentDue")
				+"\n");
		}
		System.out.println();

		//select booking number
		String selectedbnum = "";
		while (!bnums.contains(selectedbnum)) {
			System.out.print("Please select a reservation booking-number the customer would like to pay for from the list: ");
			selectedbnum = scanner.nextLine();
		}

		//print payment due
		st = db.createStatement();
		st.execute("SELECT paymentdue\n" +
				"FROM \"Project Hotel\".reservationcurrent\n" +
				"WHERE userid =" + selectedUser + "AND bookingNum = " + selectedbnum);
		rs = st.getResultSet();
		String paymentdue = "";
		if(rs.next()) { paymentdue = rs.getString("paymentDue");}

		//get response
		String ans = "";
		while (!ans.equals("Y") && !ans.equals("N")) {
			System.out.println("The amount to pay is " + paymentdue + ", would you like to pay the bills now? (Y/N)");
			ans = scanner.nextLine();
		}

		//pay bill
		if (ans.equals("Y")) {
			st = db.createStatement();
			st.execute("UPDATE \"Project Hotel\".reservationcurrent\n" +
					"SET paymentdue = 0\n" +
					"WHERE userid =\n" + selectedUser +"AND bookingNum =" + selectedbnum);
			System.out.println("The bills have been payed.");
			st.execute("SELECT check_rescur_expiry()");
			System.out.println("Payed reservations cleaned.");
		}

	}

	private static void displayFreeRooms(Connection db) throws SQLException {
		System.out.print("Here are the available rooms: ");
		Statement st = db.createStatement();
		st.execute("set search_path=\"Project Hotel\";");
		st.execute("SELECT roomIDfree(LOCALTIMESTAMP,LOCALTIMESTAMP,"+empHID+") AS roomID;");
		ResultSet rs = st.getResultSet();
		while (rs.next()) { System.out.print(rs.getString("roomID") + ", ");}
	}

	private static void displayOccuRooms(Connection db) throws SQLException {
		System.out.println("Here are the reserved rooms: ");
		Statement st = db.createStatement();
		st.execute("set search_path=\"Project Hotel\";");
		st.execute("SELECT userID,roomID,bookingNum,isrented,paymentDue,dateBeg,dateEnd\n"
				+ "FROM \"Project Hotel\".reservationCurrent\n"
				+ "WHERE hotelID = "+empHID+" AND LOCALTIMESTAMP BETWEEN dateBeg AND dateEnd");
		ResultSet rs = st.getResultSet();
		System.out.println("userID,\troomID,\tbook#,\trented,\tpaymentDue,\tdateBeg,\tdateEnd");
		while (rs.next()){
			System.out.print(rs.getString("userID")
					+",\t"+rs.getString("roomID")
					+",\t"+rs.getString("bookingNum")
					+",\t"+rs.getString("isRented")
					+",\t"+rs.getString("paymentDue")
					+",\t"+rs.getString("dateBeg")
					+",\t"+rs.getString("dateEnd")
					+"\n");
		}

	}

	private static void displayBookedRooms(Connection db) throws SQLException {
		System.out.println("Here are the booked rooms: ");
		Statement st = db.createStatement();
		st.execute("set search_path=\"Project Hotel\";");
		st.execute("SELECT userID,roomID,bookingNum,isrented,paymentDue,dateBeg,dateEnd\n"
				+ "FROM \"Project Hotel\".reservationCurrent\n"
				+ "WHERE hotelID = "+empHID);
		ResultSet rs = st.getResultSet();
		System.out.println("userID,\troomID,\tbook#,\trented,\tpaymentDue,\tdateBeg,\tdateEnd");
		while (rs.next()){
			System.out.print(rs.getString("userID")
				+",\t"+rs.getString("roomID")
				+",\t"+rs.getString("bookingNum")
				+",\t"+rs.getString("isRented")
				+",\t"+rs.getString("paymentDue")
				+",\t"+rs.getString("dateBeg")
				+",\t"+rs.getString("dateEnd")
				+"\n");
		}
	}

}
