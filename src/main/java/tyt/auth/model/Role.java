package tyt.auth.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Role implements GrantedAuthority {

    ADMIN,
    MANAGER,
    CASHIER;

    @Override
    public String getAuthority() {
        return name();
    }
}
