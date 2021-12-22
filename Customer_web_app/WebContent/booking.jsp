<%@page import="java.util.ArrayList" %>
<%@page import="eHotel.entities.Room" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Locale" %>
<%@ page contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Booking Page</title>
</head>
<body>

<%
    String CustName = (String) request.getAttribute("CustName");
%>
<form method="post" action="roombook">
    <h4>
        Welcome,
            <%=CustName%>
        <h4>
            <h4>Here are the room(s) you booked</h4>
                <%

                        session.setAttribute("userID", request.getAttribute("userID"));
                        System.out.println("bookingjsp:"+request.getAttribute("userID"));
                        session.setAttribute("CustName", request.getAttribute("CustName"));
                        System.out.println("bookingjsp:"+request.getAttribute("CustName"));

                    Object obj1 = request.getAttribute("bookedRooms");
                    ArrayList<Room> broomList = new ArrayList<>();
                    if (obj1 instanceof ArrayList) {
                        broomList = (ArrayList<Room>) obj1;
                    }
                    if (!broomList.isEmpty()) {
                        for (Room room : broomList) {
                            String roominfo = room.getRoom_no();
                %>
            <li><%=roominfo%>
            </li>
                <%
                        }
                    }
                %>
            <input type="hidden" name="custName" value="<%=CustName%>"/>
            <h4>Here are available rooms</h4>
            <label>
                <select name="roomno">
                    <%
                        Object obj = request.getAttribute("allRooms");
                        ArrayList<Room> roomList = new ArrayList<>();
                        if (obj instanceof ArrayList) {
                            roomList = (ArrayList<Room>) obj;
                        }
                        if (!roomList.isEmpty()) {
                            for (Room room : roomList) {
                                /* String roominfo = room.getRoom_no() + "---" + room.getRoom_status(); */
                    %>
                    <option><%=room.getRoom_no()%>
                    </option>
                    <%
                            }
                        }
                    %>
                </select>
            </label>
            <button type="submit" onclick="return confirm('book?');">book</button>
            <label for="dateFrom"></label><input type="date" id="dateFrom" name="dateFrom"
                                                 value="2021-03-30"
                                                 min=2021-03-30 max="2021-05-30">
            <label for="dateTo"></label><input type="date" id="dateTo" name="dateTo"
                                               value="2021-03-30"
                                               min=2021-03-30 max="2021-05-30">

</form>
</body>
</html>