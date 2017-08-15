<#include "common/header.ftl">

<#include "common/search_form.ftl">

<div id="result">
  <#include "common/search_results_options.ftl">

	<ul id="resultLinks">
    <#list result as item>
			<li><a href="${item }">${item }</a></li>
    </#list>
		</c:forEach>
	</ul>
</div>

<#include "common/footer.ftl">