package eHotel.servlet;

import eHotel.connections.PostgreSqlConn;
import eHotel.entities.Room;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class CustomerRegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        HttpSession session = req.getSession();
        String userID = req.getParameter("userID");
        String custName = req.getParameter("CustName");
        String custPwd = req.getParameter("custPwd");
        String SIN = req.getParameter("SIN");
        String apt_number = req.getParameter("apt_number");
        String street_number = req.getParameter("street_number");
        String street_name = req.getParameter("street_name");
        String city = req.getParameter("city");
        String state = req.getParameter("state");
        String ZIP = req.getParameter("ZIP");

        String[] customer = new String[]{userID, SIN, custName, custPwd, String.valueOf(new Timestamp(System.currentTimeMillis())),
                apt_number,street_number, street_name,city,state,ZIP};
        PostgreSqlConn con = new PostgreSqlConn();
        boolean inserted = con.insertNewCustomer(customer);
        if(inserted){
            ArrayList<Room> bookedRooms = con.getBookedRooms(userID);
            ArrayList<Room> allRooms = con.getAllAvailRooms();
            req.setAttribute("userID", userID);
            req.setAttribute("CustName", custName);
            req.setAttribute("bookedRooms", bookedRooms);
            req.setAttribute("allRooms", allRooms);
            req.getRequestDispatcher("booking.jsp").forward(req, resp);
            return;
        }
        resp.sendRedirect("register_failure.jsp");
    }
}
