//package com.aldo.whatdotodo;
//
//import org.h2.jdbcx.JdbcDataSource;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class DataSourceConfig {
//    private static final String TEMP_DIRECTORY = System.getProperty("java.io.tmpdir");
//
//    @Bean(name = "mainDataSource")
//    public DataSource createMainDataSource() {
//        LoggerFactory.getLogger(this.getClass()).warn("--------------- Passo: {}", TEMP_DIRECTORY);
//        JdbcDataSource ds = new JdbcDataSource();
//        ds.setURL("jdbc:h2:" + TEMP_DIRECTORY + "/whatdotodo;MODE=MySQL");
//
//        return ds;
//    }
//
//}
