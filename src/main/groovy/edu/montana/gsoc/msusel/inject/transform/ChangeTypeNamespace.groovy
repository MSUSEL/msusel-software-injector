package edu.montana.gsoc.msusel.inject.transform

import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.inject.InjectorContext

class ChangeTypeNamespace extends AbstractSourceTransform {

    /**
     * Constructs a new ChangeTypeNamespace transform
     * @param context The current InjectorContext
     * @param file The filenode to be transformed
     */
    ChangeTypeNamespace(InjectorContext context, FileNode file) {
        super(context, file)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void initializeConditions() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    void execute() {

    }
}
