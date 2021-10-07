/*
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
 * Transform to add a Getter method for a Field to a Type
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddFieldGetter extends CompositeSourceTransform {

    Type type
    Field field
    Method method

    /**
     * Constructs a new AddFieldGetter transform
     * @param file File to be modified
     * @param type Type to add the getter to
     * @param field Field for which the getter will be constructed
     */
    @Builder(buildMethodName = "create")
    AddFieldGetter(File file, Type type, Field field) {
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
                .method(method)
                .type(type)
                .bodyContent("    return ${field.getName()};")
                .create()
                .execute()
    }

    void generateMethod() {
        method = Method.builder()
                .name("get${field.getName().capitalize()}")
                .type(field.getType()).compKey("get${field.getName().capitalize()}")
                .accessibility(Accessibility.PUBLIC)
                .create()
        if (field.hasModifier("static"))
            method.addModifier("static")
    }
}
