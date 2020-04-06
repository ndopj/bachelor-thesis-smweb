package cz.sm.ng.core.SMWeb.modules.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class ModulesWebsocket {

    @Autowired private SimpMessageSendingOperations messagingTemplate;

    public void sendReload() {
        messagingTemplate.convertAndSend("/user/core/ws/module/", new Object(){
            private final boolean reload = true;
            public void Object() {}
            public boolean getReload() { return reload; }
        });
    }
}
