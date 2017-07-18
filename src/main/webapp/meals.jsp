<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Meals</title>
    <style>
        tr:nth-child(even) {
            background-color: white;
        }
    </style>
</head>
<body style="font-family:verdana; background-color: azure;">
<form action="meals" method="post" id="formAdd"></form>
<c:forEach var="listlist" items="${mealsWithExceeded}">
    <form action="meals" method="post" id="form<c:out value="${listlist.id}"/>"></form>
</c:forEach>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>

<table style="width:100%; border-collapse: collapse">
    <tr style="font-weight: bold;text-align: left;">
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th></th>
        <th></th>
    </tr>
    <tr>
        <td><input type="datetime-local" name="dateTime" form="formAdd" title="Meal date/time"></td>
        <td><input type="text" name="description" form="formAdd" title="Meal description"></td>
        <td><input type="number" name="calories" form="formAdd" title="Meal calories"></td>
        <td><input type="submit" name="formButton" value="add" form="formAdd"></td>
        <td></td>
    </tr>
    <c:forEach var="list" items="${mealsWithExceeded}">
        <tr style="color:${list.exceed ? 'red':'green'}; ">
            <c:set var="updatedId" value="${updatedMealId}"/>
            <c:choose>
                <c:when test="${updatedId == list.id}">
                    <td><input type="hidden"
                               name="mealId"
                               value="<c:out value="${list.id}"/>"
                               form="form<c:out value="${list.id}"/>">
                        <input type="datetime-local"
                               name="dateTime"
                               value="<c:out value="${list.dateTime}"/>"
                               form="form<c:out value="${list.id}"/>"
                               title="Meal date/time"></td>
                    <td><input type="text"
                               name="description"
                               value="<c:out value='${list.description}'/>"
                               form="form<c:out value="${list.id}"/>"
                               title="Meal description"></td>
                    <td><input type="number"
                               name="calories"
                               value="<c:out value='${list.calories}'/>"
                               form="form<c:out value="${list.id}"/>"
                               title="Meal calories"></td>
                    <td><input type="submit"
                               name="formButton"
                               value="update"
                               form="form<c:out value="${list.id}"/>"></td>
                    <td></td>
                </c:when>
                <c:otherwise>
                    <td><input type="hidden"
                               name="mealId"
                               value="<c:out value="${list.id}"/>"
                               form="form<c:out value="${list.id}"/>">
                        <c:out value="${fn:replace(list.dateTime, 'T', ' ')}"/></td>
                    <td><c:out value="${list.description}"/></td>
                    <td><c:out value="${list.calories}"/></td>
                    <td><input type="submit"
                               name="formButton"
                               value="edit"
                               form="form<c:out value="${list.id}"/>"></td>
                    <td><input type="submit"
                               name="formButton"
                               value="delete"
                               form="form<c:out value="${list.id}"/>">
                    </td>
                </c:otherwise>
            </c:choose>
        </tr>
    </c:forEach>

</table>
</body>
</html>
