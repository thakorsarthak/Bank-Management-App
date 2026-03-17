package com.example.bankapp.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.bankapp.entity.Account;
import com.example.bankapp.enums.AccountStatus;

public class CustomAcountDetails implements UserDetails {

    private final Account account;
    private final String identifier; // the value user logged in with

    public CustomAcountDetails(Account account, String identifier) {
        this.account = account;
        this.identifier = identifier;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<? extends GrantedAuthority> authorities =
                Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + account.getRole().name())
                );

        System.out.println("Authorities: " + authorities);

        return authorities;
    }


    @Override
    public String getPassword() {
        // This must be the encoded pin from DB
        System.out.println("Spring checking encoded pin: " + account.getPassword());
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        // Return the actual identifier used for login
        System.out.println("Spring checking identifier: " + identifier);
        return identifier;
    }

    // Extra getters for other fields (not required by Spring, but handy in app)
    
    public Long getId() {
        return account.getId();
    }
    
    public String getAccountNumber() {
        return account.getAccountNumber();
    }

    public String getEmail() {
        return account.getEmail();
    }

    public String getAadhaarNo() {
        return account.getAadhaarNo();
    }

    public String getPanNo() {
        return account.getPanNo();
    }

    public String getContact() {
        return String.valueOf(account.getContact());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return account.getStatus()== AccountStatus.ACTIVE || account.getStatus()== AccountStatus.PENDING_KYC;
        
    }
}
