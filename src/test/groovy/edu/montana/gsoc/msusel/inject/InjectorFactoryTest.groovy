package edu.montana.gsoc.msusel.inject

import edu.isu.isuese.datamodel.PatternInstance
import edu.montana.gsoc.msusel.inject.grime.ClassGrimeInjector
import edu.montana.gsoc.msusel.inject.grime.GrimeInjector
import edu.montana.gsoc.msusel.inject.grime.ModularGrimeInjector
import edu.montana.gsoc.msusel.inject.grime.ModularOrgGrimeInjector
import edu.montana.gsoc.msusel.inject.grime.PackageOrgGrimeInjector
import spock.lang.Specification

class InjectorFactoryTest extends Specification {

    def "test createInjector happy path"(PatternInstance inst, String type, String form, result) {
        given:
        InjectorFactory fixture = InjectorFactory.instance

        when:
        SourceInjector value = fixture.createInjector(inst, type, form)

        then:
        value in result

        where:
        inst | type    | form   | result
        null | "grime" | "pig"  | GrimeInjector.class
        null | "grime" | "what" | NullInjector.class
        null | "rot"   | "what" | NullInjector.class
        null | ""      | "pig"  | NullInjector.class
        null | "grime" | ""     | NullInjector.class
    }

    def "test createInjector exceptional path"(PatternInstance inst, String type, String form, result) {
        given:
        InjectorFactory fixture = InjectorFactory.instance

        when:
        fixture.createInjector(inst, type, form)

        then:
        thrown result

        where:
        inst | type    | form   | result
        null | null    | "pig"  | IllegalArgumentException.class
        null | "grime" | null   | IllegalArgumentException.class
    }

    def "test selectGrimeForm"(String form, result) {
        given:
        InjectorFactory fixture = InjectorFactory.instance

        when:
        SourceInjector val = fixture.selectGrimeForm(null, form)

        then:
        val in result

        where:
        form    | result
        "DIPG"  | ClassGrimeInjector.class
        "dipg"  | ClassGrimeInjector.class
        "DiPg"  | ClassGrimeInjector.class
        "disg"  | ClassGrimeInjector.class
        "depg"  | ClassGrimeInjector.class
        "iisg"  | ClassGrimeInjector.class
        "iesg"  | ClassGrimeInjector.class
        "iepg"  | ClassGrimeInjector.class
        "pig"   | ModularGrimeInjector.class
        "tig"   | ModularGrimeInjector.class
        "peeg"  | ModularGrimeInjector.class
        "peag"  | ModularGrimeInjector.class
        "teag"  | ModularGrimeInjector.class
        "teeg"  | ModularGrimeInjector.class
        "pecg"  | PackageOrgGrimeInjector.class
        "picg"  | PackageOrgGrimeInjector.class
        "perg"  | PackageOrgGrimeInjector.class
        "pirg"  | PackageOrgGrimeInjector.class
        "mpecg" | ModularOrgGrimeInjector.class
        "mpeug" | ModularOrgGrimeInjector.class
        "mpicg" | ModularOrgGrimeInjector.class
        "mpiug" | ModularOrgGrimeInjector.class
        "mtecg" | ModularOrgGrimeInjector.class
        "mteug" | ModularOrgGrimeInjector.class
        "mticg" | ModularOrgGrimeInjector.class
        "mtiug" | ModularOrgGrimeInjector.class
        ""      | NullInjector.class
    }

    def "test selectGrimeForm exceptional path"() {
        given:
        InjectorFactory fixture = InjectorFactory.instance

        when:
        SourceInjector val = fixture.selectGrimeForm(null, null)

        then:
        thrown IllegalArgumentException
    }
}
