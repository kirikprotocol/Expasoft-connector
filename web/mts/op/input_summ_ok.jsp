<%@ page import="java.util.Calendar"
%><%@ page import="java.util.TimeZone"
%><%@ page import="java.text.DateFormat"
%><%@ page import="java.text.SimpleDateFormat"
%><%@ page import="java.util.Date"
%><%@ page import="com.eyeline.miniapps.connector.client.AiApplicationClient"
%><%@ page import="com.eyeline.miniapps.ai.model.AiAgent"
%><%@ page import="java.util.List"
%><%@ page import="java.util.regex.Pattern"
%><%@ page import="java.util.regex.Matcher"
%><%@page language="java" contentType="text/xml; charset=UTF-8"
%><%!
	private AiApplicationClient agent =  new AiApplicationClient("http://devel.globalussd.mobi/ai/ai", "ai-expasoft-chatme");
%><%
	String amountStr = request.getParameter("amount");
	int amountInt;
	try {
		Pattern pattern = Pattern.compile("(([0-9]*[.])?[0-9]+)");
		String input = amountStr;
		input = input.replaceAll(",",".");
		Matcher matcher = pattern.matcher(input);
		String lastMatch = null;
		if (matcher.find()) {
			lastMatch = matcher.group();
		}
		double inputSumm = Double.parseDouble(lastMatch);
		amountInt = (int) inputSumm;
		session.setAttribute("op.summ", amountInt);
	} catch (Exception e){
		List<AiAgent.Prediction> predictions = null;
		try {
			predictions = agent.predict(amountStr);
		} catch (Exception ex) {
			request.setAttribute("error", "Извините, не смог разобрать Ваш ответ. Пожалуйста, введите сумму цифрами в рублях.");
			%><jsp:forward page="input_summ.jsp"/><%
			return;
		}
		AiAgent.Prediction p = predictions.get(0);
		String intent = p.getIntent();
		if ("decline".equals(intent)) {
			response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid=2");
			return;
		}
		request.setAttribute("error", "Извините, не смог разобрать Ваш ответ. Пожалуйста, введите сумму цифрами в рублях.");
		%><jsp:forward page="input_summ.jsp"/><%
		return;
	}
	Integer max = (Integer) session.getAttribute("op.max");
	if (amountInt>max) {
		request.setAttribute("error", "Извините, но введенная сумма больше максимально допустимой по вашему номеру. Пожалуйста, введите сумму поменьше.");
		%><jsp:forward page="input_summ.jsp"/><%
		return;
	}
	int comission;
	if (amountInt <= 30) {
		comission = 0;
	} else if (amountInt< 100) {
		comission = 7;
	} else if (amountInt< 200) {
		comission = 10;
	} else if (amountInt< 500) {
		comission = 25;
	} else {
		comission = 50;
	}
	Calendar cal = Calendar.getInstance();
	cal.setTimeZone(TimeZone.getTimeZone("europe/moscow"));
	cal.add(Calendar.DAY_OF_YEAR, 3);
	Date tillDate = cal.getTime();
	DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	df.setTimeZone(TimeZone.getTimeZone("europe/moscow"));
	String till = df.format(tillDate);
%><page version="2.0">
	<div>В этом случае плата за использование услуги составит <%=comission%> р. Платеж будет действовать три дня.<br/>
	Полная сумма <%=amountInt+comission%> р. будет списана при следующем пополнении счета или после завершения действия платежа.<br/>
	Правильно ли я понял, что Вы согласны с условиями сервиса и желаете получить обещанный платеж на сумму <%=amountInt%> руб. до <%=till%>?
	</div>
    <navigation>
		<link accesskey="1" pageId="op_get.jsp">Да</link>
		<link accesskey="2" pageId="input_summ.jsp">Нет</link>
	</navigation>
	<attributes>
		<attribute name="resource-id" value="/686/85/15"/>
	</attributes>
</page>