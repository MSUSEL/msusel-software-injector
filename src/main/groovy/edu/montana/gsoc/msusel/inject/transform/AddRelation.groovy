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
import edu.montana.gsoc.msusel.inject.FileOperations
import edu.montana.gsoc.msusel.inject.InjectorContext
import org.apache.commons.lang3.tuple.Pair

/**
 * Base class for those transforms which construct relationships between two types
 * @author Isaac Griffith
 * @version 1.2.0
 */
abstract class AddRelation extends BasicSourceTransform {

    /**
     * Constructs a new AddRelation transform
     * @param context current InjectorContext
     * @param file the file to be modified
     */
    AddRelation(InjectorContext context, FileNode file) {
        super(context, file)
    }

    /**
     * finds the line into which a statement may be injected within a method
     * @param methodNode Method into which the statement is to be injected
     * @return the line number for the injection
     */
    int findStatementInsertionPoint(MethodNode methodNode) {
        FileOperations ops = context.getController().getOps(file)
        List<String> content = ops.contentRegion(methodNode)

        if (content.last().trim() == "}") {
            if (content[-2].trim().startsWith("return "))
                return content.size() - 2
            else
                return content.size() - 1
        } else {
            return content.size() - 1
        }
    }

    /**
     * Selects a field from the given first type that has the given type
     * @param owner The type to select the field from
     * @param calleeOwner Type
     * @return the selected field with the given type, null if no such field can be found
     */
    FieldNode selectField(TypeNode callerOwner, TypeNode calleeOwner) {
        callerOwner.fields().find { it.type.name() == calleeOwner.name() }
    }

    /**
     * Checks whether the given first type has a field with the given type
     * @param owner The type to check
     * @param calleeOwner Type
     * @return true if owner contains a field with the given type, false otherwise
     */
    boolean hasField(TypeNode owner, TypeNode calleeOwner) {
        owner.fields().find { it.type.name() == calleeOwner.name() } != null
    }

    /**
     * Selects a parameter from the given method that has the given type
     * @param caller Method to select a parameter from
     * @param calleeOwner Type
     * @return the selected parameter or null if no parameter meets the criteria
     */
    ParameterNode selectParameter(MethodNode caller, TypeNode calleeOwner) {
        caller.params.find { it.type.name() == calleeOwner.name() }
    }

    /**
     * Checks whether the given method has a parameter with the given type
     * @param methodNode Method to check
     * @param typeNode Type
     * @return true if the method contains a parameter with the given type, false otherwise
     */
    boolean hasParam(MethodNode methodNode, TypeNode typeNode) {
        methodNode.params.find { it.type.name() == typeNode.name() } != null
    }

    /**
     * Searches the given method for a local variable defined with the given type
     * @param methodNode Method to search
     * @param typeNode Type
     * @return a string representation of the local variable meeting the criteria if one is found, otherwise null
     */
    String selectVariable(MethodNode methodNode, TypeNode typeNode) {
        if (methodNode.end - methodNode.start > 1) {
            FileOperations ops = context.controller.getOps(file)
            List<String> lines = ops.contentRegion(methodNode)
            def pairs = []
            lines.each { line ->
                def group = (line =~ /^\s*(\b[\w_$]+\b)(<.*>)?(\[])?\s(\b[\w_$]+\b)(\[])?/)
                if (group.hasGroup()) {
                    def type = group[0][1]
                    def var = group[0][4]
                    pairs << Pair.of(type, var)
                }
            }

            return pairs.find { Pair<String, String> pair ->
                pair.key == typeNode.name()
            }.value
        }

        null
    }

    /**
     * Checkes whether a local variable with the given type exists in the given method
     * @param methodNode Method to search
     * @param typeNode Type being looked for
     * @return true if such a local variable has been found
     */
    boolean hasLocalVar(MethodNode methodNode, TypeNode typeNode) {
        if (methodNode.end - methodNode.start > 1) {
            FileOperations ops = context.controller.getOps(file)
            List<String> lines = ops.contentRegion(methodNode)
            def pairs = []
            lines.each { line ->
                def group = (line =~ /^\s*(\b[\w_$]+\b)(<.*>)?(\[])?\s(\b[\w_$]+\b)(\[])?/)
                if (group.hasGroup()) {
                    def type = group[0][1]
                    def var = group[0][4]
                    pairs << Pair.of(type, var)
                }
            }

            return (pairs.find { Pair<String, String> pair ->
                pair.key == typeNode.name()
            } != null)
        }

        false
    }

    /**
     * Checks whether the two provided types are the same
     * @param type1 First type
     * @param type2 Second type
     * @return true if the provided types are the same, false otherwise
     */
    boolean sameContainingType(TypeNode type1, TypeNode type2) {
        type1 == type2
    }

    /**
     * Retrieves the owning type of the provided method
     * @param method Method whose type is requested
     * @return The type containing the provided method
     */
    TypeNode getMethodOwner(MethodNode method) {
        context.tree.utils.findType(method.key.split(/#/)[0])
    }

    /**
     * Creates a new use dependency between the two provided types
     * @param src source side of the dependency
     * @param dest destination side of the dependency
     */
    void addUseDep(TypeNode src, TypeNode dest) {
        context.tree.addUse(src, dest)
    }
}
