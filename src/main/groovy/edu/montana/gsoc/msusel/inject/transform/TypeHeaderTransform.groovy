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

import edu.montana.gsoc.msusel.codetree.node.Modifiers
import edu.montana.gsoc.msusel.codetree.node.member.MethodNode
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.type.InterfaceNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.inject.InjectorContext

/**
 * Base class for type header modifying transforms
 * @author Isaac Griffith
 * @version 1.2.0
 */
abstract class TypeHeaderTransform extends CompositeSourceTransform {

    /**
     * TypeNode whose header will be modified
     */
    TypeNode type

    /**
     * Constructs a new TypeHeaderTransform
     * @param context the current InjectorContext
     * @param file the file to be modified
     * @param type the type whose header is to be modified
     */
    TypeHeaderTransform(InjectorContext context, FileNode file, TypeNode type) {
        super(context, file)
        this.type = type
    }

    /**
     * @return the complete type header
     */
    String getTypeHeader() {
        StringBuilder header = new StringBuilder()
        int current = type.start
        while (!header.toString().contains("{")) {
            header << ops.contentAt(current)
            current += 1
            header << "\n"
        }

        header.toString()
    }

    /**
     * constructs the transforms necessary to implement any abstract methods not already implemented
     * @param other the type from which to gather abstract methods
     */
    void implementAbstractMethods(TypeNode other) {
        if (!type.isAbstract()) {
            if (other instanceof InterfaceNode) {
                other.methods().each { MethodNode m ->
                    if (!m.modifiers.contains(Modifiers.STATIC) && !m.modifiers.contains(Modifiers.FINAL)) {
                        MethodNode copy = MethodNode.builder().type(m.type).accessibility(m.accessibility).params(m.params).create()
                        transforms << AddMethod.builder().context(context).file(file).type(type).node(copy).create()
                    }
                }
            }
            else {
                other.methods().each { MethodNode m ->
                    if (m.isAbstract()) {
                        MethodNode copy = MethodNode.builder().type(m.type).accessibility(m.accessibility).params(m.params).create()
                        transforms << AddMethod.builder().context(context).file(file).type(type).node(copy).create()
                    }
                }
            }
        }
    }
}
