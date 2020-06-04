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

import edu.isu.isuese.datamodel.Module
import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.model.ProjectModelTransform
import edu.montana.gsoc.msusel.inject.transform.source.structural.DeleteModule

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DeleteModuleModelTransform extends ProjectModelTransform {

    Module mod

    DeleteModuleModelTransform(Project proj, Module mod) {
        super(proj)
        this.mod = mod
    }

    @Override
    void verifyPreconditions() {
        // 1. mod is not null
        if (!mod)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. mod is contained in proj
        if (!proj.modules.contains(mod))
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        proj.removeModule(mod) // execute transform
        mod.thaw()
        // construct source transform
        new DeleteModule(proj, mod).execute()
    }

    @Override
    void verifyPostconditons() {
        // 1. proj no longer contains mod
        assert(!proj.modules.contains(mod))
    }
}
