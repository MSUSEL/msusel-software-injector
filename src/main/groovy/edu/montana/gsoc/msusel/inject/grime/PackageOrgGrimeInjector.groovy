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

import com.google.common.graph.GraphBuilder
import com.google.common.graph.MutableGraph
import edu.isu.isuese.datamodel.Accessibility
import edu.isu.isuese.datamodel.FileType
import edu.isu.isuese.datamodel.Finding
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.PatternInstance
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.InjectionFailedException
import edu.montana.gsoc.msusel.inject.transform.model.file.AddTypeModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.namespace.AddFileModelTransform
import groovy.transform.builder.Builder

/**
 * Injector strategy for Package type Organizational Grime
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class PackageOrgGrimeInjector extends OrgGrimeInjector {

    /**
     * Flag indicating internal (true), or external (false) grime
     */
    protected boolean internal
    /**
     * Flag indicating closure (true), or reuse (false) grime
     */
    protected boolean closure

    /**
     * Constructs a new Package type OrgGrime injector for the given pattern instance, parameterized by the provided flags
     * @param pattern Pattern instance to be injected with grime
     * @param internal Flag indicating internal (true), or external (false) grime
     * @param closure Flag indicating closure (true), or reuse (false) grime
     */
    @Builder(buildMethodName = "create")
    PackageOrgGrimeInjector(PatternInstance pattern, boolean internal, boolean closure) {
        super(pattern)
        this.internal = internal
        this.closure = closure
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void inject() {
        Namespace pkg = selectPatternNamespace()
        Namespace other
        Type type, dest

        if (internal) {
            type = selectInternalClass(pkg)
        } else {
            type = selectOrCreateExternalClass(pkg)
        }

        MutableGraph<Namespace> graph = createGraph(pkg.getParentProject())

        if (closure) {
            other = selectUnreachableNamespace(graph, pkg)
        } else {
            other = selectReachableNamespace(graph, pkg)
        }
        dest = selectExternalClass(other)

        if (other && dest) {
            RelationType rel = selectRelationship(type, dest, rand.nextBoolean())
            createRelationship(rel, type, dest)
        }

        createFinding(internal, closure, type)
    }

    void createFinding(boolean internal, boolean closure, Type type) {
        if (internal) {
            if (closure) {
                Finding.of(GrimeInjectorConstants.grimeTypes["PICG"]).injected().on(type)
            } else {
                Finding.of(GrimeInjectorConstants.grimeTypes["PIRG"]).injected().on(type)
            }
        } else {
            if (closure) {
                Finding.of(GrimeInjectorConstants.grimeTypes["PECG"]).injected().on(type)
            } else {
                Finding.of(GrimeInjectorConstants.grimeTypes["PERG"]).injected().on(type)
            }
        }
    }

    Namespace selectPatternNamespace() {
        Set<Namespace> spaces = [].toSet()
        pattern.getTypes().each {
            spaces.add(it.getParentNamespace())
        }

        List<Namespace> selectFrom = spaces.toList()
        if (!selectFrom.isEmpty()) {
            Collections.shuffle(selectFrom)
            return selectFrom.first()
        }
        return null
    }

    /**
     * Selects a class within the given namespace, but external to the pattern instance
     * @param ns Namespace
     * @return An external to the pattern instance type
     */
    Type selectExternalClass(Namespace ns) {
        if (!ns)
            throw new InjectionFailedException()

        List<Type> patternTypes = pattern.getTypes()
        List<Type> nsTypes = []
        ns.getFiles().each {
            nsTypes += it.getAllTypes()
        }

        nsTypes.removeAll(patternTypes)

        if (nsTypes.isEmpty()) {
            null
        } else {
            Collections.shuffle(nsTypes)
            nsTypes[0]
        }
    }

    Type selectInternalClass(Namespace ns) {
        if (!ns)
            throw new InjectionFailedException()

        List<Type> patternTypes = pattern.getTypes()
        List<Type> nsTypes = patternTypes.findAll {
            it.getParentNamespace() == ns
        }

        Collections.shuffle(nsTypes)
        nsTypes[0]
    }

    /**
     * Selects a namespace form the namespaces covered by the pattern instance
     * @param graph The graph of the namespaces
     * @param ns the namespace to start from
     * @return A namespace for use in grime injection
     */
    Namespace selectReachableNamespace(MutableGraph<Namespace> graph, Namespace ns) {
        if (!graph)
            throw new InjectionFailedException()
        if (!ns)
            throw new InjectionFailedException()

        Map<Namespace, Boolean> visited = [:]
        graph.nodes().each {
            visited[it] = false
        }

        dfsUtil(graph, ns, visited)

        List<Namespace> list = []
        visited.remove(ns)

        visited.each { Namespace key, Boolean val ->
            if (val) list << key
        }

        Collections.shuffle(list)

        list[0]
    }

    /**
     * Selects a namespace which is currently unreachable from the provided namespace
     * @param graph The graph of the namespaces
     * @param ns Namespace used for reachability calculations
     * @return A namespace currently unreachable from the provided namespace
     */
    Namespace selectUnreachableNamespace(MutableGraph<Namespace> graph, Namespace ns) {
        if (!graph)
            throw new InjectionFailedException()
        if (!ns)
            throw new InjectionFailedException()

        Map<Namespace, Boolean> visited = [:]
        graph.nodes().each {
            visited[it] = false
        }

        dfsUtil(graph, ns, visited)

        List<Namespace> list = []

        visited.each { Namespace key, Boolean val ->
            if (!val) list << key
        }

        Collections.shuffle(list)

        list[0]
    }

    private void dfsUtil(MutableGraph<Namespace> graph, Namespace ns, Map<Namespace, Boolean> visited) {
        visited[ns] = true

        graph.adjacentNodes(ns).each {
            if (!visited[it])
                dfsUtil(graph, it, visited)
        }
    }

    /**
     * Selects or creates a new type external from the design pattern and contained in the provided namespace
     * @param ns Namespace that currently contains or will contain the type returned
     * @return A type external to the pattern instance but contained within the given namespace
     */
    Type selectOrCreateExternalClass(Namespace ns) {
        Type type = selectExternalClass(ns)

        if (!type) {
            AddFileModelTransform addFile = new AddFileModelTransform(ns, "GenExternalType.java", FileType.SOURCE)
            addFile.execute()
            AddTypeModelTransform addType = new AddTypeModelTransform(addFile.file, "GenExternalType", Accessibility.PUBLIC, "class")
            addType.execute()
            addType.type
        } else {
            type
        }

    }

    static MutableGraph<Namespace> createGraph(Project p) {
        MutableGraph<Namespace> graph = GraphBuilder.undirected().build()
        p.getNamespaces().each {
            if (!it.getFiles().isEmpty())
                graph.addNode(it)
        }

        p.getAllTypes().each { Type t ->
            t.getAssociatedFrom().each { Type o ->
                addEdge(graph, t.getParentNamespace(), o.getParentNamespace())
            }
            t.getAssociatedTo().each { Type o ->
                addEdge(graph, t.getParentNamespace(), o.getParentNamespace())
            }
            t.getRealizes().each { Type o ->
                addEdge(graph, t.getParentNamespace(), o.getParentNamespace())
            }
            t.getRealizedBy().each { Type o ->
                addEdge(graph, t.getParentNamespace(), o.getParentNamespace())
            }
            t.getUseTo().each { Type o ->
                addEdge(graph, t.getParentNamespace(), o.getParentNamespace())
            }
            t.getUseFrom().each { Type o ->
                addEdge(graph, t.getParentNamespace(), o.getParentNamespace())
            }
            t.getGeneralizedBy().each { Type o ->
                addEdge(graph, t.getParentNamespace(), o.getParentNamespace())
            }
            t.getGeneralizes().each { Type o ->
                addEdge(graph, t.getParentNamespace(), o.getParentNamespace())
            }
        }

        graph
    }

    private static void addEdge(MutableGraph<Namespace> graph, Namespace from, Namespace to) {
        if (from != to && !graph.hasEdgeConnecting(from, to))
            graph.putEdge(from, to)
    }
}
