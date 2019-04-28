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
package edu.montana.gsoc.msusel.inject.grime

import edu.isu.isuese.datamodel.Pattern
import edu.montana.gsoc.msusel.inject.NullInjector

/**
 * Factory for constructing instances of Grime Injectors
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
class GrimeInjectorFactory {

    /**
     * Factory method for creating class grime injectors
     * @param type String representation of the type, found in ClassGrimeTypes
     * @param pattern Pattern instance
     * @return a ClassGrimeInjector, or a NullInjector if the type was unknown
     */
    def createClassGrimeInjector(String type, Pattern pattern) {
        switch (type) {
            case ClassGrimeTypes.DEPG:
                return createDEPGInjector(pattern)
            case ClassGrimeTypes.DESG:
                return createDESGInjector(pattern)
            case ClassGrimeTypes.DIPG:
                return createDIPGInjector(pattern)
            case ClassGrimeTypes.DISG:
                return createDISGInjector(pattern)
            case ClassGrimeTypes.IEPG:
                return createIEPGInjector(pattern)
            case ClassGrimeTypes.IESG:
                return createIESGInjector(pattern)
            case ClassGrimeTypes.IIPG:
                return createIIPGInjector(pattern)
            case ClassGrimeTypes.IISG:
                return createIISGInjector(pattern)
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
     * @param pattern Pattern to be injected with grime
     * @return A DEPG grime injector to inject grime into the provided pattern
     */
    private def createDEPGInjector(Pattern pattern) {
        return ClassGrimeInjector.builder().pattern(pattern).direct(true).internal(false).pair(true).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A DESG grime injector to inject grime into the provided pattern
     */
    private def createDESGInjector(Pattern pattern) {
        return ClassGrimeInjector.builder().pattern(pattern).direct(true).internal(false).pair(false).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A DIPG grime injector to inject grime into the provided pattern
     */
    private def createDIPGInjector(Pattern pattern) {
        return ClassGrimeInjector.builder().pattern(pattern).direct(true).internal(true).pair(true).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A DISG grime injector to inject grime into the provided pattern
     */
    private def createDISGInjector(Pattern pattern) {
        return ClassGrimeInjector.builder().pattern(pattern).direct(true).internal(true).pair(false).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A IEPG grime injector to inject grime into the provided pattern
     */
    private def createIEPGInjector(Pattern pattern) {
        return ClassGrimeInjector.builder().pattern(pattern).direct(false).internal(false).pair(true).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A IESG grime injector to inject grime into the provided pattern
     */
    private def createIESGInjector(Pattern pattern) {
        return ClassGrimeInjector.builder().pattern(pattern).direct(false).internal(false).pair(false).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A IIPG grime injector to inject grime into the provided pattern
     */
    private def createIIPGInjector(Pattern pattern) {
        return ClassGrimeInjector.builder().pattern(pattern).direct(false).internal(true).pair(true).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A IISG grime injector to inject grime into the provided pattern
     */
    private def createIISGInjector(Pattern pattern) {
        return ClassGrimeInjector.builder().pattern(pattern).direct(false).internal(true).pair(false).create()
    }

    /**
     * Factory method for creating modular grime injectors
     * @param type String representation of the type, found in ModularGrimeTypes
     * @param pattern Pattern instance
     * @return a ModularGrimeInjector, or a NullInjector if the type was unknown
     */
    def createModGrimeInjector(String type, Pattern pattern) {
        switch (type) {
            case ModularGrimeTypes.PIG:
                return createPIGInjector(pattern)
            case ModularGrimeTypes.TIG:
                return createTIGInjector(pattern)
            case ModularGrimeTypes.PEEG:
                return createPEEGInjector(pattern)
            case ModularGrimeTypes.PEAG:
                return createPEAGInjector(pattern)
            case ModularGrimeTypes.TEEG:
                return createTEEGInjector(pattern)
            case ModularGrimeTypes.TEAG:
                return createTEAGInjector(pattern)
            default:
                return createNullInjector()
        }
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A PIG grime injector to inject grime into the provided pattern
     */
    private def createPIGInjector(Pattern pattern) {
        return ModularGrimeInjector.builder().pattern(pattern).persistent(true).external(false).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A TIG grime injector to inject grime into the provided pattern
     */
    private def createTIGInjector(Pattern pattern) {
        return ModularGrimeInjector.builder().pattern(pattern).persistent(false).external(false).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A PEEG grime injector to inject grime into the provided pattern
     */
    private def createPEEGInjector(Pattern pattern) {
        return ModularGrimeInjector.builder().pattern(pattern).persistent(true).external(true).efferent(true).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A PEAG grime injector to inject grime into the provided pattern
     */
    private def createPEAGInjector(Pattern pattern) {
        return ModularGrimeInjector.builder().pattern(pattern).persistent(true).external(true).efferent(false).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A TEEG grime injector to inject grime into the provided pattern
     */
    private def createTEEGInjector(Pattern pattern) {
        return ModularGrimeInjector.builder().pattern(pattern).persistent(false).external(true).efferent(true).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A TEAG grime injector to inject grime into the provided pattern
     */
    private def createTEAGInjector(Pattern pattern) {
        return ModularGrimeInjector.builder().pattern(pattern).persistent(false).external(true).efferent(false).create()
    }

    /**
     * Factory method for creating organizational grime injectors
     * @param type String representation of the type, found in OrgGrimeTypes
     * @param pattern Pattern instance
     * @return a OrgGrimeInjector, or a NullInjector if the type was unknown
     */
    def createOrgGrimeInjector(String type, Pattern pattern) {
        switch (type) {
            case OrgGrimeTypes.MPECG:
                return createMPECGInjector(pattern)
            case OrgGrimeTypes.MPEUG:
                return createMPEUGInjector(pattern)
            case OrgGrimeTypes.MPICG:
                return createMPICGInjector(pattern)
            case OrgGrimeTypes.MPIUG:
                return createMPIUGInjector(pattern)
            case OrgGrimeTypes.MTECG:
                return createMTECGInjector(pattern)
            case OrgGrimeTypes.MTEUG:
                return createMTEUGInjector(pattern)
            case OrgGrimeTypes.MTICG:
                return createMTICGInjector(pattern)
            case OrgGrimeTypes.MTIUG:
                return createMTIUGInjector(pattern)
            case OrgGrimeTypes.PECG:
                return createPECGInjector(pattern)
            case OrgGrimeTypes.PERG:
                return createPERGInjector(pattern)
            case OrgGrimeTypes.PICG:
                return createPICGInjector(pattern)
            case OrgGrimeTypes.PIRG:
                return createPIRGInjector(pattern)
            default:
                return createNullInjector()
        }
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A MPECG grime injector to inject grime into the provided pattern
     */
    def createMPECGInjector(Pattern pattern) {
        return ModularOrgGrimeInjector.builder().pattern(pattern).persistent(true).internal(false).cyclical(true).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A MPEUG grime injector to inject grime into the provided pattern
     */
    def createMPEUGInjector(Pattern pattern) {
        return ModularOrgGrimeInjector.builder().pattern(pattern).persistent(true).internal(false).cyclical(false).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A MPICG grime injector to inject grime into the provided pattern
     */
    def createMPICGInjector(Pattern pattern) {
        return ModularOrgGrimeInjector.builder().pattern(pattern).persistent(true).internal(true).cyclical(true).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A MPIUG grime injector to inject grime into the provided pattern
     */
    def createMPIUGInjector(Pattern pattern) {
        return ModularOrgGrimeInjector.builder().pattern(pattern).persistent(true).internal(true).cyclical(false).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A MTECG grime injector to inject grime into the provided pattern
     */
    def createMTECGInjector(Pattern pattern) {
        return ModularOrgGrimeInjector.builder().pattern(pattern).persistent(false).internal(false).cyclical(true).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A MTEUG grime injector to inject grime into the provided pattern
     */
    def createMTEUGInjector(Pattern pattern) {
        return ModularOrgGrimeInjector.builder().pattern(pattern).persistent(false).internal(false).cyclical(false).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A MTICG grime injector to inject grime into the provided pattern
     */
    def createMTICGInjector(Pattern pattern) {
        return ModularOrgGrimeInjector.builder().pattern(pattern).persistent(false).internal(true).cyclical(true).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A MTIUG grime injector to inject grime into the provided pattern
     */
    def createMTIUGInjector(Pattern pattern) {
        return ModularOrgGrimeInjector.builder().pattern(pattern).persistent(false).internal(true).cyclical(false).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A PECG grime injector to inject grime into the provided pattern
     */
    def createPECGInjector(Pattern pattern) {
        return PackageOrgGrimeInjector.builder().pattern(pattern).internal(false).closure(true).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A PERG grime injector to inject grime into the provided pattern
     */
    def createPERGInjector(Pattern pattern) {
        return PackageOrgGrimeInjector.builder().pattern(pattern).internal(false).closure(false).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A PICG grime injector to inject grime into the provided pattern
     */
    def createPICGInjector(Pattern pattern) {
        return PackageOrgGrimeInjector.builder().pattern(pattern).internal(true).closure(true).create()
    }

    /**
     * @param pattern Pattern to be injected with grime
     * @return A PIRG grime injector to inject grime into the provided pattern
     */
    def createPIRGInjector(Pattern pattern) {
        return PackageOrgGrimeInjector.builder().pattern(pattern).internal(true).closure(false).create()
    }
}
