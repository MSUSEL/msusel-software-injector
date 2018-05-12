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
 * Base class on which all transforms are built
 * @author Isaac Griffith
 * @version 1.2.0
 */
abstract class AbstractSourceTransform implements SourceTransform {

    /**
     * File containing the component on which this transform operates
     */
    FileNode file
    /**
     * List of pre-conditions which must be true prior to this transform executing
     */
    List<Condition> conditions
    /**
     * The current injector context
     */
    InjectorContext context

    /**
     * Constructs a new AbstractSourceTransform
     * @param context The current InjectorContext
     * @param file The filenode to be transformed
     */
    AbstractSourceTransform(InjectorContext context, FileNode file) {
        this.file = file
        this.context = context
        initializeConditions()
    }

    /**
     * Method to initialize the list of pre-conditions
     */
    abstract void initializeConditions()

    /**
     * Checks all conditions in the condition list
     * @return true iff all conditions are met or the list is empty, false otherwise
     */
    boolean checkConditions() {
        boolean val = true
        conditions.each { val = val && it.check() }
        val
    }

    /**
     * Updates all items following the item at the provided line, by shifting their start and end values by length
     * @param line The line after which items need to be shifted
     * @param length The offset to update following items by
     */
    void updateAllFollowing(int line, int length) {
        file.following(line).each { CodeNode c ->
            if (c.start < line) {
                c.setEnd(c.end + length)
            } else if (c.start >= line) {
                c.setStart(c.start + length)
            }
        }
    }

    /**
     * Updates the file's list of imports with those items in the provided list
     * @param imports List of imports to update with
     */
    void updateImports(List<AbstractTypeRef> imports) {
        List<String> keys = []
        imports.each { keys << it.type() }
        addImports(keys)
    }

    /**
     * Updates the file's list of imports with the provide type ref
     * @param imp TypeRef
     */
    void updateImports(AbstractTypeRef imp) {
        addImports([imp.type()])
    }

    /**
     * Updates the file's list of imports with the provided Type
     * @param typ Type
     */
    void updateImports(TypeNode typ) {
        addImports([typ.key])
    }

    /**
     * Adds to the file's list of imports with the provided string representations of imports to add
     * @param imports List of strings representing imports to add
     */
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

    /**
     * Updates the node containing the given line by shifting its end value by the provided length. Then it updates all
     * items following the given line by shifting their start and end values by the given length.
     * @param line Line where the insertion occurred
     * @param length Length of the change
     */
    void updateContainingAndAllFollowing(int line, int length) {
        file.containing(line).each {
            it.end += length
        }
        updateAllFollowing(line, length)
    }
}
