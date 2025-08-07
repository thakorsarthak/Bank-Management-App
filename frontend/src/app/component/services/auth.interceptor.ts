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

    const authReq = token
    
    ? req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      })
    : req;

     return next(authReq).pipe(
    catchError((error) => {
      if (error.status === 401) {
        authService.logout(); // clear localStorage and redirect
      }
      return throwError(() => error);
    })
  );
  
};
