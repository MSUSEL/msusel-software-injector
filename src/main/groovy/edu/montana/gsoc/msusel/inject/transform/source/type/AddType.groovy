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
package edu.montana.gsoc.msusel.inject.transform.source.type

import com.google.common.collect.Sets
import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.inject.transform.source.BasicSourceTransform
import groovy.transform.builder.Builder
import groovy.util.logging.Log4j2

/**
 * Transform which injects a new Type into a given File
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class AddType extends BasicSourceTransform {

    /**
     * Type to be injected
     */
    Type type

    List<String> textLines

    /**
     * Constructs a new CreateType transform
     * @param file the file to be modified
     * @param type the type to be added
     */
    @Builder(buildMethodName = "create")
    AddType(File file, Type type) {
        super(file)
        this.type = type
    }

    @Override
    void setup() {
        ops = new java.io.File(file.getFullPath())
        start = findTypeInsertionPoint(file)
        lines = ops.readLines().toList()
    }

    @Override
    void buildContent() {
        text = ""
        switch (type) {
            case Class:
                text = createTemplate("class")
                break
            case Enum:
                text = createTemplate("enum")
                break
            case Interface:
                text = createTemplate("interface")
                break
        }

        textLines = text.stripIndent().split("\n")
        textLines.add(0, "")

//        start = this.file.getEnd()
        lines.add(start, textLines.join("\n"))

    }

    @Override
    void injectContent() {
        ops.text = lines.join("\n")
    }

    @Override
    void updateModel() {
        int length = textLines.size()
        end = start + length
        type.setStart(start + 8)
        type.setEnd(end)
        file.setEnd(file.getEnd() + length)

        updateAllFollowing(type.getStart() + 1, length)
        type.refresh()
        file.refresh()
    }

    /**
     * Finds the insertion point in the file for the new type
     * @return line number at which the new type is to be inserted
     */
    private int findTypeInsertionPoint(File file) {
        return file.getEnd()
    }

    private String createTemplate(String kind) {
        String name = type.name

        """\
        /**
        ${typeComment()}
         */
        ${access()}${modifiers()}$kind ${name} {

        }
        """
    }

    private String typeComment() {
        """ * Generated Type
         *
         * @author Isaac Griffith
         * @version 1.0"""
    }

    private String access() {
        String access = type.accessibility.toString().toLowerCase().replaceAll(/_/, " ") + " "

        if (access == " ")
            access = ""
        access
    }

    private String modifiers() {
        String content = ""

        if (type.modifiers) {
            content += type.modifiers.collect { it.name.toLowerCase() }.join(" ")
            content += " "
        }

        content
    }
}
