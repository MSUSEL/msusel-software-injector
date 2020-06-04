package edu.montana.gsoc.msusel.inject

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
class ConfigLoader {

    ConfigObject loadConfiguration(File config) {
        if (!config)
            throw new IllegalArgumentException("loadConfiguration: config cannot be null")

        if (!config.exists()) {
            System.err << "Config file ${config.name} does not exist\n"
            System.exit 1
        }

        ConfigSlurper slurper = new ConfigSlurper()
        slurper.parse(config.text)
    }
}
