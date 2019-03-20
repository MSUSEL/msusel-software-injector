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
import edu.montana.gsoc.msusel.codetree.node.member.MethodNode
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.type.ClassNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.inject.FileOperations

class AddMethodSpec extends BaseTransformSpec {

    def testExecute() {
        deleteDir(new File('testdata'))

        given: 'Empty FileNode and new TypeNode'
        FileOperations ops = new FileOperations()
        CodeTree tree = new DefaultCodeTree()
        FileNode fn = FileNode.builder().key('testdata/Test.java').create()
        TypeNode tn = ClassNode.builder().key("Test").accessibility(Accessibility.PUBLIC).create()

        new CreateFile(ops, fn, tree).execute()
        new CreateType(ops, fn, tree, tn).execute()

        MethodNode cn = MethodNode.builder().key(tn.getKey() + "#" + tn.name()).accessibility(Accessibility.PUBLIC).create()

        when: "Create the new AddMethod transform"
        SourceTransform trans = new AddMethod(ops, fn, tree, tn, cn)

        then: "Execute the transform"
        def x = trans.execute()

        expect: "Type start and end are set, and the type is now a member of the file"
        cn.end - cn.start > 0
        fn.types().contains(tn)
        tn.methods().contains(cn)
        tn.start <= cn.start
        tn.end >= cn.end
    }
}
