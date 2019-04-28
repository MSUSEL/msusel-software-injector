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

import edu.isu.isuese.datamodel.Constructor
import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Enum
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.InjectorContext

/**
 * Base transform class for those transforms which add a member to a type
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class AddMember extends BasicSourceTransform {

    /**
     * Constructs a new AddMember transform
     * @param context current InjectorContext
     * @param file the file which will be modified
     */
    AddMember(InjectorContext context, File file) {
        super(context, file)
    }

    /**
     * Identifies the insertion point for a new method
     * @param type Type to be modified
     * @return line number where a new method can be inserted
     */
    int findMethodInsertionPoint(Type type) {
        int line = 0

        for (Method method : (List<Method>) type.methods()) {
            if (method.getEnd() > line)
                line = method.getEnd()
        }

        return line
    }

    /**
     * Identifies the insertion point for a new field
     * @param type Type to be modified
     * @return line number where a new field can be inserted
     */
    int findFieldInsertionPoint(Type type) {
        int line = 0

        for (Field field : (List<Field>) type.fields()) {
            if (field.getEnd() > line)
                line = field.getEnd()
        }

        int mstart = Integer.MAX_VALUE;
        for (Method method : (List<Method>) type.methods()) {
            if (method.getStart() < mstart)
                mstart = method.getStart()
        }

        if (line <= 0 && mstart == Integer.MAX_VALUE)
            return type.getEnd() - 1
        if (line < mstart && mstart == Integer.MAX_VALUE)
            return line + 1
        if (line < mstart && mstart < Integer.MAX_VALUE)
            return mstart - 1

        return line + 1
    }

    /**
     * Identifies the insertion point for a new enum literal
     * @param Enum The Enum to be modified
     * @return line number where a new enum literal can be inserted
     */
    int findEnumItemInsertionPoint(Enum Enum) {
        return 0
    }

    /**
     * Identifies the insertion point for a new constructor
     * @param type Type to be modified
     * @return line number where a new constructor can be inserted
     */
    int findConstructorInsertionPoint(Type type) {
        int line

        if (!type.methods().isEmpty()) {
            if (!type.constructors().isEmpty()) {
                int lastLine = 0
                type.constructors().each { Constructor c ->
                    if (c.end >= lastLine)
                        lastLine = c.end
                }
                line = lastLine
            }
            else {
                int lastLine = type.end
                type.methods().each { Method m ->
                    if (m.start <= lastLine)
                        lastLine = m.start
                }
                line = lastLine
            }
        } else {
            if (!type.fields().isEmpty()) {
                int lastLine = 0
                type.fields().each { Field fld ->
                    if (fld.end >= lastLine)
                        lastLine = fld.end
                }
                line = lastLine
            }
            else {
                line = type.end
            }
        }

        line
    }

    /**
     * Constructs the the method body
     * @param builder StringBuilder to which the method contents will be added
     * @param bodyContent Parameterized string containing the body content
     */
    void body(StringBuilder builder, String bodyContent) {
        if (node.isAbstract())
            builder << ";\n\n"
        else {
            builder << " {"
            builder << "\n"
            builder << "    ${generateBodyContent(bodyContent)}"
            builder << "\n"
            builder << "    }\n\n"
        }
    }

    /**
     * @param bodyContent Parameterized string containing the body content
     * @return String representing the contents of the body of the method
     */
    def generateBodyContent(String bodyContent) {
        int count = 1
        String content = bodyContent

        node.params.each {
            content = content.replaceAll(/\[param${count}\]/, it.name())
            count += 1
        }

        content
    }

    /**
     * @return String representation of the method parameter list
     */
    def paramList() {
        StringBuilder builder = new StringBuilder()
        node.params.each {
            builder << it.type.name()
            builder << " "
            builder << it.name()
            if (node.params.last() != it)
                builder << ", "
        }
        builder.toString()
    }
}
