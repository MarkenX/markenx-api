package com.udla.markenx.api.security.infrastructure.gametoken;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to enable game token properties.
 */
@Configuration
@EnableConfigurationProperties(GameTokenProperties.class)
public class GameTokenConfig {
}
