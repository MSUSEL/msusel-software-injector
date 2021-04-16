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

import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Module
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.Type
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

        Project copy = copyModelComponents(proj)
        updateSecondaryLinks(proj, copy)
        copyPhysicalFiles(proj, copy)

        return copy
    }

    private Project copyModelComponents(Project original) {
        // 1. for each component starting with Project recurse down the tree to copy the item calling the copy method
        String newKey = "${original.getName()}_copy"
        String newRelPath = "${original.getRelPath()}_copy"
        Project copy = original.copy(newKey, newRelPath)

        original.getParentSystem().addProject(copy)
        original.getParentSystem().updateKeys()

        copy.refresh()

        return copy
    }

    private def updateSecondaryLinks(Project original, Project copy) {
        updateModule2NsLinks(original, copy)
        updateNs2NsLinks(original, copy)
        updateFile2TypeLinks(original, copy)
        updateNs2FileLinks(original, copy)
    }

    private def updateModule2NsLinks(Project original, Project copy) {
        original.getModules().each { mod ->
            Module parent = copy.findModule(mod.name)
            mod.getNamespaces().each {
                Namespace copiedNamespace = copy.findNamespace(it.name)
                parent.addNamespace(copiedNamespace)
            }
        }
    }

    private def updateNs2NsLinks(Project original, Project copy) {
        original.getNamespaces().each {ns ->
            Namespace parent = copy.findNamespace(ns.name)
            ns.getNamespaces().each {
                Namespace copiedNamespace = copy.findNamespace(it.name)
                parent.addNamespace(copiedNamespace)
            }
        }
    }

    private def updateNs2FileLinks(Project original, Project copy) {
        original.getNamespaces().each { ns->
            Namespace parent = copy.findNamespace(ns.name)
            ns.getFiles().each {
                Namespace copiedNs = copy.findNamespace(it.getParentNamespace().name)
                File copiedFile = copiedNs.getFileByName(it.name)
                parent.addFile(copiedFile)
            }
        }
    }

    private def updateFile2TypeLinks(Project original, Project copy) {
        original.getFiles().each {file ->
            File parent = copy.getFileByName(file.name)
            file.getAllTypes().each {
                Namespace copiedNs = original.findNamespace(it.getParentNamespace().name)
                Type copiedType = copiedNs.getTypeByName(it.name)
                parent.addType(copiedType)
            }
        }
    }

    private def copyPhysicalFiles(Project original, Project copy) {
        // 2. copy directory contents over
        java.io.File src = new java.io.File(original.getFullPath())
        java.io.File dest = new java.io.File(copy.getFullPath())
        FileUtils.copyDirectory(src, dest)
    }
}
