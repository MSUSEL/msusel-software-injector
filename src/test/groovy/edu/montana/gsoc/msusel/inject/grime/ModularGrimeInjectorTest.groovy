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
package edu.montana.gsoc.msusel.inject.grime

import edu.isu.isuese.datamodel.Class
import edu.isu.isuese.datamodel.Type
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner.class)
class ModularGrimeInjectorTest extends GrimeInjectorBaseTest {

    ModularGrimeInjector fixture

    @Override
    void localSetup() {
        fixture = new ModularGrimeInjector(inst, true, true, true)
    }

    @Test
    @Parameters([
            "true, false, false",
            "true, false, true",
            "true, true, false",
            "true, true, true",
            "false, false, false",
            "false, false, true",
            "false, true, false",
            "false, false, true"
    ])
    void inject(boolean persist, boolean extern, boolean effer) {
        // given
        fixture = new ModularGrimeInjector(inst, persist, extern, effer)

        // when
        fixture.inject()

        // then

    }

    @Test
    void selectExternClass() {
        // given
        def types = [Class.findFirst("name = ?", "TypeF"),
                     Class.findFirst("name = ?", "TypeG"),
                     Class.findFirst("name = ?", "TypeH")]

        // when
        def result = fixture.selectExternClass()

        // then
        the(types).shouldContain(result)
    }

    @Test
    void selectPatternClass() {
        // given
        def types = [Class.findFirst("name = ?", "TypeA"),
                     Class.findFirst("name = ?", "TypeB"),
                     Class.findFirst("name = ?", "TypeC"),
                     Class.findFirst("name = ?", "TypeD"),
                     Class.findFirst("name = ?", "TypeE")]

        // when
        def result = fixture.selectPatternClass()

        // then
        the(types).shouldContain(result)
    }

    @Test
    void select2PatternClasses() {
        // given
        def types = [Class.findFirst("name = ?", "TypeA"),
                     Class.findFirst("name = ?", "TypeB"),
                     Class.findFirst("name = ?", "TypeC"),
                     Class.findFirst("name = ?", "TypeD"),
                     Class.findFirst("name = ?", "TypeE")]

        // when
        def (result1, result2) = fixture.select2PatternClasses()

        // then
        the(result1).shouldNotBeEqual(result2)
        the(types).shouldContain(result1)
        the(types).shouldContain(result2)
    }

    @Test
    @Parameters([
            "TypeA, TypeB",
            "TypeD, TypeF",
    ])
    void selectPersistentRel(String src, String dest) {
        // given
        Type srcType = Class.findFirst("name = ?", src)
        Type destType = Class.findFirst("name = ?", dest)

        // when
        RelationType result = fixture.selectPersistentRel(srcType, destType)

        // then
        the([RelationType.GEN, RelationType.ASSOC]).shouldContain(result)
    }

    @Test
    @Parameters([
            "TypeA, TypeB",
            "TypeD, TypeF",
    ])
    void selectTemporaryRel(String src, String dest) {
        // given
        Type srcType = Class.findFirst("name = ?", src)
        Type destType = Class.findFirst("name = ?", dest)

        // when
        RelationType result = fixture.selectTemporaryRel(srcType, destType)

        // then
        the([RelationType.USE_PARAM, RelationType.USE_VAR, RelationType.USE_RET]).shouldContain(result)
    }
}