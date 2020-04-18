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
package edu.montana.gsoc.msusel.inject.transform.model.project

import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.model.ProjectModelTransform
import edu.montana.gsoc.msusel.inject.transform.source.structural.RenameProject

class RenameProjectModelTransform extends ProjectModelTransform {

    String name
    String parentKey

    RenameProjectModelTransform(Project proj, String name, String parentKey) {
        super(proj)
        this.name = name
        this.parentKey = parentKey
    }

    @Override
    void verifyPreconditions() {
        // 1. name is not null or empty
        if (!name)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. project does not already have the same name
        if (proj.name == name)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. parentKey is not null or empty
        if (!parentKey)
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        String oldName = proj.getFullPath()
        proj.setName(name) // make change
        proj.updateKeys(parentKey)
        new RenameProject(proj, oldName).execute()
    }

    @Override
    void verifyPostconditons() {
        // 1. project name is now new name
        assert(proj.name == name)
        // 2. project key now contains new name
        assert(proj.projectKey.endsWith(":${name}-${proj.version}"))
    }
}
