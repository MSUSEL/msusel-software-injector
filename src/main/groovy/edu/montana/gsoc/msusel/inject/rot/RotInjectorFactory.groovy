/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
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
package edu.montana.gsoc.msusel.inject.rot

import edu.isu.isuese.datamodel.Pattern
import edu.montana.gsoc.msusel.inject.NullInjector
import edu.montana.gsoc.msusel.inject.SourceInjector

/**
 * Factory used to construct Design Pattern Rot injectors
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
class RotInjectorFactory {

    /**
     * Factory method for creating rot injectors
     * @param type String representation of the type, found in RotTypes
     * @param pattern Pattern instance
     * @return a RotInjector corresponding to the provided type, or a NullInjector if the type was unknown
     */
    SourceInjector create(String type, Pattern pattern) {
        switch (type) {
            case RotTypes.BLOB:
                return createBlobRole(pattern)
            case RotTypes.INAP_DEP:
                return createInappropriateDep(pattern)
            case RotTypes.INAP_INV:
                return createInappropriateInv(pattern)
            case RotTypes.MISSING:
                return createMissingRole(pattern)
            default:
                return createNullInjector()
        }
    }

    /**
     * @return New instance of a NullInjector
     */
    private def createNullInjector() {
        return new NullInjector()
    }

    /**
     * Constructs a new BlobRole rot Injector for the given pattern instance
     * @param pattern Pattern instance to inject rot into
     * @return The RotInjector
     * @throws IllegalArgumentException if the provided pattern is null
     */
    private RotInjector createBlobRole(Pattern pattern) {
        if (!pattern)
            throw new IllegalArgumentException("Pattern instance may not be null")
        BlobRoleInjector.builder().patten(pattern).create()
    }

    /**
     * Constructs a new BlobRole rot Injector for the given pattern instance
     * @param pattern Pattern instance to inject rot into
     * @return The RotInjector
     * @throws IllegalArgumentException if the provided pattern is null
     */
    private RotInjector createInappropriateDep(Pattern pattern) {
        if (!pattern)
            throw new IllegalArgumentException("Pattern instance may not be null")
        InappropriateDependencyInjector.builder().pattern(pattern).create()
    }

    /**
     * Constructs a new BlobRole rot Injector for the given pattern instance
     * @param pattern Pattern instance to inject rot into
     * @return The RotInjector
     * @throws IllegalArgumentException if the provided pattern is null
     */
    private RotInjector createInappropriateInv(Pattern pattern) {
        if (!pattern)
            throw new IllegalArgumentException("Pattern instance may not be null")
        InappropriateInversionInjector.builder().patten(pattern).create()
    }

    /**
     * Constructs a new BlobRole rot Injector for the given pattern instance
     * @param pattern Pattern instance to inject rot into
     * @return The RotInjector
     * @throws IllegalArgumentException if the provided pattern is null
     */
    private RotInjector createMissingRole(Pattern pattern) {
        if (!pattern)
            throw new IllegalArgumentException("Pattern instance may not be null")
        MissingRoleInjector.builder().patten(pattern).create()
    }
}
