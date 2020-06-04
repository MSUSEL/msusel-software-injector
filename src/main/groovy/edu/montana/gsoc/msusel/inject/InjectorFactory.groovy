package edu.montana.gsoc.msusel.inject

import edu.isu.isuese.datamodel.PatternInstance
import edu.montana.gsoc.msusel.inject.grime.ClassGrimeInjector
import edu.montana.gsoc.msusel.inject.grime.ClassGrimeTypes
import edu.montana.gsoc.msusel.inject.grime.ModularGrimeInjector
import edu.montana.gsoc.msusel.inject.grime.ModularGrimeTypes
import edu.montana.gsoc.msusel.inject.grime.ModularOrgGrimeInjector
import edu.montana.gsoc.msusel.inject.grime.OrgGrimeTypes
import edu.montana.gsoc.msusel.inject.grime.PackageOrgGrimeInjector

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
class InjectorFactory {

    SourceInjector createInjector(PatternInstance inst, String type, String form) {
        if (type == null)
            throw new IllegalArgumentException("createInjector: type cannot be null")
        if (form == null)
            throw new IllegalArgumentException("createInjector: form cannot be null")

        switch (type.toLowerCase()) {
            case "grime":
                return selectGrimeForm(inst, form)
            case "rot":
                return selectRotForm(inst, form)
            default:
                return new NullInjector()
        }
    }

    SourceInjector selectGrimeForm(PatternInstance inst, String form) {
        if (form == null)
            throw new IllegalArgumentException("selectGrimeForm: form cannot be null")

        switch (form.toUpperCase()) {
            case ClassGrimeTypes.DIPG:
                return ClassGrimeInjector.builder().direct(true).internal(true).pair(true).pattern(inst).create()
            case ClassGrimeTypes.DISG:
                return ClassGrimeInjector.builder().direct(true).internal(true).pair(false).pattern(inst).create()
            case ClassGrimeTypes.DEPG:
                return ClassGrimeInjector.builder().direct(true).internal(false).pair(true).pattern(inst).create()
            case ClassGrimeTypes.DESG:
                return ClassGrimeInjector.builder().direct(true).internal(false).pair(false).pattern(inst).create()
            case ClassGrimeTypes.IISG:
                return ClassGrimeInjector.builder().direct(false).internal(true).pair(false).pattern(inst).create()
            case ClassGrimeTypes.IESG:
                return ClassGrimeInjector.builder().direct(false).internal(false).pair(false).pattern(inst).create()
            case ClassGrimeTypes.IEPG:
                return ClassGrimeInjector.builder().direct(false).internal(false).pair(true).pattern(inst).create()
            case ModularGrimeTypes.PIG:
                return ModularGrimeInjector.builder().persistent(true).external(false).efferent(false).pattern(inst).create()
            case ModularGrimeTypes.TIG:
                return ModularGrimeInjector.builder().persistent(false).external(false).efferent(false).pattern(inst).create()
            case ModularGrimeTypes.TEEG:
                return ModularGrimeInjector.builder().persistent(false).external(true).efferent(true).pattern(inst).create()
            case ModularGrimeTypes.TEAG:
                return ModularGrimeInjector.builder().persistent(false).external(true).efferent(false).pattern(inst).create()
            case ModularGrimeTypes.PEEG:
                return ModularGrimeInjector.builder().persistent(false).external(true).efferent(true).pattern(inst).create()
            case ModularGrimeTypes.PEAG:
                return ModularGrimeInjector.builder().persistent(false).external(true).efferent(false).pattern(inst).create()
            case OrgGrimeTypes.PECG:
                return PackageOrgGrimeInjector.builder().internal(false).closure(true).pattern(inst).create()
            case OrgGrimeTypes.PERG:
                return PackageOrgGrimeInjector.builder().internal(false).closure(false).pattern(inst).create()
            case OrgGrimeTypes.PICG:
                return PackageOrgGrimeInjector.builder().internal(true).closure(true).pattern(inst).create()
            case OrgGrimeTypes.PIRG:
                return PackageOrgGrimeInjector.builder().internal(true).closure(false).pattern(inst).create()
            case OrgGrimeTypes.MPECG:
                return ModularOrgGrimeInjector.builder().persistent(true).internal(false).cyclical(true).pattern(inst).create()
            case OrgGrimeTypes.MPEUG:
                return ModularOrgGrimeInjector.builder().persistent(true).internal(false).cyclical(false).pattern(inst).create()
            case OrgGrimeTypes.MPICG:
                return ModularOrgGrimeInjector.builder().persistent(true).internal(true).cyclical(true).pattern(inst).create()
            case OrgGrimeTypes.MPIUG:
                return ModularOrgGrimeInjector.builder().persistent(true).internal(true).cyclical(false).pattern(inst).create()
            case OrgGrimeTypes.MTECG:
                return ModularOrgGrimeInjector.builder().persistent(false).internal(false).cyclical(true).pattern(inst).create()
            case OrgGrimeTypes.MTEUG:
                return ModularOrgGrimeInjector.builder().persistent(false).internal(false).cyclical(false).pattern(inst).create()
            case OrgGrimeTypes.MTICG:
                return ModularOrgGrimeInjector.builder().persistent(false).internal(true).cyclical(true).pattern(inst).create()
            case OrgGrimeTypes.MTIUG:
                return ModularOrgGrimeInjector.builder().persistent(false).internal(true).cyclical(false).pattern(inst).create()
            default:
                return new NullInjector()
        }
    }

    SourceInjector selectRotForm(PatternInstance inst, String form) {
        return new NullInjector()
    }
}
