/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
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
package edu.montana.gsoc.msusel.inject.transform.source

import com.google.common.collect.Sets
import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.inject.cond.Condition
import edu.montana.gsoc.msusel.inject.transform.source.structural.UpdateImports

/**
 * Base class on which all transforms are built
 * 
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class AbstractSourceTransform implements SourceTransform {

    /**
     * File containing the component on which this transform operates
     */
    File file
    /**
     * List of pre-conditions which must be true prior to this transform executing
     */
    List<Condition> conditions

    /**
     * Constructs a new AbstractSourceTransform
     * @param context The current InjectorContext
     * @param file The filenode to be transformed
     */
    AbstractSourceTransform(File file) {
        this.file = file
    }

    /**
     * Updates all items following the item at the provided line, by shifting their start and end values by length
     * @param line The line after which items need to be shifted
     * @param length The offset to update following items by
     */
    void updateAllFollowing(File file = this.file, int line, int length) {
        file.following(line).each { c ->
            if (c instanceof Component) {
                c.setStart(c.getStart() + length)
                c.setEnd(c.getEnd() + length)
            } else if (c instanceof Import) {
                c.setStart(c.getStart() + length)
                c.setEnd(c.getEnd() + length)
            }
        }
    }

    /**
     * Updates the file's list of imports with those items in the provided list
     * @param imports List of imports to update with
     */
    void updateImports(List<TypeRef> imports) {
        List<String> keys = []
        imports.each {
            if (it.getType() == TypeRefType.Type)
                keys << it.getTypeFullName()
        }
        addImports(keys)
    }

    /**
     * Updates the file's list of imports with the provide type ref
     * @param imp TypeRef
     */
    void updateImports(TypeRef imp) {
        if (!file.getParentProjects().isEmpty()) {
            String name = imp.getTypeFullName()
//            Import imprt = file.getImports().find {it.name == name}
//            if (!imprt)
                addImports([name])
        }
    }

    /**
     * Updates the file's list of imports with the provided Type
     * @param typ Type
     */
    void updateImports(Type typ) {
//        Import imprt = file.getImports().find {it.name == typ.getFullName() }
//        if (!imprt)
            addImports([typ.getFullName()])
    }

    /**
     * Adds to the file's list of imports with the provided string representations of imports to add
     * @param imports List of strings representing imports to add
     */
    void addImports(List<String> imports) {
        java.io.File actual = new java.io.File(file.getFullPath())
        actual.readLines().each {
            if (it.startsWith("import ")) {
                String impOnly = it.split(/ /)[1]
                impOnly = impOnly.substring(0, impOnly.indexOf(";"))
                imports << impOnly
                println "ImpOnly: $impOnly"
            }
        }

        file.imports.each {
            imports.removeAll(it.getName())
        }
        imports.remove(file.getParentNamespace().getFullName())

        imports.each { String name ->
            println "Import name: $name"
            Import imp = Import.findFirst("name = ?", name)
            if (!imp) {
                imp = Import.builder().name(name).create()
            }

            file.addImport(imp)
        }

        UpdateImports.builder().file(file).create().execute()
    }

    /**
     * Updates the node containing the given line by shifting its end value by the provided length. Then it updates all
     * items following the given line by shifting their start and end values by the given length.
     * @param line Line where the insertion occurred
     * @param length Length of the change
     */
    void updateContainingAndAllFollowing(int line, int length) {
        file.containing(line).each {
            if (it instanceof Import)
                it.setEnd(it.getEnd() + length)
            else if (it instanceof Component) {
                it.setEnd(it.getEnd() + length)
            }
        }
        file.setEnd(file.getEnd() + length)
        updateAllFollowing(line, length)
    }
}
