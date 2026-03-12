import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthServiceService } from '../services/auth-service.service';

export const roleGuard: CanActivateFn = (route, state) => {

  const authService = inject(AuthServiceService);
  const router = inject(Router);

  const allowedRoles = route.data?.['roles'] as String[];

  const userRole= authService.getRole();
  console.log("Role Guard - User Role:", userRole);

  if(userRole && !allowedRoles.includes(userRole)){
    return true;
  }
  
//   if (!allowedRoles.includes(userRole)) {
//   authService.redirectAfterLogin();
//   return false;
// }


  return true;

};
