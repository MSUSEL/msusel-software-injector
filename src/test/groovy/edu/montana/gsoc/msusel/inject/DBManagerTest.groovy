/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
