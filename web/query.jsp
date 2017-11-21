<%@ page import="java.text.DecimalFormat"
%><%@ page import="java.text.NumberFormat"
%><%@ page import="org.apache.log4j.Logger"
%><%@ page import="org.apache.commons.lang.StringUtils"
%><%@ page import="com.eyeline.miniapps.connector.client.AiApplicationClient"
%><%@ page import="com.eyeline.miniapps.ai.model.AiAgent"
%><%@ page import="java.util.List"
%><%@ page contentType="text/html;charset=UTF-8" language="java"
%><%!
    private AiApplicationClient agent =  new AiApplicationClient("http://devel.globalussd.mobi/ai/ai", "ai-expasoft-chatme");
    private Logger log = Logger.getLogger("ai_chat_logs");
%><%
    String eventType = request.getParameter("event.type");
    String input = null;
    if ("text".equals(eventType)) {
        input = request.getParameter("event.text");
    }
    List<AiAgent.Prediction> predictions = null;
    if (StringUtils.isNotBlank(input)) {
        try{
            predictions = agent.predict(input);
            log.info(input+" - "+predictions);
        } catch (Exception e) {
            log.error("error on input: "+input, e);
            predictions = null;
        }
    }
    NumberFormat formatter = new DecimalFormat("#0.00");
    String msg = (String) request.getAttribute("msg");
    session.removeAttribute("intent.info");
%><page version="2.0">
    <% if(StringUtils.isNotBlank(msg)) { %>
    <div><%=msg%></div>
    <% } %>
    <div>
        <% if (predictions == null) { %>
            Введите запрос к боту<br/>
        <% } else { %>
            Запрос: <i><%=input%></i><br/>
            <%
                for (AiAgent.Prediction p: predictions) {
            %><b><%=p.getIntent()%></b> – <i><%=(formatter.format(p.getProba()*100))%>%</i><br/><%
                }
            %>
        <% } %>
    </div>
    <navigation>
        <link accesskey="1" pageId="intents.jsp">Интенты</link>
        <!-- link accesskey="1" pageId="learn.jsp">Добавить</link>
        <link accesskey="3" pageId="delete.jsp">Удалить</link -->
    </navigation>
</page>
