/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2018 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
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
package edu.montana.gsoc.msusel.source

import edu.montana.gsoc.msusel.codetree.CodeTree
import edu.montana.gsoc.msusel.codetree.node.member.FieldNode
import edu.montana.gsoc.msusel.codetree.node.member.MethodNode
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.type.EnumNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode

abstract class AbstractSourceTransform implements SourceTransform {

    FileNode file
    CodeTree tree
    FileOperations ops

    AbstractSourceTransform(FileNode file) {
        this.file = file
    }

    def execute(FileOperations ops) {

    }

    int findInnerTypeInsertionPoint(TypeNode type) {
        return type.getEnd() - 1
    }

    int findTypeInsertionPoint() {
        int line = 0

        for (TypeNode type : (List<TypeNode>) file.types()) {
            if (type.getEnd() > line)
                line = type.getEnd()
        }

        return line
    }

    int findMethodInsertionPoint(TypeNode type) {
        int line = 0

        for (MethodNode method : (List<MethodNode>) type.methods()) {
            if (method.getEnd() > line)
                line = method.getEnd()
        }

        return line
    }

    int findFieldInsertionPoint(TypeNode type) {
        int line = 0

        for (FieldNode field : (List<FieldNode>) type.fields()) {
            if (field.getEnd() > line)
                line = field.getEnd()
        }

        int mstart = Integer.MAX_VALUE;
        for (MethodNode method : (List<MethodNode>) type.methods()) {
            if (method.getStart() < mstart)
                mstart = method.getStart()
        }

        if (line <= 0 && mstart == Integer.MAX_VALUE)
            return type.getEnd() - 1
        if (line < mstart && mstart == Integer.MAX_VALUE)
            return line + 1
        if (line < mstart && mstart < Integer.MAX_VALUE)
            return mstart - 1

        return line + 1
    }

    int findEnumItemInsertionPoint(EnumNode enumNode) {
        return 0
    }
}
