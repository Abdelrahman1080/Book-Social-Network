import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";
import {NgIf} from "@angular/common";
import {CodeInputModule} from "angular-code-input";

@Component({
  selector: 'app-activate-account',
  standalone: true,
  imports: [
    NgIf,
    CodeInputModule
  ],
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.scss'
})
export class ActivateAccountComponent {
  message='';
  isokay=true;
  submitted=false;
  constructor( private router:Router,
               private authenticationService: AuthenticationService
  ) {

  }

  protected onCodeCompleted(token: string) {
    this.confirmAccount(token);
  }

  protected redirectToLogin() {
    this.router.navigate(['login']);
  }

  private confirmAccount(token: string) {
    this.authenticationService.activateAccount({token}).subscribe({
      next: result => {
        this.message='your account has been activated \n not you can proceeed to login'
          this.submitted=true;
        this.isokay=true;
      },
      error: err => {
        this.message='your account has not been activated \n please try again';
        this.isokay=false;
        this.submitted=true;
      }
    })
  }
}
