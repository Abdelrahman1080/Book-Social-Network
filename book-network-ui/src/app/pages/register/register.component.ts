import { Component } from '@angular/core';
import {RegistrationRequest} from "../../services/models/registration-request";
import {FormsModule} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    NgIf
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  registerRequest :RegistrationRequest={email:'',firstName:'',lastName:'',password:''};
  errormsg: Array<String> = [];
  constructor(private router:Router,private authService:AuthenticationService) {
  }

  protected register() {
    this.errormsg=[];
    this.authService.registerUser({body:this.registerRequest}).subscribe({
      next: () => {
        this.router.navigateByUrl('activate-account');
      },
      error: (err) => {
        this.errormsg=err.error.validationErrors;
      }
    })

  }

  protected login() {
this.router.navigate(['login']);
  }
}
