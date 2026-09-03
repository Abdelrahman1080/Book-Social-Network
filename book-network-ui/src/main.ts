import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { HttpTokenInterceptor } from './app/services/interceptor/http-token.interceptor';

appConfig.providers.push(
  provideHttpClient(withInterceptorsFromDi()),
  HttpTokenInterceptor
);
(window as any).global = window;
bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
