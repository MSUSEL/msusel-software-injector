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
import edu.montana.gsoc.msusel.inject.transform.source.structural.DeleteProject

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DeleteProjectModelTransform extends SystemModelTransform {

    Project proj

    DeleteProjectModelTransform(System system, Project proj) {
        super(system)
        this.proj = proj
    }

    @Override
    void verifyPreconditions() {
        // 1. proj is not null
        if (!proj)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. sys contains project
        if (!sys.getProjects().contains(proj))
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute transform
        sys.removeProject(proj)
        proj.thaw()
        // Generate source transform
        new DeleteProject(sys, proj).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. sys no longer contains project
        assert(!sys.getProjects().contains(proj))
    }
}
