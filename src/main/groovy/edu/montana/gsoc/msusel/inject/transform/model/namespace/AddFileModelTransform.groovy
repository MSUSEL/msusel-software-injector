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
package edu.montana.gsoc.msusel.inject.transform.model.namespace

import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.FileType
import edu.isu.isuese.datamodel.Namespace
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.model.NamespaceModelTransform
import edu.montana.gsoc.msusel.inject.transform.source.structural.AddFile

import java.nio.file.Paths

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddFileModelTransform extends NamespaceModelTransform {

    String path
    String relPath
    FileType type
    File file

    AddFileModelTransform(Namespace ns, String path, FileType type) {
        super(ns)
        this.relPath = path
        this.path = Paths.get(ns.getFullPath(type, 0)).toAbsolutePath().toString() + "/" + path
        this.type = type
    }

    @Override
    void verifyPreconditions() {
        // 1. path is not null or empty
        if (!path)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. type is not null
        if (!type)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. a file with the given path does not already exist in ns
        if (ns.getFiles().find { it.name == path })
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        file = File.builder().name(path).relPath(relPath).fileKey(path).type(type).create()
        file.save()
        file.refresh()
        ns.addFile(file)
        ns.getParentProject().addFile(file)
        file.updateKey()
        // Generate Source Transform
        new AddFile(file, ns).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. ns contains file
        assert(ns.getFiles().contains(file))
        // 2. file parent is ns
        assert(file.getParentNamespace() == ns)
    }
}
