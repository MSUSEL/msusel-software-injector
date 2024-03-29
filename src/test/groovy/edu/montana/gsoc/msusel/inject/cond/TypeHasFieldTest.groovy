/*
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
package edu.montana.gsoc.msusel.inject.cond


import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.Type
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class TypeHasFieldTest extends DBSpec {

    TypeHasField fixture
    Type type
    Field field

    @Before
    void setUp() throws Exception {
        type = Type.builder().type(Type.CLASS).name("Test").compKey("Test").create()
        field = Field.builder().name("Field").compKey("Test:Field").create()
        fixture = new TypeHasField(type, field)
    }

    @After
    void tearDown() throws Exception {
    }

    @Test
    void "test check when field has been added"() {
        // given
        type.addMember(field)

        // when
        boolean result = fixture.check()

        // then
        the(result).shouldBeTrue()
    }

    @Test
    void "test check when field has not been added"() {
        // given
        fixture

        // when
        boolean result = fixture.check()

        // then
        the(result).shouldBeFalse()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test check when field is null"() {
        // given
        fixture = new TypeHasField(type, null)

        // when
        fixture.check()

        // then
    }

    @Test(expected = IllegalArgumentException.class)
    void "test check when type is null"() {
        // given
        fixture = new TypeHasField(null, field)

        // when
        fixture.check()
    }
}