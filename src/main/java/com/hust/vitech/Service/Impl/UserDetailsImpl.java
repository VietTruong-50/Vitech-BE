package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.Customer;
import com.hust.vitech.Model.ShoppingSession;
import com.hust.vitech.Model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

//    private static final long serialVersionUID = 1L;

    User user;

    Customer customer;

    public Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(User user, Customer customer,
                           Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
        this.customer = customer;
    }

    public static UserDetailsImpl build(User user, Customer customer) {
        List<GrantedAuthority> authorities = null;
        
        if(user != null){
            authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

            return new UserDetailsImpl(
                    user, null,
                    authorities);
        }else{
            authorities = Collections.singletonList(new SimpleGrantedAuthority(customer.getRole()));

            return new UserDetailsImpl(
                    null, customer,
                    authorities);
        }
    }

    public Long getId(){
        return user != null ? user.getId() : customer.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user != null ? user.getPassword() : customer.getPassword();
    }

    @Override
    public String getUsername() {
        return user != null ? user.getUserName() : customer.getUserName();
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
        return true;
    }

}
