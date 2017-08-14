<%@ page import="org.apache.commons.lang.StringUtils"
%><%@page language="java" contentType="text/xml; charset=UTF-8"
%><%
    String source = (String) session.getAttribute("transfer.source");
    if (StringUtils.isBlank(source)) {
        source = "phone";
        session.setAttribute("transfer.source", "phone");
    }
    if ("card".equals(source)) {
        session.setAttribute("switch.source", "phone");
    } else {
        session.setAttribute("switch.source", "card");
    }
%><page version="2.0">
    <div>
        <% if ("phone".equals(source)) { %>
        Вы хотите перевести деньги с Вашей привязанной банковской карты вашей карты Alfa ***5476?<br/>
        <% } else { %>
        Вы хотите перевести деньги с вашего лицевого счета?<br/>
        <% } %>
    </div>
    <navigation>
        <link accesskey="1" pageId="source.jsp?skip=true">Да</link>
        <link accesskey="2" pageId="http://hosting-api-test.eyeline.mobi/index?sid=686&amp;pid=101">Нет</link>
    </navigation>
    <attributes>
        <attribute name="resource-id" value="/686/85/106"/>
    </attributes>
</page>