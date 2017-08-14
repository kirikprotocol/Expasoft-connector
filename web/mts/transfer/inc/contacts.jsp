<%@page language="java" contentType="text/xml; charset=UTF-8"
%><%@ page import="java.util.HashMap"
%><%@ page import="java.util.Map" %><%!
    private Map<String, String> initContacts = new HashMap<String,String>();
    {
        initContacts.put("mom", "9111111111");
        initContacts.put("dad", "9222222222");
        initContacts.put("spouse", "9333333333");
        initContacts.put("child", "9655555555");
        initContacts.put("love", "9777777777");
    }

    private String getContactName(String contact) {
        if ( "mom".equals(contact)) {
            return "Маме";
        } else if ("dad".equals(contact)){
            return "Папе";
        } else if ("spouse".equals(contact)){
            return "Вашей второй половинке";
        } else if ("child".equals(contact)) {
            return "ребенку";
        } else if ("love".equals(contact)) {
            return "любимому человеку";
        }
        return contact;
    }

    private String getNumber(String contact){
        String number = initContacts.get(contact);
        if (number == null) {
            number = ""+ (9100000000l + (int)(Math.random()*100000000));
        }
        return number;
    }
%>