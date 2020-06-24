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
package edu.montana.gsoc.msusel.inject.grime

import edu.isu.isuese.datamodel.Accessibility
import edu.isu.isuese.datamodel.Class
import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.FileType
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Module
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Pattern
import edu.isu.isuese.datamodel.PatternInstance
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.Role
import edu.isu.isuese.datamodel.RoleBinding
import edu.isu.isuese.datamodel.RoleType
import edu.isu.isuese.datamodel.System
import edu.isu.isuese.datamodel.Type
import edu.isu.isuese.datamodel.TypeRef
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before

abstract class GrimeInjectorBaseTest extends DBSpec {

    System sys
    Project proj
    Namespace ns1
    Namespace ns2
    Namespace ns3
    Namespace ns4
    Namespace ns5
    Namespace ns6

    PatternInstance inst

    Type typeA
    Type typeB
    Type typeC
    Type typeD
    Type typeE
    Type typeF
    Type typeG
    Type typeH

    @Before
    void setup() {
        createModelComponents()
        createPatternInstance()
        createDirStructure()
        localSetup()
    }

    abstract void localSetup()

    @After
    void teardown() {
        java.io.File dir = new java.io.File("testdata")
        dir.deleteDir()
    }

    void createModelComponents() {
        sys = System.builder().name("testdata").key("TestData").basePath("testdata").create()
        proj = Project.builder().name("testproj").version("1.0").relPath("testproj").create()
        Module mod = Module.builder().name("testmod").relPath("testmod").srcPath("src/main/java").create()
        ns1 = Namespace.builder().name("test").nsKey("test1").relPath("test").create()
        ns2 = Namespace.builder().name("test").nsKey("test2").relPath("test/test").create()
        ns3 = Namespace.builder().name("test2").nsKey("test3").relPath("test/test2").create()
        ns4 = Namespace.builder().name("test3").nsKey("test4").relPath("test3").create()
        ns5 = Namespace.builder().name("test4").nsKey("test5").relPath("test4").create()
        ns6 = Namespace.builder().name("test5").nsKey("test6").relPath("test/test5").create()
        ns1.addNamespace(ns2)
        ns1.addNamespace(ns3)
        mod.addNamespace(ns1)
        mod.addNamespace(ns4)
        mod.addNamespace(ns5)
        ns1.addNamespace(ns6)
        proj.addModule(mod)
        sys.addProject(proj)

        File fileA = File.builder().name("TypeA.java").relPath("TypeA.java").type(FileType.SOURCE).start(1).end(8).create()
        typeA = Class.builder().name("TypeA").accessibility(Accessibility.PUBLIC).start(3).end(8).create()
        fileA.addType(typeA)
        ns4.addFile(fileA)

        File fileB = File.builder().name("TypeB.java").relPath("TypeB.java").type(FileType.SOURCE).start(1).end(7).create()
        typeB = Class.builder().name("TypeB").accessibility(Accessibility.PUBLIC).start(3).end(7).create()
        typeB.addModifier("ABSTRACT")
        fileB.addType(typeB)
        ns2.addFile(fileB)

        File fileC = File.builder().name("TypeC.java").relPath("TypeC.java").type(FileType.SOURCE).start(1).end(8).create()
        typeC = Class.builder().name("TypeC").accessibility(Accessibility.PUBLIC).start(3).end(8).create()
        fileC.addType(typeC)
        ns5.addFile(fileC)

        File fileD = File.builder().name("TypeD.java").relPath("TypeD.java").type(FileType.SOURCE).start(1).end(7).create()
        typeD = Class.builder().name("TypeD").accessibility(Accessibility.PUBLIC).start(3).end(7).create()
        fileD.addType(typeD)
        ns2.addFile(fileD)

        File fileE = File.builder().name("TypeE.java").relPath("TypeE.java").type(FileType.SOURCE).start(1).end(7).create()
        typeE = Class.builder().name("TypeE").accessibility(Accessibility.PUBLIC).start(3).end(7).create()
        fileE.addType(typeE)
        ns2.addFile(fileE)

        File fileF = File.builder().name("TypeF.java").relPath("TypeF.java").type(FileType.SOURCE).start(1).end(7).create()
        typeF = Class.builder().name("TypeF").accessibility(Accessibility.PUBLIC).start(3).end(7).create()
        fileF.addType(typeF)
        ns1.addFile(fileF)

        File fileG = File.builder().name("TypeG.java").relPath("TypeG.java").type(FileType.SOURCE).start(1).end(7).create()
        typeG = Class.builder().name("TypeG").accessibility(Accessibility.PUBLIC).start(3).end(7).create()
        fileG.addType(typeG)
        ns2.addFile(fileG)

        File fileH = File.builder().name("TypeH.java").relPath("TypeH.java").type(FileType.SOURCE).start(1).end(5).create()
        typeH = Class.builder().name("TypeH").accessibility(Accessibility.PUBLIC).start(3).end(5).create()
        fileH.addType(typeH)
        ns3.addFile(fileH)

        sys.updateKeys()
        typeA.refresh()
        typeB.refresh()
        typeC.refresh()
        typeD.refresh()
        typeE.refresh()
        typeF.refresh()
        typeG.refresh()
        typeH.refresh()
        fileA.refresh()
        fileB.refresh()
        fileC.refresh()
        fileD.refresh()
        fileE.refresh()
        fileF.refresh()
        fileG.refresh()
        fileH.refresh()
        ns1.refresh()
        ns2.refresh()
        ns3.refresh()
        ns4.refresh()
        ns5.refresh()
        ns6.refresh()

        Field fldAB = Field.builder().name("typeab").type(typeB.createTypeRef()).accessibility(Accessibility.PRIVATE).start(5).end(5).create()
        typeA.addMember(fldAB)
        Field fldCB = Field.builder().name("typecb").type(typeB.createTypeRef()).accessibility(Accessibility.PRIVATE).start(5).end(5).create()
        typeC.addMember(fldCB)

        Method mA = Method.builder().name("methodA").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(6).end(7).create()
        typeA.addMember(mA)
        Method mB = Method.builder().name("methodB").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(5).end(6).create()
        typeB.addMember(mB)
        Method mC = Method.builder().name("methodC").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(6).end(7).create()
        typeC.addMember(mC)
        Method mD = Method.builder().name("methodD").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(5).end(6).create()
        typeD.addMember(mD)
        Method mE = Method.builder().name("methodE").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(5).end(6).create()
        typeE.addMember(mE)
        Method mF = Method.builder().name("methodF").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(5).end(6).create()
        typeF.addMember(mF)
        Method mG = Method.builder().name("methodG").type(TypeRef.createPrimitiveTypeRef("void")).accessibility(Accessibility.PUBLIC).start(5).end(6).create()
        typeG.addMember(mG)

        typeA.associatedTo(typeB)
        typeC.associatedTo(typeB)
        typeD.generalizedBy(typeB)
        typeE.generalizedBy(typeB)

        sys.updateKeys()
    }

