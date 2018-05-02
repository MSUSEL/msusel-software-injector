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

import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.type.ClassNode
import edu.montana.gsoc.msusel.codetree.node.type.EnumNode
import edu.montana.gsoc.msusel.codetree.node.type.InterfaceNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode

class AddType extends AbstractSourceTransform {

    TypeNode type

    AddType(FileNode file, TypeNode node) {
        super(file)
        this.type = type
    }

    @Override
    def execute() {
        StringBuilder builder = new StringBuilder()

        builder << accessibilityString()
        builder << "${modifierString()} "
        builder << "${typeString()} "
        builder << nameString()
        builder << generalizes()
        builder << realizes()
        builder << " {\n"
        builder << "" // type body
        builder << "}"

        String content = builder.toString()

        ops = new FileOperations()
        ops.open(file)
        int start = ops.injectAtEnd(builder.toString())
        ops.save()

        int end = start + content.split(/\n/).length
        type.start = start
        type.end = end
        file.addChild(type)

        ""
    }

    String accessibilityString() {
        type.accessibility.toString().toLowerCase()
    }

    String modifierString() {
        StringBuilder builder = new StringBuilder()
        type.modifiers.each {
            builder << it.toString().toLowerCase()
            if (type.modifiers.last() != it)
                builder << " "
        }

        builder.toString()
    }

    String typeString() {
        if (type instanceof ClassNode)
            return "class"
        if (type instanceof InterfaceNode)
            return "interface"
        if (type instanceof EnumNode)
            return "enum"

        ""
    }

    String nameString() {
        type.name()
    }

    String generalizes() {
        def gen = tree?.getGeneralizedFrom(type)
        if (!gen?.empty)
            " extends " + gen[0].name()
        else
            ""
    }

    String realizes() {
        def real = tree?.getRealizedFrom(type)
        StringBuilder builder = new StringBuilder()
        if (!real?.empty) {
            builder << " implements "
            real.each {
                builder << it.name()
                if (real.last() != it)
                    builder << ", "
            }
            builder.toString()
        } else {
            ""
        }
    }
}
