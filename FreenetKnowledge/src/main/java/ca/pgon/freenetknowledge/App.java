/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.foilen.smalltools.upgrader.UpgraderTools;
import com.foilen.smalltools.upgrader.tasks.UpgradeTask;
import com.foilen.smalltools.upgrader.trackers.DatabaseUpgraderTracker;

import ca.pgon.freenetknowledge.action.ActionController;
import ca.pgon.freenetknowledge.action.generators.CrawlingActionGenerator;
import ca.pgon.freenetknowledge.action.generators.FmsActionGenerator;
import ca.pgon.freenetknowledge.freenet.IntegerToFnTypeConverter;
import ca.pgon.freenetknowledge.repository.starter.UrlStarter;
import ca.pgon.freenetknowledge.search.impl.LuceneSearchEngine;
import ca.pgon.freenetknowledge.spiders.news.NewsUtils;

@EnableAutoConfiguration
@EnableTransactionManagement
@Configuration
@ComponentScan
public class App {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(App.class);
        springApplication.run(args);
    }

    @Bean
    public ConversionServiceFactoryBean conversionService() {
        ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
        Set<Converter<?, ?>> converters = new HashSet<>();
        converters.add(new IntegerToFnTypeConverter());
        factoryBean.setConverters(converters);
        return factoryBean;
    }

    @Bean
    public ActionController crawlingActionController(CrawlingActionGenerator crawlingActionGenerator) {
        ActionController actionController = new ActionController();
        actionController.setCorePoolSize(10);
        actionController.setMaximumPoolSize(20);
        actionController.setActionGenerator(crawlingActionGenerator);
        return actionController;
    }

    @Bean
    public ActionController fmsActionController(FmsActionGenerator fmsActionGenerator) {
        ActionController actionController = new ActionController();
        actionController.setCorePoolSize(1);
        actionController.setMaximumPoolSize(3);
        actionController.setActionGenerator(fmsActionGenerator);
        return actionController;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public LuceneSearchEngine luceneSearchEngine() {
        LuceneSearchEngine searchEngine = new LuceneSearchEngine();
        searchEngine.setIndexDirectory("freenetknowledge.index");
        searchEngine.setMaxAddInIndexBeforeComputing(50);
        return searchEngine;
    }

    @Bean
    public NewsUtils newsUtils() {
        NewsUtils newsUtils = new NewsUtils();
        newsUtils.setPort(1119);
        newsUtils.setHost("127.0.0.1");
        return newsUtils;
    }

    @Bean
    public UpgraderTools upgraderTools(DataSource dataSource, List<UpgradeTask> upgradeTasks) {
        UpgraderTools upgraderTools = new UpgraderTools(upgradeTasks);
        DatabaseUpgraderTracker upgraderTracker = new DatabaseUpgraderTracker(jdbcTemplate(dataSource));
        upgraderTools.setDefaultUpgraderTracker(upgraderTracker);
        upgraderTools.getUpgraderTrackerByName().put("db", upgraderTracker);
        return upgraderTools;
    }

    @Bean
    public UrlStarter urlStarter() {
        // Freenet's directories bookmarks as of 2017-08-14
        UrlStarter urlStarter = new UrlStarter();
        urlStarter.setDefaultUrls(new String[] { //
                "http://127.0.0.1:8888/USK@XJZAi25dd5y7lrxE3cHMmM-xZ-c-hlPpKLYeLC0YG5I,8XTbR1bd9RBXlX6j-OZNednsJ8Cl6EAeBBebC3jtMFU,AQACAAE/index/711/", //
                "http://127.0.0.1:8888/USK@ozMQYaCEXnlHQQggITYSIeNSxqdMknqjOIYyCdMKqJA,gJyID9FRxaM5zDql3D8-wHACAusOYa5Aag3M4tSEt~g,AQACAAE/Index/539/", //
                "http://127.0.0.1:8888/USK@Isel-izgllc8sr~1reXQJz1LNGLIY-voOnLWWOyagYQ,xWfr4py0YZqAQSI-BX7bolDe-kI3DW~i9xHCHd-Bu9k,AQACAAE/linkageddon/1128/", //
                "http://127.0.0.1:8888/USK@tiYrPDh~fDeH5V7NZjpp~QuubaHwgks88iwlRXXLLWA,yboLMwX1dChz8fWKjmbdtl38HR5uiCOdIUT86ohUyRg,AQACAAE/nerdageddon/247/" //
        });
        return urlStarter;
    }

}
