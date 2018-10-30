package cn.j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Author J
 * @Date 2018/10/30 16:43
 * @Description
 **/
@Configuration
@ConditionalOnClass(Docket.class)
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(prefix = "swagger.doc", value = "enabled", matchIfMissing = true)
public class SwaggerServiceAutoConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(SwaggerServiceAutoConfiguration.class);

    @Autowired
    private SwaggerProperties swaggerProperties;

    @Bean
    @ConditionalOnMissingBean(Docket.class)
    public Docket defaultApiDoc() {
        return new Docket(DocumentationType.SWAGGER_2)
                //接口基本信息
                .apiInfo(
                        new ApiInfoBuilder()
                                .title(swaggerProperties.getTitle())
                                .description(swaggerProperties.getDescription())
                                .version(swaggerProperties.getVersion())
                                .build()
                )
                .select()
                //该包下的接口生成接口文档
                //注意 扫描的包是以包名为basePackage开头的包
                //如:配置包 xx.api 若存在xx.api2   xx.api2下的接口也会被扫描进去
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }

}