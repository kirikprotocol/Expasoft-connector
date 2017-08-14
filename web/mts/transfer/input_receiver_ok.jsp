<%@ page import="java.util.regex.Pattern"
%><%@ page import="mobi.eyeline.miniapps.bot.mnp.MnoApi"
%><%@ page import="org.apache.commons.lang.StringUtils"
%><%@ page import="com.eyeline.miniapps.connector.client.AiApplicationClient"
%><%@ page import="com.eyeline.miniapps.ai.model.AiAgent"
%><%@ page import="java.util.List"
%><%@page language="java" contentType="text/xml; charset=UTF-8"
%><%!
    private AiApplicationClient agent =  new AiApplicationClient("http://devel.globalussd.mobi/ai/ai", "ai-expasoft-chatme");
%><%@include file="inc/contacts.jsp"%><%
    Pattern cardPattern = Pattern.compile("[0-9]{16}");
    String receiverInput = request.getParameter("receiver");
    String filteredReceiver = receiverInput.replaceAll("[\\D]", "");
    if (cardPattern.matcher(filteredReceiver).matches()) {
        session.setAttribute("receiverType", "card");
        session.setAttribute("receiver", filteredReceiver);
        %><jsp:forward page="input_receiver_done.jsp"/><%
        return;
    } else if (filteredReceiver.length()>9 && filteredReceiver.length()<12) {
        String msisdn = MnoApi.toMsisdn(receiverInput);
        if (StringUtils.isNotBlank(msisdn)) {
            session.setAttribute("receiverType", "phone");
            session.setAttribute("receiver", msisdn);
            %><jsp:forward page="input_receiver_done.jsp"/><%
            return;
        }
    }
    List<AiAgent.Prediction> predictions = null;
    try {
        predictions = agent.predict(receiverInput);
    } catch (Exception e) {
        request.setAttribute("error", "Извините, временнные технические неполадки. Адресная книга контактов недоступна. Пожалуйста, воспользуйтесь переводом по номеру карты или номеру телефона");
        %><jsp:forward page="input_receiver.jsp"/><%
    }
    AiAgent.Prediction p = predictions.get(0);
    String intent = p.getIntent();
    if ("decline".equals(intent)) {
        String source = (String) session.getAttribute("transfer.source");
        if (StringUtils.isNotBlank(source)) {
            source = "phone";
        }
        if ("phone".equals(source)) {
            response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid=101");
            return;
        } else {
            response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid=134");
            return;
        }
    }
    if (initContacts.containsKey(intent)) {
        session.setAttribute("receiverType", "contact");
        session.setAttribute("receiver", intent);
        %><jsp:forward page="input_receiver_done.jsp"/><%
        return;
    }
    request.setAttribute("error", "Извините, но мне не удалось распознать реквизиты получателя средств.<br/>" +
            "Если необходим перевод на карту, введите номер карты.<br/>" +
            "Если Вы хотите совершить перевод одному из своих контактов, то просто назовите его.<br/>" +
            "В случае если Вы хотите сделать перевод на мобильный телефон, то напишите его в удобном для вас формате.");
    %><jsp:forward page="input_receiver.jsp"/>