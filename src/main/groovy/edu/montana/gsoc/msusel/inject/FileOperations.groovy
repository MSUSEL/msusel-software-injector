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
package edu.montana.gsoc.msusel.inject

import edu.montana.gsoc.msusel.codetree.node.CodeNode
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * Container for contents of files currently under modification. This class acts as the receiver
 * of the transforms.
 * @author Isaac Griffith
 * @version 1.2.0
 */
class FileOperations {

    /**
     * The string representation of the path for the file to be modified
     */
    String file
    /**
     * List containing the string contents of the file under modification
     */
    List<String> lines

    /**
     * Saves the file, if it already exists this method will delete and rewrite the file.
     */
    void save() {
        Path path = Paths.get(file)
        try {
            Files.deleteIfExists(path)
        } catch (IOException e) {

        }

        PrintWriter pw
        try {
            pw = new PrintWriter(Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE))

            lines.stream().each { pw.println(it) }
            pw.flush()
        } catch (IOException e) {

        } finally {
            pw?.close()
        }
    }

    /**
     * Opens the file at the location specified in the key of the provided FileNode
     * @param node Node representation of the file
     * @throws IllegalArgumentException if the provided FileNode is null
     */
    void open(FileNode node) {
        if (file == null) {
            throw new IllegalArgumentException("No FileOperations can be defined for a null FileNode")
        }

        file = node.getKey()

        Path path = Paths.get(file)
        try {
            lines = Files.readAllLines(path)
        } catch (IOException e) {

        }
    }

    /**
     * Injects the contents of the provided string at the provided location
     * @param line Location for the injection
     * @param content Content to be injected
     * @return the length of the injected content, in lines
     * @throws IllegalArgumentException if line is outside the bounds of the file or if the content is null
     */
    int inject(int line, String content) {
        if (line - 1 < 0 || line - 1 >= lines.size())
            throw new IllegalArgumentException("No such line as ${line} in file ${file}")
        if (content == null)
            throw new IllegalArgumentException("Cannot inject null content")

        String[] newLines = content.split(/\n/)

        lines.addAll(line - 1, Arrays.asList(newLines))

        return newLines.length
    }

    /**
     * Retrieves the content at the line provided
     * @param line index of the line of content (assumed to start at 1)
     * @return The actual content of the at that line in the file
     * @throws IllegalArgumentException if the line is outside the bounds of the file
     */
    String contentAt(int line) {
        if (line - 1 < 0 || line - 1 >= lines.size())
            throw new IllegalArgumentException("No such line as ${line} in file ${file}")
        return lines[line - 1]
    }

    /**
     * Retrieves the content in the file between the start and end lines of the provided code node (inclusive)
     * @param node Node whose content is requested
     * @return List of strings covering the lines in the region requested
     * @throws IllegalArgumentException if the provide node is null
     */
    List<String> contentRegion(CodeNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Cannot retrieve content region of a null CodeNode")
        }
        try {
            lines.subList(node.start - 1, node.end - 1)
        } catch (IndexOutOfBoundsException e) {
            []
        }
    }

    /**
     * Injects the provided string at the very end of the file
     * @param s The content to be injected
     * @return The length of the injected content in lines
     * @throws IllegalArgumentException if the content provided is null
     */
    int injectAtEnd(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Cannot inject null content")
        }
        int start = lines.size()

        def newLines = s.split(/\n/)
        lines.addAll(newLines)

        newLines.size()
    }
}
