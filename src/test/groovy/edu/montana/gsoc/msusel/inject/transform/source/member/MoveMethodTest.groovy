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
package edu.montana.gsoc.msusel.inject.transform.source.member


import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.transform.BaseSourceTransformSpec
import org.junit.Assert
import org.junit.Test

class MoveMethodTest extends BaseSourceTransformSpec {

    @Test
    void execute() {
        // given
        File fromFile = File.findFirst("name = ?","Test10.java")
        File toFile = File.findFirst("name = ?", "Test11.java")
        Type fromType = Type.findFirst("name = ?", "Test10")
        Type toType = Type.findFirst("name = ?", "Test11")
        Method toMove = Method.findFirst("name = ?", "method4")
        MoveMethod fixture = new MoveMethod(fromFile, fromType, toFile, toType, toMove)
        java.io.File fromActual = new java.io.File(fromFile.getFullPath())
        java.io.File toActual = new java.io.File(toFile.getFullPath())

        // when
        fixture.execute()

        // then
        Assert.assertEquals(fromActual.text, '''\
package test.test;

import java.util.*;

public class Test10 implements Test2 {

    private volatile static final int COUNT;

}''')

        the(toActual.text).shouldEqual('''\
package test.test;

import java.util.*;

public class Test11 implements Test2 {

    public void paramsTest(int param1, int param2, int param3) {
    
    }

    public static void method4(Test3 param) {
    
    }
}''')
    }
}