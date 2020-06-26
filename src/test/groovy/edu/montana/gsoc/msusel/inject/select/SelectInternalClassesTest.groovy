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
package edu.montana.gsoc.msusel.inject.select

import edu.isu.isuese.datamodel.Class
import edu.isu.isuese.datamodel.Component
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Module
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.PatternInstance
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.Reference
import edu.isu.isuese.datamodel.Role
import edu.isu.isuese.datamodel.RoleBinding
import edu.isu.isuese.datamodel.RoleType
import edu.isu.isuese.datamodel.Type
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class SelectInternalClassesTest extends DBSpec {

    SelectInternalClasses fixture
    PatternInstance parent
    Role binding
    Type type1
    Type type2

    @Before
    void setUp() throws Exception {
        Project proj = Project.builder().name("Test").projKey("Test").relPath("Test").create()
        Namespace ns = Namespace.builder().name("Test").nsKey("Test").create()
        File file = File.builder().name("Test.java").fileKey("Test.java").create()
        proj.addNamespace(ns)
        proj.addFile(file)
        ns.addFile(file)

        parent = PatternInstance.builder().instKey("Test").create()
        fixture = new SelectInternalClasses(1)

        type1 = Class.builder().name("Test1").compKey("Test1").create()
        type2 = Class.builder().name("Test2").compKey("Test2").create()
        binding = Role.builder().roleKey("Test").name("Test").type(RoleType.CLASSIFIER).create()
        file.addType(type1)
        ns.addType(type1)
        file.addType(type2)
        ns.addType(type2)
        proj.addPatternInstance(parent)
    }

    @After
    void tearDown() throws Exception {
    }

    @Test
    void "test select when parent contains classes"() {
        // given
        parent.addRoleBinding(RoleBinding.of(binding, Reference.to(type1)))
        parent.addRoleBinding(RoleBinding.of(binding, Reference.to(type2)))

        // when
        List<Component> types = fixture.select(parent, binding)

        // then
        the(types.contains(type1)).shouldBeTrue()
    }

    @Test
    void "test select when parent contains classes and num is large"() {
        // given
        fixture = new SelectInternalClasses(4)
        parent.addRoleBinding(RoleBinding.of(binding, Reference.to(type1)))
        parent.addRoleBinding(RoleBinding.of(binding, Reference.to(type2)))

        // when
        List<Component> types = fixture.select(parent, binding)

        // then
        the(types.contains(type1)).shouldBeTrue()
        the(types.contains(type2)).shouldBeTrue()
    }

    @Test
    void "test select when parent does not contain role"() {
        // given
        Role other = Role.builder().name("Test2").type(RoleType.CLASSIFIER).create()

        // when
        List<Component> types = fixture.select(parent, other)

        // then
        the(types.isEmpty()).shouldBeTrue()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test select when parent is null"() {
        // given
        parent = null

        // when
        fixture.select(parent, binding)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test select when binding is null"() {
        // given
        binding = null

        // when
        fixture.select(parent, binding)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test select when num is less than 0"() {
        // given
        fixture = new SelectInternalClasses(-1)

        // when
        fixture.select(parent, binding)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test select when num is 0"() {
        // given
        fixture = new SelectInternalClasses(0)

        // when
        fixture.select(parent, binding)
    }
}