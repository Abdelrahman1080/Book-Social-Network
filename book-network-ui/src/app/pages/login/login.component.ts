import { Component } from '@angular/core';
//import { Router } from '@angular/router';
import {AuthenticationRequest} from "../../services/models/authentication-request";
import {FormsModule} from "@angular/forms";
import {AuthenticationService} from "../../services/services/authentication.service";
import { NgFor, NgIf } from '@angular/common';
import { Router } from '@angular/router';
import {TokenService} from "../../services/token/token.service";
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    NgFor,
    NgIf,
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  authenticationRequest: AuthenticationRequest = {
    email: '',
    password: ''
  };
  errormsg: Array<String> = [];

  constructor(
   private router: Router,
    private authService: AuthenticationService,
   private tokenService: TokenService
  ) {
  }

  protected login() {
        this.errormsg= [];
        this.authService.loginUser({body:this.authenticationRequest}).subscribe({
          next:(res):void => {
            this.tokenService.token=res.token as string;
            this.router.navigate(['books'])
            console.log(res);
          },
          error:(err) => {
            console.log(err);
            if(err.error.validationErrors){
              this.errormsg=err.error.validationErrors
            }else{
              this.errormsg.push(err.error.error)
            }

          }
        });

  }

  protected register() {
    this.router.navigate(['/register']);

  }
}
