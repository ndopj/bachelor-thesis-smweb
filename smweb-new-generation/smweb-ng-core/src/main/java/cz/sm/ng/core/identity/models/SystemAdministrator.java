package cz.sm.ng.core.identity.models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="IDENT_ADMIN")
public class SystemAdministrator extends Identity
{
    private static final long serialVersionUID = 1L;
}

