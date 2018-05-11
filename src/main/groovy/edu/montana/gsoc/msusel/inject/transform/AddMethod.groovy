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
import edu.montana.gsoc.msusel.codetree.node.Accessibility
import edu.montana.gsoc.msusel.codetree.node.member.MethodNode
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.inject.FileOperations
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.cond.TypeHasMethod
import groovy.transform.builder.Builder
/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class AddMethod extends AddMember {

    TypeNode type
    MethodNode node
    private List<AbstractTypeRef> imports

    @Builder(buildMethodName = "create")
    private AddMethod(InjectorContext context, FileNode file, TypeNode type, MethodNode node) {
        super(context, file)
        this.type = type
        this.node = node
    }

    @Override
    void execute() {
        FileOperations ops = context.controller.getOps(file)
        StringBuilder builder = new StringBuilder()

        // 1. find line of last method in type
        int line = findMethodInsertionPoint()
        // 2. construct method header
        builder << "    ${accessibility()}${name()}(${paramList()})"
        // 3. construct method body
        body(builder)
        // 4. Conduct Injection
        int length = ops.inject(line, builder.toString())
        type.children << node
        node.start = line
        node.end = line + length
        // 5. update all following items with size of insert
        updateAllFollowing(line, length)
        // 6. for return type check if primitive, if not check if an import is needed
        updateImports(node.type)
        // 7. for each parameter check if primitive, if not check if an import is needed
        updateImports(imports)
    }

    def body(StringBuilder builder) {
        if (node.isAbstract())
            builder << ";\n\n"
        else {
            builder << " {"
            builder << "\n"
            builder << "    ${bodyContent()}"
            builder << "\n"
            builder << "    }\n\n"
        }
    }

    def bodyContent() {
        ""
    }


    def paramList() {
        StringBuilder builder = new StringBuilder()
        node.params.each {
            builder << it.type.name()
            builder << " "
            builder << it.name()
            if (node.params.last() != it)
                builder << ", "
        }
        builder.toString()
    }

    def name() {
        node.name()
    }

    def accessibility() {
        if (node.accessibility != Accessibility.DEFAULT)
            node.accessibility.toString().toLowerCase()
        else
            ""
    }

    @Override
    void initializeConditions() {
        conditions << new TypeHasMethod(type, node)
    }
}
