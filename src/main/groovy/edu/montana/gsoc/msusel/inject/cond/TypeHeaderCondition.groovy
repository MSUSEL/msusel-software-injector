package edu.montana.gsoc.msusel.inject.cond

import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.inject.FileOperations
import edu.montana.gsoc.msusel.inject.InjectorContext

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
abstract class TypeHeaderCondition implements Condition {

    protected final FileNode file
    protected final TypeNode node
    protected final InjectorContext context

    TypeHeaderCondition(InjectorContext context, FileNode file, TypeNode node) {
        this.context = context
        this.file = file
        this.node = node
    }

    String getTypeHeader() {
        FileOperations ops = context.controller.getOps(file)
        StringBuilder header = new StringBuilder()
        int current = node.start
        while (!header.toString().contains("{")) {
            header << ops.contentAt(current)
            current += 1
            header << "\n"
        }

        header.toString()
    }
}
