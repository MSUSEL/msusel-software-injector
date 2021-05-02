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

import edu.isu.isuese.datamodel.Accessibility
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.transform.source.type.ChangeTypeAccessibility
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.model.TypeModelTransform

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ChangeTypeAccessibilityModelTransform extends TypeModelTransform {

    Accessibility access

    ChangeTypeAccessibilityModelTransform(Type type, Accessibility access) {
        super(type)
        this.access = access
    }

    @Override
    void verifyPreconditions() {
        // 1. access is not null
        if (!access)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. type does access is not currently access
        if (type.getAccessibility() == access)
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        Accessibility oldAccessibility = type.getAccessibility()
        type.setAccessibility(access)
        // Generate Source Transform
        new ChangeTypeAccessibility(type.getParentFile(), type, oldAccessibility).execute()
    }

    @Override
    void verifyPostconditions() {
        assert(type.getAccessibility() == access)
    }
}
