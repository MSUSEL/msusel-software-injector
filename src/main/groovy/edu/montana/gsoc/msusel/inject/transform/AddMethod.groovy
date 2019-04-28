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

import edu.isu.isuese.datamodel.TypeRef
import edu.isu.isuese.datamodel.Accessibility
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.FileOperations
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.cond.TypeHasMethod
import groovy.transform.builder.Builder

/**
 * Transform which adds a method to a given type
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddMethod extends AddMember {

    /**
     * Type to which a method will be added
     */
    Type type
    /**
     * The method to be added
     */
    Method node
    /**
     * List of possible imports to add
     */
    private List<TypeRef> imports
    /**
     * The parameterizable body content
     */
    private String bodyContent

    /**
     * Constructs a new AddMethod transform
     * @param context the current InjectorContext
     * @param file the file to be modified
     * @param type the type to which a method will be added
     * @param node the method to add
     */
    @Builder(buildMethodName = "create")
    private AddMethod(InjectorContext context, File file, Type type, Method node, String bodyContent) {
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
        StringBuilder builder = new StringBuilder()

        // 1. find line of last method in type
        int line = findMethodInsertionPoint()
        // 2. construct method header
        builder << "    ${accessibility()}${type()} ${name()}(${paramList()})"
        // 3. construct method body
        body(builder, bodyContent)
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

    /**
     * {@inheritDoc}
     */
    @Override
    void initializeConditions() {
        conditions << new TypeHasMethod(type, node)
    }

    /**
     * @return method name
     */
    def name() {
        node.name()
    }

    def type() {
        node.type.name()
    }

    /**
     * @return String representation of the method accessibility
     */
    def accessibility() {
        if (node.accessibility != Accessibility.DEFAULT)
            node.accessibility.toString().toLowerCase()
        else
            ""
    }
}
