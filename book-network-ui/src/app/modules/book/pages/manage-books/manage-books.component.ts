import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {BookRequest} from "../../../../services/models/book-request";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {BookService} from "../../../../services/services/book.service";
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-manage-books',
  standalone: true,
  imports: [
    NgForOf,
    FormsModule,
    RouterLink,
    NgIf
  ],
  templateUrl: './manage-books.component.html',
  styleUrl: './manage-books.component.scss'
})
export class ManageBooksComponent implements OnInit{
  errorMsg:Array<string>=[];
  protected selectedPicture: string | undefined;
  selectedBookCover:any;
  protected bookRequest: BookRequest={sharable: true, authorName:'',isbn:'',synopsis:'',title:''}

  constructor(private bookService:BookService,private router:Router,private activatedRoute:ActivatedRoute
  ,private toastrService: ToastrService
  ) {
  }

  ngOnInit(): void {
            const bookId=this.activatedRoute.snapshot.params['bookId'];
            if(bookId){
              this.bookService.findBookById({'book-id': bookId}).subscribe({
                next:(book)=>{
                  this.bookRequest={
                    id:bookId,
                    title:book.title as string,
                    authorName:book.authorName as string,
                    isbn:book.isbn as string,
                    synopsis:book.synopsis as string,
                    sharable:book.sharable as boolean
                  }
                  if(book.cover){
                    this.selectedPicture='data:image/jpg;base64,' +book.cover;
                  }
                }
              })
            }

    }

  protected onFileSelected($event: Event) {
    const target = $event.target as HTMLInputElement;
    if (target && target.files && target.files.length > 0) {
      this.selectedBookCover = target.files[0];
    }
      console.log(this.selectedBookCover);
    if(this.selectedBookCover){
      const reader=new FileReader();
      reader.onload=() => {
        this.selectedPicture=reader.result as string;
      }
      reader.readAsDataURL(this.selectedBookCover);
    }
  }

  protected saveBook() {
    this.bookService.saveBook({body:this.bookRequest}).subscribe({
      next:(bookId)=>{
        this.bookService.uploadBookCover({
          'book-id':bookId,
          body:{
            'file':this.selectedBookCover
          }
        }).subscribe({
          next:()=>{
            this.toastrService.success("Book Saved Successfully");
           this.router.navigate(['/books/my-books']);
          },
          error : (err)=>{
            this.toastrService.error(err.error.error);
            this.errorMsg=err.error.validationErrors;
          }
        })
      }
    });
  }
}
