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
package edu.montana.gsoc.msusel.inject


import spock.lang.Specification

class AppTest extends Specification {

    ByteArrayOutputStream consoleText
    PrintStream oldOut
    PrintStream console

    void setup() {
        oldOut = System.out
        consoleText = new ByteArrayOutputStream()
        console = new PrintStream(consoleText)
        System.setOut(console)
    }

    void cleanup() {
        System.setOut(oldOut)
    }
    
    def "test main help"() {
        given:
        String[] args = ['-h']
        String expected = '''\
Usage: si [options] <base>

Options:
  -c, --config-file=<file>   Name of config file found in the base directory
  -h, --help                 Print this help text and exit
  -v, --version              Print the version information

Copyright (c) 2018-2020 Isaac Griffith and Montana State University
'''

        SecurityManager secManager = new TestingSecurityManager()
        System.setSecurityManager(secManager)


        when:
        App.main(args)

        then:
        thrown SecurityException
        consoleText.toString() == expected
    }

    def "test main -v"() {
        given:
        String[] args = ['-v']
        String expected = '''\
software injector version 1.3.0
'''

        SecurityManager secManager = new TestingSecurityManager()
        System.setSecurityManager(secManager)


        when:
        App.main(args)

        then:
        thrown SecurityException
        consoleText.toString() == expected
    }
}