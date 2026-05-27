import {
  HttpErrorResponse,
  HttpInterceptorFn
} from '@angular/common/http';

import {
  catchError,
  throwError
} from 'rxjs';

import { inject } from '@angular/core';

import { AuthServiceService }
from '../services/auth-service.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthServiceService);
  const token = localStorage.getItem('token');
  const lowerUrl = req.url.toLowerCase();

  if (
    lowerUrl.includes('/login') ||
    lowerUrl.includes('/register') ||
    lowerUrl.includes('/refresh')
  ) {
    return next(req);
  }

  let authReq = req;
  if (token) {
    authReq = req.clone({
      headers: req.headers
        .set('Authorization', `Bearer ${token}`)
        .set('Content-Type', 'application/json')
    });
  }

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        const role = localStorage.getItem('role');

        // Only auto-logout for USER on 401
        if (role === 'USER') {
          authService.logout();
        }
        // For ADMIN/EMPLOYEE, let the timer handle refresh
        // Don't logout here — the countdown will trigger refresh
      }
      return throwError(() => error);
    })
  );
};