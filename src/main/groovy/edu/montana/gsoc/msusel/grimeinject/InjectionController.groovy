package edu.montana.gsoc.msusel.grimeinject

import edu.montana.gsoc.msusel.codetree.CodeTree
import edu.montana.gsoc.msusel.codetree.node.structural.PatternNode
import edu.montana.gsoc.msusel.rbml.model.Pattern

class InjectionController {

    GrimeInjector injector
    TransformInvoker invoker

    InjectionController(String injection) {
        injector = InjectorFactory.instance.createInjector(injection)
        invoker = new TransformInvoker()
    }

    void process(CodeTree tree, PatternNode node, Pattern rbml) {
//        RBMLBinding binding = RBMLBinding.between(rbml, node)
//
//        injector.inject(tree, binding, invoker)
//
//        invoker.executeTransforms()
    }
}
