<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List, com.example.demo.entities.Document" %>
<!DOCTYPE html>	
<html>
<head>
<meta charset="ISO-8859-1">
<title>Result page</title>
</head>
<body>
<%
	List<Document> documents = (List<Document>)pageContext.getAttribute("documents");
%>
	<h2>Indexing complete</h2>
	<a href="/">Home</a>
</body>
</html>