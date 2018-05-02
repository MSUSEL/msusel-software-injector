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
package edu.montana.gsoc.msusel.source

import edu.montana.gsoc.msusel.codetree.node.Accessibility
import edu.montana.gsoc.msusel.codetree.node.member.FieldNode
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode

class AddField extends AbstractSourceTransform {

    TypeNode type
    FieldNode field

    AddField(FileNode file, TypeNode type, FieldNode field) {
        super(file)
        this.type = type
        this.field = field
    }

    @Override
    def execute() {
        // 1. find line of last field in type
        int line = findFieldInsertionPoint(file, type)
        // 2. add field to type
        String content = String.format("%s%s%s %s;", getAccessibility(), getModifierString(), getTypeString(), getName())
        int length = ops.inject(line, content)
        // 3. update all following items by the length of the insert
        updateAllFollowing(line, length, file)
        // 4. check if an import is needed
        updateImports()
    }

    private String getAccessibility() {
        if (field.accessibility != Accessibility.DEFAULT)
            return field.getAccessibility().toString().toLowerCase()
        return ""
    }

    private String getModifierString() {
        StringBuilder builder = new StringBuilder()
        field.modifiers.each {
            builder << " "
            builder << it.toString().toLowerCase()
        }
        builder << " "

        builder.toString()
    }

    private String getTypeString() {
        field.type.name()
    }

    private String getName() {
        field.name()
    }

    private void updateAllFollowing() {

    }

    private void updateImports() {

    }
}
