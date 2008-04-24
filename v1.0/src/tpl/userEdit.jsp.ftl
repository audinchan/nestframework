<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Edit ${hssutil.getDeclarationName(element)}</title>
</head>
<body>
<html:form action="/${hssutil.toBeanName(element)}${ction}">
<input type="hidden" name="act" value="save"/>
<html:hidden property="userid"/>
username: <html:text property="username"/><br/>
password: <html:password property="password"/><br/>
<html:submit>Submit</html:submit>
</html:form>
</body>
</html>