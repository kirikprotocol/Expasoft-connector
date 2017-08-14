<%@ page import="com.eyeline.miniapps.connector.client.AiApplicationClient"
%><%@ page import="com.eyeline.miniapps.ai.model.AiAgent"
%><%@ page import="java.util.List"
%><%@ page import="java.util.regex.Pattern"
%><%@ page import="java.util.regex.Matcher"
%><%@ page import="org.apache.commons.lang.StringUtils"
%><%@ page import="java.text.DecimalFormat"
%><%@page language="java" contentType="text/xml; charset=UTF-8"
%><%!
	private AiApplicationClient agent =  new AiApplicationClient("http://devel.globalussd.mobi/ai/ai", "ai-expasoft-chatme");
%><%@include file="inc/contacts.jsp"%><%@include file="inc/mno.jsp"%><%
	Integer amountInt = (Integer) session.getAttribute("transfer.summ");
	if (amountInt == null) {
		String amountStr = request.getParameter("amount");
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
			session.setAttribute("transfer.summ", amountInt);
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
				response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid=101");
				return;
			}
			request.setAttribute("error", "Извините, не смог разобрать Ваш ответ. Пожалуйста, введите сумму цифрами в рублях.");
			%><jsp:forward page="input_summ.jsp"/><%
			return;
		}

	}
	String receiverType = (String) session.getAttribute("receiverType");
	String receiver = (String) session.getAttribute("receiver");
	double comissionValue = 0;
	if ("contact".equals(receiverType)) {
		String msisdn = MnoApi.toMsisdn(this.getNumber(receiver));
		String mnoId = mnoApi.mnoSubsciber(msisdn).getId();
		comissionValue = this.getComissionPhone(mnoId, amountInt);
	} else if ("phone".equals(receiverType)) {
		String mnoId = mnoApi.mnoSubsciber(receiver).getId();
		comissionValue = this.getComissionPhone(mnoId, amountInt);
	} else if ("card".equals(receiverType)) {
		comissionValue = this.getComissionCard(amountInt);
	} else {
		request.setAttribute("error", "Извините, но мне не удалось распознать реквизиты получателя средств.<br/>" +
				"Если необходим перевод на карту, введите номер карты.<br/>" +
				"Если Вы хотите совершить перевод одному из своих контактов, то просто назовите его.<br/>" +
				"В случае если Вы хотите сделать перевод на мобильный телефон, то напишите его в удобном для вас формате.");
		%><jsp:forward page="input_receiver.jsp"/><%
	}

	String source = (String) session.getAttribute("transfer.source");
	if (StringUtils.isBlank(source)) {
		source = "phone";
		session.setAttribute("transfer.source", "phone");
	}
	String sourceText = "вашего лицевого счета";
	if ("card".equals(source)) {
		sourceText = "вашей карты Alfa ***5476";
	}
	DecimalFormat df = new DecimalFormat("###,###.##");
%><page version="2.0">
	<div>
		<% if (comissionValue>0) {%>
		Полная сумма платежа за перевод составит <%=df.format(comissionValue+amountInt)%> р., включая комиссию <%=df.format(comissionValue)%> р.<br/>
		<% } else { %>
		Полная сумма перевода составит <%=df.format(amountInt)%> р., комиссия за перевод не взымается.<br/>
		<% } %>

		Вы согласны с условиями сервиса и желаете совершить перевод с <%=sourceText%>?<br/>
	</div>
    <navigation>
		<link accesskey="1" pageId="transfer.jsp">Да</link>
		<link accesskey="2" pageId="switch_source.jsp">Нет</link>
	</navigation>
	<attributes>
		<attribute name="resource-id" value="/686/85/104"/>
	</attributes>
</page>