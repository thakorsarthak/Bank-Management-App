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

export const authInterceptor:
HttpInterceptorFn = (
  req,
  next
) => {

  const authService =
    inject(AuthServiceService);

  const token =
    localStorage.getItem('token');

  const lowerUrl =
    req.url.toLowerCase();

  // SKIP AUTH APIs
  if (
    lowerUrl.includes('/login')
    || lowerUrl.includes('/register')
    || lowerUrl.includes('/refresh')
  ) {

    return next(req);
  }

  let authReq = req;

  // ATTACH JWT TOKEN
  if (token) {

    authReq = req.clone({

      headers: req.headers
        .set(
          'Authorization',
          `Bearer ${token}`
        )
        .set(
          'Content-Type',
          'application/json'
        )
    });
  }

  return next(authReq).pipe(

    catchError(
      (
        error:
        HttpErrorResponse
      ) => {

        // TOKEN INVALID / EXPIRED
        if (error.status === 401) {

          console.log(
            '401 Unauthorized - Logging out'
          );

          authService.logout();
        }

        return throwError(
          () => error
        );
      }
    )
  );
};