grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsCentral()
        grailsRepo "http://grails.org/plugins"
        mavenRepo "http://nexus.dbnp.org/content/repositories/releases"

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        mavenLocal()
        mavenCentral()

//	mavenRepo "http://repo.grails.org/grails/repo/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.18'
        compile "org.grails:grails-webflow:$grailsVersion"
    }

    plugins {
        build(":tomcat:$grailsVersion",

              ":release:2.2.1",
              ":rest-client-builder:1.0.3") {
            export = false
        }

        compile(
        ":rest:0.8",
		":hibernate:$grailsVersion",
                ":ajaxflow:0.2.4",
                ":jquery:1.11.1",
                ":webflow:2.0.0") {
            exclude 'grails-webflow'
            export = true
        }

    }
}

grails.plugin.location.'gdt' = '../GDT'