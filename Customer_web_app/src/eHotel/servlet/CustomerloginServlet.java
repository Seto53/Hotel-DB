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

public class CustomerloginServlet extends HttpServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
//		employee account = new employee();
		String userID = req.getParameter("userID");
		String password = req.getParameter("password");
		System.out.println("loginservet:"+userID);
		System.out.println("loginservet:"+password);
		PostgreSqlConn con = new PostgreSqlConn();
//		[0]:name,[1]:pwd
		String[] userInfo = con.getCusInfoByUserID(String.valueOf(userID));
		
		if (password.equals(userInfo[1])) {
			ArrayList<Room> bookedRooms = con.getBookedRooms(userID);
			ArrayList<Room> allRooms = con.getAllAvailRooms();
			req.setAttribute("userID", userID);
			req.setAttribute("CustName", userInfo[0]);
			req.setAttribute("bookedRooms", bookedRooms);
			req.setAttribute("allRooms", allRooms);
			req.getRequestDispatcher("booking.jsp").forward(req, resp);
			return;	
		}
		resp.sendRedirect("login_failure.jsp");
	}
}