import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class Hotel {

	static String DB="";
	static String USER="";
	static String PASS="";

	/**phone counter*/
	public static int cp = 0;

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		Class.forName("org.postgresql.Driver");
		Connection db = DriverManager.getConnection("jdbc:postgresql://web0.site.uottawa.ca:15432/"+DB,USER,PASS);
		Statement st = db.createStatement();
		// Add the brands
		dbBrand(st);
		// Add the hotel
		dbHotel(st);
		// Add the room
		dbRoom(st);
		// Add the employee
		dbCust(st);
		// Add the customer
		dbCus2t(st);
		// Add reservations
		//generateResCur();
	}



	enum type {
		SIN, Name, STR, EMAIL, PHONE, ZIP, SINT, SNum, CITYPROV, Place, INT, RATE, AS, Pas, Sal, Job, Date;
	}

	public static String mod(String str) {
		return "\'" + str + "\'";
	}

	public static String email(String str) {
		return mod(str.replaceAll(" ", "") + "@hotel.ca");
	}

	public static String phone() {
		String a = "647123";
		String b = Integer.toString(cp);
		cp++;
		while (b.length() < 4) {
			b += "0";
		}
		return mod(a + b);
	}

	static String[] street = { "Apple", "Leaf", "Flower", "First", "College", "King", "Bay", "Cummer", "Bloor", "Queen",
			"Finch", "Main", "Allen", "Main", "Lake" };
	static String[] street2 = { "AV", "ST", "WAY", "CT", "DR", "LN", "CRES", "CIR", "CTR" };
	static ArrayList<String> arr2 = new ArrayList<>();

	public static String street() {
		String s = "";
		do {
			Random rand = new Random();
			s = street[rand.nextInt(street.length)] + " " + street2[rand.nextInt(street2.length)];
		} while (arr2.contains(s));
		arr.add(s);
		return mod(s);
	}

	static String[] jobs = { "Bellhop", "Housekeeping", "Reception", "Kitchen Staff", "Middle Management" };

	public static String job(boolean x) {
		if (x) {
			return mod("Manager");
		}
		Random r = new Random();
		return mod(jobs[r.nextInt(jobs.length)]);
	}

	public static String generateRoom(int roomID, int hotelID) {
		String[] amenities = { "Kitchen", "TV", "Internet", "Soaps", "Hair dryer", "Towels", "AC", "Fridge" };
		Random rand = new Random();
		String[] s = new String[7];
		s[0] = Integer.toString(roomID);
		s[1] = Integer.toString(hotelID);
		int n = rand.nextInt(amenities.length);
		ArrayList<String> t = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			int a = rand.nextInt(amenities.length);
			if (!t.contains(amenities[a])) {
				t.add(amenities[a]);
			}
		}
		s[2] = t.toString().replace("[", "").replace("]", "");
		s[3] = String.valueOf(((double) (int) ((100 + Math.random() * 900) * 100)) / 100);
		s[4] = String.valueOf((int) (Math.random() * 5) + 1);
		s[5] = String.valueOf(rand.nextBoolean());
		int b = rand.nextInt(3);
		switch (b) {
		case 0 -> s[6] = "Sea View";
		case 1 -> s[6] = "Mountain View";
		case 2 -> s[6] = "No View";
		}
		String as = "";
		for (int z = 0; z < s.length; z++) {
			if (z == 0 || z == 1 || z == 3 || z == 4) {
				as += s[z];
			} else {
				as += mod(s[z]);
			}

			if (z + 1 < s.length) {
				as += ",";
			}
		}
		return as;
	}

	static String[] na = { "Kate", "Ruth", "Rose", "Beth", "Jean", "Ann", "Eve", "Blaire", "Paige", "Gail", "Gwen",
			"Kim", "Liv", "Grace", "Sue", "Dee", "Elle", "Dawn", "Claire", "Faye", "Jane", "Madge", "Pearl", "Tess",
			"Beau", "Blake", "Brock", "Cade", "Chad", "Chase", "Clark", "Cole", "Drake", "Grant", "Heath", "Jack",
			"Jake", "Kent", "Kurt", "Luke", "Max", "Neil", "Rhett", "Ross", "Todd", "Trent", "Troy", "Vince" };
	static String[] lna = { "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez",
			"Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson",
			"Martin", "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson",
			"Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores", "Green",
			"Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell", "Carter", "Roberts" };

	public static String name() {
		Random r = new Random();
		return mod(na[r.nextInt(na.length)] + " " + lna[r.nextInt(lna.length)]);
	}

	public static String sin() {
		Random r = new Random();
		String a = "";
		for (int x = 0; x < 9; x++) {
			if (x == 0) {
				a += Integer.toString(r.nextInt(9) + 1);
			} else {
				a += Integer.toString(r.nextInt(10));
			}

		}
		return a;
	}

	static ArrayList<String> arr = new ArrayList<>();

	public static String zip() {

		String s = "";
		do {
			Random rand = new Random();
			s = "";
			s += (char) (rand.nextInt(26) + 'a');
			s += rand.nextInt(10);
			s += (char) (rand.nextInt(26) + 'a');
			s += rand.nextInt(10);
			s += (char) (rand.nextInt(26) + 'a');
			s += rand.nextInt(10);
			s = s.toUpperCase();
		} while (arr.contains(s));
		arr.add(s);
		return mod(s);
	}

	public static String streetNum() {
		return Integer.toString((int) (Math.random() * 100) + 1);
	}

	public static String app() {
		if (Math.random() > 0.5) {
			return streetNum();
		}
		return "null";
	}

	static String[] Prov = { "Saskatchewan", "Ontario", "Alberta", "BC", "Quebec", "Manitoba", "NB", "NV", "PEI" };
	static String[] City = { "Regina", "Ottawa", "Edmonton", "Vancouver", "Montreal", "Winnipeg", "Moncton", "Halifax",
			"Charlottetown" };

	public static String prov() {
		int x = (int) (Math.random() * Prov.length);
		return mod(City[x] + "','" + Prov[x]);
	}

	public static String date(String x) {
		Random r = new Random();
		return "'2020-"+x+"-10 " + "0" + Integer.toString(r.nextInt(10)) + ":0" + Integer.toString(r.nextInt(10)) + ":0"
				+ Integer.toString(r.nextInt(10)) + "'";
	}

	private static DecimalFormat df2 = new DecimalFormat("#.##");

	public static String cust(type x, int y, int z, boolean w) {
		switch (x) {
		case Job:
			return job(w);
		case Pas:
			return "'password'";
		case Sal:
			return df2.format(Math.random() * 20000 + 20000);
		case SINT:
			return Integer.toString(y);
		case SIN:
			return sin();
		case Name:
			return name();
		case PHONE:
			return phone();
		case AS:
			return app();
		case SNum:
			return streetNum();
		case INT:
			return Integer.toString(z);
		case Date:
			return date("03");
		case ZIP:
			return zip();
		case CITYPROV:
			return prov();
		case Place:
			return street();
		default:
			return "-----";
		}
	}

	public static String cases(type x, String str, int y) {
		switch (x) {

		case EMAIL:
			return email(str);
		case STR:
			return mod(str);
		case RATE:
			return Integer.toString((int) (Math.random() * 5) + 1);
		case SINT:
			return Integer.toString(y);
		case PHONE:
			return phone();
		case SNum:
			return streetNum();
		case ZIP:
			return zip();
		case CITYPROV:
			return prov();
		case Place:
			return street();
		default:
			return "-----";
		}
	}

	public static String cases(type x, String str, String str2, int y, int key, int rate) {
		switch (x) {
		case INT:
			return Integer.toString(key);
		case EMAIL:
			return email(str2);
		case STR:
			return mod(str);
		case RATE:
			return Integer.toString(rate);
		case SINT:
			return Integer.toString(y);
		case PHONE:
			return phone();
		case SNum:
			return streetNum();
		case ZIP:
			return zip();
		case CITYPROV:
			return prov();
		case Place:
			return street();
		default:
			return "-----";
		}
	}

	static String[] Hotel_Chain = { "Marriott", "Hilton", "Holiday Inn", "MGM Resorts", "Novotel", "Sheraton" };
	static String Hotel[][] = { { "Ottawa Marriott Hotel", "Marriott 2", "Marriott 3", "Marriott 4", "Marriott 5" },
			{ "Hilton 1", "Hilton 2", "Hilton 3", "Hilton 4", "Hilton 5" },
			{ "Holiday Inn 1", "Holiday Inn 2", "Holiday Inn 3", "Holiday Inn 4", "Holiday Inn 5" },
			{ "MGM Resorts 1", "MGM Resorts 2", "MGM Resorts 3", "MGM Resorts 4", "MGM Resorts 5" },
			{ "Novotel Ottawa", "Novotel 2", "Novotel 3", "Novotel 4", "Novotel 5" },
			{ "Sheraton Ottawa Hotel", "Sheraton 2", "Sheraton 3", "Sheraton 4", "Sheraton 5" } };
	static type[] Brand = { type.STR, type.SINT, type.PHONE, type.EMAIL, type.SNum, type.Place, type.CITYPROV,
			type.ZIP };
	static type[] Hotels = { type.INT, type.STR, type.SINT, type.RATE, type.PHONE, type.EMAIL, type.SNum, type.Place,
			type.CITYPROV, type.ZIP };
	static type[] Cust = { type.SINT, type.SIN, type.Name, type.Pas, type.Date, type.PHONE, type.INT, type.Sal,
			type.Job, type.AS, type.SNum, type.Place, type.CITYPROV, type.ZIP };
	static type[] Cus2t = { type.SINT, type.SIN, type.Name, type.Pas, type.Date, type.PHONE, type.INT, type.AS,
			type.SNum, type.Place, type.CITYPROV, type.ZIP };

	public static void dbBrand(Statement st) {
		for (int x = 0; x < 6; x++) {
			String b = "";
			for (int y = 0; y < Brand.length; y++) {
				b += cases(Brand[y], Hotel_Chain[x], 0);
				if (y + 1 < Brand.length) {
					b += ",";
				}
			}
			try {
				st.executeUpdate(
						"INSERT INTO \"Project Hotel\".Brand(brandName, hotelCount, phone, email, street_number, street_name, city, state, zip)"
								+ " VALUES (" + b + ");");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void dbCus2t(Statement st) {
		for (int x = 0; x < 30; x++) {
			for (int y = 0; y < 5; y++) {
				String b = "";
				for (int z = 0; z < Cus2t.length; z++) {
					b += cust(Cus2t[z], x * 5 + y + 1000, 1, y == 0);
					if (z + 1 < Cus2t.length) {
						b += ",";
					}
				}
				try {
					st.executeUpdate(
							"INSERT INTO \"Project Hotel\".Customer(userID, SIN, name, password, dateCreated, phone, bookingNum, apt_number,street_number, street_name, city, state, zip)"
									+ " VALUES (" + b + ");");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}

	public static void dbCust(Statement st) {
		for (int x = 0; x < 30; x++) {
			for (int y = 0; y < 5; y++) {
				String b = "";
				for (int z = 0; z < Cust.length; z++) {
					b += cust(Cust[z], x * 5 + y + 1000, x, y == 0);
					if (z + 1 < Cust.length) {
						b += ",";
					}
				}
				try {
					st.executeUpdate(
							"INSERT INTO \"Project Hotel\".Employee(userID, SIN, name, password, dateCreated, phone, hotelID, salary, job, apt_number,street_number, street_name, city, state, zip)"
									+ " VALUES (" + b + ");");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}

	public static void dbHotel(Statement st) {
		for (int x = 0; x < 6; x++) {
			for (int y = 0; y < 5; y++) {
				String b = "";
				for (int z = 0; z < Hotels.length; z++) {
					b += cases(Hotels[z], Hotel_Chain[x], Hotel[x][y], 0, x * 5 + y, y + 1);
					if (z + 1 < Hotels.length) {
						b += ",";
					}
				}
				try {
					st.executeUpdate(
							"INSERT INTO \"Project Hotel\".Hotel(hotelID, brandName, roomCount, rating, phone, email, street_number, street_name, city, state, zip)"
									+ " VALUES (" + b + ");");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			st.executeUpdate("delete from \"Project Hotel\".Hotel where hotelID = 0");
			st.executeUpdate("delete from \"Project Hotel\".Hotel where hotelID = 25");
			st.executeUpdate("delete from \"Project Hotel\".Hotel where hotelID = 20");
			st.executeUpdate(
					"INSERT INTO \"Project Hotel\".Hotel(hotelID, brandName, roomCount	, rating,phone, email, street_number, street_name, city, state, zip)"
							+ "VALUES (20, 'Novotel', 0, 4, '6132303033', 'novotelottawa@novotelottawa.com', 33, 'Nicholas ST', 'Ottawa', 'Ontario', 'K1N9M7')");
			st.executeUpdate(
					"INSERT INTO \"Project Hotel\".Hotel(hotelID, brandName, roomCount	, rating,phone, email, street_number, street_name, city, state, zip)"
							+ "VALUES (0, 'Marriott', 0, 4, '6132381122', 'marriottottawa@marriott.com', 100, 'Kent ST', 'Ottawa', 'Ontario', 'K1P5R7')");
			st.executeUpdate(
					"INSERT INTO \"Project Hotel\".Hotel(hotelID, brandName, roomCount	, rating,phone, email, street_number, street_name, city, state, zip)"
							+ "VALUES (25, 'Sheraton', 0, 4, '6132381500', 'sheratonottawa@sheraton.com', 150, 'Albert ST', 'Ottawa', 'Ontario', 'K1P5G2')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void dbRoom(Statement st) {
		for (int roomID = 0; roomID < 30; roomID++) {
			for (int realRID = 0; realRID < 5; realRID++) {
				try {
					st.executeUpdate(
							"INSERT INTO \"Project Hotel\".Room(roomID, hotelID, amenities, price, capacity, extendable, view)"
									+ " VALUES (" + generateRoom(roomID * 5 + realRID, roomID) + ");");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void generateResCur(String beg, String end) {
		//This is the generator we used for reservation current
				Random r = new Random();
				ArrayList<String> arr123 = new ArrayList<>();
				for (int y = 0; y < 3; y++) {
					String w = Integer.toString(1000+r.nextInt(150));
					for (int x = 0; x < 5; x++) {
							int z = r.nextInt(150);
							String s = "INSERT INTO \"Project Hotel\".ReservationCurrent(userid,hotelid,roomid,isrented,bookingnum,occupants,dateBeg,dateEnd)"
									+ " VALUES ("+w+","+ "(SELECT hotelID FROM \"Project Hotel\".Room WHERE (roomID = "+Integer.toString(z)+")),"+ Integer.toString(z) + ", " + true + ", 0,1, " + date(beg).substring(0, date("03").indexOf(" "))+"'" +", "+ date(end).substring(0, date("04").indexOf(" "))+"'"+");";
							if (!arr123.contains(s)) {
								arr123.add(s);
								System.out.println(s);
							}
					}
				}
	}

}
