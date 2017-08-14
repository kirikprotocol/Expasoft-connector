<%@page language="java" contentType="text/xml; charset=UTF-8"
%><%@include file="inc/contacts.jsp"%><%@include file="inc/mno.jsp"%><%
    String receiverType = (String) session.getAttribute("receiverType");
    String receiver = (String) session.getAttribute("receiver");
    String comission = null;
    String title = null;
	String error = (String) request.getAttribute("error");
%><page version="2.0">
<% if (error!=null) { %>
   <div><%=error%></div>
<% } %>
    <%
    if ("contact".equals(receiverType)) {
        String msisdn = MnoApi.toMsisdn(this.getNumber(receiver));
        //String msisdn = "7XXXXXXXXXX";
        String mnoId = mnoApi.mnoSubsciber(msisdn).getId();
        title = getContactName(receiver) +" на номер "+this.formatPhone(msisdn)+", "+this.mnoToName(mnoId);
        comission = getComissionText(mnoId);
    } else if ("phone".equals(receiverType)) {
        String mnoId = mnoApi.mnoSubsciber(receiver).getId();
        title = "на номер "+this.formatPhone(receiver)+", "+this.mnoToName(mnoId);
        comission = getComissionText(mnoId);
    } else if ("card".equals(receiverType)) {
        title = "на карту "+this.formatCard(receiver);
        comission = "4,3%, но не менее 60 рублей.";
    } else {
        request.setAttribute("error", "Извините, но мне не удалось распознать реквизиты получателя средств.<br/>" +
                "Если необходим перевод на карту, введите номер карты.<br/>" +
                "Если Вы хотите совершить перевод одному из своих контактов, то просто назовите его.<br/>" +
                "В случае если Вы хотите сделать перевод на мобильный телефон, то напишите его в удобном для вас формате.");
    %><jsp:forward page="input_receiver.jsp"/><%
    }
    %>
    <div>
        Перевод <%=title%>.<br/>
        Комиссия за перевод составит: <%=comission%>
    </div>
    <div>
    <input navigationId="submit" name="amount" title="Какую сумму Вы желаете перевести?" />
  </div>
  <navigation id="submit">
    <link accesskey="1" pageId="input_summ_ok.jsp">Ok</link>
  </navigation>
	<attributes>
		<attribute name="resource-id" value="/686/85/103"/>
	</attributes>
</page>
