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
package edu.montana.gsoc.msusel.inject.transform

import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.inject.InjectorContext
import groovy.transform.builder.Builder
/**
 * Transform which adds an association in one Type to another Type
 * @author Isaac Griffith
 * @version 1.2.0
 */
class AddAssociation extends CompositeSourceTransform {

    /**
     * Type in which the association is added
     */
    TypeNode from
    /**
     * Type of the association's field
     */
    TypeNode to
    /**
     * Name of the to side of the association (as a field in the From type)
     */
    String toName
    /**
     * Name of the from side of the association (as a field in the To type, if bidirectional)
     */
    String fromName
    /**
     * boolean indicating whether the association is bidirectional or not
     */
    boolean bidirect
    /**
     * File which contains the To Type
     */
    FileNode toFile

    /**
     * Constructs a new AddAssociation
     * @param context The current Injector Context
     * @param file The file in which modifications will occur
     * @param from The from Type
     * @param toFile the To File
     * @param to the To side type
     * @param toName the name of the to reference
     * @param fromName the name of the from reference
     * @param bidirect boolean flag indicating bidirectionality
     */
    @Builder(buildMethodName = "create")
    private AddAssociation(InjectorContext context, FileNode file, TypeNode from, FileNode toFile, TypeNode to, String toName, String fromName, boolean bidirect) {
        super(context, file)
        this.from = from
        this.to = to
        this.toName = toName
        this.fromName = fromName
        this.bidirect = bidirect
        this.toFile = toFile
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void execute() {
        if (!bidirect) {
            transforms << CreateEncapsulatedField.builder().context(context).file(file).type(from).fieldType(to).fieldName(fromName).create()
        } else {
            transforms << CreateEncapsulatedField.builder().context(context).file(file).type(from).fieldType(to).fieldName(fromName).create()
            transforms << CreateEncapsulatedField.builder().context(context).file(toFile).type(to).fieldType(from).fieldName(toName).create()
        }
        // TODO add association to tree
        context.invoker.submitAll(transforms)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void initializeConditions() {

    }
}
