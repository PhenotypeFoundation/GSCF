<%
/**
 * first wizard page / tab
 *
 * @author Jeroen Wesbeek
 * @since  20101206
 *
 * Revision information:
 * $Rev$
 * $Author$
 * $Date$
 */
%>
<af:page>
    <title>Importer wizard (simple)</title>
    <h1>Importer wizard</h1>
    <p>You can import your Excel data to the server by choosing a file from your local harddisk in the form below.</p>
	<table border="0">
    	<tr>
	    <td width="100px">
		Choose your Excel file to import:
	    </td>
	    <td width="100px">
		<wizard:fileFieldElement name="importfile" value=""/>
	    </td>
	</tr>
	<tr>
	    <td width="100px">
		Use data from sheet:
	    </td>
	    <td width="100px">
		<g:select name="sheetindex" from="${1..25}"/>
	    </td>
	</tr>
	<tr>
	    <td width="100px">
		Columnheader starts at row:
	    </td>
	    <td width="100px">
		<g:select name="headerrow" from="${1..10}"/>
	    </td>
	</tr>
	<tr>
	    <td width="100px">
		Data starts at row:
	    </td>
	    <td width="100px">
		<g:select name="datamatrix_start" from="${2..10}"/>
	    </td>
	</tr>
	<tr id="studyfield">
	    <td>
		Choose your study:
	    </td>
	    <td>
		<g:select name="study.id" from="${studies}" optionKey="id"/>
	    </td>
	</tr>
	<tr>
	    <td>
		Choose type of data:
	    </td>
	    <td>
		<g:select
		name="entity"
		id="entity"
		from="${importer_importableentities}"
		optionValue="${{it.value.name}}"
		optionKey="${{it.value.encrypted}}"
		noSelection="['':'-Choose type of data-']"
		onChange="${remoteFunction( controller: 'importer',
					    action:'ajaxGetTemplatesByEntity',
					    params: '\'entity=\'+escape(this.value)',
					    onSuccess:'updateSelect(\'template_id\',data,false,false,\'default\')')}" />
	    </td>
	</tr>
	<tr>
	    <td>
		<div id="datatemplate">Choose type of data template:</div>
	    </td>
	    <td>
		<g:select rel="typetemplate" entity="none" name="template_id" optionKey="id" optionValue="name" from="[]" />
	    </td>
	</tr>
	</table>
</af:page>