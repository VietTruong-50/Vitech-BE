package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.Role;
import com.hust.vitech.Repository.RoleRepository;
import com.hust.vitech.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Page<Role> getAllRoles(int size, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return roleRepository.findAll(pageable);
    }
}
