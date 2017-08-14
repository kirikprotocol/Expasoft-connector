<%@ page import="com.eyeline.expasoft.chatme.model.ChatmeAgent"
%><%@ page import="org.apache.log4j.Logger"
%><%@ page import="com.eyeline.expasoft.chatme.model.Intents"
%><%@ page import="org.apache.commons.lang.StringUtils"
%><%@ page import="java.net.URLEncoder"
%><%@ page import="java.util.Set"
%><%@ page import="java.util.TreeSet"
%><%@ page import="java.util.Comparator"
%><%@ page contentType="text/html;charset=UTF-8" language="java"
%><%@include file="constant.jsp" %><%!
    private ChatmeAgent agent = new ChatmeAgent("http://ubuntu@expasoft.com:5000/chatme/pilot/api/v1.0", AGENT_ID, AGENT_PWD);
    private Logger log = Logger.getLogger("expasoft_chat_logs");
%><%
    session.removeAttribute("intent");
    Intents intents = agent.getIntents();
    Set<Intents.Info> intentSet = new TreeSet<Intents.Info>(new Comparator<Intents.Info>() {
        @Override
        public int compare(Intents.Info o1, Intents.Info o2) {
            return o1.getName().compareTo(o2.getName());
        }
    });
    intentSet.addAll(intents.getIntents());
    String eventType = request.getParameter("event.type");
    String input = null;
    if ("text".equals(eventType)) {
        input = request.getParameter("event.text");
    }
    String output = "";
    String intentId = request.getParameter("id");
    if (StringUtils.isBlank(intentId) && StringUtils.isNotBlank(input)) {
        intentId = input;
    }
//    if (StringUtils.isNotBlank(input)&&input.startsWith("/")) {
    if (StringUtils.isNotBlank(intentId)) {
        for (Intents.Info info: intents.getIntents()) {
            if (intentId.trim().equals(info.getName())) {
//            if (input.trim().substring(1).equalsIgnoreCase(info.getName())) {
                session.setAttribute("intent.info", info);
                %><jsp:forward page="intent.jsp"/><%
                break;
            }
        }
    }
    String msg = (String) request.getAttribute("msg");
%><page version="2.0">
    <% if(StringUtils.isNotBlank(msg)) { %>
    <div><%=msg%></div>
    <% } %>
    <div>
        Загруженные интенты:
    </div>
    <navigation>
        <link accesskey="1" pageId="learn.jsp">Добавить интент</link>
        <link accesskey="2" pageId="query.jsp">Назад</link>
    </navigation>
    <navigation>
        <%
            int i=0;
            for (Intents.Info info: intentSet) { %>
        <link pageId="intents.jsp?id=<%=URLEncoder.encode(info.getName(), "UTF-8")%>"><%=info.getName()%> (<%=info.getMessageNumber()%>)</link>
        <% } %>
    </navigation>
</page>
