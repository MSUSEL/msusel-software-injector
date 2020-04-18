package edu.montana.gsoc.msusel.inject

import org.javalite.activejdbc.Base

@Singleton
class DBManager {

    def open(ConfigObject config) {
        Base.open(config.data.driver, config.data.url, config.data.user, config.data.pass)
    }

    def close() {
        Base.close()
    }
}
