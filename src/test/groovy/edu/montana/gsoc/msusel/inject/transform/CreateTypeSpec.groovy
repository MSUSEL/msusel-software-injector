/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
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
package edu.montana.gsoc.msusel.inject.transform

import edu.montana.gsoc.msusel.codetree.CodeTree
import edu.montana.gsoc.msusel.codetree.DefaultCodeTree
import edu.montana.gsoc.msusel.codetree.node.Accessibility
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.structural.NamespaceNode
import edu.montana.gsoc.msusel.codetree.node.type.ClassNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode

class CreateTypeSpec extends BaseTransformSpec {

    def testExecute() {
        deleteDir(new File('testdata'))

        given: 'Empty FileNode and new TypeNode'
        CodeTree tree = new DefaultCodeTree()
        FileNode fn = FileNode.builder().key('testdata/Test.java').create()
        TypeNode tn = ClassNode.builder().key("Test").accessibility(Accessibility.PUBLIC).create()

        new CreateFile(fn, tree).execute()

        when: "Create the new CreateType transform"
        SourceTransform trans = new CreateType(fn, tree, tn)
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

        new CreateFile(fn).execute()

        when: 'Create the new CreateType transform'
        SourceTransform trans = new CreateType(fn, tn)
        trans.tree = new DefaultCodeTree()

        then: 'Execute the transform'
        def x = trans.execute()

        expect: "Type start and end are set, and the type is now a member of the file"
        tn.end - tn.start > 0
        fn.types().contains(tn)
    }
}
