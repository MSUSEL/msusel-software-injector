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
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.cond.TypeHasField
import groovy.transform.builder.Builder

/**
 * Transform to add a new Type to a given file
 * @author Isaac Griffith
 * @version 1.2.0
 */
class AddField extends AddMember {

    /**
     * Type to which the field will be added
     */
    TypeNode type
    /**
     * Field to add
     */
    FieldNode field

    /**
     * Constructs a new Add Field transform
     * @param context Current InjectorContext
     * @param file File to be modified
     * @param type Type to add field to
     * @param field Field to be added
     */
    @Builder(buildMethodName = "create")
    private AddField(InjectorContext context, FileNode file, TypeNode type, FieldNode field) {
        super(context, file)
        this.type = type
        this.field = field
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void execute() {
        // 1. find line of last field in type
        int line = findFieldInsertionPoint(type)
        // 2. add field to type
        String content = String.format("%s%s%s %s;", getAccessibility(), getModifierString(), getTypeString(), getName())
        int length = ops.inject(line, content)
        field.start = line
        field.end = line + length
        type.children << field
        // 3. update all following items by the length of the insert
        updateAllFollowing(line, length)
        // 4. check if an import is needed
        updateImports(field.type)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void initializeConditions() {
        conditions << new TypeHasField(type, field)
    }

    /**
     * @return String representation of the Accessibility
     */
    private String getAccessibility() {
        if (field.accessibility != Accessibility.DEFAULT)
            return field.getAccessibility().toString().toLowerCase()
        return ""
    }

    /**
     * @return String representation of the modifiers for the field
     */
    private String getModifierString() {
        StringBuilder builder = new StringBuilder()
        field.modifiers.each {
            builder << " "
            builder << it.toString().toLowerCase()
        }
        builder << " "

        builder.toString()
    }

    /**
     * @return String representation of the Type of the field
     */
    private String getTypeString() {
        field.type.name()
    }

    /**
     * @return Name of the field
     */
    private String getName() {
        field.name()
    }
}
