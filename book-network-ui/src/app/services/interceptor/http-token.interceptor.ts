import {Injectable} from "@angular/core";

import {Observable} from "rxjs";
import {HttpHeaders, HttpInterceptor} from "@angular/common/http";
import {HttpRequest} from "@angular/common/http";
import {HttpHandler} from "@angular/common/http";
import {HttpEvent} from "@angular/common/http";
import {TokenService} from "../token/token.service";
@Injectable()
export class HttpTokenInterceptor implements HttpInterceptor {
    constructor(private tokenService:TokenService) {
    }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = this.tokenService.token;
    if(token){
      const authreq=req.clone({
        headers: new HttpHeaders({
          Authorization:`Bearer ${token}`
        })
      })
      return next.handle(authreq);
    }

      return next.handle(req);
  }
}
