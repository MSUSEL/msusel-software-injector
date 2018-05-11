package edu.montana.gsoc.msusel.grimeinject

import edu.montana.gsoc.msusel.arc.impl.pattern4.codetree.PatternNode
import edu.montana.gsoc.msusel.codetree.node.structural.NamespaceNode
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.transform.SourceTransform
import edu.montana.gsoc.msusel.rbml.model.Pattern
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
    private ModularOrgGrimeInjector(PatternNode pattern, Pattern rbml, boolean persistent, boolean internal, boolean cyclical) {
        super(pattern, rbml)
        this.persistent = persistent
        this.internal = internal
        this.cyclical = cyclical
    }

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
