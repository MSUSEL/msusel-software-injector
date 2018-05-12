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
package edu.montana.gsoc.msusel.inject.grime

import edu.montana.gsoc.msusel.codetree.node.Accessibility
import edu.montana.gsoc.msusel.codetree.node.member.FieldNode
import edu.montana.gsoc.msusel.codetree.node.member.MethodNode
import edu.montana.gsoc.msusel.codetree.node.member.ParameterNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.codetree.typeref.PrimitiveTypeRef
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.transform.*
import groovy.transform.builder.Builder

/**
 * Injection Strategy for Class Grime
 * @author Isaac Griffith
 * @version 1.2.0
 */
class ClassGrimeInjector extends GrimeInjector {

    protected boolean direct
    protected boolean internal
    protected boolean pair

    @Builder(buildMethodName = "create")
    ClassGrimeInjector(/*PatternNode pattern, Pattern rbml,*/ boolean direct, boolean internal, boolean pair) {
//        super(pattern, rbml)
        this.direct = direct
        this.internal = internal
        this.pair = pair
    }

    /**
     * {@inheritDoc}
     */
    @Override
    List<SourceTransform> createTransforms(InjectorContext context) {
        List<SourceTransform> transforms = []
        TypeNode clazz = selectPatternClass()
        FieldNode field = createField("test", clazz, transforms)

        MethodNode method1
        MethodNode method2

        if (internal) {
            method1 = selectPatternMethod()
        } else {
            method1 = selectOrCreateMethod()
        }

        if (pair) {
            method2 = selectOrCreateMethod()
        }

        if (direct) {
            createFieldUse(transforms, clazz, field, method1)
            if (method2) {
                createFieldUse(transforms, clazz, field, method2)
            }
        } else {
            MethodNode mutator = createGetter(clazz, field, transforms)
            createMethodCall(clazz, method1, mutator, transforms)
            if (method2) {
                createMethodCall(clazz, method2, mutator, transforms)
            }
        }
        transforms
    }

    MethodNode selectOrCreateMethod(TypeNode type) {
        List<MethodNode> methods = type.methods()
        if (methods.size() >= 1) {
            Random rand = new Random()
            int count = 0
            int max = rand.nextInt(methods.size()) + 1
            for (MethodNode method : methods) {
                // TODO Fix This
                if (count >= max) {
                    return method
                }
                break
            }
        } else {
            return createMethod(type, "testMethod1", transforms)
        }
    }

    MethodNode selectPatternMethod(TypeNode type) {
        List<MethodNode> methods = type.methods()
        if (methods.size() >= 1) {
            Random rand = new Random()
            int count = 0
            int max = rand.nextInt(methods.size()) + 1
            for (MethodNode method : methods) {
                // TODO Fix This
                if (count >= max) {
                    return method
                }
                break
            }
        } else {
            return methods[0]
        }
    }

    protected FieldNode createField(String fieldName, TypeNode type, List<SourceTransform> transforms) {
        FieldNode field = FieldNode.builder()
                .key("${type.key}#${fieldName}")
                .accessibility(Accessibility.PRIVATE)
                .create()

        transforms << AddField.builder()
                .type(type)
                .field(field)
                .file(file)
                .context(context)
                .create()

        field
    }

    protected void createMethodWithFieldUse(String methodName, TypeNode type, List<SourceTransform> transforms, FieldNode field) {
        MethodNode extMethod = createMethod(type, methodName, transforms)
        createFieldUse(transforms, type, field, extMethod)
    }

    protected List<SourceTransform> createFieldUse(List<SourceTransform> transforms, TypeNode type, FieldNode field, MethodNode extMethod) {
        transforms << AddFieldUse.builder()
                .file(file)
                .context(context)
                .type(type)
                .field(field)
                .method(extMethod)
                .create()
    }

    protected MethodNode createGetter(TypeNode type, FieldNode field, List<SourceTransform> transforms) {
        method = MethodNode.builder()
                .accessibility(Accessibility.PUBLIC)
                .key("${type.key}#get${capitalizedName()}")
                .type(node.type)
                .create()

        transforms << AddFieldGetter.builder()
                .file(file)
                .context(context)
                .type(type)
                .field(field)
                .create()

        method
    }

    protected MethodNode createSetter(TypeNode type, FieldNode field, List<SourceTransform> transforms) {
        method = MethodNode.builder()
                .accessibility(Accessibility.PUBLIC)
                .key("${type.key}#set${capitalizedName()}")
                .type(PrimitiveTypeRef.getInstance("void"))
                .params([ParameterNode.builder().type(node.type).key(node.name()).create()])
                .create()

        transforms << AddFieldSetter.builder()
                .file(file)
                .context(context)
                .type(type)
                .field(field)
                .create()

        method
    }

    protected MethodNode createMethod(TypeNode type, String methodName, List<SourceTransform> transforms) {
        MethodNode extMethod = MethodNode.builder()
                .key("${type.key}#${methodName}")
                .type(PrimitiveTypeRef.getInstance("void"))
                .accessibility(Accessibility.PUBLIC)
                .create()

        transforms << AddMethod.builder()
                .type(type)
                .node(extMethod)
                .file(file)
                .context(context)
                .create()
        extMethod
    }

    def createMethodCall(TypeNode typeNode, MethodNode callee, MethodNode call, List<SourceTransform> transforms) {

    }
}
