package edu.montana.gsoc.msusel.inject

import java.security.Permission

class TestingSecurityManager extends SecurityManager {

    @Override
    void checkExit(int status) {
        throw new SecurityException()
    }

    @Override
    void checkPermission(Permission perm) {
        // Allow other activities by default
    }
}
