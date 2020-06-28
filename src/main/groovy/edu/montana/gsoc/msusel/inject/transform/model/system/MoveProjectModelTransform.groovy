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
import edu.montana.gsoc.msusel.inject.transform.source.structural.MoveProject

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MoveProjectModelTransform extends SystemModelTransform {

    Project proj
    System newParent

    MoveProjectModelTransform(System system, Project proj, System newParent) {
        super(system)
        this.proj = proj
        this.newParent = newParent
    }

    @Override
    void verifyPreconditions() {
        // 1. proj is not null
        if (!proj)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. newParent is not null
        if (!newParent)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. sys contains proj
        if (!sys.getProjects().contains(proj))
            throw new ModelTransformPreconditionsNotMetException()
        // 4. newParent does not contain an equivalent project
        if (newParent.getProjects().find { it.name == proj.name })
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute transform
        sys.removeProject(proj)
        proj.thaw()
        newParent.addProject(proj)
        proj.updateKeys()
        // Generate source transform
        new MoveProject(proj, sys, newParent).execute()
    }

    @Override
    void verifyPostconditons() {
        // 1. sys no longer contains proj
        assert(!sys.getProjects().contains(proj))
        // 2. newParent contains proj
        assert(newParent.getProjects().contains(proj))
    }
}
