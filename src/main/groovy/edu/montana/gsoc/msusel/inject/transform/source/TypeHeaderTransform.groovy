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


import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.inject.transform.source.member.AddMethod

/**
 * Base class for type header modifying transforms
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class TypeHeaderTransform extends CompositeSourceTransform {

    /**
     * Type whose header will be modified
     */
    Type type

    /**
     * Constructs a new TypeHeaderTransform
     * @param file the file to be modified
     * @param type the type whose header is to be modified
     */
    TypeHeaderTransform(File file, Type type) {
        super(file)
        this.type = type
    }

    /**
     * @return the complete type header
     */
    String getTypeHeader() {
        java.io.File ops = new java.io.File(file.getFullPath())
        def text = ops.text
        def matcher = text =~ /${type.getAccessibility().toString()}\s*(\w+\s+)*${type.getName()}(<.+>)?(\s+extends\s+\w+(<.+>)?)?(\s+implements\s+\w+(<.+>)?(\s*,\s+\w+(<.+>)?)*)?\s*\{/
        if (matcher.find()) {
            return matcher[0][0]
        }
        return null
    }

    /**
     * constructs the transforms necessary to implement any abstract methods not already implemented
     * @param other the type from which to gather abstract methods
     */
    void implementAbstractMethods(Type other) {
        if (!type.isAbstract()) {
            if (other instanceof Interface) {
                other.getMethods().each { Method m ->
                    if (!m.modifiers.contains(Modifier.forName("static")) && !m.modifiers.contains(Modifier.forName("final"))) {
                        copyAndAddMethod(m)
                    }
                }
            }
            else {
                other.getMethods().each { Method m ->
                    if (m.isAbstract()) {
                        copyAndAddMethod(m)
                    }
                }
            }
        }
    }

    void copyAndAddMethod(Method m) {
        Method copy = Method.builder().name(m.name).compKey(m.name).type(m.type).accessibility(m.accessibility).create()
        m.getParams().each {
            copy.addParameter(Parameter.builder().name(it.name).type(it.getType()).create())
        }
        m.getModifiers().each {
            if (it.getName() != "abstract")
                copy.addModifier(it)
        }
        AddMethod.builder().file(file).type(type).method(copy).bodyContent("    throw new OperationNotSupportedException();").create().execute()
    }
}
