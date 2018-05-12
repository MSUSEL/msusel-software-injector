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
package edu.montana.gsoc.msusel.inject.cond

import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.inject.FileOperations
import edu.montana.gsoc.msusel.inject.InjectorContext

/**
 * A Base Condition class for TypeHeader modification conditions
 * @author Isaac Griffith
 * @version 1.2.0
 */
abstract class TypeHeaderCondition implements Condition {

    protected final FileNode file
    protected final TypeNode node
    protected final InjectorContext context

    /**
     * Constructs a new TypeHeaderCondition
     * @param context The current InjectorContext
     * @param file The File containing the type
     * @param node The actual type to be modified
     */
    TypeHeaderCondition(InjectorContext context, FileNode file, TypeNode node) {
        this.context = context
        this.file = file
        this.node = node
    }

    /**
     * Method to collect the header for the type, for examination prior to modification
     * @return The entire Type header
     */
    String getTypeHeader() {
        FileOperations ops = context.controller.getOps(file)
        StringBuilder header = new StringBuilder()
        int current = node.start
        while (!header.toString().contains("{")) {
            header << ops.contentAt(current)
            current += 1
            header << "\n"
        }

        header.toString()
    }
}
