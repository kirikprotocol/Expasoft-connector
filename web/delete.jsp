<%@ page import="com.eyeline.expasoft.chatme.model.*"
%><%@ page contentType="text/html;charset=UTF-8" language="java"
%><%@include file="constant.jsp" %><%!
    private ChatmeAgent agent = new ChatmeAgent("http://ubuntu@expasoft.com:5000/chatme/pilot/api/v1.0", AGENT_ID, AGENT_PWD);
%><%
    String intent = request.getParameter("intent");
    String approve = request.getParameter("approve");
%><page version="2.0">
    <%if ("true".equals(approve)) {
        IntentResponse res = agent.deleteIntent(intent);
        agent.doSave();
        String msg = "Успешно: "+res.getSuccess()+"<br/>"+
                "Интент: "+res.getIntentName()+"<br/>"+
                "Статус удаления: "+res.getResponse();
        request.setAttribute("msg", msg);
    %><jsp:forward page="query.jsp"/><%
    } else { %>
        <div>Подтвердите удаление интента: <b><%=intent%></b><br/></div>
        <navigation>
            <link accesskey="2" pageId="delete.jsp?approve=true&amp;intent=<%=intent%>">Подтвердить</link>
        </navigation>
    <% } %>
    <navigation>
        <link accesskey="1" pageId="query.jsp">Назад</link>
    </navigation>
</page>
