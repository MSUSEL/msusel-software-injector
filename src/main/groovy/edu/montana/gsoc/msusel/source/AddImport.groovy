package edu.montana.gsoc.msusel.source

import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.structural.ImportNode

class AddImport extends AbstractSourceTransform {

    ImportNode node

    AddImport(FileNode file, ImportNode node) {
        super(file)
    }

    @Override
    def execute() {
        return null
    }
}
