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
public class DatasourcePrimaryConfiguration {


    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private HibernateProperties hibernateProperties;

    //主库的数据库文件配置信息
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.mysql")
    public DataSourceProperties masterDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("masterDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.mysql")
    public DataSource masterDataSource() {
        return masterDataSourceProperties().initializeDataSourceBuilder().build();
    }

    //主库连接工厂
    @Bean("masterEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory(
            EntityManagerFactoryBuilder builder,@Qualifier("masterDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.joyce.jpa.domain_primary")//实体类包名
                .properties(getVendorProperties(dataSource))
                .persistenceUnit("master")
                .build();
    }

    private Map<String, ?> getVendorProperties(DataSource dataSource) {
        return hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(), new HibernateSettings());
    }

    // 主库jpa事务配置，作用于dao层
    @Primary
    @Bean("masterPlatformTM")
    public PlatformTransactionManager masterPlatformTM(@Qualifier("masterEntityManagerFactory") LocalContainerEntityManagerFactoryBean masterEntityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager(masterEntityManagerFactory.getObject());
//        transactionManager.setGlobalRollbackOnParticipationFailure(true);
        return transactionManager;
    }

//    主库Repository的包名
    @EnableJpaRepositories(basePackages={"com.joyce.jpa.dao_primary"},//主库Repository的包名，就是dao所在的包
            entityManagerFactoryRef = "masterEntityManagerFactory"
            , transactionManagerRef = "masterPlatformTM"
            ,enableDefaultTransactions = true
    )
    @Primary
    public class MasterConfiguration {
    }

    // 主库事务配置, 这个主事务可以在service层生效：
    // @Transactional(value = "masterTransactionManager")
    // 或者
    // @Transactional(transactionManager = "masterTransactionManager")
    // 因为多数据源，所以得具体指定哪个事务
    @Bean(name = "masterTransactionManager")
    @Primary
    public DataSourceTransactionManager getMasterTransactionManager(@Qualifier("masterDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}