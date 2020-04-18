package edu.montana.gsoc.msusel.inject.grime

import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Role
import edu.isu.isuese.datamodel.RoleBinding
import edu.isu.isuese.datamodel.RoleType
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.InjectionFailedException
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner.class)
class ModularOrgGrimeInjectorTest extends GrimeInjectorBaseTest {

    ModularOrgGrimeInjector fixture

    @Override
    void localSetup() {
        fixture = new ModularOrgGrimeInjector(inst, true, true, true)
    }

    @Test
    @Parameters([
            "true, false, false",
            "true, false, true",
            "true, true, false",
            "true, true, true",
            "false, false, false",
            "false, false, true",
            "false, true, false",
            "false, false, true"
    ])
    void inject(boolean persistent, boolean internal, boolean cyclical) {
        // given
        // try {
        fixture = new ModularOrgGrimeInjector(inst, persistent, internal, cyclical)
        // } catch (Exception e) {
        //     Assert.fail()
        // }
    }

    @Test
    void "test hasRelationship happy path true"() {
        // given
        Namespace src = ns4
        Namespace dest = ns2

        // when
        boolean result = ModularOrgGrimeInjector.hasRelationship(src, dest)

        // then
        the(result).shouldBeTrue()
    }

    @Test
    void "test hasRelationship happy path false"() {
        // given
        Namespace src = ns2
        Namespace dest = ns4

        // when
        boolean result = ModularOrgGrimeInjector.hasRelationship(src, dest)

        // then
        the(result).shouldBeFalse()
    }

    @Test(expected = InjectionFailedException.class)
    void "test hasRelationship src ns is null"() {
        // given
        Namespace src = null
        Namespace dest = ns4

        // when
        ModularOrgGrimeInjector.hasRelationship(src, dest)
    }

    @Test(expected = InjectionFailedException.class)
    void "test hasRelationship dest ns is null"() {
        // given
        Namespace src = ns4
        Namespace dest = null

        // when
        ModularOrgGrimeInjector.hasRelationship(src, dest)
    }

    @Test
    void "test selectType happy path"() {
        // given
        Namespace ns = ns2
        def types = [ typeB, typeD, typeE, typeG ]

        // when
        Type type = fixture.selectType(ns)

        // then
        the(types).shouldContain(type)
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectType null namespace"() {
        // given
        Namespace ns = null

        // when
        Type type = fixture.selectType(ns)
    }

    @Test
    void "test selectOrCreateExternNamespace happy path"() {
        // given
        def namespaces = [ns1, ns3]

        // when
        Namespace ns = fixture.selectOrCreateExternNamespace()

        // then
        the(namespaces).shouldContain(ns)
    }

    @Test
    void "test selectOrCreateExternNamespace create extern happy path"() {
        // given
        Role roleF = Role.builder().name("RoleF").type(RoleType.CLASSIFIER).create()
        Role roleH = Role.builder().name("RoleH").type(RoleType.CLASSIFIER).create()
        inst.getParentPattern().addRole(roleF)
        inst.getParentPattern().addRole(roleH)
        RoleBinding rbF = RoleBinding.of(roleF, typeF.createReference())
        RoleBinding rbH = RoleBinding.of(roleH, typeH.createReference())
        inst.addRoleBinding(rbF)
        inst.addRoleBinding(rbH)

        // when
        Namespace ns = fixture.selectOrCreateExternNamespace()

        // then
        the(ns.getName()).shouldBeEqual("genexternns")
    }

    @Test
    void "test splitNamespace happy path no boundary"() {
        // given
        boolean boundary = false

        // when
        Namespace n1, n2
        (n1, n2) = fixture.splitNamespace(ns2, boundary)

        // then
        the(n1.getFiles().size()).shouldBeEqual(2)
        the(n2.getFiles().size()).shouldBeEqual(2)
    }

    @Test
    void "test splitNamespace happy path on boundary"() {
        // given
        boolean boundary = true

        // when
        Namespace n1, n2
        (n1, n2) = fixture.splitNamespace(ns2, boundary)

        // then
        the(n1.getFiles()).shouldContain(File.findFirst("name = ?", "TypeB.java"))
        the(n1.getFiles()).shouldContain(File.findFirst("name = ?", "TypeD.java"))
        the(n1.getFiles()).shouldContain(File.findFirst("name = ?", "TypeE.java"))
        the(n2.getFiles()).shouldContain(File.findFirst("name = ?", "TypeG.java"))
    }

    @Test(expected = InjectionFailedException.class)
    void "test splitNamespace null namespace"() {
        // given
        boolean boundary = true

        // when
        Namespace n1, n2
        fixture.splitNamespace(null, boundary)
    }

    @Test
    void "test selectNamespace happy path"() {
        // given
        def list = [ ns1, ns2, ns3, ns4, ns5, ns6]

        // when
        def selected = fixture.selectNamespace(list)

        // then
        the(selected).shouldNotBeNull()
    }

    @Test
    void "test selectNamespace single item list"() {
        // given
        def list = [ ns1 ]

        // when
        def selected = fixture.selectNamespace(list)

        // then
        the(selected).shouldNotBeNull()
        the(selected).shouldEqual(ns1)
    }

    @Test
    void "test selectNamespace empty list"() {
        // given
        def list = []

        // when
        def selected = fixture.selectNamespace(list)

        // then
        the(selected).shouldBeNull()
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectNamespace null list"() {
        // given
        def list = null

        // when
        fixture.selectNamespaces(list)
    }

    @Test
    void "test selectNamespaces happy path"() {
        // given
        def list = [ ns1, ns2, ns3, ns4, ns5, ns6]

        // when
        def selected = fixture.selectNamespaces(list)

        // then
        the(selected.size()).shouldBeEqual(2)
    }

    @Test
    void "test selectNamespaces empty list"() {
        // given
        def list = []

        // when
        def selected = fixture.selectNamespaces(list)

        // then
        the(selected.size()).shouldBeEqual(0)
    }

    @Test(expected = InjectionFailedException.class)
    void "test selectNamespaces null list"() {
        // given
        def list = null

        // when
        fixture.selectNamespaces(list)
    }

    @Test
    void "test findPatternNamespaces happy path"() {
        // given
        List<Namespace> namespaces = [ns2, ns4, ns5]

        // when
        def result = fixture.findPatternNamespaces()

        // then
        result.each {
            the(namespaces).shouldContain(it)
        }
    }
}