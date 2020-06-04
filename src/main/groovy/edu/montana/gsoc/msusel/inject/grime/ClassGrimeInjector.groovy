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
package edu.montana.gsoc.msusel.inject.grime

import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.inject.InjectionFailedException
import edu.montana.gsoc.msusel.inject.transform.model.member.AddFieldUseModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.member.AddMethodCallModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.type.AddFieldGetterModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.type.AddFieldModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.type.AddFieldSetterModelTransform
import groovy.transform.builder.Builder

/**
 * Injection Strategy for Class Grime
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ClassGrimeInjector extends GrimeInjector {

    /**
     * Flag indicating direct (true) and indirect (false) class grime
     */
    protected boolean direct
    /**
     * Flag indicating internal (true) and external (false) class grime
     */
    protected boolean internal
    /**
     * Flag indicating pair (true) and singular (false) class grime
     */
    protected boolean pair

    /**
     * Constructs a new ClassGrimeInjector for the given pattern and parameterized by the given flags
     * @param pattern Pattern instance to be injected with class grime
     * @param direct Flag indicating either direct or indirect grime
     * @param internal Flag indicating either internal or external grime
     * @param pair Flag indicating either pair or singular grime
     */
    @Builder(buildMethodName = "create")
    ClassGrimeInjector(PatternInstance pattern, boolean direct, boolean internal, boolean pair) {
        super(pattern)
        this.direct = direct
        this.internal = internal
        this.pair = pair
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void inject() {
        Type type = selectPatternClass()
        Field field = createField(type, "test", selectType(type))

        Method method1 = null
        Method method2 = null

        while (!method1) {
            if (internal) {
                method1 = selectPatternMethod(type)
            } else {
                method1 = selectOrCreateMethod(type, [])
            }
        }

        if (pair) {
            while (!method2 && method2 != method1) {
                method2 = selectOrCreateMethod(type, [method1])
            }
        }

        if (direct) {
            createFieldUse(method1, field)
            if (method2) {
                createFieldUse(method2, field)
            }
        } else {
            Method mutator = createGetter(type, field)
            createMethodCall(type, method1, mutator)
            if (method2) {
                createMethodCall(type, method2, mutator)
            }
        }
    }

    Type selectType(Type type) {
        List<Type> knownTypes = type.getParentProject().getAllTypes()
        knownTypes.remove(type)

        if (knownTypes) {
            Collections.shuffle(knownTypes)
            return knownTypes.first()
        }

        null
    }

    /**
     * Selects a method from the given type that is specified by the pattern definition or used by such a method
     * @param type Type
     * @return a method that is part of the pattern and is found within the given type
     */
    Method selectPatternMethod(Type type) {
        if (!type)
            throw new InjectionFailedException()

        List<Method> methods = []
        pattern.getRoleBindings().each { RoleBinding rb ->
            if (rb.reference.type == RefType.METHOD) {
                Method method = Method.findFirst("compKey = ?", rb.reference.refKey)
                if (method && method.getParentType() == type)
                    methods << method
            }
        }

        if (methods.size() >= 1) {
            Collections.shuffle(methods)
            return methods.first()
        } else {
            Method method = createMethod(type, "roleBound")
            Role role = pattern.getRoles().find {
                it.getType() == RoleType.BEHAVE_FEAT
            }
            if (!role) {
                role = Role.builder().type(RoleType.BEHAVE_FEAT).name("_test_role_").roleKey("${pattern.getParentPattern().patternKey}:_test_role_").create()
            }
            pattern.getParentPattern().addRole(role)
            pattern.addRoleBinding(RoleBinding.of(role, method.createReference()))
            return method
        }
    }

    /**
     * Constructs a new field with the given name, in the given type
     * @param fieldName Name of the new field to create
     * @param type Type in which the new field will be contained
     * @return The field node to be created
     */
    protected Field createField(Type parent, String fieldName, Type type) {
        AddFieldModelTransform trans = new AddFieldModelTransform(parent, fieldName, type, Accessibility.PRIVATE)
        trans.execute()
        trans.getField()
    }

    /**
     * Constructs a new method with a field use for the specified field in the given type
     * @param methodName Name of the method to be created
     * @param type Type which will contain the new method and which already contains the field to be used
     * @param field the field to be referenced
     */
    protected void createMethodWithFieldUse(String methodName, Type type, Field field) {
        Method extMethod = createMethod(type, methodName)
        createFieldUse(extMethod, field)
    }

    /**
     * Constructs a new field use for the given field in the given method
     * @param method Method into which to inject the field use
     * @param field the field to be used
     */
    protected void createFieldUse(Method method, Field field) {
        new AddFieldUseModelTransform(method, field).execute()
    }

    /**
     * Constructs a getter method contained in the given type and for the given field
     * @param type Type to contain the getter method
     * @param field Field the getter is for
     * @return the newly constructed getter method
     */
    protected Method createGetter(Type type, Field field) {
        AddFieldGetterModelTransform trans = new AddFieldGetterModelTransform(type, field)
        trans.execute()
        trans.getMethod()
    }

    /**
     * Constructs a setter method contained in the given type and for the given field
     * @param type Type to contain the getter method
     * @param field Field the setter is for
     * @return the newly constructed setter method
     */
    protected Method createSetter(Type type, Field field) {
        AddFieldSetterModelTransform trans = new AddFieldSetterModelTransform(type, field)
        trans.execute()
        trans.getMethod()
    }

    /**
     *
     * @param type
     * @param callee
     * @param call
     */
    def createMethodCall(Type type, Method callee, Method call) {
        new AddMethodCallModelTransform(callee, call).execute()
    }
}
