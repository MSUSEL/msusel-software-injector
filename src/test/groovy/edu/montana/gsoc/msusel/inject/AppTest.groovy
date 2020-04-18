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
usage: si [options] <base>

Options:
 -c,--config-file <file>   Name of config file found in the base directory
 -h,--help                 Print this help text and exit
 -v,--version              Print the version information

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