package edu.montana.gsoc.msusel.inject.transform

import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.inject.InjectorContext

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
abstract class AddRelation extends BasicSourceTransform {

    AddRelation(InjectorContext context, FileNode file) {
        super(context, file)
    }
}
