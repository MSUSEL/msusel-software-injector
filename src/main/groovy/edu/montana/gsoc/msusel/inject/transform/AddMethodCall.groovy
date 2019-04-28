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
import edu.isu.isuese.datamodel.TypeRef
import edu.montana.gsoc.msusel.inject.InjectorContext
import groovy.transform.builder.Builder

/**
 * Transform that injects a method call into an existing method's body
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddMethodCall extends AddRelation {

    Method caller
    Method callee

    /**
     * Constructs a new AddMethodCall transform
     * @param context the current InjectorContext
     * @param file the file to be modified
     * @param caller the method making the call
     * @param callee the method being called
     */
    @Builder(buildMethodName = "create")
    private AddMethodCall(InjectorContext context, File file, Method caller, Method callee) {
        super(context, file)
        this.caller = caller
        this.callee = callee
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void execute() {
        Type calleeOwner = getMethodOwner(callee)
        Type callerOwner = getMethodOwner(caller)

        context.controller.getOps(file)
        int line = findStatementInsertionPoint(caller)
        String content
        if (callee.hasModifier(Modifier.STATIC)) {
            content = "        ${calleeOwner.name()}.${callee.name()}(${params(callee)});\n"
        } else if (sameContainingType(callerOwner, calleeOwner)) {
            content = "        this.${callee.name()}(${params(callee)});\n"
        } else {
            if (hasLocalVar(caller, calleeOwner)) {
                String var = selectVariable(caller, calleeOwner)
                content = "        ${var}.${callee.name()}(${params(callee)});\n"
            } else if (hasParam(caller, calleeOwner)) {
                Parameter p = selectParameter(caller, calleeOwner)
                content = "        ${p.name()}.${callee.name()}(${params(callee)});\n"
            } else if (hasField(callerOwner, calleeOwner)) {
                Field f = selectField(callerOwner, calleeOwner)
                content = "        ${f.name()}.${callee.name()}(${params(callee)});\n"
            } else {
                StringBuilder builder = new StringBuilder()
                builder << "        ${calleeOwner.name()} ${calleeOwner.name().toLowerCase()} = new ${calleeOwner.name()}();\n"
                builder << "        ${calleeOwner.name().toLowerCase()}.${callee.name()}(${params(callee)});\n"
                content = builder.toString()
            }
        }
        int length = context.getController().getOps(file).inject(line, content)
        updateContainingAndAllFollowing(line, length)
        addUseDep(callerOwner, calleeOwner)
        updateImports(calleeOwner)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void initializeConditions() {

    }

    /**
     * Generates the string for the actual parameters of the method call
     * @param methodNode Method to be called
     * @return A string of the actual parameters to the method call, uses null for object, 0 or 0.0 for numbers, '' for char, and false for boolean
     */
    String params(Method methodNode) {
        StringBuilder builder = new StringBuilder()

        methodNode.params.each {
            switch (it.type) {
                case TypeRef:
                    switch (it.type.name()) {
                        case "int":
                        case "byte":
                        case "short":
                        case "long":
                            builder << "0"
                            break
                        case "float":
                        case "double":
                            builder << "0.0"
                            break
                        case "char":
                            builder << "''"
                            break
                        case 'boolean':
                            builder << "true"
                            break
                    }
                    break
                default:
                    builder << "null"
            }
            if (it != methodNode.params.last()) {
                builder << ", "
            }
        }

        builder.toString()
    }
}
