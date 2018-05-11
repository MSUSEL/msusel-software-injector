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

import edu.montana.gsoc.msusel.codetree.node.member.FieldNode
import edu.montana.gsoc.msusel.codetree.node.member.MethodNode
import edu.montana.gsoc.msusel.codetree.node.member.ParameterNode
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.codetree.typeref.PrimitiveTypeRef
import edu.montana.gsoc.msusel.inject.InjectorContext
import groovy.transform.builder.Builder
/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class AddMethodCall extends AddRelation {

    MethodNode caller
    MethodNode callee

    @Builder(buildMethodName = "create")
    private AddMethodCall(InjectorContext context, FileNode file) {
        super(context, file)
    }

    @Override
    void execute() {
        TypeNode calleeOwner = getMethodOwner(callee)
        TypeNode callerOwner = getMethodOwner(caller)

        context.controller.getOps(file)
        int line = findStatementInsertionPoint(caller)
        String content
        if (callee.hasModifier("static")) {
            content = "        ${calleeOwner.name()}.${callee.name()}(${params(callee)});\n"
        }
        else if (sameContainingType(callerOwner, calleeOwner)) {
            content = "        this.${callee.name()}(${params(callee)});\n"
        }
        else {
            if (hasLocalVar(caller, calleeOwner)) {
                String var = selectVariable(caller, calleeOwner)
                content = "        ${var}.${callee.name()}(${params(callee)});\n"
            } else if (hasParam(caller, calleeOwner)) {
                ParameterNode p = selectParameter(caller, calleeOwner)
                content = "        ${p.name()}.${callee.name()}(${params(callee)});\n"
            } else if (hasField(callerOwner, calleeOwner)) {
                FieldNode f = selectField(callerOwner, calleeOwner)
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

    boolean sameContainingType(TypeNode callerOwner, TypeNode calleeOwner) {
        callerOwner == calleeOwner
    }

    boolean hasField(TypeNode owner, TypeNode calleeOwner) {
        owner.fields().find { it.type.name() == calleeOwner.name() } != null
    }

    boolean hasParam(MethodNode methodNode, TypeNode typeNode) {
        methodNode.params.find { it.type.name() == typeNode.name() }
    }

    def addUseDep(TypeNode src, TypeNode dest) {
        context.tree.addUse(src, dest)
    }

    boolean hasLocalVar(MethodNode methodNode, TypeNode typeNode) {
        false
    }

    String params(MethodNode methodNode) {
        StringBuilder builder = new StringBuilder()

        methodNode.params.each {
            switch(it.type) {
                case PrimitiveTypeRef:
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

    TypeNode getMethodOwner(MethodNode method) {
        context.tree.utils.findType(method.key.split(/#/)[0])
    }

    @Override
    void initializeConditions() {

    }
}
