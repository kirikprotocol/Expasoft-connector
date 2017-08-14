<%@page language="java" contentType="text/xml; charset=UTF-8"
%><%
	if ("true".equals(session.getAttribute("op.connected"))) {
		response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid=11");
	}
	Integer amountInt = (Integer) session.getAttribute("op.summ");
	if (amountInt == null){
		request.setAttribute("error", "Извините, произошла небольшая ошибка. Пожалуйста, повторите ввод.");
		%><jsp:forward page="input_summ.jsp"/><%
		return;
	}
	////
	session.setAttribute("op.connected", "true");
%><page version="2.0">
	<div>Обещанный платеж на сумму <%=amountInt%> р. по вашему номеру установлен.<br/>
		Пожалуйста, не забудьте пополнить счет в течение 3 ближайших дней, чтобы оставаться на связи. Общайтесь с удовольствием!<br/>
		Я могу Вам чем-то еще помочь?
	</div>
	<div>
		<input navigationId="submit" name="amount"/>
	</div>
	<navigation id="submit">
		<link accesskey="1" pageId="http://hosting-api-test.eyeline.mobi/index?sid=686&amp;=pid=18">Ok</link>
	</navigation>
	<attributes><attribute name="resource-id" value="/686/85/16"/></attributes>
</page>