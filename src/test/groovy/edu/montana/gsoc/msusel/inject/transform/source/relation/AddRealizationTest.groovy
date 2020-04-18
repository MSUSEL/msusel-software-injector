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
package edu.montana.gsoc.msusel.inject.transform.source.relation

import edu.isu.isuese.datamodel.Class
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Interface
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.transform.BaseSourceTransformSpec
import org.junit.Assert
import org.junit.Test

class AddRealizationTest extends BaseSourceTransformSpec {

    @Test
    void "test execute without other interfaces"() {
        // given
        File file = File.findFirst("name = ?", "Test13.java")
        Type type = Class.findFirst("name = ?", "Test13")
        Type real = Interface.findFirst("name = ?", "Test2")
        AddRealization fixture = new AddRealization(file, type, real)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        //the(actual.text).shouldEqual("""\
        Assert.assertEquals(actual.text, """\
package test.test;

import java.util.*;

public class Test13 implements Test2 {

    private volatile static final int COUNTX;

    public void method(Test3 param) {
        throw new OperationNotSupportedException();
    }

}""")
    }

    @Test
    void "test execute with other interfaces"() {
        // given
        File file = File.findFirst("name = ?", "Test13.java")
        updateFileText(file, "implements TestX")
        Type type = Class.findFirst("name = ?", "Test13")
        Type real = Interface.findFirst("name = ?", "Test2")
        AddRealization fixture = new AddRealization(file, type, real)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        //the(actual.text).shouldEqual("""\
        Assert.assertEquals(actual.text, """\
package test.test;

import java.util.*;

public class Test13 implements TestX, Test2 {

    private volatile static final int COUNTX;

    public void method(Test3 param) {
        throw new OperationNotSupportedException();
    }

}""")
    }

    @Test
    void "test execute without other interfaces but with extends"() {
        // given
        File file = File.findFirst("name = ?", "Test13.java")
        updateFileText(file, "extends TestX")
        Type type = Class.findFirst("name = ?", "Test13")
        Type real = Interface.findFirst("name = ?", "Test2")
        AddRealization fixture = new AddRealization(file, type, real)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        //the(actual.text).shouldEqual("""\
        Assert.assertEquals(actual.text, """\
package test.test;

import java.util.*;

public class Test13 extends TestX implements Test2 {

    private volatile static final int COUNTX;

    public void method(Test3 param) {
        throw new OperationNotSupportedException();
    }

}""")
    }

    private def updateFileText(File f, value) {
        java.io.File file = new java.io.File(f.getFullPath())
        file.text = """\
package test.test;

import java.util.*;

public class Test13 $value {

    private volatile static final int COUNTX;

}
"""
    }
}