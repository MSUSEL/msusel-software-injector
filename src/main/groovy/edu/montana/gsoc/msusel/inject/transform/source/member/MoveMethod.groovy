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

import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.transform.source.AddMember

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MoveMethod extends AddMember {

    Type from
    Type to
    File toFile
    Method method

    int injectPoint
    java.io.File toOps
    List<String> toLines
    List<String> methodLines

    /**
     * Constructs a new BasicSourceTransform
     * @param file the file to be modified
     */
    MoveMethod(File file, Type from, File toFile, Type to, Method method) {
        super(file)

        this.from = from
        this.to = to
        this.method = method
        this.toFile = toFile
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setup() {
        injectPoint = findMethodInsertionPoint(to)
        ops = new java.io.File(file.getFullPath())
        toOps = new java.io.File(toFile.getFullPath())

        start = method.getStart() - 1
        end = method.getEnd() - 1

        lines = ops.readLines()
        toLines = toOps.readLines()
        methodLines = lines[start..end]
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void buildContent() {
        method.setStart(1)
        method.setEnd(1)
        from.removeMember(method)
        method.thaw()

        methodLines.each {
            lines.remove(start)
        }

        toLines.add(injectPoint, "")
        toLines.add(injectPoint + 1, methodLines.join("\n"))
        toLines = toLines.join("\n").split("\n")
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void injectContent() {
        ops.text = lines.join("\n")
        toOps.text = toLines.join("\n")
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void updateModel() {
        updateAllFollowing(toFile, injectPoint, methodLines.size())

        to.addMember(method)
        method.setStart(injectPoint + 1)
        method.setEnd(injectPoint + methodLines.size())

    }
}
