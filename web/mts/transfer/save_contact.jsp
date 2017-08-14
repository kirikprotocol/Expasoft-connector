<%@ page import="org.apache.commons.lang.StringUtils"
%><%@page language="java" contentType="text/xml; charset=UTF-8"
%><%@include file="inc/contacts.jsp"%><%@include file="inc/mno.jsp"%><%
    String receiver = (String) session.getAttribute("receiver");
    String receiverType = (String) session.getAttribute("receiverType");
    String title = "";
    if ("phone".equals(receiverType)) {
        String mnoId = mnoApi.mnoSubsciber(receiver).getId();
        title = this.formatPhone(receiver)+" ("+this.mnoToName(mnoId)+")";
    } else if ("card".equals(receiverType)) {
        title = this.formatCard(receiver);
    } else {
        request.setAttribute("error", "Извините, но мне не удалось распознать реквизиты получателя средств.<br/>" +
                "Если необходим перевод на карту, введите номер карты.<br/>" +
                "Если Вы хотите совершить перевод одному из своих контактов, то просто назовите его.<br/>" +
                "В случае если Вы хотите сделать перевод на мобильный телефон, то напишите его в удобном для вас формате.");
%><jsp:forward page="input_receiver.jsp"/><%
    }
%><page version="2.0">
    <div>
        Ваш платеж выполнен.<br/>
        Вам отправлено смс об успешном выполнении операции.<br/>
        Вы хотели бы сохранить  контакт <%=title%> в адресной книге, для быстрого перевода средств в следующий раз?
    </div>
    <navigation>
        <link accesskey="1" pageId="http://hosting-api-test.eyeline.mobi/index?sid=686&amp;pid=139">Да</link>
        <link accesskey="2" pageId="http://hosting-api-test.eyeline.mobi/index?sid=686&amp;pid=11">Нет</link>
    </navigation>
    <attributes>
        <attribute name="resource-id" value="/686/85/138"/>
    </attributes>
</page>