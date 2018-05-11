package edu.montana.gsoc.msusel.grimeinject

import edu.montana.gsoc.msusel.arc.impl.pattern4.codetree.PatternNode
import edu.montana.gsoc.msusel.codetree.node.structural.NamespaceNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.transform.SourceTransform
import edu.montana.gsoc.msusel.rbml.model.Pattern
import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class PackageOrgGrimeInjector extends OrgGrimeInjector {

    protected boolean internal
    protected boolean closure

    @Builder(buildMethodName = "create")
    private PackageOrgGrimeInjector(PatternNode pattern, Pattern rbml, boolean internal, boolean closure) {
        super(pattern, rbml)
        this.internal = internal
        this.closure = closure
    }

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

    RelationType selectRelationship() {
        null
    }

    void createRelationship(TypeNode src, TypeNode dest, RelationType rel) {

    }

    TypeNode selectExternalClass(NamespaceNode ns) {
        null
    }

    NamespaceNode selectNamespace() {
        null
    }

    NamespaceNode selectUnreachableNamespace(NamespaceNode ns) {
        null
    }

    TypeNode selectOrCreateExternalClass(NamespaceNode ns) {
        null
    }
}
