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
package edu.montana.gsoc.msusel.inject.transform.source

import edu.isu.isuese.datamodel.Accessibility
import edu.isu.isuese.datamodel.Class
import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.FileType
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Type
import edu.isu.isuese.datamodel.TypeRef
import edu.montana.gsoc.msusel.inject.transform.BaseSourceTransformSpec
import edu.montana.gsoc.msusel.inject.transform.model.file.AddTypeModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.namespace.AddFileModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.type.AddFieldModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.type.AddMethodModelTransform
import edu.montana.gsoc.msusel.inject.transform.source.member.CreateEncapsulatedField
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner.class)
class ComplicatedTransformsTest  extends BaseSourceTransformSpec {

    @Test
    void testCaseOne() {
        // Given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test.test")
        String path = "TestX.java"
        String typeName = "TestX"
        String fieldName = "typeX"
        String methodName = "methodX"

        // When
        AddFileModelTransform addFile = new AddFileModelTransform(ns, path, FileType.SOURCE)
        addFile.execute()
        File file = addFile.getFile()

        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(27)

        AddTypeModelTransform addType = new AddTypeModelTransform(file, typeName, Accessibility.PUBLIC, "class")
        addType.execute()
        Type type = addType.getType()

        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(37)
        the(type.getStart()).shouldBeEqual(35)
        the(type.getEnd()).shouldBeEqual(37)

        Type fieldType = Class.findFirst("name = ?", "Test1")
        AddFieldModelTransform addField = new AddFieldModelTransform(type, fieldName, fieldType, Accessibility.PRIVATE)
        addField.execute()
        Field field = addField.getField()
        file.refresh()
        type.refresh()

        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(39)
        the(type.getStart()).shouldBeEqual(35)
        the(type.getEnd()).shouldBeEqual(39)
        the(field.getStart()).shouldBeEqual(37)
        the(field.getEnd()).shouldBeEqual(37)

        AddMethodModelTransform addMethod = new AddMethodModelTransform(type, methodName, TypeRef.createPrimitiveTypeRef("void"), Accessibility.PUBLIC)
        addMethod.execute()
        Method method = addMethod.getMethod()
        file.refresh()
        type.refresh()

        // Then
        java.io.File actual = new java.io.File(file.getFullPath())
        String text = actual.getText()
        the(text).shouldBeEqual('''\
/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
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

package test.test;

/**
 * Generated Type
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public class TestX {

    private Test1 typeX;


    public void methodX() {
    
    }
}''')
        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(43)
        the(type.getStart()).shouldBeEqual(35)
        the(type.getEnd()).shouldBeEqual(43)
        the(field.getStart()).shouldBeEqual(37)
        the(field.getEnd()).shouldBeEqual(37)
        the(method.getStart()).shouldBeEqual(40)
        the(method.getEnd()).shouldBeEqual(42)
    }

    @Test
    void testCaseTwo() {
        // Given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test.test")
        File file = File.findFirst("name = ?", "Test23.java")
        Type type = Class.findFirst("name = ?", "Test23")
        Method method = Method.findFirst("name = ?", "aMethod23")
        String fieldName = "typeX"
        Type fieldType = Class.findFirst("name = ?", "Test1")

        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(43)
        the(type.getStart()).shouldBeEqual(35)
        the(type.getEnd()).shouldBeEqual(43)
        the(method.getStart()).shouldBeEqual(40)
        the(method.getEnd()).shouldBeEqual(42)

        // When
        AddFieldModelTransform addField = new AddFieldModelTransform(type, fieldName, fieldType, Accessibility.PRIVATE)
        addField.execute()
        Field field = addField.getField()
        file.refresh()
        type.refresh()
        method.refresh()

        // Then
        java.io.File actual = new java.io.File(file.getFullPath())
        String text = actual.getText()
        the(text).shouldBeEqual('''\
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
package test.test;

import java.util.*;

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
public class Test23 {

    private Test1 typeX;

    /**
     * A method that does something
     */
    public void aMethod23() {
    
    }
}''')

        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(45)
        the(type.getStart()).shouldBeEqual(35)
        the(type.getEnd()).shouldBeEqual(45)
        the(field.getStart()).shouldBeEqual(37)
        the(field.getEnd()).shouldBeEqual(37)
        the(method.getStart()).shouldBeEqual(42)
        the(method.getEnd()).shouldBeEqual(44)
    }

