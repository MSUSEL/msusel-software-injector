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
        start = findStatementInsertionPoint(method)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void buildContent() {
        if (field.hasModifier("static")) {
            text = "        ${fieldOwner.name}.${field.name};"
            delta = 1
        } else if (sameContainingType(fieldOwner, type)) {
            text = "        this.${field.name};"
            delta = 1
        } else {
            if (hasLocalVar(method, fieldOwner)) {
                String var = selectVariable(method, fieldOwner)
                text = "        ${var}.${field.name};"
                delta = 1
            } else if (hasParam(method, fieldOwner)) {
                Parameter p = selectParameter(method, fieldOwner)
                text = "        ${p.name}.${field.name};"
                delta = 1
            } else if (hasField(type, fieldOwner)) {
                Field f = selectField(type, fieldOwner)
                text = "        ${f.name}.${field.name};"
                delta = 1
            } else {
                StringBuilder builder = new StringBuilder()
                builder << "        ${fieldOwner.name} ${fieldOwner.name.toLowerCase()} = new ${fieldOwner.name}();\n"
                builder << "        ${fieldOwner.name.toLowerCase()}.${field.name};"
                text = builder.toString()
                delta = 2
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
        lines.add(start, text)
        ops.text = lines.join("\n")
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void updateModel() {
        updateContainingAndAllFollowing(start, delta)
        addUseDep(type, fieldOwner)
        updateImports(fieldOwner)
    }

    Type findOwningType() {
        if (!field.getParentTypes().isEmpty())
            return field.getParentTypes().first()

        return null
    }
}