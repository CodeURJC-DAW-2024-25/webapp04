// Initialize the map with a default center (Madrid) but no marker
let map = L.map('map').setView([40.4168, -3.7038], 10);

// OpenStreetMap layer
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);

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

// Event: Click on the map to add the marker and move it
map.on('click', function (e) {
    // Remove the previous marker if it exists
    if (window.marker) {
        map.removeLayer(window.marker);
    }
    
    // Add a new draggable marker
    window.marker = L.marker(e.latlng, { draggable: true }).addTo(map);
    
    // Update iframe only when a marker is added
    updateIframe(e.latlng.lat.toFixed(6), e.latlng.lng.toFixed(6));

    // Event: Drag the marker to change location
    window.marker.on('dragend', function () {
        let position = window.marker.getLatLng();
        updateIframe(position.lat.toFixed(6), position.lng.toFixed(6));
    });
});

// Do not generate the iframe initially, it will be generated after click.
