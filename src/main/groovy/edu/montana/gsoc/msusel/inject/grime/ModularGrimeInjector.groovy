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
package edu.montana.gsoc.msusel.inject.grime

import com.google.common.collect.Lists
import edu.isu.isuese.datamodel.Finding
import edu.isu.isuese.datamodel.PatternInstance
import edu.isu.isuese.datamodel.Type
import groovy.transform.builder.Builder
import groovy.util.logging.Log4j2

/**
 * A source injector to inject modular grime into a design pattern instance
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
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
        log.info "Starting injection"
        Type src = null
        Type dest = null
        RelationType rel = null

        (rel, src, dest) = selectComponents(src, dest, rel)

        performInjection(rel, src, dest)
        log.info "Injection Complete"
    }

    @Override
    void inject(String... params) {
        log.info "Starting Controlled Injection"

        Type src = pattern.getParentProject().findTypeByQualifiedName(params[0])
        Type dest = pattern.getParentProject().findTypeByQualifiedName(params[1])

        if (dest == null) {
            if (external && !efferent) dest = createExternClass(params[1])
            else dest = createPatternClass(params[1])
        }

        RelationType rel = selectRelationship(src, dest, persistent)
        performInjection(rel, src, dest)

        log.info "Controlled Injection Complete"
    }

    private void performInjection(RelationType rel, Type src, Type dest) {
        createRelationship(rel, src, dest)
        createFinding(persistent, external, efferent, src)
    }

    private List selectComponents(Type src, Type dest, RelationType rel) {
        while (!src && !dest) {
            if (external) {
                if (efferent) {
                    src = selectOrCreateExternClass()
                    dest = selectOrCreatePatternClass()
                } else {
                    src = selectOrCreatePatternClass()
                    dest = selectOrCreateExternClass()
                }
            } else {
                (src, dest) = selectOrCreate2PatternClasses()
            }

            rel = selectRelationship(src, dest, persistent)

            if (!rel)
                src = dest = null
        }
        [rel, src, dest]
    }

    void createFinding(boolean persistent, boolean external, boolean efferent, Type type) {
        log.info "Creating Finding"
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
        log.info "Finding Created"
    }

    def selectOrCreate2PatternClasses() {
        [selectOrCreatePatternClass(), selectOrCreatePatternClass()]
    }

    /**
     * Selects a type external to the pattern definition
     * @return the Type selected
     */
    Type selectOrCreateExternClass() {
        log.info "Selecting/Creating External Class"
        if (!pattern.getParentProjects().isEmpty()) {
            List<Type> types = Lists.newArrayList(pattern.getParentProjects().first().getAllTypes())
            types.removeAll(pattern.getTypes())

            if (types.isEmpty()) {
                return createType()
            } else
                return types[rand.nextInt(types.size())]
        }
        return null
    }

    Type createExternClass(String name) {
        createType(name)
    }

    Type createPatternClass(String name) {
        log.info "Creating Pattern Class"

        Type type = createPatternType(name)
        affectedEntities << type.getCompKey()
        type
    }
}
