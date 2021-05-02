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
import edu.montana.gsoc.msusel.inject.transform.model.MemberModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DeleteMethodCallModelTransform extends MemberModelTransform {

    Method method

    DeleteMethodCallModelTransform(Member member, Method method) {
        super(member)
        this.method = method
    }

    @Override
    void verifyPreconditions() {
        // 1. member is a method
        if (!(member instanceof Method))
            throw new ModelTransformPreconditionsNotMetException()
        // 2. method is not null
        if (!method)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. member calls method
        if (!member.getMethodsCalled().contains(method))
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        member.removeCalledMethod(method)
        // Generate Source Transform
        // new DeleteMethodCall(file, member, method).execute() // FIXME
    }

    @Override
    void verifyPostconditions() {
        // 1. member no longer calls method
        assert(!member.getMethodsCalled().contains(method))
        // 2. method no longer called by member
        assert(!method.getMethodsCalling().contains(member))
    }
}
