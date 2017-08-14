<%@page language="java" contentType="text/xml; charset=UTF-8"
%><%!
    private String getTarif(HttpServletRequest request) {
        String tarif = (String) request.getSession().getAttribute("tarif");
        if (tarif == null) {
            tarif = "smart";
            this.setTarif(tarif, request);
        }
        return tarif;
    }

    private void setTarif(String tarif, HttpServletRequest request) {
        request.getSession().setAttribute("tarif", tarif);
    }

%><%
        String tarif = request.getParameter("tarif");
        String currentTarif = this.getTarif(request);
        if (session.getAttribute("tariff.connected")==null) {
            session.setAttribute("tariff.connected", "true");
        } else if (currentTarif.equals(tarif)) {
            session.removeAttribute("tariff.connected");
            response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid=18");
            return;
        }
        if (currentTarif.equals(tarif)) {
            response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid=133");
            return;
        } else {
            this.setTarif(tarif, request);
            if ("bezlim".equals(tarif)) {
                response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid=122");
                return;
            } else if ("plus".equals(tarif)) {
                response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid=123");
                return;
            } else if ("smart".equals(tarif)) {
                response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid=124");
                return;
            } else if ("mini".equals(tarif)) {
                response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid=125");
                return;
            } else if ("top".equals(tarif)) {
                response.sendRedirect("http://hosting-api-test.eyeline.mobi/index?sid=686&pid=126");
                return;
            }
        }
%>
