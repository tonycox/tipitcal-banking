package org.tonycox.banking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Activates audit specific functions when audit annotations are handled.
 */
@Configuration
@EnableJpaAuditing
public class AuditConfig {

}
