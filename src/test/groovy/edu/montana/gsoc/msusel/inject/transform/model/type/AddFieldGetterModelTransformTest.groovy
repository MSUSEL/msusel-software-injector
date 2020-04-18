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
package edu.montana.gsoc.msusel.inject.transform.model.type

import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.Method
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class AddFieldGetterModelTransformTest extends TypeModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        Field fld = Field.findFirst("name = ?", "fieldZ")
        fixture = new AddFieldGetterModelTransform(type, fld)

        // when
        fixture.execute()

        // then
        the(type.getMethods()).shouldContain(fixture.method)
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute field is null"() {
        // given
        Field fld = null
        fixture = new AddFieldGetterModelTransform(type, fld)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute getter already exists"() {
        // given
        Field fld = Field.findFirst("name = ?", "fieldZ")
        type.addMember(Method.builder().name("getFieldZ").compKey("getFieldZ").create())
        fixture = new AddFieldGetterModelTransform(type, fld)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute type does not contain field"() {
        // given
        Field fld = Field.findFirst("name = ?", "cX")
        fixture = new AddFieldGetterModelTransform(type, fld)

        // when
        fixture.execute()
    }
}