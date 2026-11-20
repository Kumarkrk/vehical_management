
package com.te.config;

import java.util.Properties;

import javax.sql.DataSource;
import io.github.cdimascio.dotenv.Dotenv;

import org.hibernate.SessionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {
    "com.te"
})
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dataSource =
                new DriverManagerDataSource();
        Dotenv dotenv = Dotenv.configure()
                .directory("D:/java_projects/vehicleusingmvc")
                .ignoreIfMissing()
                .load();

        dataSource.setDriverClassName("org.postgresql.Driver");

        dataSource.setUrl(dotenv.get("DB_URL"));
        dataSource.setUsername(dotenv.get("DB_USERNAME"));
        dataSource.setPassword(dotenv.get("DB_PASSWORD"));

        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(
            DataSource dataSource) {

        LocalSessionFactoryBean factory =
                new LocalSessionFactoryBean();

        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.te.model");

        Properties properties = new Properties();

        properties.put(
            "hibernate.dialect",
            "org.hibernate.dialect.PostgreSQLDialect"
        );

        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");

        factory.setHibernateProperties(properties);

        return factory;
    }

    @Bean
    public HibernateTemplate hibernateTemplate(
            SessionFactory sessionFactory) {

        return new HibernateTemplate(sessionFactory);
    }

    @Bean
    public HibernateTransactionManager transactionManager(
            SessionFactory sessionFactory) {

        return new HibernateTransactionManager(sessionFactory);
    }
}
