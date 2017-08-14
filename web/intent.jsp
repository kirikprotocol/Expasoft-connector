<%@ page import="com.eyeline.expasoft.chatme.model.ChatmeAgent"
%><%@ page import="org.apache.log4j.Logger"
%><%@ page import="com.eyeline.expasoft.chatme.model.Intents"
%><%@ page import="java.util.Map"
%><%@ page contentType="text/html;charset=UTF-8" language="java"
%><%@include file="constant.jsp" %><%!
    private ChatmeAgent agent = new ChatmeAgent("http://ubuntu@expasoft.com:5000/chatme/pilot/api/v1.0", AGENT_ID, AGENT_PWD);
    private Logger log = Logger.getLogger("expasoft_chat_logs");
%><%
    Intents.Info info = (Intents.Info) session.getAttribute("intent.info");
    Map<String,String> userSays = agent.getUsersays(info.getId());
%><page version="2.0">
    <div>
        <b><%=info.getName()%></b><br/>
        <% for (Map.Entry<String,String> e: userSays.entrySet()) { %>
        <%=e.getValue()%><br/>
        <% } %>
    </div>
    <navigation>
        <link accesskey="1" pageId="delete.jsp?intent=<%=info.getName()%>">Удалить</link>
        <link accesskey="2" pageId="intents.jsp">Назад</link>
    </navigation>
</page>
