package edu.montana.gsoc.msusel.inject.cond

import edu.montana.gsoc.msusel.codetree.node.structural.NamespaceNode
import edu.montana.gsoc.msusel.inject.InjectorContext

class NamespaceExists implements Condition {

    NamespaceNode namespace
    InjectorContext context

    NamespaceExists(InjectorContext context, NamespaceNode namespace) {
        this.context = context
        this.namespace = namespace
    }

    @Override
    boolean check() {
        return false
    }
}
