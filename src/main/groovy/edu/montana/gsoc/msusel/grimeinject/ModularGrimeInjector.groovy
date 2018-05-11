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
package edu.montana.gsoc.msusel.grimeinject

import com.google.common.collect.Lists
import edu.montana.gsoc.msusel.arc.impl.pattern4.codetree.PatternNode
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.transform.AddAssociation
import edu.montana.gsoc.msusel.inject.transform.AddInheritance
import edu.montana.gsoc.msusel.inject.transform.AddRealization
import edu.montana.gsoc.msusel.inject.transform.SourceTransform
import edu.montana.gsoc.msusel.rbml.model.Pattern
import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class ModularGrimeInjector extends GrimeInjector {

    protected boolean external
    protected boolean efferent
    protected boolean persistent

    @Builder(buildMethodName = "create")
    ModularGrimeInjector(PatternNode pattern, Pattern rbml, boolean persistent, boolean external, boolean efferent) {
        super(pattern, rbml)
        this.persistent = persistent
        this.external = external
        this.efferent = efferent
    }

    @Override
    List<SourceTransform> createTransforms(InjectorContext context) {
        List<SourceTransform> transforms = []
        TypeNode src
        TypeNode dest

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

        createRelationship(context, rel, src, dest, transforms)
    }

    TypeNode selectExternClass(InjectorContext context) {
        List<TypeNode> types = Lists.newArrayList(context.tree.utils.types)
        types.removeAll(pattern.types())

        Random rand = new Random()
        types[rand.nextInt(types.size())]
    }

    RelationType selectPersistentRel(InjectorContext context, TypeNode src, TypeNode dest) {

    }

    RelationType selectTemporaryRel(InjectorContext context, TypeNode src, TypeNode dest) {

    }

    void createRelationship(InjectorContext context, FileNode file, RelationType rel, TypeNode src, TypeNode dest, ArrayList<SourceTransform> sourceTransforms) {
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
