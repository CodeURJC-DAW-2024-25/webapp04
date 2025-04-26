import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const userEmail = sessionStorage.getItem('userEmail');
    const userRoles = JSON.parse(sessionStorage.getItem('userRoles') || '[]');
    console.log('User email from sessionStorage in AuthGuard:', userEmail);
    console.log('User roles from sessionStorage:', userRoles);

    const isAuthenticated = userEmail !== null && userEmail !== '';
    console.log('Authenticated status:', isAuthenticated);

    if (!isAuthenticated) {
      console.log('User is not authenticated, redirecting to access-denied');
      this.router.navigate(['/access-denied']);
      return false;
    }

    const requiredRoles = route.data['roles'];  // Los roles necesarios para la ruta (array de roles)
  
    // Si la ruta tiene roles específicos requeridos y el usuario no tiene uno de esos roles, se deniega el acceso
    if (requiredRoles && !requiredRoles.some((role: string) => userRoles.includes(role))) {
      console.log('User does not have the required role, redirecting to access-denied');
      this.router.navigate(['/access-denied']);
      return false;  // Impide el acceso
    }

    console.log('User is authenticated, granting access');
    return true;
  }
}