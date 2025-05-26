package com.NanoPurse.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("app.info")
public class AppInfo {
    private String title;

    private String description;

    private String version;

    private String contactName;

    private String contactUrl;

    private String contactEmail;

}
