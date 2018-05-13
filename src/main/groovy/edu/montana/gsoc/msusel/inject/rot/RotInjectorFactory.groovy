package edu.montana.gsoc.msusel.inject.rot

import edu.montana.gsoc.msusel.codetree.node.structural.PatternNode
import edu.montana.gsoc.msusel.inject.NullInjector
import edu.montana.gsoc.msusel.inject.SourceInjector

/**
 * Factory used to construct Design Pattern Rot injectors
 * @author Isaac Griffith
 * @version 1.2.0
 */
@Singleton
class RotInjectorFactory {

    /**
     * Factory method for creating rot injectors
     * @param type String representation of the type, found in RotTypes
     * @param pattern Pattern instance
     * @return a RotInjector corresponding to the provided type, or a NullInjector if the type was unknown
     */
    SourceInjector create(String type, PatternNode pattern) {
        switch (type) {
            case RotTypes.BLOB:
                return createBlobRole(pattern)
            case RotTypes.INAP_DEP:
                return createInappropriateDep(pattern)
            case RotTypes.INAP_INV:
                return createInappropriateInv(pattern)
            case RotTypes.MISSING:
                return createMissingRole(pattern)
            default:
                return createNullInjector()
        }
    }

    /**
     * @return New instance of a NullInjector
     */
    private def createNullInjector() {
        return new NullInjector()
    }

    /**
     * Constructs a new BlobRole rot Injector for the given pattern instance
     * @param pattern Pattern instance to inject rot into
     * @return The RotInjector
     * @throws IllegalArgumentException if the provided pattern is null
     */
    private RotInjector createBlobRole(PatternNode pattern) {
        if (!pattern)
            throw new IllegalArgumentException("Pattern instance may not be null")
        BlobRoleInjector.builder().patten(pattern).create()
    }

    /**
     * Constructs a new BlobRole rot Injector for the given pattern instance
     * @param pattern Pattern instance to inject rot into
     * @return The RotInjector
     * @throws IllegalArgumentException if the provided pattern is null
     */
    private RotInjector createInappropriateDep(PatternNode pattern) {
        if (!pattern)
            throw new IllegalArgumentException("Pattern instance may not be null")
        InappropriateDependencyInjector.builder().pattern(pattern).create()
    }

    /**
     * Constructs a new BlobRole rot Injector for the given pattern instance
     * @param pattern Pattern instance to inject rot into
     * @return The RotInjector
     * @throws IllegalArgumentException if the provided pattern is null
     */
    private RotInjector createInappropriateInv(PatternNode pattern) {
        if (!pattern)
            throw new IllegalArgumentException("Pattern instance may not be null")
        InappropriateInversionInjector.builder().patten(pattern).create()
    }

    /**
     * Constructs a new BlobRole rot Injector for the given pattern instance
     * @param pattern Pattern instance to inject rot into
     * @return The RotInjector
     * @throws IllegalArgumentException if the provided pattern is null
     */
    private RotInjector createMissingRole(PatternNode pattern) {
        if (!pattern)
            throw new IllegalArgumentException("Pattern instance may not be null")
        MissingRoleInjector.builder().patten(pattern).create()
    }
}
