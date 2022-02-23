package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PermissionDto {
    private String permission;
    private String roleName;
    private boolean isTrue;
}
