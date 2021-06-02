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
package edu.montana.gsoc.msusel.inject.transform.source

import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.inject.transform.BaseSourceTransformSpec
import edu.montana.gsoc.msusel.inject.transform.source.type.RenameType
import org.junit.Test

class TypeTransformTest extends BaseSourceTransformSpec {

    @Test
    void "kind for class"() {
        // given
        File file = File.builder().name("test").create()
        Type type = Type.builder().name("test").create()
        file.addType(type)
        TypeTransform fixture = new RenameType(file, type, "newtest")

        // when
        String result = fixture.kind()

        // then
        the(result).shouldBeEqual("class")
    }

    @Test
    void "kind for interface"() {
        // given
        File file = File.builder().name("test").create()
        Type type = Type.builder().type(Type.INTERFACE).name("test").create()
        file.addType(type)
        TypeTransform fixture = new RenameType(file, type, "newtest")

        // when
        String result = fixture.kind()

        // then
        the(result).shouldBeEqual("interface")
    }

    @Test
    void "kind for enum"() {
        // given
        File file = File.builder().name("test").create()
        Type type = Type.builder().type(Type.ENUM).name("test").create()
        file.addType(type)
        TypeTransform fixture = new RenameType(file, type, "newtest")

        // when
        String result = fixture.kind()

        // then
        the(result).shouldBeEqual("enum")
    }

    @Test
    void "kind for other"() {
        // given
        File file = File.builder().name("test").create()
        Type type = UnknownType.builder().name("test").create()
        TypeTransform fixture = new RenameType(file, type, "newtest")

        // when
        String result = fixture.kind()

        // then
        the(result).shouldBeEqual("")
    }
}