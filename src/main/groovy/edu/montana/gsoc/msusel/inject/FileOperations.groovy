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
package edu.montana.gsoc.msusel.inject

import edu.montana.gsoc.msusel.codetree.node.structural.FileNode

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
class FileOperations {

    String file
    List<String> lines

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

    void open(FileNode node) {
        file = node.getKey()

        Path path = Paths.get(file)
        try {
            lines = Files.readAllLines(path)
        } catch (IOException e) {

        }
    }

    int inject(int line, String content) {
        String[] newLines = content.split(/\n/)

        lines.addAll(line, Arrays.asList(newLines))

        return newLines.length
    }

    String contentAt(int line) {
        return lines[line]
    }

    int injectAtEnd(String s) {
        int start = lines.size()

        lines.addAll(s.split(/\n/))

        start
    }
}
