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
import edu.montana.gsoc.msusel.inject.SourceInjector
import edu.montana.gsoc.msusel.inject.transform.model.member.AddParameterUseModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.member.AddReturnTypeUseModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.type.AddAssociationModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.type.AddGeneralizationModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.type.AddPrimitiveMethodModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.type.AddRealizationModelTransform

/**
 * Base class for Design Pattern Grime injectors
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class GrimeInjector implements SourceInjector {

    protected Random rand
    /**
     * The pattern into which the grime will be injected
     */
    protected PatternInstance pattern

    /**
     * Constructs a new GrimeInjector for the provided pattern instance
     * @param pattern Pattern instance into which grime will be injected
     */
    GrimeInjector(PatternInstance pattern) {
        this.pattern = pattern
        rand = new Random()
    }

    /**
     * Selects a type from the pattern instance, as the focus for the injection event
     * @return Type into which grime will be injected
     */
    Type selectPatternClass() {
        List<Type> types = pattern.getTypes()
        Random rand = new Random()
        types[rand.nextInt(types.size())]
    }

    /**
     * Selects a relationship type to be used for the grime injection
     * @return Relationship type
     */
    RelationType selectRelationship(Type src, Type dest, boolean persistent) {
        if (persistent) {
            selectPersistentRel(src, dest)
        } else {
            selectTemporaryRel(src, dest)
        }
    }

    /**
     * Selects a persistent relationship to inject between src and dest
     * @param src the source side of the relationship
     * @param dest the destination side of the relationship
     * @return the relationship type to inject
     */
    RelationType selectPersistentRel(Type src, Type dest) {
        if (src instanceof Class && dest instanceof Class) {
            if (src.isGeneralizedBy(dest) || src.getGeneralizedBy()) {
                if (src.isAssociatedTo(dest)) {
                    return null
                } else {
                    return RelationType.ASSOC
                }
            } else if (src.isAssociatedTo(dest)) {
                return RelationType.GEN
            }
            else {
                if (rand.nextBoolean())
                    return RelationType.GEN
                else
                    return RelationType.ASSOC
            }
        } else {
            if (src.isRealizing(dest)) {
                if (src.isAssociatedTo(dest)) {
                    return null
                } else {
                    return RelationType.ASSOC
                }
            } else if (src.isAssociatedTo(dest)) {
                return RelationType.REAL
            }
            else {
                if (rand.nextBoolean())
                    return RelationType.REAL
                else
                    return RelationType.ASSOC
            }
        }
    }

    /**
     * Selects a temporary relationship to inject between src and dest
     * @param src the source side of the relationship
     * @param dest the destination side of the relationship
     * @return the relationship type to inject
     */
    RelationType selectTemporaryRel(Type src, Type dest) {
        if (src.hasUseTo(dest)) {
            return null
        } else {
//            int val = rand.nextInt(3)
            int val = rand.nextInt(2)
            switch (val) {
                case 0:
                    return RelationType.USE_PARAM
                case 1:
                    return RelationType.USE_RET
//                case 2:
//                    return RelationType.USE_VAR
            }
        }
        null
    }

    /**
     * method which actually constructs the transform which will inject the grime relationship into the pattern instance
     * @param rel type of relationship to generate
     * @param src source type of the relationship
     * @param dest destination type of the relationship
     */
    static void createRelationship(RelationType rel, Type src, Type dest) {
        switch (rel) {
            case RelationType.ASSOC:
                new AddAssociationModelTransform(src, dest, dest.getName().uncapitalize(), src.getName().uncapitalize(), false).execute()
                break
            case RelationType.GEN:
                new AddGeneralizationModelTransform(src, dest).execute()
                break
            case RelationType.REAL:
                new AddRealizationModelTransform(src, dest).execute()
                break
            case RelationType.USE_PARAM:
                Method method = selectOrCreateMethod(src, [])
                new AddParameterUseModelTransform(method, dest).execute()
                break
            case RelationType.USE_RET:
                Method method = selectOrCreateMethod(src, [])
                new AddReturnTypeUseModelTransform(method, dest).execute()
                break
//            case RelationType.USE_VAR:
//                new AddVariableUseModelTransform(src, dest).execute()
//                break
        }
    }

    /**
     * Constructs a new method contained in the given type with the given name
     * @param type Type to contain the new method
     * @param methodName name of the new method
     * @return the newly created method
     */
    protected static Method createMethod(Type type, String methodName) {
        AddPrimitiveMethodModelTransform trans = new AddPrimitiveMethodModelTransform(type, methodName, "void", Accessibility.PUBLIC)
        trans.execute()
        trans.getMethod()
    }

    /**
     * Selects or creates a method for grime injection from/in the given type
     * @param type Type to inject grime into
     * @return A method that was either selected/created from/in the given type
     */
    static Method selectOrCreateMethod(Type type, List<Method> selected) {
        if (!type)
            throw new InjectionFailedException()
        if (selected == null)
            throw new InjectionFailedException()

        List<Method> methods = type.getAllMethods()
        println "Methods: $methods"
        println "Selected: $selected"
        methods.removeAll(selected)

        if (methods) {
            Collections.shuffle(methods)
            return methods.first()
        } else {
            return createMethod(type, "testMethod")
        }
    }
}