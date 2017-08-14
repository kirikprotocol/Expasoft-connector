<%@page language="java" contentType="text/xml; charset=UTF-8"
%><%!
    private boolean isConnectedGoodok(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String disconnected = (String) session.getAttribute("goodok.disconnected");
        return disconnected == null;
    }
%><%
    if (isConnectedGoodok(request)) {
        response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid=4");
        return;
    } else {
        response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid=131");
        return;
    }
%>