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

import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.inject.transform.BaseSourceTransformSpec
import org.junit.Test

class AddMethodParameterTest extends BaseSourceTransformSpec {

    @Test
    void execute() {
        // given
        File file = File.findFirst("name = ?", "Test10.java")
        Type type = Type.findFirst("name = ?", "Test10")
        Method method = Method.findFirst("name = ?", "method4")
        Parameter param = Parameter.builder().name("paramX").type(TypeRef.createPrimitiveTypeRef("int")).create()
        method.addParameter(param)
        AddMethodParameter fixture = new AddMethodParameter(file, type, method, param)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test10 implements Test2 {

    private volatile static final int COUNT;

    public static void method4(Test3 param, int paramX) {
    
    }
}""")
    }
}