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

import com.google.common.graph.MutableGraph
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.InjectionFailedException
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner.class)
class PackageOrgGrimeInjectorTest extends GrimeInjectorBaseTest {

    PackageOrgGrimeInjector fixture

    @Override
    void localSetup() {
        fixture = new PackageOrgGrimeInjector(inst, true, true)
    }

    @Test
    @Parameters([
            "true, false",
            "true, true",
            "false, false",
            "false, true"
    ])
    void inject(boolean internal, boolean closure) {
        // given
        fixture = new PackageOrgGrimeInjector(inst, internal, closure)

        // when
        try {
            fixture.inject()
        } catch (Exception e) {
            Assert.fail()
        }
    }

    @Test
    void "test selectPatternNamespace"() {
        // given
        List<Namespace> namespaces = [ns2, ns4, ns5]

        // when
        Namespace ns = fixture.selectPatternNamespace()

        // then
        the(namespaces).shouldContain(ns)
    }

    @Test
    void "test selectExternalClass with namespace internal to pattern but with external classes"() {
        // given
        Namespace ns = ns2
        List<Type> types = [
                typeF, typeG, typeH
        ]

        // when
        Type type = fixture.selectExternalClass(ns)

        // then
        the(types).shouldContain(type)
    }

    @Test
    void "test selectExternalClass with empty namespace"() {
        // given
        Namespace ns = ns6

        // when
        Type type = fixture.selectExternalClass(ns)

        // then
        the(type).shouldBeNull()
    }

    @Test(expected = InjectionFailedException)
    void "test selectExternalClass with null namespace"() {
        // given
        Namespace ns = null

        // when
        fixture.selectExternalClass(ns)
    }

    @Test
    void "test selectInternalClass with namespace internal to pattern"() {
        // given
        Namespace ns = ns2
        List<Type> types = [
                typeA, typeB, typeC, typeD, typeE
        ]

        // when
        Type type = fixture.selectInternalClass(ns)

        // then
        the(types).shouldContain(type)
    }

    @Test
    void "test selectInternalClass with namespace external to pattern"() {
        // given
        Namespace ns = ns1

        // when
        Type type = fixture.selectInternalClass(ns)

        // then
        the(type).shouldBeNull()
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectInternalClass with null namespace"() {
        // given
        Namespace ns = null

        // when
        fixture.selectExternalClass(ns)
    }

    @Test
    void "test selectReachableNamespace happy path"() {
        // given
        MutableGraph<Namespace> graph = fixture.createGraph(proj)
        Namespace ns = ns2
        List<Namespace> reachable = [
                ns4,
                ns5
        ]

        // when
        Namespace result = fixture.selectReachableNamespace(graph, ns)

        // then
        the(reachable).shouldContain(result)
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectReachableNamespace null graph"() {
        // given
        MutableGraph<Namespace> graph = null
        Namespace ns = ns2

        // when
        fixture.selectReachableNamespace(graph, ns)
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectReachableNamespace null namespace"() {
        // given
        MutableGraph<Namespace> graph = fixture.createGraph(proj)
        Namespace ns = null

        // when
        fixture.selectReachableNamespace(graph, ns)
    }

    @Test
    void "test selectUnreachableNamespace happy path"() {
        // given
        MutableGraph<Namespace> graph = fixture.createGraph(proj)
        Namespace ns = ns2
        List<Namespace> unreachable = [
                ns1,
                ns3
        ]

        // when
        Namespace result = fixture.selectUnreachableNamespace(graph, ns)

        // then
        the(unreachable).shouldContain(result)
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectUnreachableNamespace null graph"() {
        // given
        MutableGraph<Namespace> graph = null
        Namespace ns = ns2

        // when
        fixture.selectUnreachableNamespace(graph, ns)
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectUnreachableNamespace null namespace"() {
        // given
        MutableGraph<Namespace> graph = fixture.createGraph(proj)
        Namespace ns = null

        // when
        fixture.selectUnreachableNamespace(graph, ns)
    }

    @Test
    void "test selectOrCreateExternalClass with empty namespace"() {
        // given
        Namespace ns = ns6

        // when
        Type type = fixture.selectOrCreateExternalClass(ns)

        // then
        the(type).shouldNotBeNull()
        the(type.getName()).shouldEqual("GenExternalType")
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectOrCreateExternalClass with null namespace"() {
        // given
        Namespace ns = null

        // when
        fixture.selectExternalClass(ns)
    }
}