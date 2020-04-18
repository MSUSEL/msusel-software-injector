package edu.montana.gsoc.msusel.inject

import edu.isu.isuese.datamodel.Pattern
import edu.isu.isuese.datamodel.PatternInstance
import edu.isu.isuese.datamodel.Project

@Singleton
class Director {

    void inject(ConfigObject config) {

        DBManager.instance.open(config)

        Project proj = Project.findFirst("projectKey = ?", (String) config.where.projectKey)

        ProjectCopier copier = new ProjectCopier()
        proj = copier.execute(proj)

        Pattern pattern = Pattern.findFirst("patternKey = ?", (String) config.where.patternKey)
        PatternInstance inst = proj.findPatternInstance(pattern, config.where.patternInst)

        SourceInjector injector = selectInjector(inst, config)
        int min = config.what.min
        int max = config.what.max

        Random rand = new Random()
        int number = rand.nextInt(max - min) + min
        number.times {
            injector.inject()
        }

        DBManager.instance.close()
    }

    SourceInjector selectInjector(PatternInstance inst, ConfigObject config) {
        String type = config.what.type
        String form = config.what.form

        InjectorFactory.instance.createInjector(inst, type, form)
    }
}
