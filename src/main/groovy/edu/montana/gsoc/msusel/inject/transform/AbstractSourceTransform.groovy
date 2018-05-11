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
package edu.montana.gsoc.msusel.inject.transform

import edu.montana.gsoc.msusel.codetree.AbstractTypeRef
import edu.montana.gsoc.msusel.codetree.node.CodeNode
import edu.montana.gsoc.msusel.codetree.node.member.ConstructorNode
import edu.montana.gsoc.msusel.codetree.node.member.FieldNode
import edu.montana.gsoc.msusel.codetree.node.member.MethodNode
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.type.EnumNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.cond.Condition
/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
abstract class AbstractSourceTransform implements SourceTransform {

    FileNode file
    List<Condition> conditions
    InjectorContext context

    AbstractSourceTransform(InjectorContext context, FileNode file) {
        this.file = file
        this.context = context
        initializeConditions()
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

    int findConstructorInsertionPoint(TypeNode type) {
        int line

        if (!type.methods().isEmpty()) {
            if (!type.constructors().isEmpty()) {
                int lastLine = 0
                type.constructors().each { ConstructorNode c ->
                    if (c.end >= lastLine)
                        lastLine = c.end
                }
                line = lastLine
            }
            else {
                int lastLine = type.end
                type.methods().each { MethodNode m ->
                    if (m.start <= lastLine)
                        lastLine = m.start
                }
                line = lastLine
            }
        } else {
            if (!type.fields().isEmpty()) {
                int lastLine = 0
                type.fields().each { FieldNode fld ->
                    if (fld.end >= lastLine)
                        lastLine = fld.end
                }
                line = lastLine
            }
            else {
                line = type.end
            }
        }

        line
    }

    void updateAllFollowing(int line, int length) {
        file.following(line).each { CodeNode c ->
            if (c.start < line) {
                c.setEnd(c.end + length)
            } else if (c.start >= line) {
                c.setStart(c.start + length)
            }
        }
    }

    void updateImports(List<AbstractTypeRef> imports) {

    }

    void updateImports(AbstractTypeRef imp) {

    }

    void updateImports(TypeNode typ) {

    }

    abstract void initializeConditions()

    boolean checkConditions() {
        boolean val = true
        conditions.each { val = val && it.check() }
        val
    }

    int findStatementInsertionPoint(MethodNode methodNode) {
        0
    }

    def updateContainingAndAllFollowing(int line, int length) {

    }
}
