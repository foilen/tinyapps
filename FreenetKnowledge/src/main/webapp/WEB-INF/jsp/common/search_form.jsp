<%
/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- BEGIN search_form -->
<div id="search">
	<form action="${pageContext.request.contextPath}/knowledge/search/urlSearch" method="post">
		<fieldset>
			<legend>Search in Url</legend>
			<label>Text: </label><input id="urlQuery" name="urlQuery" type="text" value="${urlQuery }" />
			<input type="submit" value="Search" />
			<br>
			<fieldset>
				<legend>Search type</legend>
				<label>Ends with</label>
				<c:if test="${endsWith }">
					<c:set var="endsWithChecked" value="checked='checked'" />
				</c:if>
				<input name="endsWith" type="checkbox" ${endsWithChecked } />
			</fieldset>
			
		</fieldset>
	</form>
	
	<form action="${pageContext.request.contextPath}/knowledge/search/contentSearch" method="post">
		<fieldset>
			<legend>Search in the content</legend>
			<label>Text: </label><input id="contentQuery" name="contentQuery" type="text" value="${contentQuery }" />
			<input type="submit" value="Search" />
		</fieldset>
	</form>
</div>
<!-- END search_form -->