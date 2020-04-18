package edu.montana.gsoc.msusel.inject.transform.source.member

import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.inject.transform.BaseSourceTransformSpec
import org.junit.Test

class ChangeMemberTypeTest extends BaseSourceTransformSpec {

    @Test
    void "test execute on field"() {
        // given
        File file = File.findFirst("name = ?", "Test10.java")
        TypedMember member = Field.findFirst("name = ?", "COUNT")
        Type type = Class.findFirst("name = ?", "TypeZ")
        TypeRef typeRef = type.createTypeRef()
        ChangeMemberType fixture = new ChangeMemberType(file, member, typeRef)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test10 implements Test2 {

    private volatile static final TypeZ COUNT;

    public static void method4(Test3 param) {
    
    }
}""")
    }

    @Test
    void "test execute on method"() {
        // given
        File file = File.findFirst("name = ?", "Test10.java")
        TypedMember member = Method.findFirst("name = ?", "method4")
        Type type = Class.findFirst("name = ?", "TypeZ")
        TypeRef typeRef = type.createTypeRef()
        ChangeMemberType fixture = new ChangeMemberType(file, member, typeRef)
        java.io.File actual = new java.io.File(file.getFullPath())

        // when
        fixture.execute()

        // then
        the(actual.text).shouldEqual("""\
package test.test;

import java.util.*;

public class Test10 implements Test2 {

    private volatile static final int COUNT;

    public static TypeZ method4(Test3 param) {
    
    }
}""")
    }
}
