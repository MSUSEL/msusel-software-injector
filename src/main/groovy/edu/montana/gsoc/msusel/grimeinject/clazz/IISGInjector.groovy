/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2018 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
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
package edu.montana.gsoc.msusel.grimeinject.clazz

import edu.montana.gsoc.msusel.codetree.node.CodeNode
import edu.montana.gsoc.msusel.codetree.node.member.FieldNode
import edu.montana.gsoc.msusel.codetree.node.member.MethodNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.grimeinject.ClassGrimeInjector
import edu.montana.gsoc.msusel.grimeinject.ClassGrimeTypes
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.transform.SourceTransform
/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class IISGInjector extends ClassGrimeInjector {

    /**
     *
     */
    IISGInjector() {
        super(ClassGrimeTypes.IISG, null, null, null)
    }

    @Override
    List<SourceTransform> createTransforms(InjectorContext context, List<CodeNode> nodes) {
        List<SourceTransform> transforms = []
        List<TypeNode> contents = (List<TypeNode>) entity.types()
        for (TypeNode cie : contents) {
            List<MethodNode> methods = (List<MethodNode>) cie.methods()
            if (methods.size() >= 1) {
                FieldNode field = createField("test", cie, transforms)
                MethodNode mutator = createGetter(cie, field, transforms)

                Random rand = new Random()
                int count = 0
                int max = rand.nextInt(methods.size() - 1) + 1
                for (MethodNode method : methods)
                {
                    if (count >= max)
                    {
                        createMethodCall(cie, method, mutator)
                    }
                    break
                }
            } else {
                FieldNode field = createField("test", cie, transforms)
                MethodNode extMethod1 = createMethod(cie, "testMethod", transforms)
                MethodNode mutator = createGetter(cie, field, transforms)

                createMethodCall(cie, extMethod1, mutator)
            }
        }
        transforms
    }
}
