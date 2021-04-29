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
package edu.montana.gsoc.msusel.inject.grime

import com.google.common.collect.Lists
import edu.isu.isuese.datamodel.Finding
import edu.isu.isuese.datamodel.PatternInstance
import edu.isu.isuese.datamodel.Type
import groovy.transform.builder.Builder

/**
 * A source injector to inject modular grime into a design pattern instance
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ModularGrimeInjector extends GrimeInjector {

    /**
     * Flag indicating whether the grime to inject is external (true), or internal (false).
     */
    protected boolean external
    /**
     * Flag indicating whether the grime to inject is efferent (true), or afferent (false).
     */
    protected boolean efferent
    /**
     * Flag indicating whether the grime to inject is persistent (true), or temporary (false).
     */
    protected boolean persistent

    /**
     * Constructs a new ModularGrimeInjector for the given pattern, and parameterized by the three Boolean flags
     * @param pattern Pattern instance into which the grime is to be injected
     * @param persistent flag indicating whether the injected grime is persistent (true), or temporary (false)
     * @param external flag indicating whether the injected grime is external (true), or internal (false)
     * @param efferent flag indicating whether the injected grime is efferent (true), or afferent (false)
     */
    @Builder(buildMethodName = "create")
    ModularGrimeInjector(PatternInstance pattern, boolean persistent, boolean external, boolean efferent) {
        super(pattern)
        this.persistent = persistent
        this.external = external
        this.efferent = efferent
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void inject() {
        Type src = null
        Type dest = null
        RelationType rel = null

        do {
            while (!src && !dest) {
                if (external) {
                    if (efferent) {
                        src = selectExternClass()
                        dest = selectPatternClass()
                    } else {
                        src = selectPatternClass()
                        dest = selectExternClass()
                    }
                } else {
                    (src, dest) = select2PatternClasses()
                }

                rel = selectRelationship(src, dest, persistent)

                if (!rel)
                    src = dest = null
            }
        } while (affectedEntities.contains(src.getCompKey()))

        createRelationship(rel, src, dest)
        createFinding(persistent, external, efferent, src)
    }

    void createFinding(boolean persistent, boolean external, boolean efferent, Type type) {
        affectedEntities << type.getCompKey()
        if (persistent) {
            if (external) {
                if (efferent) {
                    Finding.of(GrimeInjectorConstants.grimeTypes["PEEG"]).injected().on(type)
                } else {
                    Finding.of(GrimeInjectorConstants.grimeTypes["PEAG"]).injected().on(type)
                }
            } else {
                Finding.of(GrimeInjectorConstants.grimeTypes["PIG"]).injected().on(type)
            }
        } else {
            if (external) {
                if (efferent) {
                    Finding.of(GrimeInjectorConstants.grimeTypes["TEEG"]).injected().on(type)
                } else {
                    Finding.of(GrimeInjectorConstants.grimeTypes["TEAG"]).injected().on(type)
                }
            } else {
                Finding.of(GrimeInjectorConstants.grimeTypes["TIG"]).injected().on(type)
            }
        }
    }

    def select2PatternClasses() {
        def types = Lists.newArrayList(pattern.getTypes())
        Collections.shuffle(types)

        if (types.size() >= 2)
            return [types[0], types[1]]
        else
            return [types[0], types[0]]
    }

    /**
     * Selects a type external to the pattern definition
     * @return the Type selected
     */
    Type selectExternClass() {
        if (!pattern.getParentProjects().isEmpty()) {
            List<Type> types = Lists.newArrayList(pattern.getParentProjects().first().getAllTypes())
            types.removeAll(pattern.getTypes())

            return types[rand.nextInt(types.size())]
        }
        return null
    }
}
