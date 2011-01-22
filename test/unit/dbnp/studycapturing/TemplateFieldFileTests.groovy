package dbnp.studycapturing

import grails.test.*
import nl.grails.plugins.gdt.*

class TemplateFieldFileTests extends GrailsUnitTestCase {
    def testEvent;
    def fileService;


    protected void setUp() {
        super.setUp()

        fileService = new FileService();

        // Override uploadDir method because the applicationContext is not
        // available in testcases
        fileService.metaClass.getUploadDir = {
            return new File( System.properties['base.dir'] + File.separator + 'web-app' + File.separator + 'fileuploads' );
        }
        
        // Create the template itself
        def testTemplate = new Template(
                name: 'Template for testing relative file fields',
                entity: dbnp.studycapturing.Event,
                fields: [
                    new TemplateField(
                        name: 'testRelTime',
                        type: TemplateFieldType.RELTIME,
	                    entity: Event
                    ),
                    new TemplateField(
                        name: 'testFile',
                        type: TemplateFieldType.FILE
                    )
                ]
            );

        this.testEvent = new Event(
                template: testTemplate,
                startTime: 3600,
                endTime: 7200
        )

        // Sometimes the fileService is not created yet
        if( !testEvent.fileService ) {
            testEvent.fileService = fileService;
        }

        // Create two files
        println( fileService.getUploadDir() );

        new File( fileService.getUploadDir(), 'TemplateFieldTest.txt' ).createNewFile();
        new File( fileService.getUploadDir(), 'TemplateFieldTest2.txt' ).createNewFile();
        new File( fileService.getUploadDir(), 'TemplateFieldTest3.txt' ).createNewFile();

        // Create a new directory
        new File( fileService.getUploadDir(), 'subdir' ).mkdir();
        new File( fileService.getUploadDir(), 'subdir/TemplateFieldSub.txt' ).createNewFile();

    }

    protected void tearDown() {
        super.tearDown()

        new File( fileService.getUploadDir(), 'TemplateFieldTest.txt' ).delete();
        new File( fileService.getUploadDir(), 'TemplateFieldTest2.txt' ).delete();
        new File( fileService.getUploadDir(), 'TemplateFieldTest3.txt' ).delete();

        new File( fileService.getUploadDir(), 'subdir/TemplateFieldSub.txt' ).delete();
        new File( fileService.getUploadDir(), 'subdir' ).delete();
    }

    void testFileFieldCreation() {
        def FileField = new TemplateField(
                name: 'testFile',
                type: TemplateFieldType.FILE
        );
    }


        // If NULL is given, the field value is emptied and the old file is removed
        // If an empty string is given, the field value is kept as was
        // If a file is given, it is moved to the right directory. Old files are deleted. If
        //   the file does not exist, the field is kept
        // If a string is given, it is supposed to be a file in the upload directory. If
        //   it is different from the old one, the old one is deleted. If the file does not
        //   exist, the field is kept.

    void testFileSetValue() {
        // Check whether the field exists
        assert this.testEvent.fieldExists( 'testFile' );

        // See that it is not a domain field
        assert !this.testEvent.isDomainField( 'testFile' );

        // See that it is a FILE field
        // assert !this.testEvent.getField( 'testFile' ).type == TemplateFieldType.FILE;
        println( this.testEvent.getStore( TemplateFieldType.FILE ) );
        
        // Set the name of an existing file
        assert fileService.fileExists( 'TemplateFieldTest.txt' );
        this.testEvent.setFieldValue( 'testFile', 'TemplateFieldTest.txt' );
        assert this.testEvent.getFieldValue( 'testFile' ) == 'TemplateFieldTest.txt';
        assert fileService.fileExists( 'TemplateFieldTest.txt' );

        // Set the name of a non existing file
        assert !fileService.fileExists( 'TemplateFieldTestNotExisting.txt' );
        this.testEvent.setFieldValue( 'testFile', 'TemplateFieldTestNotExisting.txt' );
        assert this.testEvent.getFieldValue( 'testFile' ) == 'TemplateFieldTest.txt';
        assert fileService.fileExists( 'TemplateFieldTest.txt' );
        assert !fileService.fileExists( 'TemplateFieldTestNotExisting.txt' );

        // Set the name of a new existing file, and the old one is deleted
        assert fileService.fileExists( 'TemplateFieldTest.txt' );
        assert fileService.fileExists( 'TemplateFieldTest2.txt' );
        this.testEvent.setFieldValue( 'testFile', 'TemplateFieldTest2.txt' );
        assert this.testEvent.getFieldValue( 'testFile' ) == 'TemplateFieldTest2.txt';
        assert !fileService.fileExists( 'TemplateFieldTest.txt' );
        assert fileService.fileExists( 'TemplateFieldTest2.txt' );

        // Set a nonexisting File object
        assert fileService.fileExists( 'TemplateFieldTest2.txt' );
        assert !fileService.fileExists( 'NonExistent.txt' );
        this.testEvent.setFieldValue( 'testFile', new File( fileService.getUploadDir(), 'NonExistent.txt' ) );
        assert this.testEvent.getFieldValue( 'testFile' ) == 'TemplateFieldTest2.txt';
        assert fileService.fileExists( 'TemplateFieldTest2.txt' );

        // Set a existing File object
        assert fileService.fileExists( 'TemplateFieldTest2.txt' );
        assert fileService.fileExists( 'subdir/TemplateFieldSub.txt' );
        assert !fileService.fileExists( 'TemplateFieldSub.txt' );

        def f = new File( fileService.getUploadDir(), 'subdir/TemplateFieldSub.txt' )
        this.testEvent.setFieldValue( 'testFile', f );
        
        assert this.testEvent.getFieldValue( 'testFile' ) == 'TemplateFieldSub.txt';
        assert fileService.fileExists( this.testEvent.getFieldValue( 'testFile' ) );
        assert !fileService.fileExists( 'TemplateFieldTest2.txt' );

        //fileService.delete( this.testEvent.getFieldValue( 'testFile' ) );

        // Set the name of an empty string
        assert fileService.fileExists( 'TemplateFieldTest3.txt' );
        this.testEvent.setFieldValue( 'testFile', 'TemplateFieldTest3.txt' );
        this.testEvent.setFieldValue( 'testFile', '' );
        assert this.testEvent.getFieldValue( 'testFile' ) == 'TemplateFieldTest3.txt';
        assert fileService.fileExists( 'TemplateFieldTest3.txt' );

        // THe old file must be deleted
        assert !fileService.fileExists( 'TemplateFieldSub.txt' );

        // Set the name to NULL (empty the field
        this.testEvent.setFieldValue( 'testFile', null );
        assert this.testEvent.getFieldValue( 'testFile' ) == null;
        assert !fileService.fileExists( 'TemplateFieldTest3.txt' );

    }

}
