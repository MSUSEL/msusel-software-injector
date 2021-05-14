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
import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.transform.BaseSourceTransformSpec
import org.junit.Assert
import org.junit.Test

class AddFieldUseTest extends BaseSourceTransformSpec {

    @Test
    void "test execute with static field"() {
        // given
        File file = File.findFirst("name = ?", "Test16.java")
        Type type = Class.findFirst("name = ?", "Test16")
        Field field = type.getFieldWithName("yz")
        Method method = Method.findFirst("name = ?", "testXY")
        AddFieldUse fixture = new AddFieldUse(file, type, field, method)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        //the(actual.text).shouldEqual("""\
        Assert.assertEquals(actual.text, """\
package test.test;

import java.util.*;

public class Test16 {

    private Test17 xy;
    private static int yz;

    public void testXY() {

        Test16.yz;


    }

    public void testXX() {
        Test17 xx;
    }

    public void testYY(Test18 xx) {

    }

    public void testZZ() {

    }
}""")
    }

    @Test
    void "test execute with field from same type"() {
        // given
        File file = File.findFirst("name = ?", "Test16.java")
        Type type = Class.findFirst("name = ?", "Test16")
        Field field = type.getFieldWithName("xy")
        Method method = type.getMethodWithName("testXY")
        AddFieldUse fixture = new AddFieldUse(file, type, field, method)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        //the(actual.text).shouldEqual("""\
        Assert.assertEquals(actual.text, """\
package test.test;

import java.util.*;

public class Test16 {

    private Test17 xy;
    private static int yz;

    public void testXY() {

        this.xy;


    }

    public void testXX() {
        Test17 xx;
    }

    public void testYY(Test18 xx) {

    }

    public void testZZ() {

    }
}""")
    }

    @Test
    void "test execute with field where method declares local var"() {
        // given
        File file = File.findFirst("name = ?", "Test16.java")
        Type type = Class.findFirst("name = ?", "Test16")
        Field field = Field.findFirst("name = ?", "bb")
        Method method = Method.findFirst("name = ?", "testXX")
        AddFieldUse fixture = new AddFieldUse(file, type, field, method)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        //the(actual.text).shouldEqual("""\
        Assert.assertEquals(actual.text, """\
package test.test;

import java.util.*;

public class Test16 {

    private Test17 xy;
    private static int yz;

    public void testXY() {

    }

    public void testXX() {

        xx.bb;

        Test17 xx;
    }

    public void testYY(Test18 xx) {

    }

    public void testZZ() {

    }
}""")
    }

    @Test
    void "test execute with field where method declares param"() {
        // given
        File file = File.findFirst("name = ?", "Test16.java")
        Type type = Class.findFirst("name = ?", "Test16")
        Field field = Field.findFirst("name = ?", "zz")
        Method method = Method.findFirst("name = ?", "testYY")
        AddFieldUse fixture = new AddFieldUse(file, type, field, method)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        //the(actual.text).shouldEqual("""\
        Assert.assertEquals(actual.text, """\
package test.test;

import java.util.*;

public class Test16 {

    private Test17 xy;
    private static int yz;

    public void testXY() {

    }

    public void testXX() {
        Test17 xx;
    }

    public void testYY(Test18 xx) {

        xx.zz;


    }

    public void testZZ() {

    }
}""")
    }

    @Test
    void "test execute with field where type declares field"() {
        // given
        File file = File.findFirst("name = ?", "Test16.java")
        Type type = Class.findFirst("name = ?", "Test16")
        Field field = Field.findFirst("name = ?", "bb")
        Method method = Method.findFirst("name = ?", "testZZ")
        AddFieldUse fixture = new AddFieldUse(file, type, field, method)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        //the(actual.text).shouldEqual("""\
        Assert.assertEquals(actual.text, """\
package test.test;

import java.util.*;

public class Test16 {

    private Test17 xy;
    private static int yz;

    public void testXY() {

    }

    public void testXX() {
        Test17 xx;
    }

    public void testYY(Test18 xx) {

    }

    public void testZZ() {

        xy.bb;


    }
}""")
    }

    @Test
    void "test execute with field where none of the above"() {
        // given
        File file = File.findFirst("name = ?", "Test16.java")
        Type type = Class.findFirst("name = ?", "Test16")
        Field field = Field.findFirst("name = ?", "zz")
        Method method = Method.findFirst("name = ?", "testZZ")
        AddFieldUse fixture = new AddFieldUse(file, type, field, method)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        //the(actual.text).shouldEqual("""\
        Assert.assertEquals(actual.text, """\
package test.test;

import java.util.*;

public class Test16 {

    private Test17 xy;
    private static int yz;

    public void testXY() {

    }

    public void testXX() {
        Test17 xx;
    }

    public void testYY(Test18 xx) {

    }

    public void testZZ() {

        Test18 test18 = new Test18();
        test18.zz;


    }
}""")
    }
}