import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import * as L from 'leaflet';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MapService {
  private map!: L.Map;
  private marker!: L.Marker;

  constructor(private http: HttpClient) { }

  // Initialize map with given coordinates, zoom level, and options
  private initializeMap(centerLat: number, centerLng: number, zoomLevel: number, mapElementId: string, options?: L.MapOptions): L.Map {
    const map = L.map(mapElementId, options).setView([centerLat, centerLng], zoomLevel);

    // Add OpenStreetMap layer to the map
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(map);

    return map;
  }

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
      centerLat = this.calculateCenterFromBbox(matches);
      centerLng = this.calculateCenterFromBbox(matches, true);
      zoomLevel = 12; // Increased zoom level
    }

    // Initialize the map
    this.map = this.initializeMap(centerLat, centerLng, zoomLevel, 'map');

    // Event: Click on the map to add or move the marker
    this.map.on('click', (e: L.LeafletMouseEvent) => {
      this.addOrMoveMarker(e.latlng, onLocationChange);
    });
  }


  // Load map in product detail page
  visualizeMapInContainer(userId: number, containerId: string): void {
    this.loadAndInitializeMap(userId, 15, containerId); // Increased zoom level 15
  }

  // Load map from user iframe
  visualizeMapFromUserIframe(userId: number): void {
    this.loadAndInitializeMap(userId, 10, 'profile-map'); // Default zoom level 10
  }

  // Private method to fetch iframe and initialize the map
  private loadAndInitializeMap(userId: number, zoomLevel: number, containerId: string): void {
    this.getUserIframe(userId).subscribe({
      next: (iframe: string) => {
        const matches = iframe.match(/bbox=([-\d.]+),([-\d.]+),([-\d.]+),([-\d.]+)/);
        let centerLat = 40.4168; // Default Madrid
        let centerLng = -3.7038;

        if (matches) {
          centerLat = this.calculateCenterFromBbox(matches);
          centerLng = this.calculateCenterFromBbox(matches, true);
        }

        // Initialize the map with given zoom level and container
        this.map = this.initializeMap(centerLat, centerLng, zoomLevel, containerId, {
          zoomControl: true,
          dragging: true,
          scrollWheelZoom: true,
          doubleClickZoom: true,
          boxZoom: true,
          keyboard: true,
        });
      },
      error: () => {
        console.warn('Could not load iframe/map for user', userId);
      }
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
    const bootstrapIcon = this.createMarkerIcon();

    // Marker management
    this.marker = L.marker(latlng, { icon: bootstrapIcon, draggable: true }).addTo(this.map);
    onLocationChange(latlng.lat.toFixed(6), latlng.lng.toFixed(6));    
    this.marker.on('dragend', () => {
      const position = this.marker.getLatLng();
      onLocationChange(position.lat.toFixed(6), position.lng.toFixed(6));
    });
  }

  // Method to create custom marker icon
  private createMarkerIcon(): L.DivIcon {
    return L.divIcon({
      className: 'leaflet-bootstrap-icon',
      html: `<i class="bi bi-geo-fill" style="font-size: 24px; color: #007bff;"></i>`,
      iconSize: [30, 30],
      iconAnchor: [15, 30],
      popupAnchor: [0, -30],
    });
  }

  // Method to calculate the center from a bounding box (either lat or lng)
  private calculateCenterFromBbox(bbox: RegExpMatchArray, isLongitude: boolean = false): number {
    const min = parseFloat(bbox[1]);
    const max = parseFloat(bbox[3]);
    return isLongitude ? (min + max) / 2 : (parseFloat(bbox[2]) + parseFloat(bbox[4])) / 2;
  }

  updateIframe(lat: string, lng: string): string {
    return `<iframe src="https://www.openstreetmap.org/export/embed.html?bbox=${lng},${lat},${lng},${lat}&layer=mapnik" width="500" height="450"></iframe>`;
  }

  getUserIframe(userId: number): Observable<string> {
    let url = `/api/v1/users/${userId}/iframe`;
    return this.http.get(url, { responseType: 'text' });
  }
}
