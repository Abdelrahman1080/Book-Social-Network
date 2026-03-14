import {Component, OnInit} from '@angular/core';
import {BookCardComponent} from "../../components/book-card/book-card.component";
import {NgForOf} from "@angular/common";
import {PageResponceBookResponce} from "../../../../services/models/page-responce-book-responce";
import {BookService} from "../../../../services/services/book.service";
import {Router, RouterLink} from "@angular/router";
import {BookResponce} from "../../../../services/models/book-responce";

@Component({
  selector: 'app-my-books',
  standalone: true,
  imports: [
    BookCardComponent,
    NgForOf,
    RouterLink,
  ],
  templateUrl: './my-books.component.html',
  styleUrl: './my-books.component.scss'
})
export class MyBooksComponent implements OnInit{
  bookResponse:PageResponceBookResponce={};
  public page:number = 0;
  public size:number = 4;

  constructor(
    private bookService: BookService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.findAllBooks();
  }

  private findAllBooks() {
    this.bookService.findAllBooksByOwner({
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


  protected archiveBook(book: BookResponce) {
    this.bookService.updateArchivedStatus({
      "book-id": book.id as number,
    }).subscribe({
      next:()=>{
        book.archived = !book.archived;
      }
    })
  }

  protected shareBook(book: BookResponce) {
    this.bookService.updateSHarableStatus({
      "book-id": book.id as number,
    }).subscribe({
      next:()=>{
        book.sharable = !book.sharable;
      }
    })

  }
  protected editBook(book: BookResponce) {
    console.log('Editing book:', book.id);
    this.router.navigate(['books', 'manage', book.id]);
  }

}
