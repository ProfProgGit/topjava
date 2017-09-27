<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>

<c:set var="requestPath" value="${requestScope['javax.servlet.forward.request_uri']}"/>
<li class="dropdown">
    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><c:out value='${"ru" == pageContext.response.locale ? "ru" : "en" }'/><b class="caret"></b></a>
    <ul class="dropdown-menu">
        <li><a href="${requestPath}?language=en">English</a></li>
        <li><a href="${requestPath}?language=ru">Русский</a></li>
    </ul>
</li>
