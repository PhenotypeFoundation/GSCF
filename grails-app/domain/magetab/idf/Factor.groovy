package magetab.idf

class Factor {

    String name
    OntologyTerm type

    static constraints = {

        type(nullable: true)
        
    }
}
