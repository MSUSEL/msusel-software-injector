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
package edu.montana.gsoc.msusel.inject.transform.model.type

import edu.isu.isuese.datamodel.Modifier
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.transform.source.type.DeleteTypeModifier
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.model.TypeModelTransform

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DeleteTypeModifierModelTransform extends TypeModelTransform {

    Modifier mod

    DeleteTypeModifierModelTransform(Type type, Modifier mod) {
        super(type)
        this.mod = mod
    }

    @Override
    void verifyPreconditions() {
        // 1. mod is not null
        if (!mod)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. type has modifier mod
        if (!type.getModifiers().contains(mod))
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        type.removeModifier(mod)
        // Generate Source Transform
        new DeleteTypeModifier(type.getParentFile(), type, mod).execute()
    }

    @Override
    void verifyPostconditons() {
        // 1. type no longer has modifier mod
        type.refresh()
        assert(!type.getModifiers().contains(mod))
    }
}
