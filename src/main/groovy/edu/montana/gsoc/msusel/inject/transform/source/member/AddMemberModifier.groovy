/*
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

import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.inject.transform.source.BasicSourceTransform

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddMemberModifier extends BasicSourceTransform {

    Type type
    Member member
    Modifier mod

    /**
     * Constructs a new BasicSourceTransform
     * @param file the file to be modified
     */
    AddMemberModifier(File file, Type type, Member member, Modifier mod) {
        super(file)
        this.type = type
        this.member = member
        this.mod = mod
    }

    @Override
    void setup() {
        ops = new java.io.File(file.getFullPath())
        lines = ops.readLines()

        start = member.getStart() - 1
        end = member.getEnd() - 1

        if (start == end)
            text = lines[start]
        else
            text = lines[start..end].join("\n")
    }

    @Override
    void buildContent() {
        if (member instanceof Method) {
            Method m = (Method) member
            def matcher = text =~ /${m.getType()}\s+${member.getName()}\s*\(.*\)/
            if (matcher.find()) {
                String content = matcher[0]
                text = text.replace(content, "${mod.getName()} $content")
            }
        }
        else if (member instanceof Field) {
            Field f = (Field) member
            def matcher = text =~ /${f.getType()}\s+${member.getName()}/
            if (matcher.find()) {
                String content = matcher[0]
                text = text.replace(content, "${mod.getName()} $content")
            }
        }
    }

    @Override
    void injectContent() {
        if (start == end)
            lines[start] = text
        else {
            def textLines = text.split("\n")
            for (int i = 0; i < textLines.size(); i++) {
                lines[start + i] = textLines[i]
            }
        }

        ops.text = lines.join("\n")
    }

    @Override
    void updateModel() {}
}
