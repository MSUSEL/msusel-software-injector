/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
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
package edu.montana.gsoc.msusel.inject.transform

import edu.isu.isuese.datamodel.Modifier
import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Parameter
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.FileOperations
import edu.montana.gsoc.msusel.inject.InjectorContext
import groovy.transform.builder.Builder

/**
 * Transform which adds a field use line to a method.
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

    /**
     * Constructs a new AddFieldUse transform
     * @param context the current InjectorContext
     * @param file File which is to be modified
     * @param type The type containing the method in which the field use is to be added
     * @param field The Field which is to be referenced
     * @param method The method in which the code is to be injected
     */
    @Builder(buildMethodName = "create")
    private AddFieldUse(InjectorContext context, File file, Type type, Field field, Method method) {
        super(context, file)
        this.type = type
        this.field = field
        this.method = method
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void execute() {
        Type fieldOwner = findOwningType(field)
        FileOperations ops = context.controller.getOps(file)
        int line = findStatementInsertionPoint(method)

        if (field.hasModifier(Modifier.STATIC)) {
            content = "        ${fieldOwner.name()}.${field.name()};\n"
        } else if (sameContainingType(fieldOwner, type)) {
            content = "        this.${field.name()};\n"
        } else {
            if (hasLocalVar(method, fieldOwner)) {
                String var = selectVariable(method, fieldOwner)
                content = "        ${var}.${field.name()};\n"
            } else if (hasParam(method, fieldOwner)) {
                Parameter p = selectParameter(method, fieldOwner)
                content = "        ${p.name()}.${field.name()};\n"
            } else if (hasField(type, fieldOwner)) {
                Field f = selectField(type, fieldOwner)
                content = "        ${f.name()}.${field.name()};\n"
            } else {
                StringBuilder builder = new StringBuilder()
                builder << "        ${type.name()} ${fieldOwner.name().toLowerCase()} = new ${fieldOwner.name()}();\n"
                builder << "        ${type.name().toLowerCase()}.${field.name()};\n"
                content = builder.toString()
            }
        }
        int length = ops.inject(line, content)
        updateContainingAndAllFollowing(line, length)
        addUseDep(type, fieldOwner)
        updateImports(fieldOwner)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void initializeConditions() {
    }
}
