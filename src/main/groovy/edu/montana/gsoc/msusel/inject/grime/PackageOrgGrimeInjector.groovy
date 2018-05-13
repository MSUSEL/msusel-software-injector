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

import edu.montana.gsoc.msusel.codetree.node.structural.NamespaceNode
import edu.montana.gsoc.msusel.codetree.node.structural.PatternNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.transform.SourceTransform
import groovy.transform.builder.Builder
/**
 * Injector strategy for Package type Organizational Grime
 * @author Isaac Griffith
 * @version 1.2.0
 */
class PackageOrgGrimeInjector extends OrgGrimeInjector {

    /**
     * Flag indicating internal (true), or external (false) grime
     */
    protected boolean internal
    /**
     * Flag indicating closure (true), or reuse (false) grime
     */
    protected boolean closure

    /**
     * Constructs a new Package type OrgGrime injector for the given pattern instance, parameterized by the provided flags
     * @param pattern Pattern instance to be injected with grime
     * @param internal Flag indicating internal (true), or external (false) grime
     * @param closure Flag indicating closure (true), or reuse (false) grime
     */
    @Builder(buildMethodName = "create")
    private PackageOrgGrimeInjector(PatternNode pattern, boolean internal, boolean closure) {
        super(pattern)
        this.internal = internal
        this.closure = closure
    }

    /**
     * {@inheritDoc}
     */
    @Override
    List<SourceTransform> createTransforms(InjectorContext context) {
        List<SourceTransform> transforms = []

        NamespaceNode pkg = selectPatternNamespaces()
        NamespaceNode other
        TypeNode type, dest

        if (internal) {
            type = selectPatternClass(pkg)
        } else {
            type = selectOrCreateExternalClass(pkg)
        }

        RelationType rel = selectRelationship()

        if (closure) {
            other = selectUnreachableNamespace(pkg)
        } else {
            other = selectNamespace()
        }
        dest = selectExternalClass(other)

        if (other && dest) {
            createRelationship(type, dest, rel)
        }

        transforms
    }

    /**
     * Selects a relationship type to be used for the grime injection
     * @return Relationship type
     */
    RelationType selectRelationship() {
        null
    }

    /**
     * Constructs a relationship between the provided type nodes, with the given type
     * @param src Source side of the relationship
     * @param dest Dest side of the relationship
     * @param rel type of relationship
     */
    void createRelationship(TypeNode src, TypeNode dest, RelationType rel) {

    }

    /**
     * Selects a class within the given namespace, but external to the pattern instance
     * @param ns Namespace
     * @return An external to the pattern instance type
     */
    TypeNode selectExternalClass(NamespaceNode ns) {
        null
    }

    /**
     * Selects a namespace form the namespaces covered by the pattern instance
     * @return A namespace for use in grime injection
     */
    NamespaceNode selectNamespace() {
        null
    }

    /**
     * Selects a namespace which is currently unreachable from the provided namespace
     * @param ns Namespace used for reachability calculations
     * @return A namespace currently unreachable from the provided namespace
     */
    NamespaceNode selectUnreachableNamespace(NamespaceNode ns) {
        null
    }

    /**
     * Selects or creates a new type external from the design pattern and contained in the provided namespace
     * @param ns Namespace that currently contains or will contain the type returned
     * @return A type external to the pattern instance but contained within the given namespace
     */
    TypeNode selectOrCreateExternalClass(NamespaceNode ns) {
        null
    }
}
