/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2018 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
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

import edu.montana.gsoc.msusel.inject.NullInjector

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
@Singleton
class GrimeInjectorFactory {

    def createClassGrimeInjector(String type) {
        switch (type) {
            case ClassGrimeTypes.DEPG:
                return createDEPGInjector()
            case ClassGrimeTypes.DESG:
                return createDESGInjector()
            case ClassGrimeTypes.DIPG:
                return createDIPGInjector()
            case ClassGrimeTypes.DISG:
                return createDISGInjector()
            case ClassGrimeTypes.IEPG:
                return createIEPGInjector()
            case ClassGrimeTypes.IESG:
                return createIESGInjector()
            case ClassGrimeTypes.IIPG:
                return createIIPGInjector()
            case ClassGrimeTypes.IISG:
                return createIISGInjector()
            default:
                return createNullInjector()
        }
    }

    private def createNullInjector() {
        return new NullInjector(null, null, null, null)
    }

    private def createDEPGInjector() {
        return ClassGrimeInjector.builder().direct(true).internal(false).pair(true).create()
    }

    private def createDESGInjector() {
        return ClassGrimeInjector.builder().direct(true).internal(false).pair(false).create()
    }

    private def createDIPGInjector() {
        return ClassGrimeInjector.builder().direct(true).internal(true).pair(true).create()
    }

    private def createDISGInjector() {
        return ClassGrimeInjector.builder().direct(true).internal(true).pair(false).create()
    }

    private def createIEPGInjector() {
        return ClassGrimeInjector.builder().direct(false).internal(false).pair(true).create()
    }

    private def createIESGInjector() {
        return ClassGrimeInjector.builder().direct(false).internal(false).pair(false).create()
    }

    private def createIIPGInjector() {
        return ClassGrimeInjector.builder().direct(false).internal(true).pair(true).create()
    }

    private def createIISGInjector() {
        return ClassGrimeInjector.builder().direct(false).internal(true).pair(false).create()
    }

    def createModGrimeInjector(String type) {
        switch (type) {
            case ModularGrimeTypes.PIG:
                return createPIGInjector()
            case ModularGrimeTypes.TIG:
                return createTIGInjector()
            case ModularGrimeTypes.PEEG:
                return createPEEGInjector()
            case ModularGrimeTypes.PEAG:
                return createPEAGInjector()
            case ModularGrimeTypes.TEEG:
                return createTEEGInjector()
            case ModularGrimeTypes.TEAG:
                return createTEAGInjector()
            default:
                return createNullInjector()
        }
    }

    private def createPIGInjector() {
        return ModularGrimeInjector.builder().persistent(true).external(false).create()
    }

    private def createTIGInjector() {
        return ModularGrimeInjector.builder().persistent(false).external(false).create()
    }

    private def createPEEGInjector() {
        return ModularGrimeInjector.builder().persistent(true).external(true).efferent(true).create()
    }

    private def createPEAGInjector() {
        return ModularGrimeInjector.builder().persistent(true).external(true).efferent(false).create()
    }

    private def createTEEGInjector() {
        return ModularGrimeInjector.builder().persistent(false).external(true).efferent(true).create()
    }

    private def createTEAGInjector() {
        return ModularGrimeInjector.builder().persistent(false).external(true).efferent(false).create()
    }

    def createOrgGrimeInjector(String type) {
        switch (type) {
            case OrgGrimeTypes.MPECG:
                return createMPECGInjector()
            case OrgGrimeTypes.MPEUG:
                return createMPEUGInjector()
            case OrgGrimeTypes.MPICG:
                return createMPICGInjector()
            case OrgGrimeTypes.MPIUG:
                return createMPIUGInjector()
            case OrgGrimeTypes.MTECG:
                return createMTECGInjector()
            case OrgGrimeTypes.MTEUG:
                return createMTEUGInjector()
            case OrgGrimeTypes.MTICG:
                return createMTICGInjector()
            case OrgGrimeTypes.MTIUG:
                return createMTIUGInjector()
            case OrgGrimeTypes.PECG:
                return createPECGInjector()
            case OrgGrimeTypes.PERG:
                return createPERGInjector()
            case OrgGrimeTypes.PICG:
                return createPICGInjector()
            case OrgGrimeTypes.PIRG:
                return createPIRGInjector()
            default:
                return createNullInjector()
        }
    }

    def createMPECGInjector() {

    }

    def createMPEUGInjector() {

    }

    def createMPICGInjector() {

    }

    def createMPIUGInjector() {

    }

    def createMTECGInjector() {

    }

    def createMTEUGInjector() {

    }

    def createMTICGInjector() {

    }

    def createMTIUGInjector() {

    }

    def createPECGInjector() {

    }

    def createPERGInjector() {

    }

    def createPICGInjector() {

    }

    def createPIRGInjector() {

    }
}
