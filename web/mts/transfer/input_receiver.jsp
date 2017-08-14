<%@page language="java" contentType="text/xml; charset=UTF-8"
%><%!
    private boolean isAvailableTransfer(HttpServletRequest request) {
        return true;
    }
%><%
    session.removeAttribute("transfer.summ");
    session.removeAttribute("receiverType");
    session.removeAttribute("receiver");
    session.removeAttribute("transfer.complete");
    String error = (String) request.getAttribute("error");
%><page version="2.0">
    <% if (error!=null) { %>
    <div><%=error%></div>
    <% } %>
    <div>
        <input navigationId="submit" name="receiver" title="Укажите получателя перевода, его номер телефона или карты" />
    </div>
    <navigation id="submit">
        <link accesskey="1" pageId="input_receiver_ok.jsp">Ok</link>
    </navigation>
    <attributes>
        <attribute name="resource-id" value="/686/85/102"/>
    </attributes>
</page>
