package edu.montana.gsoc.msusel.source

import edu.montana.gsoc.msusel.codetree.node.Accessibility
import edu.montana.gsoc.msusel.codetree.node.member.FieldNode
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.type.ClassNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.codetree.typeref.PrimitiveTypeRef

class AddFieldSpec extends BaseTransformSpec {

    def testExecute() {
        deleteDir(new File("testdata"))

        given: "A File containing a Type and a new Field to add to that type"
        FileNode fn = FileNode.builder().key('testdata/Test.java').create()
        TypeNode tn = ClassNode.builder().key('Test').accessibility(Accessibility.PUBLIC).create()

        new AddFile(fn).execute()
        new AddType(fn, tn).execute()
        FieldNode fld = FieldNode.builder().key("Test#field").type(PrimitiveTypeRef.getInstance("int")).create()

        when: "We create a new Transform"
        SourceTransform trans = new AddField(fn, tn, fld)

        then: "We call execute on the transform"
        trans.execute()

        expect: "Something to happen"
    }
}
