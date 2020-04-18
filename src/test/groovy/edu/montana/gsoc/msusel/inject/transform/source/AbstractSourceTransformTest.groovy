package edu.montana.gsoc.msusel.inject.transform.source

import edu.isu.isuese.datamodel.Class
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Import
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.transform.BaseSourceTransformSpec
import edu.montana.gsoc.msusel.inject.transform.source.structural.RenameFile
import org.junit.Assert
import org.junit.Test

class AbstractSourceTransformTest extends BaseSourceTransformSpec {

    @Test
    void updateAllFollowing() {
        // given
        File file = File.builder().name("test").start(1).end(10).create()
        Import imp1 = Import.builder().name("import0").start(1).end(2).create()
        Import imp2 = Import.builder().name("import1").start(3).end(3).create()
        Type type = Class.builder().name("test").start(4).end(10).create()
        file.addType(type)
        file.addImport(imp1)
        file.addImport(imp2)

        int line = 2
        int length = 3

        AbstractSourceTransform fixture = new RenameFile(file, "test2")

        // when
        fixture.updateAllFollowing(file, line, length)
        file.refresh()
        imp1.refresh()
        imp2.refresh()
        type.refresh()

        // then
        the(file.getEnd()).shouldBeEqual(10)
        the(imp1.getEnd()).shouldBeEqual(2)
        the(imp2.getStart()).shouldBeEqual(6)
        the(imp2.getStart()).shouldBeEqual(6)
        the(type.getStart()).shouldBeEqual(7)
        the(type.getEnd()).shouldBeEqual(13)
    }

    @Test
    void updateImports() {
        Assert.fail()
    }

    @Test
    void testUpdateImports() {
        Assert.fail()
    }

    @Test
    void updateContainingAndAllFollowing() {
        // given
        File file = File.builder().name("test").start(1).end(10).create()
        Import imp1 = Import.builder().name("import0").start(1).end(2).create()
        Import imp2 = Import.builder().name("import1").start(3).end(3).create()
        Type type = Class.builder().name("test").start(4).end(10).create()
        file.addType(type)
        file.addImport(imp1)
        file.addImport(imp2)

        int line = 2
        int length = 3

        AbstractSourceTransform fixture = new RenameFile(file, "test2")

        // when
        fixture.updateContainingAndAllFollowing(line, length)
        file.refresh()
        imp1.refresh()
        imp2.refresh()
        type.refresh()

        // then
        the(file.getEnd()).shouldBeEqual(13)
        the(imp1.getEnd()).shouldBeEqual(5)
        the(imp2.getStart()).shouldBeEqual(6)
        the(imp2.getStart()).shouldBeEqual(6)
        the(type.getStart()).shouldBeEqual(7)
        the(type.getEnd()).shouldBeEqual(13)
    }
}