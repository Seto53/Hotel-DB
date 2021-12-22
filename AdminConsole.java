import java.sql.*;
import java.util.Scanner;

public class AdminConsole {

	static String DB="";
	static String USER="";
	static String PASS="";

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection db = DriverManager.getConnection("jdbc:postgresql://web0.site.uottawa.ca:15432/"+DB,USER,PASS);
            System.out.println("Successfully Connected");
            while (true) {
                getQuery(db);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Failed to connect");
            e.getStackTrace();
        }
    }

    public static void getQuery(Connection db) {

        try {
            Statement st = db.createStatement();
            st.execute("set search_path = 'Project Hotel'");
            System.out.print("Enter your SQL query here: ");
            Scanner scanner = new Scanner(System.in);
            String sql = scanner.nextLine();
            System.out.println("You inputted: " + sql);
            boolean isResultSet = st.execute(sql);
            if (isResultSet) {
                ResultSet rs = st.getResultSet();
                if (!rs.wasNull()) {
                    System.out.println("Query Results:");
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(metaData.getColumnName(i) + "  |  ");
                    }
                    System.out.println();
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            String value = rs.getString(i);
                            System.out.print(value + " | ");
                        }
                        System.out.println();
                    }
                    System.out.println();
                }
            }
            System.out.println("Query Successfully Executed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
