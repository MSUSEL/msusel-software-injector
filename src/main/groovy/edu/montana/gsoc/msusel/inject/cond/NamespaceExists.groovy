/*
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
package edu.montana.gsoc.msusel.inject.cond

import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Project

/**
 * A condition to check whether a particular namespace already exists
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class NamespaceExists implements Condition {

    /**
     * The full name of the new namespace to be created
     */
    String fullname
    /**
     * The project
     */
    Project proj

    /**
     * Constructs a new NamespaceExists condition
     * @param context The current injector context
     * @param fullname The full name of the namespace to be constructed
     */
    NamespaceExists(Project proj, String fullname) {
        this.proj = proj
        this.fullname = fullname
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean check() {
        if (!fullname)
            throw new IllegalArgumentException("NamespaceExists.check(): fullname cannot be null or empty")
        if (!proj)
            throw new IllegalArgumentException("NamespaceExists.check(): context cannot be null")

        Namespace ns = proj.getNamespaces().find {
            it.getFullName() == fullname
        }

        ns != null
    }
}
