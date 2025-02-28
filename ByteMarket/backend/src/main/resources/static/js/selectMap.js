// Inicializa el mapa en una ubicación por defecto (Madrid)
let map = L.map('map').setView([40.4168, -3.7038], 10);

// Capa de OpenStreetMap
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);

// Agrega un marcador arrastrable
let marker = L.marker([40.4168, -3.7038], { draggable: true }).addTo(map);

// Función para actualizar el iframe con menos zoom
function updateIframe(lat, lng) {
    let zoomFactor = 0.01; // Define cuánto expandir el bbox
    let minLng = (parseFloat(lng) - zoomFactor).toFixed(6);
    let maxLng = (parseFloat(lng) + zoomFactor).toFixed(6);
    let minLat = (parseFloat(lat) - zoomFactor).toFixed(6);
    let maxLat = (parseFloat(lat) + zoomFactor).toFixed(6);

    document.getElementById("iframeCode").value =
        `<iframe src="https://www.openstreetmap.org/export/embed.html?bbox=${minLng},${minLat},${maxLng},${maxLat}&layer=mapnik" width="500" height="450"></iframe>`;
}

// Evento: Click en el mapa para mover el marcador
map.on('click', function (e) {
    marker.setLatLng(e.latlng);
    updateIframe(e.latlng.lat.toFixed(6), e.latlng.lng.toFixed(6));
});

// Evento: Arrastrar el marcador para cambiar ubicación
marker.on('dragend', function () {
    let position = marker.getLatLng();
    updateIframe(position.lat.toFixed(6), position.lng.toFixed(6));
});

// Generar el iframe inicial
updateIframe(40.4168, -3.7038);