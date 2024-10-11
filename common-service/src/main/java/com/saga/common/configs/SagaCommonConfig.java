package com.saga.common.configs;

import com.thoughtworks.xstream.XStream;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SagaCommonConfig {

    @Bean
    @Primary
    public Serializer serializer(){
        XStream xstream = new XStream();
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypesByWildcard(new String[] {"com.saga.**"});
        return XStreamSerializer.builder().xStream(xstream).build();
    }
}
