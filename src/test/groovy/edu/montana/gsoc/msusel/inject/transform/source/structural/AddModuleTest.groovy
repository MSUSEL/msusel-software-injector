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
package edu.montana.gsoc.msusel.inject.transform.source.structural

import edu.isu.isuese.datamodel.Module
import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.inject.transform.BaseSourceTransformSpec
import org.junit.Test

class AddModuleTest extends BaseSourceTransformSpec {

    @Test
    void execute() {
        // given
        File toCreate = new File("testdata/testproj/testmod3")
        Project parent = Project.findFirst("name = ?", "testproj")
        Module mod = Module.builder().name("testmod3").relPath("testmod3").srcPath("src/main/java").create()
        AddModule fixture = new AddModule(mod, parent)

        // when
        parent.addModule(mod)
        mod.updateKey()
        fixture.execute()

        // then
        the(toCreate.exists()).shouldBeTrue()
    }
}