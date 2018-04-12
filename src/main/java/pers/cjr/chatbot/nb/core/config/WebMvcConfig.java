package pers.cjr.chatbot.nb.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by Timor on 2017/7/17.
 */
//@EnableWebMvc //类似 <mvc:annotation-driven /> 继承WebMvcConfigurerAdapter后不需要配置
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebMvcConfig.class);
    }


    @Value("${web.updateDir}")
    private String uploadPath;

    /**
     * 配置静态访问资源
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+uploadPath+"/").resourceChain(false);
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/").resourceChain(false);
        super.addResourceHandlers(registry);
    }

    /**
     * 请求直接指向view
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/ocr_test/index").setViewName("ocr");
    }
/*
    *//**
     * 响应内容转换
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();

        // 序列换成json时,将所有的long变成string
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
//		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//		objectMapper.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(jackson2HttpMessageConverter);
    }


    @Bean
    public FreeMarkerViewResolver viewResolver() {
        final FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setOrder(0);
        resolver.setCache(false);
        resolver.setPrefix("");
        resolver.setSuffix(".ftl");
        resolver.setContentType("text/html; charset=UTF-8");

        resolver.setRequestContextAttribute("rc");

        resolver.setAllowSessionOverride(true);
        resolver.setAllowRequestOverride(true);

        resolver.setExposeRequestAttributes(true);
        resolver.setExposeSessionAttributes(true);

        resolver.setExposeSpringMacroHelpers(true);
        return resolver;
    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {
        Properties settings = new Properties();
        settings.setProperty("tag_syntax", "auto_detect");
        settings.setProperty("defaultEncoding", "UTF-8");
        settings.setProperty("url_escaping_charset", "UTF-8");
        settings.setProperty("locale", "zh_CN");
        settings.setProperty("boolean_format", "true,false");
        settings.setProperty("datetime_format", "yyyy-MM-dd HH:mm:ss");
        settings.setProperty("date_format", "yyyy-MM-dd");
        settings.setProperty("time_format", "HH:mm:ss");
        settings.setProperty("number_format", "0.######");
        settings.setProperty("whitespace_stripping", "true");

        final FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPath("classpath:templates");
        configurer.setDefaultEncoding("UTF-8");
        configurer.setFreemarkerSettings(settings);
        return configurer;
    }

}
