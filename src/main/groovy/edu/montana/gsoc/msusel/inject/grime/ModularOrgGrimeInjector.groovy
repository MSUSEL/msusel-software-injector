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
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.transform.SourceTransform
import groovy.transform.builder.Builder
/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class ModularOrgGrimeInjector extends OrgGrimeInjector {

    protected boolean persistent
    protected boolean internal
    protected boolean cyclical

    @Builder(buildMethodName = "create")
    private ModularOrgGrimeInjector(/*PatternNode pattern, Pattern rbml,*/ boolean persistent, boolean internal, boolean cyclical) {
//        super(pattern, rbml)
        this.persistent = persistent
        this.internal = internal
        this.cyclical = cyclical
    }

    /**
     * {@inheritDoc}
     */
    @Override
    List<SourceTransform> createTransforms(InjectorContext context) {
        List<SourceTransform> transforms = []

        List<NamespaceNode> pkgs = findPatternNamespaces()
        NamespaceNode ns1, ns2
        RelationType rel

        if (internal) {
            if (pkgs.size() > 1) {
                (ns1, ns2) = selectNamespaces(pkgs)
            } else {
                ns1 = selectNamespace(pkgs)
                (ns1, ns2) = splitNamespace(ns1)
            }
        } else {
            ns1 = selectPatternNamespace()
            ns2 = selectOrCreateExternNamespace()
        }

        if (persistent) {
            rel = selectPersistentRelationship()
        } else {
            rel = selectTempRelationship()
        }

        if (cyclical) {
            createCyclicalDependency(ns1, ns2, rel)
        } else {
            addInstability(ns1, ns2, rel)
        }

        transforms
    }

    def addInstability(NamespaceNode srcNs, NamespaceNode destNs, RelationType rel) {

    }

    def createCyclicalDependency(NamespaceNode srcNs, NamespaceNode destNs, RelationType rel) {

    }

    RelationType selectTempRelationship() {

    }

    RelationType selectPersistentRelationship() {

    }

    NamespaceNode selectOrCreateExternNamespace() {
        null
    }

    NamespaceNode selectPatternNamespace() {
        null
    }

    def splitNamespace(NamespaceNode namespaceNode) {

    }

    NamespaceNode selectNamespace(List<NamespaceNode> namespaceNodes) {
        null
    }

    def selectNamespaces(List<NamespaceNode> namespaceNodes) {

    }

    List<NamespaceNode> findPatternNamespaces() {

    }
}
