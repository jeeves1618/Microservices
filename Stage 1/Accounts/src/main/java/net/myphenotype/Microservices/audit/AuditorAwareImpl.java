package net.myphenotype.Microservices.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("AuditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware {
    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor.
     */
    @Override
    public Optional getCurrentAuditor() {
        return Optional.of("Bertram Wooster");
    }
}
