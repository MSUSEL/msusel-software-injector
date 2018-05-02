package edu.montana.gsoc.msusel.source

import edu.montana.gsoc.msusel.codetree.node.CodeNode

class SelectInternalClasses extends Selector {

    private int num

    SelectInternalClasses(int num) {
        this.num = num
    }

    @Override
    List<CodeNode> select(tree, binding) {
        return null
    }
}
