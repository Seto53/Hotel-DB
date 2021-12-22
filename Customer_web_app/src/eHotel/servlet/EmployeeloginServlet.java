package eHotel.servlet;

import eHotel.connections.PostgreSqlConn;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EmployeeloginServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userID = req.getParameter("userID");
        String password = req.getParameter("password");

        PostgreSqlConn con = new PostgreSqlConn();
        String pwdfromdb = con.getEmpPwdByUserID(userID);

        if (password.equals(pwdfromdb)) {
            System.out.println("success");
            req.setAttribute("employee_id", password);
            resp.sendRedirect("login_success.jsp?employee_id=" + userID);
            return;
        }
        resp.sendRedirect("login_failure.jsp");
    }
}