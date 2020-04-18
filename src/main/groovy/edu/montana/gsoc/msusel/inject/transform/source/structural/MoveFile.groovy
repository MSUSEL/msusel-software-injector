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
package edu.montana.gsoc.msusel.inject.transform.source.structural

import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Namespace

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MoveFile extends StructuralMoveTransform {

    Namespace from
    Namespace to
    File file

    /**
     * Constructs a new BasicSourceTransform
     * @param file the file to be modified
     */
    MoveFile(File file, Namespace from, Namespace to) {
        super()
        this.file = file
        this.from = from
        this.to = to
    }

    @Override
    void setup() {
        fromDir = new java.io.File(from.getFullPath(file.getType()))
        toDir = new java.io.File(to.getFullPath(file.getType()))
        oldFile = new java.io.File(fromDir, file.getRelPath())
        newFile = new java.io.File(toDir, file.getRelPath())
    }

    @Override
    void transformStructure() {
        oldFile.renameTo(newFile)
        oldFile.delete()

        newFile.text = newFile.text.replaceAll("package ${from.getFullName()};", "package ${to.getFullName()};")
    }
}
