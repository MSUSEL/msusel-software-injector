package edu.montana.gsoc.msusel.source

import spock.lang.Specification

class BaseTransformSpec extends Specification {

    protected boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list()
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir.delete()
    }
}
