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

import edu.isu.isuese.datamodel.Accessibility
import edu.isu.isuese.datamodel.Class
import edu.isu.isuese.datamodel.Modifier
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class AddFieldModelTransformTest extends TypeModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        String name = "fieldX"
        Type fldType = Class.findFirst("name = ?", "TypeX")
        Accessibility access = Accessibility.PRIVATE

        // when
        fixture = new AddFieldModelTransform(type, name, fldType, access)
        fixture.execute()

        // then
        the(type.getFields().find { it.name == name }).shouldNotBeNull()
    }

    @Test
    void "test execute happy path modifiers"() {
        // given
        String name = "fieldX"
        Type fldType = Class.findFirst("name = ?", "TypeX")
        Accessibility access = Accessibility.PRIVATE

        // when
        fixture = new AddFieldModelTransform(type, name, fldType, access, Modifier.forName("final"), Modifier.forName("volatile"))
        fixture.execute()

        // then
        the(type.getFields().find { it.name == name }).shouldNotBeNull()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute name is null"() {
        // given
        String name = null
        Type fldType = Class.findFirst("name = ?", "TypeX")
        Accessibility access = Accessibility.PRIVATE

        // when
        fixture = new AddFieldModelTransform(type, name, fldType, access)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute name is empty"() {
        // given
        String name = ""
        Type fldType = Class.findFirst("name = ?", "TypeX")
        Accessibility access = Accessibility.PRIVATE

        // when
        fixture = new AddFieldModelTransform(type, name, fldType, access)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute type has field"() {
        // given
        String name = "fieldZ"
        Type fldType = Class.findFirst("name = ?", "TypeX")
        Accessibility access = Accessibility.PRIVATE

        // when
        fixture = new AddFieldModelTransform(type, name, fldType, access)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute field type is null"() {
        // given
        String name = "fieldX"
        Type fldType = null
        Accessibility access = Accessibility.PRIVATE

        // when
        fixture = new AddFieldModelTransform(type, name, fldType, access)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute access is null"() {
        // given
        String name = "fieldX"
        Type fldType = Class.findFirst("name = ?", "TypeX")
        Accessibility access = null

        // when
        fixture = new AddFieldModelTransform(type, name, fldType, access)
        fixture.execute()
    }
}