import {Component, OnInit} from '@angular/core';
import {BookService} from "../../../../services/services/book.service";
import {Router} from "@angular/router";
import {PageResponceBookResponce} from "../../../../services/models/page-responce-book-responce";
import {NgForOf, NgIf} from "@angular/common";
import {BookCardComponent} from "../../components/book-card/book-card.component";
import {BookResponce} from "../../../../services/models/book-responce";

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [
    NgForOf,
    BookCardComponent,
    NgIf
  ],
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss'
})
export class BookListComponent implements OnInit{
  bookResponse:PageResponceBookResponce={};
  public page:number = 0;
  public size:number = 4;
  public message: string='';
  public level: string='success';

  constructor(
    private bookService: BookService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.findAllBooks();
    }

  private findAllBooks() {
    this.bookService.findAllBooks({
      size:this.size,
      page:this.page
    }).subscribe({
      next:(books:PageResponceBookResponce)=>{
        this.bookResponse=books;
      }
    })
  }


  protected goToFirstPage() {
    this.page = 0;
    this.findAllBooks();

  }

  protected goToPreviousPage() {
    this.page--;
    this.findAllBooks();

  }

  protected goToPage(number: number) {
    this.page = number;
    this.findAllBooks();
  }

  protected goToNextPage() {
      this.page++;
      this.findAllBooks();
  }

  protected gotoLastPage() {
this.page=this.bookResponse.totalPages as number - 1;
this.findAllBooks();
  }

 get isLastPage():boolean {
   return this.page == this.bookResponse.totalPages as number - 1;
 }

  protected borrowBook(book: BookResponce) {
    this.message="";
      this.bookService.borrowBook({'book-id':book.id as number}).subscribe({
        next:()=>{
          this.level="success";
          this.message="Book Borrowed Successfully and added to you list";
        },
        error:(err)=>{
          this.level="error";
          this.message= err.error.error;
        }
      })
  }


}
