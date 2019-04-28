/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
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

import com.google.common.collect.Lists
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Pattern
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.transform.AddAssociation
import edu.montana.gsoc.msusel.inject.transform.AddInheritance
import edu.montana.gsoc.msusel.inject.transform.AddRealization
import edu.montana.gsoc.msusel.inject.transform.SourceTransform
import groovy.transform.builder.Builder

/**
 * A source injector to inject modular grime into a design pattern instance
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ModularGrimeInjector extends GrimeInjector {

    /**
     * Flag indicating whether the grime to inject is external (true), or internal (false).
     */
    protected boolean external
    /**
     * Flag indicating whether the grime to inject is efferent (true), or afferent (false).
     */
    protected boolean efferent
    /**
     * Flag indicating whether the grime to inject is persistent (true), or temporary (false).
     */
    protected boolean persistent

    /**
     * Constructs a new ModularGrimeInjector for the given pattern, and parameterized by the three Boolean flags
     * @param pattern Pattern instance into which the grime is to be injected
     * @param persistent flag indicating whether the injected grime is persistent (true), or temporary (false)
     * @param external flag indicating whether the injected grime is external (true), or internal (false)
     * @param efferent flag indicating whether the injected grime is efferent (true), or afferent (false)
     */
    @Builder(buildMethodName = "create")
    private ModularGrimeInjector(Pattern pattern, boolean persistent, boolean external, boolean efferent) {
        super(pattern)
        this.persistent = persistent
        this.external = external
        this.efferent = efferent
    }

    /**
     * {@inheritDoc}
     */
    @Override
    List<SourceTransform> createTransforms(InjectorContext context) {
        List<SourceTransform> transforms = []
        Type src
        Type dest

        if (external) {
            if (efferent) {
                src = selectExternClass(context)
                dest = selectPatternClass()
            } else {
                src = selectPatternClass()
                dest = selectExternClass(context)
            }
        } else {
            (src, dest) = select2PatternClass()
        }

        if (persistent) {
            rel = selectPersistentRel(context, src, dest)
        } else {
            rel = selectTemporaryRel(context, src, dest)
        }

        File file = context.tree.utils.findParentFile(src)

        createRelationship(context, file, rel, src, dest, transforms)
    }

    /**
     * Selects a type external to the pattern definition
     * @param context current InjectorContext
     * @return the Type selected
     */
    Type selectExternClass(InjectorContext context) {
        List<Type> types = Lists.newArrayList(context.tree.utils.types)
        types.removeAll(pattern.types())

        Random rand = new Random()
        types[rand.nextInt(types.size())]
    }

    /**
     * Selects a persistent relationship to inject between src and dest
     * @param context current InjectorContext
     * @param src the source side of the relationship
     * @param dest the destination side of the relationship
     * @return the relationship type to inject
     */
    RelationType selectPersistentRel(InjectorContext context, Type src, Type dest) {
        // TODO Finish This
    }

    /**
     * Selects a temporary relationship to inject between src and dest
     * @param context current InjectorContext
     * @param src the source side of the relationship
     * @param dest the destination side of the relationship
     * @return the relationship type to inject
     */
    RelationType selectTemporaryRel(InjectorContext context, Type src, Type dest) {
        // TODO Finish This
    }

    /**
     * method which actually constructs the transform which will inject the grime relationship into the pattern instance
     * @param context current InjectorContext
     * @param file File to be modified
     * @param rel type of relationship to generate
     * @param src source type of the relationship
     * @param dest destination type of the relationship
     * @param sourceTransforms List transforms to add this relationship to
     */
    void createRelationship(InjectorContext context, File file, RelationType rel, Type src, Type dest, List<SourceTransform> sourceTransforms) {
        // TODO Finish This
        switch (rel) {
            case RelationType.ASSOC:
                transforms << AddAssociation.builder().context(context).file(file).bidirect(false).from(src).fromName().to(dest).toName().create()
                break
            case RelationType.GEN:
                transforms << AddInheritance.builder().context(context).file(file).node(src).gen(dest).create()
                break
            case RelationType.REAL:
                transforms << AddRealization.builder().context(context).file(file).node(src).real(dest).create()
                break
            case RelationType.USE_PARAM:
                break
            case RelationType.USE_RET:
                break
            case RelationType.USE_VAR:
                break
        }
    }
}
