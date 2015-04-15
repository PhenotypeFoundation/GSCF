<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="main"/>
	<title>Query results</title>
	<r:require module="advancedQuery" />
</head>
<body>

<h1>Query results: ${search.toString()}</h1>

<div class="searchoptions">
	${search.getNumResults()} <g:if test="${search.getNumResults() == 1}">study</g:if><g:else>studies</g:else> found 
	<g:render template="criteria" model="[criteria: search.getCriteria()]" />
</div>
<g:if test="${search.getNumResults() > 0}">
	<% 
		def resultFields = search.getShowableResultFields();
		def extraFields = search.getShowableResultFieldNames(resultFields);
	%>
	<table id="searchresults" class="paginate">
		<thead>
		<tr>
			<th class="nonsortable"><input type="checkbox" id="checkAll" onClick="checkAllPaginated(this);" /></th>
			<th>Title</th>
			<th>Code</th>
			<th>Subjects</th>
			<th>Events</th>
			<th>Assays</th>
			<g:each in="${extraFields}" var="fieldName">
				<th>${fieldName}</th>
			</g:each>			
		</tr>
		</thead>
		<tbody>
		<g:each in="${search.getResults()}" var="studyInstance" status="i">
			<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
				<td width="3%">
					<% /* 
						The value of this checkbox will be moved to the form (under this table) with javascript. This
						way the user can select items from multiple pages of the paginated result list correctly. See
						also http://datatables.net/examples/api/form.html and advancedQueryResults.js
					*/ %>
					<g:checkBox name="uuid" value="${studyInstance.UUID}" checked="${false}" onClick="updateCheckAll(this);" />
				</td>
				<td>
					<g:link controller="study" action="show" id="${studyInstance?.id}">${fieldValue(bean: studyInstance, field: "title")}</g:link>
					
				</td>
				<td>${fieldValue(bean: studyInstance, field: "code")}</td>
				<td>
					<% def subjectCounts = studyInstance.getSubjectCountsPerSpecies() %>
					<g:if test="${!subjectCounts}">
						-
					</g:if>
					<g:else>
						${subjectCounts.collect { it.value + " " + it.key }.join( ", " )}							
					</g:else>
				</td>

				<td>
					<% def eventTemplates = studyInstance.giveEventTemplates() %>
					<g:if test="${eventTemplates.size()==0}">
						-
					</g:if>
					<g:else>
						${eventTemplates*.name.join(', ')}
					</g:else>
				</td>

				<td>
					<% def assayModules = studyInstance.giveUsedModules() %>
					<g:if test="${assayModules.size()==0}">
						-
					</g:if>
					<g:else>
						${assayModules*.name.join(', ')}
					</g:else>
				</td>

				<g:each in="${extraFields}" var="fieldName">
					<td>
						<% 
							def fieldValue = resultFields[ studyInstance.id ]?.get( fieldName );
							if( fieldValue ) { 
								if( fieldValue instanceof Collection ) {
									if( fieldValue.size() > 50 ) {
										fieldValue = fieldValue.subList(0,50).collect { it.toString() }.findAll { it }.unique().join( ', ' )
										fieldValue += " ... " 
									} else {
										fieldValue = fieldValue.collect { it.toString() }.findAll { it }.unique().join( ', ' )
									}
								} else {
									fieldValue = fieldValue.toString();
								}
							} else {
								fieldValue = "";
							}
						%>
						${fieldValue}
					</td>
				</g:each>
			</tr>
		</g:each>
		</tbody>
	</table>
	<g:render template="resultsform" />

</g:if>
<g:render template="resultbuttons" model="[queryId: queryId]" />
</body>
</html>
