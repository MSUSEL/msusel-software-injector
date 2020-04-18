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
package edu.montana.gsoc.msusel.inject.transform.model.system

import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.System
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.model.SystemModelTransform
import edu.montana.gsoc.msusel.inject.transform.source.structural.AddProject

class AddProjectModelTransform extends SystemModelTransform {

    String name
    String version
    Project proj

    AddProjectModelTransform(System system, String name, String version) {
        super(system)
        this.name = name
        this.version = version
    }

    @Override
    void verifyPreconditions() {
        // 1. name is not null or empty
        if (!name)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. version is not null or empty
        if (!version)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. sys does not contain a project with the same name and version
        if (sys.getProjects().find { it.name == name && it.version == version })
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute transform
        proj = Project.builder().name(name).projKey(name).version(version).relPath(name).create()
        sys.addProject(proj)
        proj.updateKeys(sys.getKey())
        // Generate source transform
        new AddProject(proj, sys).execute()
    }

    @Override
    void verifyPostconditons() {
        // 1. sys now contains proj
        assert(sys.getProjects().contains(proj))
        // 2. proj parent is sys
        assert(proj.parent(System.class) == sys)
    }
}
