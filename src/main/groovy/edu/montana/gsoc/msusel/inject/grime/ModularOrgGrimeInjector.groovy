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
import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.inject.InjectionFailedException
import edu.montana.gsoc.msusel.inject.transform.model.module.AddNamespaceToModuleModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.namespace.SplitNamespaceModelTransform
import groovy.transform.builder.Builder
import org.apache.commons.lang3.tuple.Pair

/**
 * Injector strategy for Modular type Organizational Grime
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ModularOrgGrimeInjector extends OrgGrimeInjector {

    /**
     * Flag indicating persistent (true) or temporary (false) org grime
     */
    protected boolean persistent
    /**
     * Flag indicating internal (true) or external (false) org grime
     */
    protected boolean internal
    /**
     * Flag indicating cyclical (true) or unstable (false) org grime
     */
    protected boolean cyclical

    Type src
    Type dest

    /**
     * Constructs a new ModularOrgGrime Injector for the given pattern and parameterized by the provided flags
     * @param pattern Pattern to inject grime into
     * @param persistent Flag indicating persistent (true) or temporary (false) org grime
     * @param internal Flag indicating internal (true) or external (false) org grime
     * @param cyclical Flag indicating cyclical (true) or unstable (false) org grime
     */
    @Builder(buildMethodName = "create")
    ModularOrgGrimeInjector(PatternInstance pattern, boolean persistent, boolean internal, boolean cyclical) {
        super(pattern)
        this.persistent = persistent
        this.internal = internal
        this.cyclical = cyclical
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void inject() {
        List<Namespace> pkgs = findPatternNamespaces()
        Namespace ns1, ns2
        RelationType rel

        if (internal) {
            if (pkgs.size() > 1) {
                (ns1, ns2) = selectNamespaces(pkgs)
            } else {
                ns1 = selectNamespace(pkgs)
                (ns1, ns2) = splitNamespace(ns1, true)
            }
        } else {
            ns1 = selectNamespace(pkgs)
            ns2 = selectOrCreateExternNamespace()
        }

        rel = selectRelationship(ns1, ns2, persistent)

        if (cyclical) {
            createCyclicalDependency(ns1, ns2, rel)
        } else {
            addInstability(ns1, ns2, rel)
        }
    }

    /**
     * Adds an instability to the given pair of namespaces using a relationship of the given type
     * @param srcNs Source namespace from which a class is selected as the source of the relationship to be created
     * @param destNs Destination namespace from which a class is selected as the destination of the relationship to be created
     * @param rel type of relationship to generate
     */
    def addInstability(Namespace srcNs, Namespace destNs, RelationType rel) {
        // increase number of abstract classes in srcNs

        // add an outgoing relationship from srcNs to destNs
        createRelationship(rel, src, dest)
    }

    /**
     * Adds a cycle to the given pair of namespaces using a relationship of the given type
     * @param srcNs Source namespace from which a class is selected as the source of the relationship to be created
     * @param destNs Destination namespace from which a class is selected as the destination of the relationship to be created
     * @param rel type of relationship to generate
     */
    def createCyclicalDependency(Namespace srcNs, Namespace destNs, RelationType rel) {
        List<Pair<Type, Type>> pairs = findTypePairs(destNs, srcNs, dest, src)
        Pair<Type, Type> pair = pairs[rand.nextInt(pairs.size())]

        if (!hasRelationship(srcNs, destNs)) {
            createRelationship(rel, src, dest)
        }
        if (!hasRelationship(destNs, srcNs)) {
            createRelationship(rel, pair.left, pair.right)
        }
    }

    static boolean hasRelationship(Namespace srcNs, Namespace destNs) {
        if (!srcNs)
            throw new InjectionFailedException()
        if (!destNs)
            throw new InjectionFailedException()

        List<Type> srcTypes = []
        def destTypes = []

        srcNs.getFiles().each {
            srcTypes += it.getAllTypes()
        }
        destNs.getFiles().each {
            destTypes += it.getAllTypes()
        }

        for (Type t : srcTypes) {
            for (Type o : destTypes) {
                if (t.getAssociatedTo().contains(o) || t.getRealizes().contains(o) || t.getUseTo().contains(o) || t.getGeneralizedBy().contains(o))
                    return true
            }
        }

        return false
    }

    private static def findTypePairs(Namespace srcNs, Namespace destNs, Type excludedSrc = null, Type excludedDest = null) {
        List<Pair<Type, Type>> pairs = []

        for (Type src : srcNs.getAllTypes()) {
            if (!excludedSrc || src != excludedSrc) {
                for (Type dest : destNs.getAllTypes()) {
                    if (!excludedDest || dest != excludedDest) {
                        if (!src.isAssociatedTo(dest) && !src.isGeneralizedBy(dest) && !src.isRealizing(dest)) {
                            pairs << Pair.of(src, dest)
                        }
                    }
                }
            }
        }

        pairs
    }

    def selectRelationship(Namespace srcNs, Namespace destNs, boolean persistent) {
        src = selectType(srcNs)
        dest = selectType(destNs)

        if (persistent) {
            selectPersistentRel(src, dest)
        } else {
            selectTemporaryRel(src, dest)
        }
    }

    static def selectType(Namespace namespace) {
        if (!namespace)
            throw new InjectionFailedException()

        List<Type> types = []
        namespace.getFiles().each {
            types += it.getAllTypes()
        }

        Collections.shuffle(types)
        types[0]
    }

    /**
     * Selects or creates an external namespace for use by the injector
     * @return the namespace selected or created
     */
    Namespace selectOrCreateExternNamespace() {
        Project project = pattern.getParentProject()
        List<Namespace> namespaces = project.getNamespaces()
        List<Namespace> patternNS = findPatternNamespaces()

        namespaces.removeAll(patternNS)

        namespaces = namespaces.findAll {
            it.getAllTypes().size() > 0
        }

        if (!namespaces.isEmpty()) {
            Collections.shuffle(namespaces)
            namespaces.first()
        } else {
            Module mod = project.getModules().first()
            AddNamespaceToModuleModelTransform trans = new AddNamespaceToModuleModelTransform(mod, "genexternns")
            trans.execute()
            trans.ns
        }
    }

    /**
     * Splits a given namespace into two or more namespaces
     * @param namespace Namespace to be split
     * @return a pair of namespaces split from the provided one
     */
    @Override
    RelationType selectRelationship(Type src, Type dest, boolean persistent) {
        return super.selectRelationship(src, dest, persistent)
    }

    def splitNamespace(Namespace namespace, boolean onPatternBoundary) {
        if (!namespace)
            throw new InjectionFailedException()

        List<File> left = []
        List<File> right = []

        if (onPatternBoundary) {
            List<Type> patternTypes = pattern.getTypes()
            namespace.getAllTypes().each {
                if (patternTypes.contains(it))
                    left << it.getParentFile()
                else
                    right << it.getParentFile()
            }
        } else {
            List<File> files = Lists.newArrayList(namespace.getFiles())
            Collections.shuffle(files)
            for (int i = 0; i < files.size() / 2; i++) {
                left << files[i]
            }

            for (int i = files.size() / 2; i < files.size(); i++) {
                right << files[i]
            }
        }

        SplitNamespaceModelTransform trans = SplitNamespaceModelTransform.builder().ns(namespace).left(left).right(right).create()
        trans.execute()
        [trans.ns1, trans.ns2]
    }

    /**
     * Selects a random namespace from the list provided
     * @param namespaces list from which the selection is to occur
     * @return A selected namespace
     */
    static Namespace selectNamespace(List<Namespace> namespaces) {
        if (namespaces == null)
            throw new InjectionFailedException()

        if (namespaces.size() >= 1) {
            Collections.shuffle(namespaces)
            return namespaces.first()
        }
        null
    }

    /**
     * Selects multiple namespaces from the list provided
     * @param namespaces list of namespaces from which a selection is to occur
     * @return a collection of selected namespaces
     */
    static def selectNamespaces(List<Namespace> namespaces) {
        if (namespaces == null)
            throw new InjectionFailedException()

        if (namespaces.size() > 1) {
            Collections.shuffle(namespaces)
            return [namespaces[0], namespaces[1]]
        }
        []
    }

    /**
     * Finds those namespaces in the codetree which are pattern namespaces
     * @return List of pattern namespaces
     */
    List<Namespace> findPatternNamespaces() {
        Set<Namespace> namespaces = [].toSet()
        pattern.getTypes().each {
            namespaces.add(it.getParentNamespace())
        }
        namespaces.toList()
    }
}
