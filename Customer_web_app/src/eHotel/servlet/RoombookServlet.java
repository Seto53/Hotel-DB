package eHotel.servlet;

import eHotel.connections.PostgreSqlConn;
import eHotel.entities.Room;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

public class RoombookServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String custName = (String) session.getAttribute("CustName");
        String roomID = req.getParameter("roomno");
        String userID = (String) session.getAttribute("userID");
        String dateFrom = req.getParameter("dateFrom");
        String dateTo = req.getParameter("dateTo");
        System.out.println("roombooklet:"+userID);
        System.out.println("roombooklet:"+custName);
        PostgreSqlConn con = new PostgreSqlConn();
        boolean booked = con.bookRoom(userID, roomID, dateFrom,dateTo);
        if (booked) {
            ArrayList<Room> bookedRooms = con.getBookedRooms(userID);
            ArrayList<Room> allRooms = con.getAllAvailRooms();
            req.setAttribute("userID", userID);
            req.setAttribute("CustName", custName);
            req.setAttribute("bookedRooms", bookedRooms);
            req.setAttribute("allRooms", allRooms);
            req.getRequestDispatcher("booking.jsp").forward(req, resp);
            return;
        }
        resp.sendRedirect("login_failure.jsp");
    }
}