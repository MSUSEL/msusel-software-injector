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
package edu.montana.gsoc.msusel.inject.transform.model.namespace

import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.FileType
import edu.isu.isuese.datamodel.Namespace
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class MoveFileModelTransformTest extends NamespaceModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        File file = File.findFirst("name = ?", "Test1.java")
        Namespace other = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test.test3")

        // when
        fixture = new MoveFileModelTransform(ns, file, other)
        fixture.execute()

        // then
        the(ns.getFiles()).shouldNotContain(file)
        the(other.getFiles()).shouldContain(file)
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute null file"() {
        // given
        File file = null
        Namespace other = Namespace.findFirst("nsKey = ?", "TestData:testproj:1.0:test.test3")

        // when
        fixture = new MoveFileModelTransform(ns, file, other)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute null parent"() {
        // given
        File file = File.findFirst("name = ?", "Test1.java")
        Namespace other = null

        // when
        fixture = new MoveFileModelTransform(ns, file, other)
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute unknown file"() {
        // given
        File file = File.builder().name("TestX.java").relPath("TestX.java").type(FileType.SOURCE).start(1).end(16).create()
        Namespace other = Namespace.findFirst("nsKey = ?", "TestData:testproj:1.0:test.test3")

        // when
        fixture = new MoveFileModelTransform(ns, file, other)
        fixture.execute()
    }
}