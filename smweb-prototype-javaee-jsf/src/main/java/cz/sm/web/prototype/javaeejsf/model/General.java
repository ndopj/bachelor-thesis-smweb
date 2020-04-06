package cz.sm.web.prototype.javaeejsf.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="IDENT_GENERAL")
public class General extends Identity implements Serializable {
    private static final long serialVersionUID = 1L;
}
