package edu.montana.gsoc.msusel.grimeinject

import edu.montana.gsoc.msusel.grimeinject.modgrime.PIGInjector
import edu.montana.gsoc.msusel.grimeinject.modgrime.TIGInjector

@Singleton
class InjectorFactory {

    GrimeInjector createInjector(String injector) {
        switch(injector) {
            case 'PIG':
                return new PIGInjector()
            case 'TIG':
                return new TIGInjector()
        }
    }
}
