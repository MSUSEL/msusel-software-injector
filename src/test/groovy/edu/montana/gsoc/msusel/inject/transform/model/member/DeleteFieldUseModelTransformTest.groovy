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
package edu.montana.gsoc.msusel.inject.transform.model.member

import edu.isu.isuese.datamodel.Field
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class DeleteFieldUseModelTransformTest extends MemberModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        Field used = Field.findFirst("name = ?", "cX")

        // when
        fixture = new DeleteFieldUseModelTransform(method, used)
        fixture.execute()

        // then
        the(method.getFieldsUsed()).shouldNotContain(used)
        the(used.getMethodsUsing()).shouldNotContain(method)
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute used field is null"() {
        // given
        Field used = null

        // when
        fixture = new DeleteFieldUseModelTransform(method, used)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute member is not a method"() {
        // given
        Field used = Field.findFirst("name = ?", "cY")

        // when
        fixture = new DeleteFieldUseModelTransform(field, used)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute field is not used by member"() {
        // given
        Field used = Field.findFirst("name = ?", "cX")

        // when
        fixture = new DeleteFieldUseModelTransform(field, used)
        fixture.execute()
    }
}