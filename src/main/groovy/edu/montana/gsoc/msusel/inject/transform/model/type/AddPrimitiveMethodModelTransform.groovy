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
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Modifier
import edu.isu.isuese.datamodel.Type
import edu.isu.isuese.datamodel.TypeRef
import edu.montana.gsoc.msusel.inject.transform.source.member.AddMethod
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.model.TypeModelTransform

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddPrimitiveMethodModelTransform extends TypeModelTransform {

    String name
    String methodType
    Accessibility access
    Modifier[] mods
    Method method

    AddPrimitiveMethodModelTransform(Type type, String name, String methodType, Accessibility access, Modifier ... mods) {
        super(type)
        this.name = name
        this.methodType = methodType
        this.access = access
        this.mods = mods
    }

    @Override
    void verifyPreconditions() {
        // 1. name is not null or empty
        if (!name)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. methodType is not null
        if (!methodType)
            methodType = "void"
        // 3. access is not null
        if (!access)
            throw new ModelTransformPreconditionsNotMetException()
        // 4. type does not have a method of the same name
        if (type.getAllMethods().find { it.name == name })
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        method = Method.builder()
                .name(name)
                .compKey(name)
                .accessibility(access)
                .type(TypeRef.createPrimitiveTypeRef(methodType))
                .create()
        mods.each {
            method.addModifier(it)
        }
        type.addMember(method)
        method.updateKey()
        // Generate Source Transform
        new AddMethod(type.getParentFile(), type, method, "").execute()
    }

    @Override
    void verifyPostconditons() {
        // 1. type contains method
        assert(type.getAllMethods().contains(method))
    }
}
