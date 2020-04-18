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
package edu.montana.gsoc.msusel.inject.transform.source.member

import edu.isu.isuese.datamodel.Accessibility
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Member
import edu.isu.isuese.datamodel.TypedMember
import edu.montana.gsoc.msusel.inject.transform.source.BasicSourceTransform

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ChangeMemberAccessibility extends BasicSourceTransform {

    Member member
    Accessibility access

    /**
     * Constructs a new BasicSourceTransform
     * @param file the file to be modified
     */
    ChangeMemberAccessibility(File file, Member member, Accessibility access) {
        super(file)
        this.member = member
        this.access = access
    }

    @Override
    void setup() {
        ops = new java.io.File(file.getFullPath())
        lines = ops.readLines()

        start = member.getStart() - 1
        end = member.getEnd() - 1

        if (start == end)
            text = lines[start]
        else {
            text = lines[start..end].join("\n")
        }
    }

    @Override
    void buildContent() {
        if (member instanceof TypedMember) {
            TypedMember m = (TypedMember) member

            if (access != Accessibility.DEFAULT) {
                def matcher = text =~ /${m.getAccessibility().toString()}\s+(\w+\s+)*${m.getType().getTypeName()}/
                if (matcher.find()) {
                    String content = matcher[0][0]
                    def newContent = content.replace(m.getAccessibility().toString(), access.toString())
                    text = text.replace(content, newContent)
                }
            } else {
                def matcher = text =~ /(\w+\s+)*${m.getType().getTypeName()}/
                if (matcher.find()) {
                    String content = matcher[0][0]
                    text = text.replace(content, "${access.toString()} $content")
                }
            }
        }
    }

    @Override
    void injectContent() {
        def textLines = text.split("\n")
        for (int i = 0; i < textLines.size(); i++)
            lines[start + i] = textLines[i]

        ops.text = lines.join("\n")
    }

    @Override
    void updateModel() {
        member.setAccessibility(access)
    }
}
