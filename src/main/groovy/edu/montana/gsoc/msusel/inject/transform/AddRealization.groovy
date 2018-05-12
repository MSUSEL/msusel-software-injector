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
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.cond.AlreadyRealizes
import groovy.transform.builder.Builder

/**
 * Transform which modifies a type's header information to contain a new interface to realize
 * @author Isaac Griffith
 * @version 1.2.0
 */
class AddRealization extends TypeHeaderTransform {

    /**
     * The inteface to realize
     */
    TypeNode real

    /**
     * Constructs a new AddRealization transform
     * @param context the current InjectorContext
     * @param file the file to be modified
     * @param node the Type whose header is to be modified
     * @param real the new Type to be realized
     */
    @Builder(buildMethodName = "create")
    private AddRealization(InjectorContext context, FileNode file, TypeNode node, TypeNode real) {
        super(context, file, node)
        this.real = real
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void execute() {
        // 1. find realization list for the type to add to
        // 2. parse the realization list and append to end
        String header = getTypeHeader()
        String content
        if (header.contains("implements")) {
            String implList = header.split("implements")[1]
            implList = implList.substring(0, implList.indexOf('{') - 1)
            String newList = implList.replaceAll(/\n/, "")
            newList += ", ${real.name()} "
            content = header.replace(implList, newList)
        } else {
            String allButBrace = header.split(/\{/)[0]
            String replacement = "${allButBrace} implements ${real.name()}"
            content = header.replace(allButBrace, replacement)
        }

        context.tree.addRealizes(type, real)
        // TODO need to define line and start
        int delta = ops.replaceRange(start, line, content)
        updateAllFollowing(type.start, delta)

        // 2. check the package to determine if an import is needed
        // 3. if an import is needed, check if it already exists, if not add the type to the import list
        updateImports(real)
        // 4. check new generalization type for any needed abstract methods (if not abstract), if any are missing add to list of things to do
        // 5. if abstract, and methods not implemented, then add them to all concrete subclasses
        implementAbstractMethods(real)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void initializeConditions() {
        conditions << new AlreadyRealizes(context, file, type, real)
    }
}
