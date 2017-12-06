package onliner.apartments.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import java.util.Set;
import java.util.regex.Pattern;

@Configuration
public class RepositoryRestConfig extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));

        Set<BeanDefinition> beans = provider.findCandidateComponents("onliner.apartments.model");

        for (BeanDefinition bean : beans) {
            Class<?> class_ = null;
            try {
                class_ = Class.forName(bean.getBeanClassName());
                config.exposeIdsFor(Class.forName(class_.getName()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Failed to expose `id` field due to", e);
            }
        }
    }
}