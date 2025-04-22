import { Injectable } from '@angular/core';
import * as L from 'leaflet';
import { FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Injectable({
    providedIn: 'root'
})
export class MapService {
    private map!: L.Map;
    private marker!: L.Marker;

    constructor(private http: HttpClient, private sanitizer: DomSanitizer) { }

    // Fetch iframe from backend and sanitize it
    getSafeIframe(iframeUrl: string): SafeHtml {
        return this.sanitizer.bypassSecurityTrustHtml(iframeUrl);
    }

    // Initialize map from iframe data
    initializeMapFromIframe(iframeUrl: string, form: FormGroup): void {
        const matches = iframeUrl.match(/bbox=([-\d.]+),([-\d.]+),([-\d.]+),([-\d.]+)/);
        let centerLat = 40.4168; // Default latitude (Madrid)
        let centerLng = -3.7038; // Default longitude (Madrid)

        if (matches) {
            const minLng = parseFloat(matches[1]);
            const minLat = parseFloat(matches[2]);
            const maxLng = parseFloat(matches[3]);
            const maxLat = parseFloat(matches[4]);

            centerLat = (minLat + maxLat) / 2;
            centerLng = (minLng + maxLng) / 2;
        }

        this.initializeMap(centerLat, centerLng, 12, form);
    }

    // Initialize the map and setup click handler
    private initializeMap(centerLat: number, centerLng: number, zoomLevel: number, form: FormGroup): void {
        this.map = L.map('map').setView([centerLat, centerLng], zoomLevel);

        // OpenStreetMap layer
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; OpenStreetMap contributors'
        }).addTo(this.map);

        // Event: Click on the map to add or move the marker
        this.map.on('click', (e) => {
            if (this.marker) {
                this.map.removeLayer(this.marker);
            }
            const bootstrapIcon = L.divIcon({
                className: 'leaflet-bootstrap-icon',
                html: `<i class="bi bi-geo-fill" style="font-size: 24px; color: #007bff;"></i>`,
                iconSize: [30, 30],
                iconAnchor: [15, 30],
                popupAnchor: [0, -30],
            });
            this.marker = L.marker(e.latlng, { icon: bootstrapIcon, draggable: true }).addTo(this.map);

            // Update iframe when the marker is added or moved
            this.updateIframe(e.latlng.lat.toFixed(6), e.latlng.lng.toFixed(6), form);

            // Event: Drag the marker to change its position
            this.marker.on('dragend', () => {
                const position = this.marker.getLatLng();
                this.updateIframe(position.lat.toFixed(6), position.lng.toFixed(6), form);
            });
        });
    }

    // Update iframe in the form when marker position changes
    private updateIframe(lat: string, lng: string, form: FormGroup): void {
        form.patchValue({
            iframe: `<iframe src="https://www.openstreetmap.org/export/embed.html?bbox=${lng},${lat},${lng},${lat}&layer=mapnik" width="500" height="450"></iframe>`
        });
    }

    getIframe(userId: number): Observable<string> {
        let url = `/api/v1/users/${userId}/iframe`;
        return this.http.get<string>(url);
    }
}
