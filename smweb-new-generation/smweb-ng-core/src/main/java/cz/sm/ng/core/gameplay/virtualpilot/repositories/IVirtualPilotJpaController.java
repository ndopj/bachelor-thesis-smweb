package cz.sm.ng.core.gameplay.virtualpilot.repositories;

import cz.sm.ng.core.gameplay.virtualpilot.VirtualPilot;
import cz.sm.ng.core.identity.models.Identity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVirtualPilotJpaController extends JpaRepository<VirtualPilot, Integer> {
    long countByOwner(Identity owner);
}
