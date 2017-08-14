<%@page language="java" contentType="text/xml; charset=UTF-8"
%><%@ page import="mobi.eyeline.miniapps.bot.mnp.MnoApi" %><%!
    private MnoApi mnoApi = new MnoApi("http://tele2msg.eyeline.mobi/mno");

    private String mnoToName(String id) {
        if (id.startsWith("ru.mts")) return "МТС";
        if (id.startsWith("ru.bee")) return "Билайн";
        if (id.startsWith("ru.mega")) return "Мегафон";
        if (id.startsWith("ru.tele2")) return "Tele2";
        if (id.startsWith("ru.gtel")) return "Глобал-Телеком";
        if (id.startsWith("ru.motiv")) return "Мотив";
        return "";
    }

    private String getComissionText(String id) {
        if (id.startsWith("ru.mts")) return "0%";
        return "10,4%";
    }

    private Double getComissionPhone(String id, int amount) {
        if (id.startsWith("ru.mts")) return 0.0;
        return 0.104*amount;
    }

    private double getComissionCard(int amount) {
        double res = 0.043*amount;
        if (res<60) return 60.0;
        return res;
    }


    private String formatPhone(String msisdn) {
        java.text.MessageFormat phoneMsgFmt=new java.text.MessageFormat("({0})-{1}-{2}");
        //suposing a grouping of 3-3-4
        String[] phoneNumArr={
                msisdn.substring(1, 4),
                msisdn.substring(4,7),
                msisdn.substring(7)
        };
        return phoneMsgFmt.format(phoneNumArr);
    }

    private String formatCard(String card) {
        java.text.MessageFormat msgFmt=new java.text.MessageFormat("{0}-{1}-{2}-{3}");
        //suposing a grouping of 3-3-4
        String[] numArr={
                card.substring(0, 4),
                card.substring(4, 8),
                card.substring(8, 12),
                card.substring(12)
        };
        return msgFmt.format(numArr);
    }
%>