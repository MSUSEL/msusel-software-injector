package edu.montana.gsoc.msusel.inject.transform.model.namespace

import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Namespace
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class SplitNamespaceModelTransformTest extends NamespaceModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        List<File> left
        List<File> right
        (left, right) = splitNamespaceFiles()
        fixture = new SplitNamespaceModelTransform(ns, left, right)

        // when
        fixture.execute()

        // then
        the(ns.getFiles().isEmpty()).shouldBeTrue()
    }

    @Test
    void "test execute happy path module parent"() {
        // given
        List<File> left
        List<File> right
        ns = Namespace.findFirst("nsKey = ?", "testdata:testproj-1.0:testmod:test4")
        (left, right) = splitNamespaceFiles()
        fixture = new SplitNamespaceModelTransform(ns, left, right)

        // when
        fixture.execute()

        // then
        the(ns.getFiles().isEmpty()).shouldBeTrue()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute ns is null"() {
        // given
        Namespace ns = null
        List<File> left = []
        List<File> right = []
        fixture = new SplitNamespaceModelTransform(ns, left, right)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute left is null"() {
        // given
        List<File> left = null
        List<File> right = ns.getFiles()
        fixture = new SplitNamespaceModelTransform(ns, left, right)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute left is empty"() {
        // given
        List<File> left = []
        List<File> right = ns.getFiles()
        fixture = new SplitNamespaceModelTransform(ns, left, right)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute right is null"() {
        // given
        List<File> left = ns.getFiles()
        List<File> right = null
        fixture = new SplitNamespaceModelTransform(ns, left, right)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute right is empty"() {
        // given
        List<File> left = ns.getFiles()
        List<File> right = []
        fixture = new SplitNamespaceModelTransform(ns, left, right)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute no parent"() {
        // given
        List<File> left
        List<File> right
        (left, right) = splitNamespaceFiles()
        ns = Namespace.builder().name("test").nsKey("test7").create()
        fixture = new SplitNamespaceModelTransform(ns, left, right)

        // when
        fixture.execute()
    }

    def splitNamespaceFiles() {
        List<File> left = []
        List<File> right = []

        List<File> files = ns.getFiles()
        left = files[0..(files.size()/2)]
        right = files[(files.size()/2 + 1)..(files.size() - 1)]
        [left,right]
    }
}