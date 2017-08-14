<%@page language="java" contentType="text/xml; charset=UTF-8"
%><%
    if ("true".equals(session.getAttribute("goodok.disconnected"))) {
        response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid=11");
    }
    ///
    session.setAttribute("goodok.disconnected", "true");
%><page version="2.0">
    <div>Услуга GOOD`OK успешно удалена по вашему номеру.<br/>
        Я могу Вам чем-то еще помочь?
    </div>
    <div>
        <input navigationId="submit" name="amount"/>
    </div>
    <navigation id="submit">
        <link accesskey="1" pageId="http://hosting-api-test.eyeline.mobi/index?sid=686&amp;=pid=18">Ok</link>
    </navigation>
    <attributes><attribute name="resource-id" value="/686/85/25"/></attributes>
</page>