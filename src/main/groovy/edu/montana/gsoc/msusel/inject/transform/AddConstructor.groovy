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

import edu.montana.gsoc.msusel.codetree.AbstractTypeRef
import edu.montana.gsoc.msusel.codetree.node.Accessibility
import edu.montana.gsoc.msusel.codetree.node.member.ConstructorNode
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.inject.FileOperations
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.cond.TypeHasConstructor
import groovy.transform.builder.Builder
/**
 * Transform to inject a constructor into a given Type
 * @author Isaac Griffith
 * @version 1.2.0
 */
class AddConstructor extends AddMember {

    /**
     * Type which will contain the new constructor
     */
    TypeNode type
    /**
     * Node representing the new constructor
     */
    ConstructorNode node
    /**
     * List of potential imports to be added to the file
     */
    private List<AbstractTypeRef> imports
    /**
     * The parameterizable body content
     */
    private String bodyContent

    /**
     * Constructs a new AddConstructor transform
     * @param context Current InjectorContext
     * @param file File which is to be modified
     * @param type Type in which the constructor is to be injected
     * @param node The constructor
     */
    @Builder(buildMethodName = "create")
    private AddConstructor(InjectorContext context, FileNode file, TypeNode type, ConstructorNode node, String bodyContent) {
        super(context, file)
        this.type = type
        this.node = node
        this.bodyContent = bodyContent
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void execute() {
        FileOperations ops = context.controller.getOps(file)
        int line = findConstructorInsertionPoint()

        StringBuilder builder = new StringBuilder()

        builder << "    ${accessibility()}${name()}(${paramList()})"
        body(builder, bodyContent)

        int length = ops.inject(line, builder.toString())
        type.children << node
        node.start = line
        node.end = line + length

        updateAllFollowing(line, length)
        updateImports(imports)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void initializeConditions() {
        conditions << new TypeHasConstructor(type, node)
    }

    /**
     * @return The name of the constructor
     */
    def name() {
        node.name()
    }

    /**
     * @return String representation of the constructor's accessibility
     */
    def accessibility() {
        if (node.accessibility != Accessibility.DEFAULT)
            node.accessibility.toString().toLowerCase()
        else
            ""
    }
}
