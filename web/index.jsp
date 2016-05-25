<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String base = request.getContextPath();
	String api = base + "/api/demo";
	pageContext.setAttribute("base", base);
	pageContext.setAttribute("api", api);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
<title>EAction DEMO</title>
<style type="text/css">
body {
	padding: 0;
	margin: 0;
	background-color: #FEFEFE;
	font-family: consolas;
}
.wrap {
	width: 500px;
}
form {
	margin-bottom: 20px;
	border-top: 1px solid #ccc;
}
</style>
</head>
<body>
	<h1>EAction DEMO</h1>
	
	<div class="wrap">
		<form action="${api}/index" target="_api">
			<h3>${api}/index</h3>
			<input type="submit" value="Submit" />
		</form>
		<form action="${api}/view" method="post" target="_api">
			<h3>${api}/view</h3>
			<p>paramA : <input text="text" name="paramA" value="paramA" /></p>
			<p>paramB : <input text="text" name="paramB" value="paramB" /></p>
			<p>paramC : <input text="text" name="paramC" value="paramC" /></p>
			<input type="submit" value="Submit" />
		</form>
		<form action="${api}/upload" method="post" enctype="multipart/form-data" target="_api">
			<h3>${api}/upload</h3>
			<p>paramA : <input text="text" name="paramA" value="paramA" /></p>
			<p>paramB : <input text="text" name="paramB" value="paramB" /></p>
			<p>paramC : <input text="text" name="paramC" value="paramC" /></p>
			<p><input type="file" name="myfile" /></p>
			<input type="submit" value="Submit" />
		</form>
	</div>

</body>
</html>
