<%@page language="java" contentType="text/xml; charset=UTF-8"
%><%
    String receiverType = (String) session.getAttribute("receiverType");
    String receiver = (String) session.getAttribute("receiver");
    Integer amountInt = (Integer) session.getAttribute("transfer.summ");
    if (session.getAttribute("transfer.complete")==null) {
        session.setAttribute("transfer.complete", "true");
        if ("contact".equals(receiverType)) {
            response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid=105");
            return;
        } else {
            %><jsp:forward page="save_contact.jsp"/><%
            return;
        }
    } else {
        session.removeAttribute("transfer.complete");
        response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid=18");
        return;
    }
%>