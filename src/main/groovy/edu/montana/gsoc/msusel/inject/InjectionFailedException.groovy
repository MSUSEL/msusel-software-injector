package edu.montana.gsoc.msusel.inject

class InjectionFailedException extends RuntimeException {

    InjectionFailedException() {
    }

    InjectionFailedException(String message) {
        super(message)
    }

    InjectionFailedException(String message, Throwable cause) {
        super(message, cause)
    }

    InjectionFailedException(Throwable cause) {
        super(cause)
    }
}
