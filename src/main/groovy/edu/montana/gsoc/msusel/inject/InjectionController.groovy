/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2018 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package edu.montana.gsoc.msusel.inject

import edu.montana.gsoc.msusel.codetree.CodeTree
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.structural.PatternNode
import edu.montana.gsoc.msusel.rbml.model.Pattern

/**
 * Class to control the basic operation of an injector. Maintains a list of open FileOperations,
 * while also providing a means to ensure that any open FileOperations save, and that the invoker
 * executes collected Transforms
 * @author Isaac Griffith
 * @version 1.2.0
 */
class InjectionController {

    /**
     * Map of current FileOperations linked to their respective FileNodes
     */
    Map<FileNode, FileOperations> opsMap = [:]

    void process(CodeTree tree, PatternNode node, Pattern rbml) {
//        RBMLBinding binding = RBMLBinding.between(rbml, node)
//
//        injector.inject(tree, binding, invoker)
//
        invoker.executeTransforms()
        complete()
    }

    /**
     * Retrieves the current FileOperations for a given FileNode, if no such FileOperation exists,
     * then one is created
     * @param file FileNode
     * @return FileOperation for the file
     * @throws IllegalArgumentException if the provided FileNode is null
     */
    FileOperations getOps(FileNode file) {
        if (file == null) {
            throw new IllegalArgumentException("No FileOperations can be defined for a null FileNode")
        }

        if (!opsMap[file]) {
            opsMap[file] = new FileOperations()
            opsMap[file].open(file)
        }

        opsMap[file]
    }

    void complete() {
        opsMap.each { key, value ->
            value.save()
        }
    }
}
