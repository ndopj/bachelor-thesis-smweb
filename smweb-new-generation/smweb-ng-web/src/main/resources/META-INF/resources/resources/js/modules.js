function getContextPath() {
    var rootPosition = location.href.indexOf('/', location.href.indexOf("//") + 2);
    var clodwarRootPosition = location.href.indexOf("/admin");
    return location.href.substr(
        rootPosition,
        clodwarRootPosition - rootPosition);
}

function handleWebSocket() {
    var socket = new SockJS(getContextPath() + "/core/ws/connector");
    ws = Stomp.over(socket);
    ws.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        subHandler = ws.subscribe("/user/core/ws/module/", proceedIncomingMessage);
    });
}

function proceedIncomingMessage(incoming) {
    var message = JSON.parse(incoming.body);
    console.log("Prijata sprava: " + message.reload);
    if (message.reload == true) {
        document.getElementById("mainForm:modules:refresh").click();
    }
}
