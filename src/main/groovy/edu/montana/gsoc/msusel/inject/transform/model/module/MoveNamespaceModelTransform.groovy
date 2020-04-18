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
package edu.montana.gsoc.msusel.inject.transform.model.module

import edu.isu.isuese.datamodel.Module
import edu.isu.isuese.datamodel.Namespace
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.model.ModuleModelTransform
import edu.montana.gsoc.msusel.inject.transform.source.structural.MoveNamespace

class MoveNamespaceModelTransform extends ModuleModelTransform {

    Namespace ns
    def newParent

    MoveNamespaceModelTransform(Module mod, Namespace ns, newParent) {
        super(mod)
        this.ns = ns
        this.newParent = newParent
    }

    @Override
    void verifyPreconditions() {
        // 1. ns is not null
        if (!ns)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. newParent is not null
        if (!newParent)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. ns is contained in mod
        if (!mod.namespaces.contains(ns))
            throw new ModelTransformPreconditionsNotMetException()
        // 4. newParent does not contain an equivalent namespace
        if (newParent.namespaces.find { it.name == ns.name })
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // execute transform
        mod.removeNamespace(ns)
        ns.thaw()
        newParent.addNamespace(ns)
        ns.updateKey()
        // create source transform
        new MoveNamespace(ns, mod, newParent).execute()
    }

    @Override
    void verifyPostconditons() {
        // 1. ns no longer is contained by mod
        assert(!mod.namespaces.contains(ns))
        // 2. newParent now contains ns
        assert(newParent.namespaces.contains(ns))
        // 3. ns' is newParent
        assert(ns.parent(Module.class) == newParent)
    }
}
