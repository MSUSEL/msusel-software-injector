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

import edu.montana.gsoc.msusel.codetree.node.member.FieldNode
import edu.montana.gsoc.msusel.codetree.node.member.MethodNode
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.inject.FileOperations
import edu.montana.gsoc.msusel.inject.InjectorContext

/**
 * Base class for field mutator transforms
 * @author Isaac Griffith
 * @version 1.2.0
 */
abstract class FieldMutatorTransform extends AddMember {

    /**
     * The Field for which a mutator will be created
     */
    FieldNode node
    /**
     * Type into which the mutator will be inserted
     */
    TypeNode type
    /**
     * Method representing the mutator
     */
    MethodNode method

    /**
     * Constructs a new FieldMutator transform
     * @param context the current InjectorContext
     * @param file the file to be modified
     * @param type the type into which the mutator will be inserted
     * @param node the method representing the mutator
     */
    FieldMutatorTransform(InjectorContext context, FileNode file, TypeNode type, FieldNode node) {
        super(context, file)
        this.node = node
        this.type = type
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void execute() {
        FileOperations ops = context.controller.getOps(file)
        int line = findMethodInsertionPoint(type)

        StringBuilder builder = new StringBuilder()

        generateContent(builder)

        int length = ops.inject(line, builder.toString())
        method.start = line
        method.end = line + length
        type.children << method

        updateAllFollowing(line, length)
        updateImports(node.type)
    }

    /**
     * Generates the content of the mutator
     * @param builder StringBuilder to which the content will be added
     */
    protected abstract void generateContent(StringBuilder builder)

    /**
     * @return name of the mutator
     */
    def name() {
        node.name().toString()
    }

    /**
     * @return return type of the mutator
     */
    def type() {
        node.type.name()
    }

    /**
     * @return a correctly capitalized name of the method
     */
    def capitalizedName() {
        node.name().toString().capitalize()
    }
}
