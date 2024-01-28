package bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@SQLDelete(sql = "UPDATE roles SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "role_name", nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private RoleName roleName;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Override
    public String getAuthority() {
        return "ROLE_" + roleName.name();
    }

    public enum RoleName {
        USER,
        ADMIN
    }
}
