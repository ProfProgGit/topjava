<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

var messages = [];
messages['<spring:message text="user.enabled" javaScriptEscape="true"/>'] = '<spring:message code="user.enabled"
                                                                                             javaScriptEscape="true"/>';
messages['<spring:message text="user.disabled" javaScriptEscape="true"/>'] = '<spring:message code="user.disabled"
                                                                                              javaScriptEscape="true"/>';
messages['<spring:message text="common.record.saved" javaScriptEscape="true"/>'] = '<spring:message
        code="common.record.saved" javaScriptEscape="true"/>';
messages['<spring:message text="common.record.deleted" javaScriptEscape="true"/>'] = '<spring:message
        code="common.record.deleted" javaScriptEscape="true"/>';

