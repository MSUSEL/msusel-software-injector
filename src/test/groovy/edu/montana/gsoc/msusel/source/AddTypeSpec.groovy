package edu.montana.gsoc.msusel.source

import edu.montana.gsoc.msusel.codetree.DefaultCodeTree
import edu.montana.gsoc.msusel.codetree.node.Accessibility
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.structural.NamespaceNode
import edu.montana.gsoc.msusel.codetree.node.type.ClassNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode

class AddTypeSpec extends BaseTransformSpec {

    def testExecute() {
        deleteDir(new File('testdata'))

        given: 'Empty FileNode and new TypeNode'
        FileNode fn = FileNode.builder().key('testdata/Test.java').create()
        TypeNode tn = ClassNode.builder().key("Test").accessibility(Accessibility.PUBLIC).create()

        new AddFile(fn).execute()

        when: "Create the new AddType transform"
        SourceTransform trans = new AddType(fn, tn)
        trans.tree = new DefaultCodeTree()

        then: "Execute the transform"
        def x = trans.execute()

        expect: "Type start and end are set, and the type is now a member of the file"
        tn.end - tn.start > 0
        fn.types().contains(tn)
    }

    def testExecuteWithFileWithNamespace() {
        deleteDir(new File("testdata"))

        given: 'FileNode with a Namespace and a new TypeNode'
        NamespaceNode ns = NamespaceNode.builder().key("test").create()
        FileNode fn = FileNode.builder().key('testdata/Test.java').namespace(ns).create()
        TypeNode tn = ClassNode.builder().key('Test').accessibility(Accessibility.PUBLIC).create()

        new AddFile(fn).execute()

        when: 'Create the new AddType transform'
        SourceTransform trans = new AddType(fn, tn)
        trans.tree = new DefaultCodeTree()

        then: 'Execute the transform'
        def x = trans.execute()

        expect: "Type start and end are set, and the type is now a member of the file"
        tn.end - tn.start > 0
        fn.types().contains(tn)
    }

    static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list()
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir.delete()
    }
}
