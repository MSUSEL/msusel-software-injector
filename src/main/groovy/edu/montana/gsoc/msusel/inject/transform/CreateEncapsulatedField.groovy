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

import edu.montana.gsoc.msusel.codetree.node.Accessibility
import edu.montana.gsoc.msusel.codetree.node.member.FieldNode
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.codetree.typeref.TypeRef
import edu.montana.gsoc.msusel.inject.InjectorContext
import groovy.transform.builder.Builder
/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class CreateEncapsulatedField extends AbstractSourceTransform {

    String fieldName
    TypeNode type
    TypeNode fieldType

    @Builder(buildMethodName = "create")
    private CreateEncapsulatedField(InjectorContext context, FileNode file, TypeNode type, TypeNode fieldType, String fieldName) {
        super(context, file)
        this.type = type
        this.fieldName = fieldName
        this.fieldType = fieldType
    }

    @Override
    void execute() {
        // 1. Determine if a field already exists, with either the given name or the given type, if so throw an exception.

        // 2. If no such field exists, then create the field
        FieldNode fld = FieldNode.builder()
                .key("${type.key}#${fieldName}")
                .accessibility(Accessibility.PRIVATE)
                .type(TypeRef.builder().type(fieldType.key).typeName(fieldType.name()).create())
                .create()

        // 3. Create the AddField transform
        AddField.builder().file(file).type(from).field(fld).tree(tree).ops(ops).create()

        // 4. Check if a method called get<FieldName> exists, if so throw an exception, else create the transform
        AddFieldGetter.builder().file(file).type(from).node(fld).tree(tree).ops(ops).create()

        // 5. Check if a method called set<FieldName> exists, if so throw an exception, else create the transform
        AddFieldSetter.builder().file(file).type(type).node(fld).tree(tree).ops(ops).create()
    }

    @Override
    void initializeConditions() {

    }
}
