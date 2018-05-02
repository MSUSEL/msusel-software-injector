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

import edu.montana.gsoc.msusel.codetree.node.structural.FileNode

class AddInheritance extends AbstractSourceTransform {

    AddInheritance(FileNode file) {
        super(file)
    }

    @Override
    def execute() {
        // 1. find type name check if already extends something, if so throw an exception, else add extends
        line = getLine()
        content = ops.contentAt(line)
        if (content =~ / extends /)
            fail()
        else
            content.replace("${typeName()} ", "${typeName()} extends ${generalizes()} ")

        // 2. check the package to determine if an import is needed
        // 3. if an import is needed, check if it already exists, if not add the type to the import list
        updateImports()
        // 4. check new generalization type for any needed abstract methods (if not abstract), if any are missing add to list of things to do
        // 5. if abstract, and methods not implemented, then add them to all concrete subclasses
        implementAbstractMethods()
    }
}
