package edu.montana.gsoc.msusel.inject.transform.source

import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.inject.transform.BaseSourceTransformSpec
import edu.montana.gsoc.msusel.inject.transform.source.type.RenameType
import org.junit.Test

class TypeTransformTest extends BaseSourceTransformSpec {

    @Test
    void "kind for class"() {
        // given
        File file = File.builder().name("test").create()
        Type type = Class.builder().name("test").create()
        file.addType(type)
        TypeTransform fixture = new RenameType(file, type, "newtest")

        // when
        String result = fixture.kind()

        // then
        the(result).shouldBeEqual("class")
    }

    @Test
    void "kind for interface"() {
        // given
        File file = File.builder().name("test").create()
        Type type = Interface.builder().name("test").create()
        file.addType(type)
        TypeTransform fixture = new RenameType(file, type, "newtest")

        // when
        String result = fixture.kind()

        // then
        the(result).shouldBeEqual("interface")
    }

    @Test
    void "kind for enum"() {
        // given
        File file = File.builder().name("test").create()
        Type type = Enum.builder().name("test").create()
        file.addType(type)
        TypeTransform fixture = new RenameType(file, type, "newtest")

        // when
        String result = fixture.kind()

        // then
        the(result).shouldBeEqual("enum")
    }

    @Test
    void "kind for other"() {
        // given
        File file = File.builder().name("test").create()
        Type type = UnknownType.builder().name("test").create()
        TypeTransform fixture = new RenameType(file, type, "newtest")

        // when
        String result = fixture.kind()

        // then
        the(result).shouldBeEqual("")
    }
}