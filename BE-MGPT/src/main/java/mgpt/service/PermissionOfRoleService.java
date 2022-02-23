package mgpt.service;

import mgpt.dao.PermissionOfRole;
import mgpt.dao.Task;
import mgpt.dao.TaskHistory;
import mgpt.model.HistoryDto;
import mgpt.model.PermissionDto;
import mgpt.repository.PermissionOfRoleRepository;
import mgpt.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionOfRoleService {
    @Autowired
    PermissionOfRoleRepository permissionOfRoleRepository;
    public ResponseEntity<?> getPermission(int roleId) throws Exception {
        try {
            List<PermissionOfRole> permissionList = permissionOfRoleRepository.findAllByRoleId_RoleId(roleId);
            List<PermissionDto> permissionDtos = permissionList.stream().map(permissionOfRole -> permissionOfRole.convertToDto()).collect(Collectors.toList());
            return ResponseEntity.ok(permissionDtos);
    } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
