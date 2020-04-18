package edu.montana.gsoc.msusel.inject

import spock.lang.Specification

class ConfigLoaderTest extends Specification {

    File config

    void setup() {
        config = new File("InjectorConfig")
        if (!config.exists()) {
            config << """\
data {
    driver = 'org.sqlite.JDBC'
    url = 'jdbc:sqlite:data/test.db'
    user = 'dev1'
    pass = 'passwd'
}

where {
    systemKey = "testsys"
    projectKey = "testsys:testproj"
    patternKey = "gof:visitor"
    patternInst = "testsys:testproj:visitor-01"
}

what {
    type = 'grime'
    form = 'TIG'
    max  = 5
    min  = 3
}
"""
        }
    }

    void cleanup() {
        config.delete()
    }

    def "LoadConfiguration happy path"() {
        given:
        ConfigLoader fixture = ConfigLoader.instance

        when:
        ConfigObject result = fixture.loadConfiguration(config)

        then:
        result.data != null
        result.where != null
        result.what != null
        result.data.driver == 'org.sqlite.JDBC'
        result.data.url == 'jdbc:sqlite:data/test.db'
        result.data.user == 'dev1'
        result.data.pass == 'passwd'
        result.where.systemKey == "testsys"
        result.where.projectKey == "testsys:testproj"
        result.where.patternKey == "gof:visitor"
        result.where.patternInst == "testsys:testproj:visitor-01"
        result.what.type == 'grime'
        result.what.form == 'TIG'
        result.what.max == 5
        result.what.min == 3
    }

    def "LoadConfiguration exceptional path"() {
        given:
        ConfigLoader fixture = ConfigLoader.instance

        when:
        fixture.loadConfiguration(null)

        then:
        thrown IllegalArgumentException
    }

    def "LoadConfiguration file does not exist"() {
        given:
        ConfigLoader fixture = ConfigLoader.instance
        config = new File("testconfig.yml")

        SecurityManager secManager = new TestingSecurityManager()
        System.setSecurityManager(secManager)

        when:
        fixture.loadConfiguration(config)

        then:
        thrown SecurityException
    }
}
