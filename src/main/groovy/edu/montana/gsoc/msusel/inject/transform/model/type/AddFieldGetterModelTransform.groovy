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
package edu.montana.gsoc.msusel.inject.transform.model.type

import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.model.TypeModelTransform
import edu.montana.gsoc.msusel.inject.transform.source.member.AddFieldGetter

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddFieldGetterModelTransform extends TypeModelTransform {

    Field field
    Method method

    AddFieldGetterModelTransform(Type type, Field field) {
        super(type)
        this.field = field
    }

    @Override
    void verifyPreconditions() {
        // 1. field is not null
        if (!field)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. type contains field
        if (!type.getFields().contains(field))
            throw new ModelTransformPreconditionsNotMetException()
        // 3. type does not already contain getter
        if (type.getMethods().find { it.getName() == "get${field.name.capitalize()}" } != null)
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        AddFieldGetter trans = new AddFieldGetter(type.getParentFile(), type, field)
        trans.execute()
        method = trans.getMethod()
    }

    @Override
    void verifyPostconditions() {
        // 1. type now contains a method with name getField
        assert (type.getMethods().find { it.getName() == "get${field.name.capitalize()}" } != null)
    }
}
