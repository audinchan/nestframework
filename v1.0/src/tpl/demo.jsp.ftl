<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Demo Action</title>
<link href="../css/main.css" rel="stylesheet" type="text/css">
<%-- 1. 引入全局的JS验证方法 --%>
<script type="text/javascript" src="../common/validateScript.jsp"></script>
</head>
<body>
msg: ${r"${msg}"}<p>
<span class="error">
<%-- 2. 显示验证错误信息 --%>
<nest:errors/>
</span>
<%-- 3. 在form的onsubmit事件中调用验证方法。 --%>
<form name="formName" action="DemoAction.a" method="post" enctype="multipart/form-data" onsubmit="return validate_formName(this)">
<%-- 4. 告诉验证框架，当验证失败时重定向到哪个页面，默认为当前页面。 --%>
<nest:from/>
<table border="1">
	<tr>
		<td>用户名:</td>
		<td><input type="text" name="username" value="${r"${username }"}"></td>
	</tr>
	<tr>
		<td>年龄:</td>
		<td><input type="text" name="age" value="${r"${age }"}"></td>
	</tr>
	<tr>
		<td>日期:</td>
		<td><input type="text" name="date" value="${r"${date }"}"></td>
	</tr>
	<tr>
		<td>时间:</td>
		<td><input type="text" name="time" value="${r"${time }"}"></td>
	</tr>
	<tr>
		<td>选项:</td>
		<td>
		
			<input type="checkbox" name="items" value="1">item 1<br>
			<input type="checkbox" name="items" value="2">item 2<br>
			<input type="checkbox" name="items" value="3">item 3<br>
		</td>
	</tr>
	<tr>
		<td>文件:</td>
		<td>
			<input type="file" name="file"><br>
			<input type="file" name="file2"><br>
			${r"${fileContent }"}
		</td>
	</tr>
	<tr>
		<td></td>
		<td><input type="submit" value="提交数据" name="doSave"></td>
	</tr>
</table>
</form>

<%-- 5. 生成跟本ActionBean相关的验证函数 --%>
<nest:javascript action="/demo/DemoAction.a" form="formName" on="doSave"/>
</body>
</html>