function refreshRadars() {
    for (var i = 1; i < 8; i++) {
        var radarScreenEl = document.getElementById('radar-screen-' + i);

        if (radarScreenEl) {
            radarScreenEl.src = radarScreenEl.src;
        }

    }
    setTimeout('refreshRadars();', 5000);
}