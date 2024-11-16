package com.fnkcode.postis.context;

import com.fnkcode.postis.records.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Setter @Getter
public class UserContext {
    private User user;
}
