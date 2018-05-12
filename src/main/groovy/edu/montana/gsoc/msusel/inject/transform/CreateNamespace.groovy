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

import edu.montana.gsoc.msusel.codetree.node.structural.NamespaceNode
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.cond.NamespaceExists
import groovy.transform.builder.Builder

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Transform which constructs new namespace, and any physical artifacts it requires
 * @author Isaac Griffith
 * @version 1.2.0
 */
class CreateNamespace extends CreateStructure {

    /**
     * Namespace to be constructed
     */
    NamespaceNode namespace

    /**
     * Constructs a new CreateNamespace transform
     * @param context current InjectorContext
     * @param namespace the namespace to be created
     */
    @Builder(buildMethodName = "create")
    private CreateNamespace(InjectorContext context, NamespaceNode namespace) {
        super(context, null)
        this.namespace = namespace
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void execute() {
        Path base = Paths.get(namespace.name().replaceAll(".", "/")).parent
        def treeBuilder = new FileTreeBuilder(baseDir: new File(base.toString()))
        treeBuilder.dir(namespace.name().split(/\./).last())
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void initializeConditions() {
        conditions << new NamespaceExists(context, namespace)
    }
}
