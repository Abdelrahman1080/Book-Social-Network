import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import {HTTP_INTERCEPTORS, provideHttpClient} from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';

import { routes } from './app.routes';
import {HttpTokenInterceptor} from "./services/interceptor/http-token.interceptor";
import { provideToastr } from 'ngx-toastr';
export const appConfig: ApplicationConfig = {
  providers: [
    provideAnimations(),
    provideToastr({
      positionClass: 'toast-bottom-left',
      tapToDismiss: true,
      preventDuplicates: true,
      countDuplicates: true,
      closeButton: true,
      progressBar: true,

      timeOut: 3000
    }),
    provideRouter(routes),
    provideHttpClient(),
    {
      provide:HTTP_INTERCEPTORS,
      useClass:HttpTokenInterceptor,
      multi:true
    }
  ]
};
