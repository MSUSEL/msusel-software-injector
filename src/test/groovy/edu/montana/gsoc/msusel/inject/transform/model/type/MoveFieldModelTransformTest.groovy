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
package edu.montana.gsoc.msusel.inject.transform.model.type


import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class MoveFieldModelTransformTest extends TypeModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        Field field = Field.findFirst("name = ?", "fieldZ")
        Type parent = Type.findFirst("name = ?", "TypeXY")

        // when
        fixture = new MoveFieldModelTransform(type, field, parent)
        fixture.execute()

        // then
        the(parent.getFields()).shouldContain(field)
        the(type.getFields()).shouldNotContain(field)
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute field is null"() {
        // given
        Field field = null
        Type parent = Type.findFirst("name = ?", "TypeXY")

        // when
        fixture = new MoveFieldModelTransform(type, field, parent)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute parent is null"() {
        // given
        Field field = Field.findFirst("name = ?", "fieldZ")
        Type parent = null

        // when
        fixture = new MoveFieldModelTransform(type, field, parent)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute type does not contain field"() {
        // given
        Field field = Field.findFirst("name = ?", "xy")
        Type parent = Type.findFirst("name = ?", "TypeXY")

        // when
        fixture = new MoveFieldModelTransform(type, field, parent)
        fixture.execute()
    }
}