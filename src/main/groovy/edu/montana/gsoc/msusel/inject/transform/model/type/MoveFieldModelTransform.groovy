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
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.transform.source.member.MoveField
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.model.TypeModelTransform

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MoveFieldModelTransform extends TypeModelTransform {

    Field field
    Type newParent

    MoveFieldModelTransform(Type type, Field field, Type newParent) {
        super(type)
        this.field = field
        this.newParent = newParent
    }

    @Override
    void verifyPreconditions() {
        // 1. field is not null
        if (!field)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. newParent is not null
        if (!newParent)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. type contains field
        if (!type.getFields().contains(field))
            throw new ModelTransformPreconditionsNotMetException()
        // 4. newParent does not contain a field with same name
        if (newParent.getFields().find { it.getName() == field.getName() })
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        type.removeMember(field)
        field.thaw()
        newParent.addMember(field)
        field.updateKey()
        // Generate Source Transform
        new MoveField(type.getParentFile(), type, newParent.getParentFile(), newParent, field).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. type no longer contains field
        assert(!type.getFields().contains(field))
        // 2. newParent now contains field
        assert(newParent.getFields().contains(field))
    }
}
