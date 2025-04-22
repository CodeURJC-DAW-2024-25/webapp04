import { Injectable } from '@angular/core';
import * as L from 'leaflet';

@Injectable({
  providedIn: 'root'
})
export class MapService {
  private map!: L.Map;
  private marker!: L.Marker;

  // Initialize map from iframe URL or with default coordinates (Madrid)
  initializeMapFromIframe(
    iframeUrl: string | null,
    onLocationChange: (lat: string, lng: string) => void
  ): void {
    let centerLat = 40.4168; // Default Madrid
    let centerLng = -3.7038;
    let zoomLevel = 10;

    // Check if iframe URL contains bounding box coordinates
    const matches = iframeUrl?.match(/bbox=([-\d.]+),([-\d.]+),([-\d.]+),([-\d.]+)/);
    if (matches) {
      const minLng = parseFloat(matches[1]);
      const minLat = parseFloat(matches[2]);
      const maxLng = parseFloat(matches[3]);
      const maxLat = parseFloat(matches[4]);

      // Calculate the center of the bounding box
      centerLat = (minLat + maxLat) / 2;
      centerLng = (minLng + maxLng) / 2;
      zoomLevel = 12; // Increased zoom level
    }

    // Initialize the map with calculated or default coordinates
    this.map = L.map('map').setView([centerLat, centerLng], zoomLevel);

    // Add OpenStreetMap layer to the map
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);

    // Event: Click on the map to add or move the marker
    this.map.on('click', (e: L.LeafletMouseEvent) => {
      this.addOrMoveMarker(e.latlng, onLocationChange);
    });
  }

  // Add or move the marker on the map and update iframe
  private addOrMoveMarker(
    latlng: L.LatLng,
    onLocationChange: (lat: string, lng: string) => void
  ): void {
    // Remove existing marker if it exists
    if (this.marker) {
      this.map.removeLayer(this.marker);
    }

    // Create a custom Bootstrap icon for the marker
    const bootstrapIcon = L.divIcon({
      className: 'leaflet-bootstrap-icon',
      html: `<i class="bi bi-geo-fill" style="font-size: 24px; color: #007bff;"></i>`,
      iconSize: [30, 30],
      iconAnchor: [15, 30],
      popupAnchor: [0, -30],
    });

    // Marker management
    this.marker = L.marker(latlng, { icon: bootstrapIcon, draggable: true }).addTo(this.map);
    onLocationChange(latlng.lat.toFixed(6), latlng.lng.toFixed(6));    
    this.marker.on('dragend', () => {
      const position = this.marker.getLatLng();
      onLocationChange(position.lat.toFixed(6), position.lng.toFixed(6));
    });
  }

  updateIframe(lat: string, lng: string): string {
    return `<iframe src="https://www.openstreetmap.org/export/embed.html?bbox=${lng},${lat},${lng},${lat}&layer=mapnik" width="500" height="450"></iframe>`;
  }
  
}
