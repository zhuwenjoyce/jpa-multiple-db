package com.joyce.jpa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
public class DatasourceSlaveConfiguration {


    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private HibernateProperties hibernateProperties;

    //从库的数据库文件配置信息
    @Bean
    @ConfigurationProperties("spring.datasource.mysql2")
    public DataSourceProperties slaveDataSourceProperties() {
        return new DataSourceProperties();
    }
    @Bean("slaveDataSource")
    @ConfigurationProperties("spring.datasource.mysql2")
    public DataSource slaveDataSource() {
        return  slaveDataSourceProperties().initializeDataSourceBuilder().build();
    }

    //从库连接工厂
    @Bean("slaveEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean slaveEntityManagerFactory(
            EntityManagerFactoryBuilder builder,@Qualifier("slaveDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .properties(getVendorProperties(dataSource))
                .packages("com.joyce.jpa.domain_slave")//实体类包名
                .persistenceUnit("slave")
                .build();
    }

    private Map<String, ?> getVendorProperties(DataSource dataSource) {
        return hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(), new HibernateSettings());
    }

    // 从库jpa事务配置，作用于dao层
    @Bean("slavePlatformTM")
    public PlatformTransactionManager slaveTransactionManager(@Qualifier("slaveEntityManagerFactory") LocalContainerEntityManagerFactoryBean slaveEntityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager(slaveEntityManagerFactory.getObject());
        return transactionManager;
    }

//  从库Repository的包名
    @EnableJpaRepositories(basePackages={"com.joyce.jpa.dao_slave"},  //从库Repository的包名，就是dao所在的包
            entityManagerFactoryRef = "slaveEntityManagerFactory",transactionManagerRef = "slavePlatformTM")
    public class SlaveConfiguration {
    }

    // 从库事务配置, 这个从事务可以在service层生效：
    // @Transactional(value = "slaveTransactionManager")
    // 或者
    // @Transactional(transactionManager = "slaveTransactionManager")
    // 因为多数据源，所以得具体指定哪个事务
    @Bean(name = "slaveTransactionManager")
    @Primary
    public DataSourceTransactionManager getMasterTransactionManager(@Qualifier("slaveDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}