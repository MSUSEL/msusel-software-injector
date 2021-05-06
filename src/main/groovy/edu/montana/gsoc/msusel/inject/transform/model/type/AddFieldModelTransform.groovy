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

import edu.isu.isuese.datamodel.Accessibility
import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.Modifier
import edu.isu.isuese.datamodel.Reference
import edu.isu.isuese.datamodel.Type
import edu.isu.isuese.datamodel.TypeRef
import edu.isu.isuese.datamodel.TypeRefType
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.model.TypeModelTransform
import edu.montana.gsoc.msusel.inject.transform.source.member.AddField

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddFieldModelTransform extends TypeModelTransform {

    String name
    Type fldType
    Accessibility access
    def mods
    Field field

    AddFieldModelTransform(Type type, String name, Type fldType, Accessibility access, Modifier... mods) {
        super(type)
        this.name = name
        this.fldType = fldType
        this.access = access
        this.mods = mods
    }

    @Override
    void verifyPreconditions() {
        // 1. name is not null or empty
        if (!name)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. fldType is not null
        if (!fldType)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. access is not null
        if (!access)
            throw new ModelTransformPreconditionsNotMetException()
        // 4. type does not have a field of the same name
        if (type.getFields().find { it.name == name })
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        field = Field.builder()
                .name(name)
                .compKey(name)
                .accessibility(access)
                .type(TypeRef.builder()
                        .typeName(fldType.getName())
                        .type(TypeRefType.Type)
                        .ref(Reference.to(fldType))
                        .create())
                .create()
        mods.each {
            field.addModifier(it)
        }
//        type.addMember(field)
//        field.updateKey()
        type.refresh()
        field.refresh()
        // Generate Source Transform
        new AddField(type.getParentFile(), type, field).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. type contains field
        assert(type.getFields().contains(field))
    }
}
