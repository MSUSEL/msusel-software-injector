package edu.montana.gsoc.msusel.inject.transform

import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.structural.NamespaceNode
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.cond.NamespaceExists
import groovy.transform.builder.Builder

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class CreateNamespace extends CreateStructure {

    NamespaceNode namespace

    @Builder(buildMethodName = "create")
    private CreateNamespace(InjectorContext context, FileNode file, NamespaceNode namespace) {
        super(context, file)
        this.namespace = namespace
    }

    @Override
    void execute() {

    }

    @Override
    void initializeConditions() {
        conditions << new NamespaceExists(context, namespace)
    }
}
