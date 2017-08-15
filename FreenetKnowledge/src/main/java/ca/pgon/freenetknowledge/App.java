/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014-2017

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
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
        // Freenet's default directories bookmarks as of 2014-08-16
        UrlStarter urlStarter = new UrlStarter();
        urlStarter.setDefaultUrls(new String[] { "http://localhost:8888/USK@XJZAi25dd5y7lrxE3cHMmM-xZ-c-hlPpKLYeLC0YG5I,8XTbR1bd9RBXlX6j-OZNednsJ8Cl6EAeBBebC3jtMFU,AQACAAE/index/440/",
                "http://localhost:8888/USK@Isel-izgllc8sr~1reXQJz1LNGLIY-voOnLWWOyagYQ,xWfr4py0YZqAQSI-BX7bolDe-kI3DW~i9xHCHd-Bu9k,AQACAAE/linkageddon/1050/" });
        return urlStarter;
    }

}
