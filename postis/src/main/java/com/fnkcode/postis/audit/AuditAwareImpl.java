package com.fnkcode.postis.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(value = "auditAware")
public class AuditAwareImpl implements AuditorAware<String> {
    //I am using this to populate the creation and update dates .
    //I don't use it for the user , so I will pass a dummy text
    //In a normal scenario , where I could use spring security , I should have an UserDetail
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("dummyString");
    }
}
