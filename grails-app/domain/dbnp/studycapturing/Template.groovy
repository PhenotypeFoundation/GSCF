package dbnp.studycapturing

/**
 * The Template class describes a study template, which is basically an extension of the study capture entities
 * in terms of extra fields (described by classes that extend the TemplateField class).
 * At this moment, only extension of the study and subject entities is implemented.
 *
 * Revision information:
 * $Rev$
 * $Author$
 * $Date$
 */
class Template implements Serializable {
	String name
	String description
	Class entity
	//nimble.User owner
	List fields
	static hasMany = [fields: TemplateField,
        sampleTemplates: Template] // only applicable when entity=SamplingEvent,
        // for storing which Sample (template)s result from a SamplingEvent (template)

	static constraints = {
		description(nullable: true, blank: true)
		// outcommented for now due to bug in Grails / Hibernate
		// see http://jira.codehaus.org/browse/GRAILS-6020
		//	name(unique:['entity'])
	}

	/**
	 * overloaded toString method
	 * @return String
	 */
	def String toString() {
		return this.name;
	}

	/**
	 * Look up the type of a certain template subject field
	 * @param	String	fieldName The name of the template field
	 * @return	String	The type (static member of TemplateFieldType) of the field, or null of the field does not exist
	 */
	def TemplateFieldType getFieldType(String fieldName) {
		def field = fields.find {
			it.name == fieldName	
		}
		field?.type
	}

	/**
	 * get all field of a particular type
	 * @param	Class	fieldType
	 * @return	Set<TemplateField>
	 */
	def getFieldsByType(TemplateFieldType fieldType) {
		def result = fields.findAll {
			it.type == fieldType
		}
		return result;
	}

	/**
	 * get all required fields
	 * @param	Class	fieldType
	 * @return	Set<TemplateField>
	 */
	def getRequiredFields() {
		def result = fields.findAll {
			it.required == true
		}
		return result;
	}

	/**
	 * overloading the findAllByEntity method to make it function as expected
	 * @param	Class		entity (for example: dbnp.studycapturing.Subject)
	 * @return	ArrayList
	 */
	public static findAllByEntity(java.lang.Class entity) {
		def results = []
		// 'this' should not work in static context, so taking Template instead of this
		Template.findAll().each() {
			if (entity.equals(it.entity)) {
				results[ results.size() ] = it
			}
		}

		return results
	}
}
