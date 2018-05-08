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

import edu.montana.gsoc.msusel.arc.impl.pattern4.codetree.PatternNode
import edu.montana.gsoc.msusel.codetree.node.CodeNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.select.Selector
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
    ModularGrimeInjector(String type, PatternNode pattern, Pattern rbml, Selector selector, boolean persistent, boolean external, boolean efferent) {
        super(type, pattern, rbml, selector)
        this.persistent = persistent
        this.external = external
        this.efferent = efferent
    }

    @Override
    List<SourceTransform> createTransforms(InjectorContext context, List<CodeNode> nodes) {
        List<SourceTransform> transforms = []
        TypeNode src
        TypeNode dest

        if (external) {
            if (efferent) {
                src = selectExternClass()
                dest = selectPatternClass()
            } else {
                src = selectPatternClass()
                dest = selectExternClass()
            }
        } else {
            (src, dest) = select2PatternClass()
        }

        if (persistent) {
            rel = selectPersistentRel()
        } else {
            rel = selectTemporaryRel()
        }

        createRelationship(src, dest, rel, transforms)
    }

    TypeNode selectPatternClass() {
        null
    }

    TypeNode selectExternClass() {
        null
    }

    def selectPersistentRel() {

    }

    def selectTemporaryRel() {

    }

    List<SourceTransform> createRelationship(TypeNode src, TypeNode dest, rel, ArrayList<SourceTransform> sourceTransforms) {
        null
    }
}
