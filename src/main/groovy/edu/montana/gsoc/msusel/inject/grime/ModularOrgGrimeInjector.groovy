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

import edu.montana.gsoc.msusel.codetree.node.structural.NamespaceNode
import edu.montana.gsoc.msusel.codetree.node.structural.PatternNode
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.transform.SourceTransform
import groovy.transform.builder.Builder
/**
 * Injector strategy for Modular type Organizational Grime
 * @author Isaac Griffith
 * @version 1.2.0
 */
class ModularOrgGrimeInjector extends OrgGrimeInjector {

    /**
     * Flag indicating persistent (true) or temporary (false) org grime
     */
    protected boolean persistent
    /**
     * Flag indicating internal (true) or external (false) org grime
     */
    protected boolean internal
    /**
     * Flag indicating cyclical (true) or unstable (false) org grime
     */
    protected boolean cyclical

    /**
     * Constructs a new ModularOrgGrime Injector for the given pattern and parameterized by the provided flags
     * @param pattern Pattern to inject grime into
     * @param persistent Flag indicating persistent (true) or temporary (false) org grime
     * @param internal Flag indicating internal (true) or external (false) org grime
     * @param cyclical Flag indicating cyclical (true) or unstable (false) org grime
     */
    @Builder(buildMethodName = "create")
    private ModularOrgGrimeInjector(PatternNode pattern, boolean persistent, boolean internal, boolean cyclical) {
        super(pattern)
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

    /**
     * Adds an instability to the given pair of namespaces using a relationship of the given type
     * @param srcNs Source namespace from which a class is selected as the source of the relationship to be created
     * @param destNs Destination namespace from which a class is selected as the destination of the relationship to be created
     * @param rel type of relationship to generate
     */
    def addInstability(NamespaceNode srcNs, NamespaceNode destNs, RelationType rel) {

    }

    /**
     * Adds a cycle to the given pair of namespaces using a relationship of the given type
     * @param srcNs Source namespace from which a class is selected as the source of the relationship to be created
     * @param destNs Destination namespace from which a class is selected as the destination of the relationship to be created
     * @param rel type of relationship to generate
     */
    def createCyclicalDependency(NamespaceNode srcNs, NamespaceNode destNs, RelationType rel) {

    }

    /**
     * Selects a temporary relationship type to generate
     * @return specific type of temporary relationship
     */
    RelationType selectTempRelationship() {

    }

    /**
     * Selects a persistent relationship type to generate
     * @return specific type of persistent relationship
     */
    RelationType selectPersistentRelationship() {

    }

    /**
     * Selects or creates an external namespace for use by the injector
     * @return the namespace selected or created
     */
    NamespaceNode selectOrCreateExternNamespace() {
        null
    }

    /**
     * Selects a pattern namespace, one containing a patter or subsumed by a pattern, for use in injection
     * @return the namespace selected
     */
    NamespaceNode selectPatternNamespace() {
        null
    }

    /**
     * Splits a given namespace into two or more namespaces
     * @param namespaceNode Namespace to be split
     * @return a pair of namespaces split from the provided one
     */
    def splitNamespace(NamespaceNode namespaceNode) {

    }

    /**
     * Selects a random namespace from the list provided
     * @param namespaceNodes list from which the selection is to occur
     * @return A selected namespace
     */
    NamespaceNode selectNamespace(List<NamespaceNode> namespaceNodes) {
        null
    }

    /**
     * Selects multiple namespaces from the list provided
     * @param namespaceNodes list of namespaces from which a selection is to occur
     * @return a collection of selected namespaces
     */
    def selectNamespaces(List<NamespaceNode> namespaceNodes) {

    }

    /**
     * Finds those namespaces in the codetree which are pattern namespaces
     * @return List of pattern namespaces
     */
    List<NamespaceNode> findPatternNamespaces() {

    }
}
