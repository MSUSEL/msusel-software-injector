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
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.InjectionFailedException
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner.class)
class ClassGrimeInjectorTest extends GrimeInjectorBaseTest {

    ClassGrimeInjector fixture

    @Override
    void localSetup() {
        fixture = new ClassGrimeInjector(inst, true, true, true)
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
    void inject(boolean direct, boolean internal, boolean pair) {
        // given
        fixture = new ClassGrimeInjector(inst, direct, internal, pair)

        // when
        try {
            fixture.inject()
        } catch (Exception e) {
            e.printStackTrace()
            Assert.fail()
        }
    }

    @Test
    void "test selectOrCreateMethod happy path for existing methods"() {
        // given
        Type type = Class.findFirst("name = ?", "TypeA")

        // when
        Method method = fixture.selectOrCreateMethod(type, [])

        // then
        the(method).shouldNotBeNull()
        the(method.getName()).shouldEqual("methodA")
    }

    @Test
    void "test selectOrCreateMethod happy path with no existing methods"() {
        // given
        Type type = Class.findFirst("name = ?", "TypeA")
        Method known = Method.findFirst("name = ?", "methodA")

        // when
        Method method = fixture.selectOrCreateMethod(type, [known])

        // then
        the(method).shouldNotBeNull()
        the(method.getName()).shouldEqual("testMethod")
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectOrCreateMethod with null type"() {
        // given
        Type type = null

        // when
        fixture.selectOrCreateMethod(type, [])
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectOrCreateMethod with null list"() {
        // given
        Type type = Class.findFirst("name = ?", "TypeA")


        // when
        fixture.selectOrCreateMethod(type, null)
    }

    @Test
    void "test selectPatternMethod happy path"() {
        // given
        Type type = Class.findFirst("name = ?", "TypeA")

        // when
        Method method = fixture.selectOrCreatePatternMethod(type)

        // then
        the(method).shouldNotBeNull()
        the(method.getName()).shouldEqual("methodA")
    }

    @Test
    void "test selectPatternMethod happy path with no existing methods"() {
        // given
        Type type = Class.findFirst("name = ?", "TypeB")

        // when
        Method method = fixture.selectOrCreatePatternMethod(type)

        // then
        the(method).shouldNotBeNull()
        the(method.getName()).shouldEqual("roleBound")
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectPatternMethod with null type"() {
        // given
        Type type = null

        // when
        fixture.selectOrCreatePatternMethod(type)
    }
}