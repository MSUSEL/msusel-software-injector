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
package edu.montana.gsoc.msusel.inject.cond

import edu.montana.gsoc.msusel.codetree.node.member.MethodNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode

/**
 * A condition which determines whether a type already contains a method with a matching signature
 * @author Isaac Griffith
 * @version 1.2.0
 */
class TypeHasMethod implements Condition {

    /**
     * The type that will contain the method
     */
    TypeNode type
    /**
     * A method node representing the method to be added
     */
    MethodNode method
    /**
     * A string name of the new method to be created
     */
    String name

    /**
     * Constructs a new TypeHasMethod condition for the given type and a method with the provided name
     * @param type Type to which the method will be added
     * @param name String name of the method
     */
    TypeHasMethod(TypeNode type, String name) {
        this.type = type
        this.name = name
    }

    /**
     * Constructs a new TypeHasMethod condition for the given type and Method
     * @param type Type to which the method will be added
     * @param method The method to be added
     */
    TypeHasMethod(TypeNode type, MethodNode method) {
        this.type = type
        this.method = method
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean check() {
        MethodNode mnode = null
        if (method) {
            mnode = type.methods().find { MethodNode m ->
                m.signature() == method.signature()
            }
        } else if (name) {
            mnode = type.methods().find { MethodNode m ->
                m.name() == name
            }
        }

        mnode == null
    }
}
