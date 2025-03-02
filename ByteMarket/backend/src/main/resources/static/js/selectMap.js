// Initialize the map at a default location (Madrid)
let map = L.map('map').setView([40.4168, -3.7038], 10);

// OpenStreetMap layer
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);

// Add a draggable marker
let marker = L.marker([40.4168, -3.7038], { draggable: true }).addTo(map);

// Function to update the iframe with less zoom
function updateIframe(lat, lng) {
    let zoomFactor = 0.01; // Define how much to expand the bbox
    let minLng = (parseFloat(lng) - zoomFactor).toFixed(6);
    let maxLng = (parseFloat(lng) + zoomFactor).toFixed(6);
    let minLat = (parseFloat(lat) - zoomFactor).toFixed(6);
    let maxLat = (parseFloat(lat) + zoomFactor).toFixed(6);

    document.getElementById("iframeCode").value =
        `<iframe src="https://www.openstreetmap.org/export/embed.html?bbox=${minLng},${minLat},${maxLng},${maxLat}&layer=mapnik" width="500" height="450"></iframe>`;
}

// Event: Click on the map to move the marker
map.on('click', function (e) {
    marker.setLatLng(e.latlng);
    updateIframe(e.latlng.lat.toFixed(6), e.latlng.lng.toFixed(6));
});

// Event: Drag the marker to change location
marker.on('dragend', function () {
    let position = marker.getLatLng();
    updateIframe(position.lat.toFixed(6), position.lng.toFixed(6));
});

// Generate the initial iframe
updateIframe(40.4168, -3.7038);