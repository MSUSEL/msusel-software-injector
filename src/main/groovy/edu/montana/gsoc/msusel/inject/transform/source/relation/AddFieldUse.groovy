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
package edu.montana.gsoc.msusel.inject.transform.source.relation

import edu.isu.isuese.datamodel.*
import groovy.transform.builder.Builder

/**
 * Transform which adds a field use line to a method.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddFieldUse extends AddRelation {

    /**
     * Type containing the method to which the field use is to be added
     */
    Type type
    /**
     * Field which is to be used
     */
    Field field
    /**
     * Method in which the field use will be inserted
     */
    Method method

    int delta
    Type fieldOwner

    /**
     * Constructs a new AddFieldUse transform
     * @param file File which is to be modified
     * @param type The type containing the method in which the field use is to be added
     * @param field The Field which is to be referenced
     * @param method The method in which the code is to be injected
     */
    @Builder(buildMethodName = "create")
    AddFieldUse(File file, Type type, Field field, Method method) {
        super(file)
        this.type = type
        this.field = field
        this.method = method
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setup() {
        fieldOwner = findOwningType()
        type.refresh()
        file.refresh()
        field.refresh()
        method.refresh()
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void buildContent() {

        if (field.hasModifier("static")) {
            text = "        ${fieldOwner.name}.${field.name} = null;"
        } else if (sameContainingType(fieldOwner, type)) {
            text = "        this.${field.name} = null;"
        } else {
            if (hasLocalVar(method, fieldOwner)) {
                String var = selectVariable(method, fieldOwner)
                text = "        ${var}.${field.name} = null;"
            } else if (hasParam(method, fieldOwner)) {
                Parameter p = selectParameter(method, fieldOwner)
                text = "        ${p.name}.${field.name} = null;"
            } else if (hasField(type, fieldOwner)) {
                Field f = selectField(type, fieldOwner)
                text = "        ${f.name}.${field.name} = null;"
            } else {
                StringBuilder builder = new StringBuilder()
                builder << "        ${fieldOwner.name} ${fieldOwner.name.toLowerCase()} = new ${fieldOwner.name}();\n"
                builder << "        ${fieldOwner.name.toLowerCase()}.${field.name} = null;"
                text = builder.toString()
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void injectContent() {
        java.io.File ops = new java.io.File(file.getFullPath())
        def lines = ops.readLines()

        method.refresh()
        type.refresh()
        file.refresh()
        lines.eachWithIndex { str, ndx ->
            println "$ndx: $str"
        }
        println "Method Name: ${method.name}"
        println "Method Start: ${method.start}"
        println "Method End: ${method.end}"
        List<String> oldContent = lines[(method.getStart() - 1)..(method.getEnd() - 1)]
        String oc = oldContent.join("\n")

        println "Old Content:"
        println oc
        println ""
        def pattern = ~/(?s).*\{(?<content>.*)}/
        def matcher = oc =~ pattern
        if (matcher.matches()) {
            String actualContent = matcher.group("content")
            List<String> oldLines = actualContent.split("\n")
            oldLines.add(0, text)
            oldLines.add(0, "\n")
            String nc = oc.replace(actualContent, oldLines.join("\n"))
            List<String> newContent = nc.split("\n")
            delta = newContent.size() - oldContent.size()
            ops.text = lines.join("\n").replace(oc, nc)
        } else {
            println "Didn't find match"
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void updateModel() {
        updateContainingAndAllFollowing(method.getStart() + 1, delta)

        method.usesField(field)
        addUseDep(type, fieldOwner)

        int oldEnd = method.getEnd()

        method.refresh()
        if (method.getEnd() - oldEnd != delta)
            method.setEnd(method.getEnd() + delta)
        method.refresh()

        updateImports()
    }

    Type findOwningType() {
        if (!field.getParentTypes().isEmpty())
            return field.getParentTypes().first()

        return null
    }
}
