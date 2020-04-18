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
import edu.montana.gsoc.msusel.inject.transform.source.CompositeSourceTransform
import groovy.transform.builder.Builder

/**
 * Transform which adds a Setter method for a field to a given type
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddFieldSetter extends CompositeSourceTransform {

    Field field
    Type type
    Method method

    /**
     * Constructs a new AddFieldSetter transform
     * @param file File to be modified
     * @param type The type to which the setter will be added
     * @param field The field the setter is for
     */
    @Builder(buildMethodName = "create")
    AddFieldSetter(File file, Type type, Field field) {
        super(file)
        this.type = type
        this.field = field
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void execute() {
        generateMethod()
        AddMethod.builder()
                .file(file)
                .method(method).type(type)
                .bodyContent("    this.${field.getName()} = ${field.getName()};")
                .create()
                .execute()
    }

    void generateMethod() {
        method = Method.builder()
                .name("set${field.getName().capitalize()}")
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .compKey("set${field.getName().capitalize()}")
                .accessibility(Accessibility.PUBLIC).create()
        method.addParameter(Parameter.builder()
                .name(field.getName())
                .type(field.getType())
                .create())
        if (field.hasModifier("static"))
            method.addModifier("static")
    }
}