    @Test
    void testCaseThree() {
        // Given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test.test")
        File file = File.findFirst("name = ?", "Test23.java")
        Type type = Class.findFirst("name = ?", "Test23")
        Method method = Method.findFirst("name = ?", "aMethod23")
        String fieldName = "test"
        TypeRef fieldType = TypeRef.createPrimitiveTypeRef("int")

        // When
        CreateEncapsulatedField encFld = new CreateEncapsulatedField(file, type, fieldType, fieldName, Accessibility.PRIVATE)
        encFld.execute()

        java.io.File actual = new java.io.File(file.getFullPath())
        String text = actual.getText()

        // Then
        the(file.getEnd()).shouldBeEqual(53)
        the(type.getStart()).shouldBeEqual(35)
        the(type.getEnd()).shouldBeEqual(53)

        the(text).shouldBeEqual('''\
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
package test.test;

import java.util.*;

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
public class Test23 {

    private int test;

    /**
     * A method that does something
     */
    public void aMethod23() {
    
    }

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }
}''')
    }

    @Test
    void testCaseFour() {
        // Given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test.test")
        String path = "TestX.java"
        String typeName = "TestX"
        String fieldName = "test"
        String methodName = "aMethodX"
        TypeRef fieldType = TypeRef.createPrimitiveTypeRef("int")

        // When
        AddFileModelTransform addFile = new AddFileModelTransform(ns, path, FileType.SOURCE)
        addFile.execute()
        File file = addFile.getFile()

        AddTypeModelTransform addType = new AddTypeModelTransform(file, typeName, Accessibility.PUBLIC, "class")
        addType.execute()
        Type type = addType.getType()

        AddMethodModelTransform addMethod = new AddMethodModelTransform(type, methodName, TypeRef.createPrimitiveTypeRef("void"), Accessibility.PUBLIC)
        addMethod.execute()
        Method method = addMethod.getMethod()
        file.refresh()
        type.refresh()

        CreateEncapsulatedField encFld = new CreateEncapsulatedField(file, type, fieldType, fieldName, Accessibility.PRIVATE)
        encFld.execute()
        file.refresh()
        type.refresh()
        method.refresh()

        java.io.File actual = new java.io.File(file.getFullPath())
        String text = actual.getText()

        // Then
        the(text).shouldBeEqual('''\
/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
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

package test.test;

/**
 * Generated Type
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public class TestX {

    private int test;


    public void aMethodX() {
    
    }

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }
}''')
    }

    @Test
    void testCaseFive() {
        // Given
        Namespace ns = Namespace.findFirst("nsKey = ?", "testdata:testproj:1.0:test.test")
        File file = File.findFirst("name = ?", "Test24.java")
        Type type = Class.findFirst("name = ?", "Test24")
        String fieldName = "test"
        TypeRef fieldType = TypeRef.createPrimitiveTypeRef("int")

        // When
        CreateEncapsulatedField encFld = new CreateEncapsulatedField(file, type, fieldType, fieldName, Accessibility.PRIVATE)
        encFld.execute()
        file.refresh()
        type.refresh()

        Field field = type.getFieldWithName("test")
        field.refresh()
        Method method = type.getMethodWithName("aMethod24")
        method.refresh()
        Method getter = type.getMethodWithName("getTest")
        getter.refresh()
        Method setter = type.getMethodWithName("setTest")
        setter.refresh()

        // Then
        the(file.getStart()).shouldBeEqual(1)
        the(file.getEnd()).shouldBeEqual(55)
        the(type.getStart()).shouldBeEqual(35)
        the(type.getEnd()).shouldBeEqual(53)
        the(field.getStart()).shouldBeEqual(37)
        the(field.getEnd()).shouldBeEqual(37)
        the(method.getStart()).shouldBeEqual(42)
//        the(method.getEnd()).shouldBeEqual(44)
//        the(getter.getStart()).shouldBeEqual(46)
//        the(getter.getEnd()).shouldBeEqual(48)
//        the(setter.getStart()).shouldBeEqual(50)
//        the(setter.getEnd()).shouldBeEqual(52)

        java.io.File actual = new java.io.File(file.getFullPath())
        String text = actual.getText()
        the(text).shouldBeEqual('''\
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
package test.test;

import java.util.*;

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
public class Test24 {

    private int test;

    /**
     * A method that does something
     */
    public void aMethod24() {
    
    }

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }
}''')
    }

    @Test
    @Parameters([
            "4,7",
            "6,6",
            "7,5",
            "8,4",
            "10,3",
            "14,2",
            "18,1",
    ])
    void testCaseSix(int line, int expected) {
        File file = File.findFirst("name = ?", "Test16.java")

        the(file.following(line).size()).shouldBeEqual(expected)
    }

    @Test
    @Parameters([
            "1,0", "2,0", "3,0", "4,0", "5,0",
            "6,1", "7,1", "8,1", "9,1", "10,1",
            "11,2", "12,1", "13,1", "14,1", "15,2",
            "16,1", "17,1", "18,1", "19,2", "20,1",
            "21,1", "22,1", "23,2", "24,1", "25,0",
    ])
    void testCaseSeven(int line, int expected) {
        File file = File.findFirst("name = ?", "Test16.java")

        the(file.containing(line).size()).shouldBeEqual(expected)
    }
}
