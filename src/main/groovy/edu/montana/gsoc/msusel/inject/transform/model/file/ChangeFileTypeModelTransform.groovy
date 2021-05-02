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
package edu.montana.gsoc.msusel.inject.transform.model.file

import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.FileType
import edu.montana.gsoc.msusel.inject.transform.model.FileModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ChangeFileTypeModelTransform extends FileModelTransform {

    FileType type

    ChangeFileTypeModelTransform(File file, FileType type) {
        super(file)
        this.type = type
    }

    @Override
    void verifyPreconditions() {
        // 1. type is not null
        if (!type)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. file does not currently have same type as type
        if (file.type == type)
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        file.setType(type)
    }

    @Override
    void verifyPostconditions() {
        // 1. file's type is type
        assert(file.getType() == type)
    }
}
