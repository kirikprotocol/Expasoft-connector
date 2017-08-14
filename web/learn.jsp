<%@ page import="com.eyeline.expasoft.chatme.model.*"
%><%@         page import="org.apache.commons.lang.StringUtils"
%><%@ page contentType="text/html;charset=UTF-8" language="java"
%><%@include file="constant.jsp" %><%!
    private ChatmeAgent agent = new ChatmeAgent("http://ubuntu@expasoft.com:5000/chatme/pilot/api/v1.0", AGENT_ID, AGENT_PWD);
%><%
    String eventType = request.getParameter("event.type");
    IntentRequest req = null;
    if ("text".equals(eventType)) {
        String input = request.getParameter("event.text");
        String[] data = input.split("\n");
        String id = data[0];
        req = new IntentRequest();
        req.setName(StringUtils.replace(id.trim().toLowerCase(), " ", "_"));
        for (int i=1; i<data.length; i++){
            req.add(data[i]);
        }
        session.setAttribute("intent", req);
    }
    if (req == null) {
        req = (IntentRequest) session.getAttribute("intent");
        session.removeAttribute("intent");
    }
    String approve = request.getParameter("approve");
%><page version="2.0">
        <% if (req == null) { %>
    <div>Введите интент в формате:<br/>
         <i>Идентификатор интента (перенос строки)<br/>
        Список фраз разделенных переносом строки<br/></i>
    </div>
        <% } else if ("true".equals(approve)) {
            IntentResponse res = agent.addIntent(req);
            agent.doSave();
            String msg = "Успешно: "+res.getSuccess()+"<br/>" +
                         "Интент: "+res.getIntentName()+"<br/>" +
                         "Статус: "+res.getResponse();
            request.setAttribute("msg",msg);
        %><jsp:forward page="intents.jsp"/><%
               return;
           } else { %>
            <div>Подтверите добавление интента: <b><%=req.getName()%></b><br/>
                <%
                    for (UserSay say: req.getUserSays()) {
                        for (UserSay.Data data: say.getData()){
                            %>* <i><%=data.getText()%></i><br/><%
                        }
                    }
                %>
            </div>
    <navigation>
        <link accesskey="2" pageId="learn.jsp?approve=true">Подтвердить</link>
    </navigation>

    <% } %>
    <navigation>
        <link accesskey="1" pageId="intents.jsp">Назад</link>
    </navigation>
</page>
