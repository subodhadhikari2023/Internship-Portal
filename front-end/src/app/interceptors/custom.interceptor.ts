import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class CustomInterceptor implements HttpInterceptor {

  constructor() { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    const localToken = localStorage.getItem('token');
    if (localToken && !request.url.includes('/login') && !request.url.includes('/register')) {

    // if (localToken && !request.url.includes('/login')) {

      const cloned = request.clone({
        
        headers: request.headers.set('Authorization', `Bearer ${localToken}`)
      });
      // console.log(cloned.headers.keys());
      return next.handle(cloned);
    }


    return next.handle(request);
  }
}
