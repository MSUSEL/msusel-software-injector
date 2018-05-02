package edu.montana.gsoc.msusel.source

import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.structural.NamespaceNode

import java.nio.file.Files
import java.nio.file.Paths

class AddFileSpec extends BaseTransformSpec {

    def testExecute() {
        deleteDir(new File("testdata"))

        given: "A FileNode with only a key for a non-existant file"
        FileNode fn = FileNode.builder().key("testdata/Test.java").create()

        when: "Create a AddFile transform"
        SourceTransform trans = new AddFile(fn)

        then: "Execute the transform"
        trans.execute()

        expect: "File for the filenode was created and is empty"
        Files.exists(Paths.get('testdata/Test.java'))
        Files.size(Paths.get('testdata/Test.java')) > 0
    }

    def testFileWithNamespace() {
        deleteDir(new File("testdata"))

        given: 'A FileNode with a key and a namespace for a non-existant file'
        NamespaceNode ns = NamespaceNode.builder().key("testdata").create()
        FileNode fn = FileNode.builder().key("testdata/Test.java").namespace(ns).create()

        when: 'Create an AddFile transform'
        SourceTransform trans = new AddFile(fn)

        then: 'Execute the transform'
        trans.execute()

        expect: 'File for the filenode was created and is not empty'
        Files.exists(Paths.get('testdata/Test.java'))
        Files.size(Paths.get('testdata/Test.java')) > 0
    }
}
