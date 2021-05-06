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
package edu.montana.gsoc.msusel.inject.transform.source.member

import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.inject.transform.source.AddMember
import groovy.transform.builder.Builder

/**
 * Transform which adds a method to a given type
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddMethod extends AddMember {

    /**
     * Type to which a method will be added
     */
    Type type
    /**
     * The method to be added
     */
    Method method
    /**
     * List of possible imports to add
     */
    private List<TypeRef> imports
    /**
     * The parameterizable body content
     */
    private String bodyContent

    StringBuilder builder

    /**
     * Constructs a new AddMethod transform
     * @param file the file to be modified
     * @param type the type to which a method will be added
     * @param method the method to add
     */
    @Builder(buildMethodName = "create")
    AddMethod(File file, Type type, Method method, String bodyContent) {
        super(file)
        this.type = type
        this.method = method
        this.bodyContent = bodyContent
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setup() {
        ops = new java.io.File(file.getFullPath())
        lines = ops.readLines()
        builder = new StringBuilder()

        // 1. find line of last method in type
        start = findMethodInsertionPoint(type)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void buildContent() {
        // 2. construct method header
        builder << "\n    ${accessibility()} ${modifiers()}${type()} ${name()}(${paramList()})"
        // 3. construct method body
        body(builder)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void injectContent() {
        // 4. Conduct Injection
        int original = lines.size()
//        if (start >= original) {
////            lines.add(start, "\n")
//            lines.add(start, builder.toString())
//        } else {
        lines.add(start, builder.toString())
//        }
        lines = lines.join("\n").split("\n")
        int current = lines.size()
        end = current - original
        ops.text = lines.join("\n")
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void updateModel() {
        // 5. update all following items with size of insert
        updateContainingAndAllFollowing(start + 1, end)
        // 6. for return type check if primitive, if not check if an import is needed
        if (method.type.getTypeFullName() != "void") {
            if (method.type.getType() == TypeRefType.Type)
                updateImports(method.type)
        }
        // 7. for each parameter check if primitive, if not check if an import is needed
        List<TypeRef> imports = []
        method.params.each {
            if (it.getType().getType() == TypeRefType.Type)
                imports << it.getType()
        }
        updateImports(imports)

        type.addMember(this.method)
        this.method.start = start + 2
        this.method.end = start + end
    }

    /**
     * @return method name
     */
    def name() {
        this.method.getName()
    }

    def type() {
        this.method.type.getTypeName()
    }

    def modifiers() {
        StringBuilder builder = new StringBuilder()
        this.method.getModifiers().each {
            builder << "${it.getName()} "
        }
        builder.toString()
    }

    /**
     * @return String representation of the method accessibility
     */
    def accessibility() {
        if (this.method.accessibility != Accessibility.DEFAULT)
            this.method.accessibility.toString().toLowerCase()
        else
            ""
    }

    /**
     * Constructs the the method body
     * @param builder StringBuilder to which the method contents will be added
     * @param bodyContent Parameterized string containing the body content
     */
    void body(StringBuilder builder) {
        if (method.isAbstract())
            builder << ";\n\n"
        else {
            builder << " {"
            builder << "\n"
            builder << "    ${generateBodyContent()}"
            builder << "\n"
            builder << "    }"
        }
    }

    /**
     * @param bodyContent Parameterized string containing the body content
     * @return String representing the contents of the body of the method
     */
    def generateBodyContent() {
        int count = 1
        String content = bodyContent

        method.getParams().each {
            content = content.replaceAll(/\[param${count}\]/, it.name)
            count += 1
        }

        content
    }

    /**
     * @return String representation of the method parameter list
     */
    def paramList() {
        StringBuilder builder = new StringBuilder()
        boolean first = true
        method.getParams().each {
            if (!first)
                builder << ", "
            builder << it.type.getTypeName()
            builder << " "
            builder << it.name
            if (first)
                first = !first
        }
        builder.toString()
    }
}
