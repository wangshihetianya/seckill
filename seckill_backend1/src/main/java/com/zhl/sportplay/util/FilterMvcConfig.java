package com.zhl.sportplay.util;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class FilterMvcConfig implements WebMvcConfigurer {

    @Bean
    public TokenFilter getTokenFilter(){
        return new TokenFilter();
    }

    //首选fastjson为json解析器，防止日期转换的时候出问题
//    @Bean
//    public HttpMessageConverters fastJsonConverters() {
//        //1.定义一个convert消息转换对象
//        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
//        //2.添加fastJson的序列化配置信息
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,
//                SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue,
//                SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty);
//        //3.处理中文乱码问题
//        List<MediaType> fastMediaTypes = new ArrayList<>();
//        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//        //4.在convert中添加配置信息
//        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
//        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
//        return new HttpMessageConverters(fastJsonHttpMessageConverter);
//    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(getTokenFilter()).addPathPatterns("/**")
//                .excludePathPatterns("/login");

    }

    //解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //设置允许跨域请求的域名
                //当**Credentials为true时，**Origin不能为星号，需为具体的ip地址【如果接口不带cookie,ip无需设成具体ip】
                .allowedOrigins("http://localhost:8080")
                //是否允许证书 不再默认开启
                .allowCredentials(true)
                //设置允许的方法
                .allowedMethods("*")
                //跨域允许时间
                .maxAge(3600);

    }
}
