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

import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.cond.TypeHasMethod
import groovy.transform.builder.Builder

/**
 * Transform to add a Getter method for a Field to a Type
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddFieldGetter extends FieldMutatorTransform {

    /**
     * Constructs a new AddFieldGetter transform
     * @param context Current InjectorContext
     * @param file File to be modified
     * @param type Type to add the getter to
     * @param field Field for which the getter will be constructed
     */
    @Builder(buildMethodName = "create")
    private AddFieldGetter(InjectorContext context, File file, Type type, Field field) {
        super(context, file, type, field)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void generateContent(StringBuilder builder) {
        builder << "    public ${type()} get${capitalizedName()}() {"
        builder << "\n"
        builder << "        return ${name()};"
        builder << "\n"
        builder << "    }"
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void initializeConditions() {
        conditions << new TypeHasMethod(type, "get${capitalizedName}")
    }
}
