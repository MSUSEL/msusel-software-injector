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
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.transform.BaseSourceTransformSpec
import org.junit.Test

class AddAssociationTest extends BaseSourceTransformSpec {

    @Test
    void execute() {
        // given
        File fromFile = File.findFirst("name = ?", "Test1.java")
        File toFile = File.findFirst("name = ?", "Test9.java")
        Type fromType = Class.findFirst("name = ?", "Test1")
        Type toType = Class.findFirst("name = ?", "Test9")
        String toName = "test1"
        String fromName = "test9"
        boolean bidirect = true
        AddAssociation fixture = new AddAssociation(fromFile, fromType, toFile, toType, toName, fromName, bidirect)
        java.io.File fromActual = new java.io.File(fromFile.getFullPath())
        java.io.File toActual = new java.io.File(toFile.getFullPath())

        println "From Type Start: ${fromType.start}"
        println "From Type End: ${fromType.end}"
        println "To Type Start: ${toType.start}"
        println "To Type End: ${toType.end}"

        // when
        fixture.execute()

        // then
        the(fromActual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test1 implements Test2 {

    private String name;
    private Test9 test9;

    public void method(Test3 param) {

    }

    public static void main(String args[]) {

    }

    public void setTest9(Test9 test9) {
        this.test9 = test9;
    }

    public Test9 getTest9() {
        return test9;
    }
}""")

        the(toActual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test9 implements Test2 {

    private String name9, other;
    private Test1 test1;

    public void method(Test3 param) {
    
    }

    public void setTest1(Test1 test1) {
        this.test1 = test1;
    }

    public Test1 getTest1() {
        return test1;
    }
}""")
        the(fromType.getAssociatedTo()).shouldContain(toType)
        the(toType.getAssociatedTo()).shouldContain(fromType)
    }
}