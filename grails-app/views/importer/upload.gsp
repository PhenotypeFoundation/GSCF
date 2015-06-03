<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="main" />
	<title>Import data</title>
	
	<r:require modules="gscfimporter" />
</head>
<body>
	<div class="basicTabLayout importer uploadFile">
		<h1>
			<span class="truncated-title">
				Upload file: ${importer.identifier}
			</span>
			<g:render template="steps" model="[active: 'uploadFile']" />
		</h1>
		
		<g:render template="/common/flashmessages" />
		
		<span class="message info"> 
			<span class="title">Upload file and specify parameters</span> 
			Below you can upload an excel file, csv file or tab separated file. 
			Please also specify the parameters for the import.
		</span>
		 
		<g:form action="upload" name="uploadFile">
			<g:hiddenField name="_action" />
			<g:hiddenField name="importer" value="${importer.identifier}" />
			<fieldset id="uploadParameters" class="importerParameters">
				<legend>Upload file</legend>
				
				<af:fileFieldElement name="file"
									 description="Choose a file"
									 id="importFileName"
									 onUpload="Importer.upload.updateDataPreview" 
 									 value="${savedParameters?.file}"
 									 />
	
				<div class="element">
					<div class="description">Use data from sheet</div>
					<div class="input">
						<g:select name="upload.sheetIndex" value="${refreshParams?.sheetIndex}"
								  from="${1..50}"
								  optionKey="${{it-1}}" />
					</div>
				</div>				
				<div class="element">
					<div class="description">Column header at line</div>
					<div class="input">
						<g:select name="upload.headerRow" value="${savedParameters?.upload?.headerRow}"
								  from="${1..9}"
								  optionKey="${{it-1}}" />
					</div>
				</div>
				
				<div class="element">
					<div class="description">Separator</div>
					<div class="input">
						<g:select name="upload.separator" value="${savedParameters?.upload?.separator}"
								  keys="${[ ",", ";", "\\t"]}"
								  from="${[ ",", ";", "{tab}"]}" />
					</div>
				</div>				
								
				<div class="element">
					<div class="description">Date format</div>
					<div class="input">
						<g:select name="upload.dateFormat" value="${savedParameters?.upload?.dateFormat}"
								  from="${['dd/MM/yyyy (EU/India/South America/North Africa/Asia/Australia)', 'yyyy/MM/dd (China/Korea/Iran/Japan)', 'MM/dd/yyyy (US)']}"
								  keys="${['dd/MM/yyyy','yyyy/MM/dd','MM/dd/yyyy']}"/>
					</div>
				</div>				
			</fieldset>

			<fieldset id="exampleData" style="display: none;">
				<legend>Data preview</legend>
				<div id="datapreview" data-url="${g.createLink(action: 'datapreview')}">
				</div>
				<g:img class="spinner" dir="images" file="spinner.gif" />
			</fieldset>
			
			<g:if test="${importer.getParameters()}">
				<fieldset id="importerParameters" class="importerParameters">
					<legend>Parameters</legend>
					<g:each in="${importer.parameters}" var="parameter">
						<div class="element">
							<div class="description">${parameter.label}</div>
							<div class="input">
								<g:if test="${parameter.type == 'select'}">
									<g:select name="parameter.${parameter.name}" from="${parameter.values}" optionKey="id" value="${savedParameters?.parameter?.get(parameter.name)}"/>
								</g:if>
								<g:else>
									<input type="text" name="parameter.${parameter.name}" value="${savedParameters?.parameter?.get(parameter.name)}" />
								</g:else>
							</div>
						</div>				
					</g:each>
				</fieldset>			
			</g:if>		
			
			<br clear="all" />

			<p class="options">
				<g:link action="chooseType" class="previous">Previous</g:link>
				<a href="#" onClick="Importer.form.submit( 'uploadFile', 'next' ); return false;" class="next">Next</a>
			</p>

		</g:form>

		<r:script>
			Importer.upload.initialize();
		</r:script>
	</div>
</body>
</html>
