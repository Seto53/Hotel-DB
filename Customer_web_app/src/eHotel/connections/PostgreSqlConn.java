package eHotel.connections;

import eHotel.entities.Room;

import java.sql.*;
import java.util.ArrayList;

public class PostgreSqlConn {

    Connection db = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Statement st = null;
    String sql = null;

    public void getConn() {
        try {
            Class.forName("org.postgresql.Driver");
            db = DriverManager.getConnection("jdbc:postgresql://web0.site.uottawa.ca:15432/group_a07_g40",
                    "", "");
        } catch (Exception e) {
            System.out.print("Error Caught");
        }
    }

    public void closeDB() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (st != null) {
                st.close();
            }
            if (db != null) {
                db.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getEmpPwdByUserID(String userID) {
        getConn();
        String pwd = "";
        try {
            st = db.createStatement();
            st.executeUpdate("set search_path =\"Project Hotel\"");
            ps = db.prepareStatement("select password from \"Project Hotel\".employee where userid=" + userID);
            rs = ps.executeQuery();
            while (rs.next()) {
                pwd = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
        return pwd;
    }

    public String[] getCusInfoByUserID(String userID) {
        getConn();
        String[] info = new String[2];
        try {
            st = db.createStatement();
            st.executeUpdate("set search_path =\"Project Hotel\"");
            ps = db.prepareStatement("select * from \"Project Hotel\".customer where userid=" + userID);
            rs = ps.executeQuery();
            while (rs.next()) {
                info[0] = rs.getString(3);
                info[1] = rs.getString(4);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
        return info;
    }

    public boolean insertNewCustomer(String[] customer) {
        getConn();
        try {
            st = db.createStatement();
            st.executeUpdate("set search_path =\"Project Hotel\"");
            st = db.createStatement();
            sql = "insert into \"Project Hotel\".customer (userid,sin,name,password,datecreated,apt_number," +
                    "street_number,street_name,city,state,zip)" +
                    " values('" + customer[0] + "','" + customer[1] + "','" + customer[2] + "','" + customer[3]
                    + "','" + customer[4] + "','" + customer[5] + "','" + customer[6] + "','" + customer[7]
                    + "','" + customer[8] + "','" + customer[9] + "','" + customer[10] + "')";
            System.out.print(sql);
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDB();
        }
        return true;
    }

    public ArrayList<Room> getAllAvailRooms() {
        getConn();
        ArrayList<Room> Rooms = new ArrayList<>();
        try {
            st = db.createStatement();
            st.executeUpdate("set search_path =\"Project Hotel\"");
            ps = db.prepareStatement("select roomid from \"Project Hotel\".room except (" +
                    "select roomid from \"Project Hotel\".room natural join \"Project Hotel\".reservationcurrent)");
            rs = ps.executeQuery();
            while (rs.next()) {
                String room_no = rs.getString("roomID");
                Room room = new Room(room_no);
                Rooms.add(room);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            closeDB();
        }
        return Rooms;
    }

    public ArrayList<Room> getBookedRooms(String userID) {
        getConn();
        ArrayList<Room> Rooms = new ArrayList<>();
        try {
            st = db.createStatement();
            st.executeUpdate("set search_path =\"Project Hotel\"");
            ps = db.prepareStatement("select roomID from \"Project Hotel\".reservationcurrent where userid=" + userID);
            rs = ps.executeQuery();
            while (rs.next()) {
                String room_no = rs.getString("roomID");
                Room room = new Room(room_no);
                Rooms.add(room);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            closeDB();
        }
        return Rooms;
    }

    public boolean bookRoom(String userID, String roomID, String dateFrom, String dateTo) {
        getConn();
        try {
            st = db.createStatement();
            st.executeUpdate("set search_path =\"Project Hotel\"");
            st = db.createStatement();
            sql = "insert into \"Project Hotel\".reservationcurrent (userid,hotelid,roomid,isrented,bookingnum,datebeg,occupants,dateend) " +
                    "values(" + userID + "," + "(SELECT hotelID FROM \"Project Hotel\".Room WHERE (roomID =" + roomID + "))"
                    + "," + roomID + "," + false + "," + 1 + ",'" + dateFrom + "'," + 0 + ",'" + dateTo + "')";
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDB();
        }
        return true;
    }
}

