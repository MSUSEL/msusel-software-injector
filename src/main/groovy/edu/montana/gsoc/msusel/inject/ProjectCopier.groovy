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
package edu.montana.gsoc.msusel.inject

import edu.isu.isuese.datamodel.Project
import org.apache.commons.io.FileUtils

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ProjectCopier {

    /**
     * Creates a duplicate of the provided project both in the database and in the physical directory structure.
     * @param proj Project to be copied
     */
    Project execute(Project proj) {
        if (!proj)
            throw new InjectionFailedException()

        // 1. for each component starting with Project recurse down the tree to copy the item calling the copy method
        String newKey = "${proj.getName()}_copy"
        String newRelPath = "${proj.getRelPath()}_copy"
        Project copy = proj.copy(newKey, newRelPath)

        proj.getParentSystem().addProject(copy)
        proj.getParentSystem().updateKeys()

        copy.refresh()

        // 2. copy directory contents over
        File src = new File(proj.getFullPath())
        File dest = new File(copy.getFullPath())
        FileUtils.copyDirectory(src, dest)

        return copy
    }
}
