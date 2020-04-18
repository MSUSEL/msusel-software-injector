package edu.montana.gsoc.msusel.inject

import edu.isu.isuese.datamodel.Interface
import org.javalite.activejdbc.Base
import org.javalite.activejdbc.DBException
import spock.lang.Specification

class DBManagerTest extends Specification {

    ConfigObject config

    def setup() {
        ConfigSlurper slurper = new ConfigSlurper()
        config = slurper.parse("""\
data {
    driver = 'org.sqlite.JDBC'
    url = 'jdbc:sqlite:data/test.db'
    user = 'dev1'
    pass = 'passwd'
}
""")
    }

    def cleanup() {
        Base.close()
    }

    def "test open"() {
        given:
        DBManager fixture = DBManager.instance

        when:
        fixture.open(config)
        Interface type = Interface.builder().name("Test").compKey("Test").create()
        type.save()

        then:
        type != null
    }

    def "test close"() {
        given:
        DBManager fixture = DBManager.instance

        when:
        fixture.open(config)
        fixture.close()
        Interface type = Interface.builder().name("Test").compKey("Test").create()

        then:
        thrown DBException
    }
}
