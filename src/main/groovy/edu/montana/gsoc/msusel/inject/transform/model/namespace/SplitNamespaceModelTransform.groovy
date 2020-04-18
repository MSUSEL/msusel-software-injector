package edu.montana.gsoc.msusel.inject.transform.model.namespace

import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Module
import edu.isu.isuese.datamodel.Namespace
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.model.NamespaceModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.module.AddNamespaceToModuleModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.module.DeleteNamespaceFromModuleModelTransform
import groovy.transform.builder.Builder

class SplitNamespaceModelTransform extends NamespaceModelTransform {

    List<File> left
    List<File> right

    Namespace ns1
    Namespace ns2
    def parent

    @Builder(buildMethodName = "create")
    SplitNamespaceModelTransform(Namespace ns, List<File> left, List<File> right) {
        super(ns)
        this.left = left
        this.right = right
    }

    @Override
    void verifyPreconditions() {
        if (!ns)
            throw new ModelTransformPreconditionsNotMetException()
        if (!left)
            throw new ModelTransformPreconditionsNotMetException()
        if (!right)
            throw new ModelTransformPreconditionsNotMetException()
        if (!ns.getParentNamespace() && !ns.getParentModule())
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // 1. Create Two New Namespaces (ns.name 1, ns.name 2) -> Use AddNamespaceModelTransform
        ModelTransform trans1
        ModelTransform trans2
        ModelTransform delete

        if (ns.getParentModule()) {
            parent = ns.getParentModule()
            trans1 = AddNamespaceToModuleModelTransform.builder().mod(ns.getParentModule()).name("${ns.getName()}1").create()
            trans2 = AddNamespaceToModuleModelTransform.builder().mod(ns.getParentModule()).name("${ns.getName()}2").create()
            delete = DeleteNamespaceFromModuleModelTransform.builder().mod(ns.getParentModule()).ns(ns).create()

            trans1.execute()
            trans2.execute()

            ns1 = ((AddNamespaceToModuleModelTransform) trans1).getNs()
            ns2 = ((AddNamespaceToModuleModelTransform) trans2).getNs()
        } else {
            parent = ns.getParentNamespace()
            trans1 = AddNamespaceToNamespaceModelTransform.builder().ns(ns.getParentNamespace()).name("${ns.getName()}_generated_1").create()
            trans2 = AddNamespaceToNamespaceModelTransform.builder().ns(ns.getParentNamespace()).name("${ns.getName()}_generated_2").create()
            delete = DeleteNamespaceFromNamespaceModelTransform.builder().ns(ns.getParentNamespace()).child(ns).create()

            trans1.execute()
            trans2.execute()

            ns1 = ((AddNamespaceToNamespaceModelTransform) trans1).getChild()
            ns2 = ((AddNamespaceToNamespaceModelTransform) trans2).getChild()
        }

        // 2. for each file in left move to ns.name1 - Use MoveFileModelTransform
        left.each { MoveFileModelTransform.builder().ns(ns).newParent(ns1).file(it).create().execute() }

        // 3. for each file in right move to ns.name2 -> Use MoveFileModelTransform
        right.each { MoveFileModelTransform.builder().ns(ns).newParent(ns2).file(it).create().execute() }

        // 4. Delete the current Namespace
        delete.execute()
    }

    @Override
    void verifyPostconditons() {
        if (parent instanceof Module) {
            Module modParent = (Module) parent
            assert(!modParent.getNamespaces().contains(ns))
            assert(modParent.getNamespaces().contains(ns1))
            assert(modParent.getNamespaces().contains(ns2))
        } else {
            Namespace nsParent = (Namespace) parent
            assert(!nsParent.getNamespaces().contains(ns))
            assert(nsParent.getNamespaces().contains(ns1))
            assert(nsParent.getNamespaces().contains(ns2))
        }
    }
}
