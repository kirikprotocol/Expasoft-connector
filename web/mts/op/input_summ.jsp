<%@page language="java" contentType="text/xml; charset=UTF-8"
%><%!
    private Integer getMaxAvailableOp(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer max = (Integer) session.getAttribute("op.max");
        if (max==null) {
            max = (int) (Math.random()*1000) + 100;
            if (max>800) max = 800;
            session.setAttribute("op.max", max);
        }
        return max;
    }

    private boolean isAvailableOp(HttpServletRequest request) {
        HttpSession session = request.getSession();
        boolean enabledForUser = true;
        boolean alreadyConnected = (session.getAttribute("op.connected")!=null);
        return enabledForUser && !alreadyConnected;
    }
%><%
	session.removeAttribute("op.summ");
	String error = (String) request.getAttribute("error");
    if (!isAvailableOp(request)) {
        response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid=127");
        return;
    }
    Integer max = getMaxAvailableOp(request);
%><page version="2.0">
<% if (error!=null) { %>
   <div><%=error%></div>
<% } %>
  <div>
    <input navigationId="submit" name="amount" title="Максимально доступная сумма обещанного платежа по вашему номеру <%=max%> руб. Какую сумму Вы желаете получить?" />
  </div>
  <navigation id="submit">
    <link accesskey="1" pageId="input_summ_ok.jsp?max=<%=max%>">Ok</link>
  </navigation>
  <navigation>
        <link accesskey="0" intents="decline" pageId="http://hosting-api-test.eyeline.mobi/index?sid=686&amp;pid=127">Назад</link>
  </navigation>
	<attributes>
		<attribute name="resource-id" value="/686/85/8"/>
	</attributes>
</page>