    void createPatternInstance() {
        Pattern p = Pattern.findFirst("name = ?", "Builder")
        inst = PatternInstance.builder().instKey().create()
        p.addInstance(inst)
        proj.addPatternInstance(inst)

        Role roleA = Role.builder().name("RoleA").type(RoleType.CLASSIFIER).create()
        Role roleB = Role.builder().name("RoleB").type(RoleType.CLASSIFIER).create()
        Role roleC = Role.builder().name("RoleC").type(RoleType.CLASSIFIER).create()
        Role roleD = Role.builder().name("RoleD").type(RoleType.CLASSIFIER).create()
        Role roleE = Role.builder().name("RoleE").type(RoleType.CLASSIFIER).create()
        Role roleAB = Role.builder().name("RoleAB").type(RoleType.RELATION).create()
        Role roleCB = Role.builder().name("RoleCB").type(RoleType.RELATION).create()
        Role roleDB = Role.builder().name("RoleDC").type(RoleType.RELATION).create()
        Role roleEB = Role.builder().name("RoleEB").type(RoleType.RELATION).create()

        Role roleMA = Role.builder().name("RoleMA").type(RoleType.BEHAVE_FEAT).create()

        p.addRole(roleA)
        p.addRole(roleB)
        p.addRole(roleC)
        p.addRole(roleD)
        p.addRole(roleE)
        p.addRole(roleAB)
        p.addRole(roleCB)
        p.addRole(roleDB)
        p.addRole(roleEB)
        p.addRole(roleMA)

        Method methodA = Method.findFirst("name = ?", "methodA")

        RoleBinding rbA = RoleBinding.of(roleA, typeA.createReference())
        RoleBinding rbB = RoleBinding.of(roleB, typeB.createReference())
        RoleBinding rbC = RoleBinding.of(roleC, typeC.createReference())
        RoleBinding rbD = RoleBinding.of(roleD, typeD.createReference())
        RoleBinding rbE = RoleBinding.of(roleE, typeE.createReference())
        RoleBinding rbMA = RoleBinding.of(roleMA, methodA.createReference())
//        RoleBinding rbAB = RoleBinding.of(roleAB, Relation.findBetween(typeA, typeB, edu.isu.isuese.datamodel.RelationType.ASSOCIATION).createReference())
//        RoleBinding rbCB = RoleBinding.of(roleCB, Relation.findBetween(typeB, typeC, edu.isu.isuese.datamodel.RelationType.ASSOCIATION).createReference())
//        RoleBinding rbDB = RoleBinding.of(roleDB, Relation.findBetween(typeD, TypeB, edu.isu.isuese.datamodel.RelationType.GENERALIZATION).createReference())
//        RoleBinding rbEB = RoleBinding.of(roleEB, Relation.findBetween(typeE, TypeB, edu.isu.isuese.datamodel.RelationType.GENERALIZATION).createReference())

        inst.addRoleBinding(rbA)
        inst.addRoleBinding(rbB)
        inst.addRoleBinding(rbC)
        inst.addRoleBinding(rbD)
        inst.addRoleBinding(rbE)
        inst.addRoleBinding(rbMA)
//        inst.addRoleBinding(rbAB)
//        inst.addRoleBinding(rbCB)
//        inst.addRoleBinding(rbDB)
//        inst.addRoleBinding(rbEB)
    }

