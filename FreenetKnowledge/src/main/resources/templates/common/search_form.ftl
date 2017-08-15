<!-- BEGIN search_form -->
<div id="search">
	<form action="/knowledge/search/urlSearch" method="post">
		<fieldset>
			<legend>Search in Url</legend>
			<label>Text: </label><input id="urlQuery" name="urlQuery" type="text" value="${urlQuery!'' }" />
			<input type="submit" value="Search" />
			<br>
			<fieldset>
				<legend>Search type</legend>
				<label>Ends with</label>
        <#if endsWith??>
  				<input name="endsWith" type="checkbox" checked='checked' />
        <#else>
  				<input name="endsWith" type="checkbox" />
				</#if>
			</fieldset>
			
		</fieldset>
	</form>
	
	<form action="/knowledge/search/contentSearch" method="post">
		<fieldset>
			<legend>Search in the content</legend>
			<label>Text: </label><input id="contentQuery" name="contentQuery" type="text" value="${contentQuery!'' }" />
			<input type="submit" value="Search" />
		</fieldset>
	</form>
</div>
<!-- END search_form -->