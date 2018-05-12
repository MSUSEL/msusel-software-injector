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

import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.structural.ImportNode
import edu.montana.gsoc.msusel.inject.FileOperations
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.cond.FileHasImport
import groovy.transform.builder.Builder
/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class AddImport extends BasicSourceTransform {

    ImportNode node

    @Builder(buildMethodName = "create")
    private AddImport(InjectorContext context, FileNode file, ImportNode node) {
        super(context, file)
        this.node = node
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void execute() {
        FileOperations ops = context.controller.getOps(file)

        int line = findImportInsertionPoint()
        String content = "import ${node.key};\n"
        int delta = ops.inject(line, content)

        updateAllFollowing(line, delta)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void initializeConditions() {
        conditions << new FileHasImport(file, node)
    }

    int findImportInsertionPoint() {
        int line = 1

        FileOperations ops = context.controller.getOps(file)
        Map<String, Integer> importMap = [:]
        ops.getLines().each {
            if (it.startsWith("import ")) {
                importMap[it] = line
            }
            line += 1
        }

        return line + 1
    }
}