    void createDirStructure() {
        FileTreeBuilder builder = new FileTreeBuilder(new java.io.File("testdata"))
        builder {
            "testproj" {
                "testmod" {
                    "src" {
                        "main" {
                            "java" {
                                "test" {
                                    "test" {
                                    }
                                    "test2" {
                                    }
                                    "test5" {}
                                }
                                "test3" {}
                                "test4" {}
                            }
                            "resources" {

                            }
                        }
                        "test" {
                            "java" {

                            }
                        }
                    }
                }
//                "build.gradle"(createBuildGradleFile())
//                "settings.gradle"(createSettingsGradleFile())
            }
        }

        new java.io.File("testdata/testproj/testmod/src/main/java/test3/TypeA.java").text = createTestFileA()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/TypeB.java").text = createTestFileB()
        new java.io.File("testdata/testproj/testmod/src/main/java/test4/TypeC.java").text = createTestFileC()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/TypeD.java").text = createTestFileD()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/TypeE.java").text = createTestFileE()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/TypeF.java").text = createTestFileF()
        new java.io.File("testdata/testproj/testmod/src/main/java/test/test/TypeG.java").text = createTestFileG()
        new java.io.File("testdata/testproj/testmod/src/main/java/test3/TypeH.java").text = createTestFileH()
    }

    String createTestFileA() {
        return """\
package test.test;

public class TypeA {
    private TypeB typeab;
    
    public void methodA() {
    }
}
"""
    }

    String createTestFileB() {
        return """\
package test.test;

public abstract class TypeB {

    public void methodB() {
    }
}
"""
    }

    String createTestFileC() {
        return """\
package test.test;

public class TypeC {
    private TypeB typecb;
    
    public void methodC() {
    }
}
"""
    }

    String createTestFileD() {
        return """\
package test.test;

public class TypeD extends TypeB {

    public void methodD() {
    }
}
"""
    }

    String createTestFileE() {
        return """\
package test.test;

public class TypeE extends TypeB {

    public void methodE() {
    }
}
"""
    }

    String createTestFileF() {
        return """\
package test.test;

public class TypeF {

    public void methodF() {
    }
}
"""
    }

    String createTestFileG() {
        return """\
package test.test;

public class TypeG {

    public void methodG() {
    }
}
"""
    }

    String createTestFileH() {
        return """\
package test.test;

public class TypeH {

}
"""
    }
}