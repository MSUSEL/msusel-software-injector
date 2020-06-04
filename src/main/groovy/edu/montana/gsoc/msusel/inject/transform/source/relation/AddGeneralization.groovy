/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
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
package edu.montana.gsoc.msusel.inject.transform.source.relation

import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.transform.source.TypeHeaderTransform
import groovy.transform.builder.Builder

/**
 * Transform which modifies the TypeHeader information and injects inheritance
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddGeneralization extends TypeHeaderTransform {

    /**
     * Type from which the modified type will inherit
     */
    Type gen

    /**
     * Constructs a new AddInheritance transform
     * @param file The file to be modified
     * @param node The type whose header is to be modified
     * @param gen The type which is to be inherited from
     */
    @Builder(buildMethodName = "create")
    AddGeneralization(File file, Type node, Type gen) {
        super(file, node)
        this.gen = gen
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void execute() {
        // 1. find type name check if already extends something, if so throw an exception, else add extends
        String header = getTypeHeader()

        String afterName = header.split(/${type.getName()}(<.+>)?/)[1]
        String newAfterName = " extends ${gen.getName()}${afterName}"
        String newContent = header.replace(afterName, newAfterName)

        // 2. add relation to model
        type.generalizedBy(gen)

        // 3. inject changes into the file
        java.io.File ops = new java.io.File(file.getFullPath())
        def text = ops.text
        ops.text = text.replace(header, newContent)

        // 3. check the package to determine if an import is needed
        // 4. if an import is needed, check if it already exists, if not add the type to the import list
        updateImports(gen)
        // 5. check new generalization type for any needed abstract methods (if not abstract), if any are missing add to list of things to do
        // 6. if abstract, and methods not implemented, then add them to all concrete subclasses
        implementAbstractMethods(gen)
    }
}
