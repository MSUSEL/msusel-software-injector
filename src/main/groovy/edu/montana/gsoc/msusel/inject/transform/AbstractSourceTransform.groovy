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
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.structural.ImportNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.inject.FileOperations
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

    abstract void initializeConditions()

    boolean checkConditions() {
        boolean val = true
        conditions.each { val = val && it.check() }
        val
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
        List<String> keys = []
        imports.each { keys << it.type() }
        addImports(keys)
    }

    void updateImports(AbstractTypeRef imp) {
        addImports([imp.type()])
    }

    void updateImports(TypeNode typ) {
        addImports([typ.key])
    }

    void addImports(List<String> imports) {
        FileOperations ops = context.controller.getOps(file)
        List<String> importList = []
        ops.getLines().each {
            if (it.startsWith("import ")) {
                String impOnly = it.split(/import /)[1]
                impOnly = impOnly.substring(0, impOnly.length() - 1)
                importList << impOnly
            }
        }

        List<String> missing = imports.findAll {
            !importList.contains(it) && it != file.namespace.name()
        }

        List<SourceTransform> trans = []
        missing.each {
            ImportNode imp = ImportNode.builder().key(it).create()
            trans << AddImport.builder().context(context).file(file).node(imp).create()
        }
        context.invoker.submitAll(trans)
    }

    def updateContainingAndAllFollowing(int line, int length) {
        file.containing(line).each {
            it.end += length
        }
        updateAllFollowing(line, length)
    }
}
