<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@page language="java" contentType="text/xml; charset=UTF-8"
%><%
    session.removeAttribute("transfer.summ");
    session.removeAttribute("receiverType");
    session.removeAttribute("receiver");
    session.removeAttribute("transfer.source");
    session.removeAttribute("switch.source");
    String pid = request.getParameter("new_pid");
    if (StringUtils.isBlank(pid)) {
        pid="18";
    }
    response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid="+pid);
    return;
%>