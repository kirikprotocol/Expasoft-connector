<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@page language="java" contentType="text/xml; charset=UTF-8"
%><%
    String source = request.getParameter("src");
    if (StringUtils.isBlank(source)) {
        String switchSource = (String) session.getAttribute("switch.source");
        if (StringUtils.isBlank(switchSource)){
            source = "phone";
        } else {
            session.removeAttribute("switch.source");
            session.setAttribute("transfer.source", switchSource);
            %><jsp:forward page="input_summ_ok.jsp"/><%
            return;
        }
    }
    String currentSource = (String) session.getAttribute("transfer.source");
    if (StringUtils.isBlank(currentSource)) {
        session.setAttribute("transfer.source", source);
    }
%><jsp:forward page="input_receiver.jsp"/>