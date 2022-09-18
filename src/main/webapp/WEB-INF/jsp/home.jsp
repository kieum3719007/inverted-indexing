<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page
	import="java.util.List, org.apache.commons.collections.CollectionUtils"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<style>
	table, th, td {
	 border: 1px solid black;
	 border-collapse: collapse;
	 margin-top: 10px;
	}
</style>
<title>Inverse index</title>
</head>
<body>

	<%
	List<String> documents = (List<String>) pageContext.findAttribute("documents");
	String searchText = (String) pageContext.findAttribute("search-text");
	%>
	<form:form action="/process-file" enctype="multipart/form-data">
		<h1>Inverse indexing</h1>
		<fieldset>
			<label for='folder'>Upload Folder:</label> <br> <input
				type="file" id="folder" name="files" webkitdirectory dicrectory />
			<input type='submit' value='submit'>
		</fieldset>
	</form:form>

	<br>

	<form:form action="/search" method="POST">
		<legend> Search </legend>
		<fieldset>
			<label for="search-query">Query</label>
			<input type="text"
				placeholder="search text" autocomplete id="search-text"
				value="${searchText}"
				name="searchText">
			<input type="submit" value="Search" />
		</fieldset>
	</form:form>

	<table>
		<thead>
			<tr>
				<th>Search Result</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${documents}" var="document">
				<tr>
					<td>${document}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</body>
</html>