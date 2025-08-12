import { HttpInterceptorFn } from '@angular/common/http';
import { AuthServiceService } from './auth-service.service';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService= inject(AuthServiceService);
  const token = localStorage.getItem('token');


 const lowerUrl = req.url.toLowerCase();


if (lowerUrl.includes('/login') || lowerUrl.includes('/register')) {
  return next(req);
}

  console.log('Outgoing URL: ', req.url);
  console.log('Auth Interceptor Token:', token);

 if (token) {
     const cloned = req.clone({
  headers: req.headers
    .set('Authorization', `Bearer ${token}`)
    .set('Content-Type', 'application/json')
});

    return next(cloned);
  }

  return next(req); 
  
};
