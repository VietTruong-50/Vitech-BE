package com.hust.vitech.Service;

import com.hust.vitech.Model.Role;
import org.springframework.data.domain.Page;

public interface RoleService {
    Page<Role> getAllRoles(int size, int page, String sortBy);
}
