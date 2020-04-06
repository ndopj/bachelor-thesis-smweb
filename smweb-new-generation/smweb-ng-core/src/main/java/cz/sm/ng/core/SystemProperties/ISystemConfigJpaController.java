package cz.sm.ng.core.SystemProperties;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISystemConfigJpaController extends JpaRepository<SystemProperty, String> {}
