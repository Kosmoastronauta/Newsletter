package com.kosmoastronauta.newsletter.Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;


@Configuration
@PropertySources( {@PropertySource("file:/home/mateusz/PropertiesFile/application.properties"),
        @PropertySource("file:/home/mateusz/PropertiesFile/application-dev.properties"),
        @PropertySource("file:/home/mateusz/PropertiesFile/application-test.properties")})
class DefaultConfiguration {}

@Configuration
public class Config
{
    @Configuration
    @Import(DefaultConfiguration.class)
    static class Configuraion{}
}
