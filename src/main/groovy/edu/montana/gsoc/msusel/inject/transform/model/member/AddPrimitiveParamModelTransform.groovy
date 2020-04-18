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
package edu.montana.gsoc.msusel.inject.transform.model.member

import edu.isu.isuese.datamodel.Member
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Modifier
import edu.isu.isuese.datamodel.Parameter
import edu.isu.isuese.datamodel.TypeRef
import edu.montana.gsoc.msusel.inject.transform.model.MemberModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.source.member.AddMethodParameter

class AddPrimitiveParamModelTransform extends MemberModelTransform {

    String name
    String type
    Modifier[] mods
    Parameter param

    AddPrimitiveParamModelTransform(Member member, String name, String type, Modifier ... mods) {
        super(member)
        this.name = name
        this.type = type
        this.mods = mods
    }

    @Override
    void verifyPreconditions() {
        // 1. member is a method
        if (!(member instanceof Method))
            throw new ModelTransformPreconditionsNotMetException()
        // 2. name is not null or empty
        if (!name)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. type is not null or empty
        if (!type)
            throw new ModelTransformPreconditionsNotMetException()
        // 4. member does not already have a param with the given name
        if (member.params.find { it.name == name })
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        param = Parameter.builder()
                .name(name)
                .type(TypeRef.createPrimitiveTypeRef(type))
                .create()
        mods.each {
            param.addModifier(it)
        }
        ((Method) member).addParameter(param)
        member.updateKey()
        // Generate Source Transform
        new AddMethodParameter(member.getParentFile(), member.getParentType(), (Method) member, param).execute()
    }

    @Override
    void verifyPostconditons() {
        // 1. member has param
        assert(((Method) member).getParams().contains(param))
    }
}
