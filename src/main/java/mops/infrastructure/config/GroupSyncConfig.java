package mops.infrastructure.config;

import lombok.NoArgsConstructor;
import mops.domain.repositories.GroupRepository;
import mops.infrastructure.groupsync.GroupSyncService;
import mops.infrastructure.groupsync.GroupSyncValidator;
import mops.infrastructure.groupsync.GroupSyncWebclient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@NoArgsConstructor
public class GroupSyncConfig {

    /**
     * Hier konfigurieren wir, dass der GroupSyncService nur erstellt wird, wenn das in der application.properties
     * eingestellt ist.
     * @param webclient Der Webclient, der die Anfrage an das SCS stellt
     * @param validator Die Komponente, die das von webclient zurückgegebene Dto validiert
     * @param groupRepository Das Repository, in das die Gruppen synchronisiert werden sollen
     * @return der Service, der die Gruppen synchronisiert.
     */
    @Autowired
    @Bean
    @ConditionalOnProperty(value = "mops.gruppen2.sync.enabled", havingValue = "true")
    public GroupSyncService syncService(GroupSyncWebclient webclient,
                                        GroupSyncValidator validator,
                                        GroupRepository groupRepository) {
        return new GroupSyncService(webclient, validator, groupRepository);
    }

}
