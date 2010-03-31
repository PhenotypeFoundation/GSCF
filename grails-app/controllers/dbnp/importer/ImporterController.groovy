/**
 * Importer controller
 *
 * The importer controller handles the uploading of tabular, comma delimited and Excel format
 * based files. When uploaded a preview is shown of the data and the user can adjust the column
 * type. Data in cells which don't correspond to the specified column type will be represented as "#error".
 *
 * The importer controller catches the actions and consecutively performs the
 * logic behind it.
 *
 * @package	importer
 * @author	t.w.abma@umcutrecht.nl
 * @since	20100126
 *
 * Revision information:
 * $Rev$
 * $Author$
 * $Date$
 */

package dbnp.importer
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.ss.usermodel.DataFormatter
import dbnp.studycapturing.Template
import dbnp.studycapturing.Study
import dbnp.studycapturing.Subject
import dbnp.studycapturing.Event
import dbnp.studycapturing.Protocol
import dbnp.studycapturing.Sample
import dbnp.studycapturing.TemplateFieldType
import dbnp.studycapturing.TemplateField

class ImporterController {
    def ImporterService

    /**
     * Default page
     **/
    def index = { 
	[templates:Template.list()]
    }

    /**
    * This method will move the uploaded file to a temporary path and send the header
    * and the first n rows to the preview
    * @param importfile uploaded file to import
    */
    def upload = {
	def downloadedfile = request.getFile('importfile');
        def tempfile = new File(System.getProperty('java.io.tmpdir') + File.separatorChar + System.currentTimeMillis() + ".nmcdsp")
        downloadedfile.transferTo(tempfile)
        
	def wb = ImporterService.getWorkbook(new FileInputStream(tempfile))
        
	def header = ImporterService.getHeader(wb, 0)
	def datamatrix= ImporterService.getDatamatrix(wb, 0, 5)

	session.header = header
	session.importtemplate_id = params.template_id
	session.workbook = wb

        render (view:"step1", model:[header:header, datamatrix:datamatrix])
    }

    /**
    * User has assigned all entities and templatefieldtypes to the columns and continues to the next step (assigning properties to columns)
    * All information of the columns is stored in a session as MappingColumn object
    *
    * @param entity list of entities and columns it has been assigned to (columnindex:entitytype format)
    * @param templatefieldtype list of celltypes and columns it has been assigned to (columnindex:templatefieldtype format)
    * @return properties page
    *
    * @see celltype: http://poi.apache.org/apidocs/org/apache/poi/ss/usermodel/Cell.html
    */
    def savepreview = {
	def tft = null
	def entities  = request.getParameterValues("entity")
	def templatefieldtypes = request.getParameterValues("templatefieldtype")
	def identifiercolumnindex = params.identifier.toInteger()

	templatefieldtypes.each { t ->	    
	    def columnindex = t.split(":")[0].toInteger()
	    def templatefieldtype = t.split(":")[1]
	    
	    switch (templatefieldtype) {
		case "STRING"	    : tft = TemplateFieldType.STRING
				      break
		case "TEXT"	    : tft = TemplateFieldType.TEXT
				      break
		case "INTEGER"	    : tft = TemplateFieldType.INTEGER
				      break
		case "FLOAT"	    : tft = TemplateFieldType.FLOAT
				      break
		case "DOUBLE"	    : tft = TemplateFieldType.DOUBLE
				      break
		case "STRINGLIST"   : tft = TemplateFieldType.STRINGLIST
				      break
		case "ONTOLOGYTERM" : tft = TemplateFieldType.ONTOLOGYTERM
				      break
		case "DATE"	    : tft = TemplateFieldType.DATE
				      break
		default: break
	    }
	    session.header[columnindex].templatefieldtype = tft
	}

	entities.each { e ->	    	    
	    def columnindex = e.split(":")[0].toInteger()
	    def entitytype = e.split(":")[1].toInteger()
	    Class clazz	    

	    switch (entitytype) {
		case 0: clazz = Study
			break
		case 1: clazz = Subject
			break
		case 2: clazz = Event
			break
		case 3: clazz = Protocol
			break
		case 4: clazz = Sample
			break
		default: clazz = Object
			break
	    }

	    session.header[columnindex].identifier = (columnindex == identifiercolumnindex) ? true : false
	    session.header[columnindex].index = columnindex
	    session.header[columnindex].entity = clazz
	}

	// currently only one template is used for all entities
	// TODO: show template fields per entity
	
	def templates = Template.get(session.importtemplate_id)

	render(view:"step2", model:[entities:entities, header:session.header, templates:templates])
    }

    /**
    * @param columnproperty array of columns and the assigned property in `column:property_id` format
    *
    */
    def saveproperties = {
	def columnproperties  = request.getParameterValues("columnproperty")

	columnproperties.each { cp ->		
		def columnindex = cp.split(":")[0].toInteger()
		def property_id = cp.split(":")[1].toInteger()
		session.header[columnindex].property = TemplateField.get(property_id)		
	}

	//import workbook
	session.importeddata = ImporterService.importdata(session.importtemplate_id, session.workbook, 0, 1, session.header)	

	render(view:"step3", model:[datamatrix:session.importeddata])
    }

    def savepostview = {
	ImporterService.savedata(session.importeddata)	
	render(view:"step4")
    }
}
