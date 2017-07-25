<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <h2>Meal list</h2>
    <form name="filterForm">
        <table border="1" cellpadding="8" cellspacing="0">
            <tr>
                <td>From date:</td>
                <td><input type="date"
                           id="startDate"
                           name="startDate"
                           value="<c:out value='${startDate == null ? "" : startDate }'/>"
                           title="Start Date">
                </td>
                <td>From time:</td>
                <td><input type="time"
                           id="startTime"
                           name="startTime"
                           value="<c:out value='${startTime == null ? "" : startTime }'/>"
                           title="Start Time">
                </td>
            </tr>
            <tr>
                <td>To date:</td>
                <td><input type="date"
                           id="endDate"
                           name="endDate"
                           value="<c:out value='${endDate == null ? "" : endDate }'/>"
                           title="End Date">
                </td>
                <td>To time:</td>
                <td><input type="time"
                           id="endTime"
                           name="endTime"
                           value="<c:out value='${endTime == null ? "" : endTime }'/>"
                           title="End Time">
                </td>
            </tr>
            <tr>
                <td colspan="4">
                    <a onclick="reset();return false;" href="#">Reset</a>
                    <a onclick="filter();return false;" href="#">Filter</a>
                </td>
            </tr>
        </table>
    </form>
    <script>
        function reset() {
            window.location = "meals?action=filter&startDate=&endDate=&startTime=&endTime=";
        }

        function filter() {
            window.location = "meals?action=filter&" + buildRequest();
        }

        function buildRequest() {
            var startDate = document.getElementById("startDate");
            var endDate = document.getElementById("endDate");
            var startTime = document.getElementById("startTime");
            var endTime = document.getElementById("endTime");
            return "startDate=" + startDate.value +
                "&endDate=" + endDate.value +
                "&startTime=" + startTime.value +
                "&endTime=" + endTime.value;
        }
    </script>
    <hr/>
    <a href="meals?action=create">Add Meal</a>
    <hr/>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>