import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {PageResponceBorrowedBookResponse} from "../../../../services/models/page-responce-borrowed-book-response";
import {BorrowedBookResponse} from "../../../../services/models/borrowed-book-response";
import {FeedbackRequest} from "../../../../services/models/feedback-request";
import {BookService} from "../../../../services/services/book.service";
import {FeedbackService} from "../../../../services/services/feedback.service";
import {BookCardComponent} from "../../components/book-card/book-card.component";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-returned-books',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    BookCardComponent
  ],
  templateUrl: './returned-books.component.html',
  styleUrl: './returned-books.component.scss'
})
export class ReturnedBooksComponent implements OnInit{



  returnedBooks: PageResponceBorrowedBookResponse={};
  page =0;
  size=5;
  public selectedBook: BorrowedBookResponse | undefined;
  feedbackRequest: FeedbackRequest={bookId: 0, comment: "",note: 0};
  message: string = "";
  level: string = "success";

  constructor(private bookService: BookService,private feedbackService: FeedbackService,
              private toastrService: ToastrService) {

  }


  ngOnInit(): void {
    this.findAllReturnedBooks()
  }

  returnedBorrowedBook(borrowedBook: BorrowedBookResponse) {
    this.selectedBook=borrowedBook;
    this.feedbackRequest.bookId=borrowedBook.id as number;

  }

  private findAllReturnedBooks(){
    this.bookService.findAllReturnedBooks({
      page:this.page,
      size:this.size
    }).subscribe({
      next: results => {
        this.returnedBooks = results;
      },
      error: err => {
        console.log(err);
      }
    })
  }


  protected goToFirstPage() {
    this.page = 0;
    this.findAllReturnedBooks();

  }

  protected goToPreviousPage() {
    this.page--;
    this.findAllReturnedBooks();

  }

  protected goToPage(number: number) {
    this.page = number;
    this.findAllReturnedBooks();
  }

  protected goToNextPage() {
    this.page++;
    this.findAllReturnedBooks();
  }

  protected gotoLastPage() {
    this.page=this.returnedBooks.totalPages as number - 1;
    this.findAllReturnedBooks();
  }

  get isLastPage():boolean {
    return this.page == this.returnedBooks.totalPages as number - 1;
  }

  protected approveBookReturn(book: BorrowedBookResponse) {
        if(!book.returned){
          this.level="error";
          this.message="Book is not returned";
          return;
        }
        this.bookService.approveReturnBook({
          "book-id":book.id as number
        }).subscribe({
          next: results => {
            this.toastrService.success("Book return Approved");  /*
            this.level="success";

              this.message="Book return Approved";*/
              this.findAllReturnedBooks();
          }
        })
  }
}
