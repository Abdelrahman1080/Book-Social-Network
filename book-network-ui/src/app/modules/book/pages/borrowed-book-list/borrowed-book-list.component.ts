import {Component, OnInit} from '@angular/core';
import {BorrowedBookResponse} from "../../../../services/models/borrowed-book-response";
import {PageResponceBorrowedBookResponse} from "../../../../services/models/page-responce-borrowed-book-response";
import {NgForOf, NgIf} from "@angular/common";
import {BookService} from "../../../../services/services/book.service";
import {FeedbackRequest} from "../../../../services/models/feedback-request";
import {FormsModule} from "@angular/forms";
import {RatingComponent} from "../../components/rating/rating.component";
import {RouterLink} from "@angular/router";
import {FeedbackService} from "../../../../services/services/feedback.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-borrowed-book-list',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    FormsModule,
    RatingComponent,
    RouterLink
  ],
  templateUrl: './borrowed-book-list.component.html',
  styleUrl: './borrowed-book-list.component.scss'
})
export class BorrowedBookListComponent implements OnInit {

  borrowedBooks: PageResponceBorrowedBookResponse={};
  page =0;
  size=5;
  public selectedBook: BorrowedBookResponse | undefined;
  feedbackRequest: FeedbackRequest={bookId: 0, comment: "",note: 0};

  constructor(private bookService: BookService,private feedbackService: FeedbackService,
              private toastrService: ToastrService) {

  }


  ngOnInit(): void {
    this.findAllBorrowedBooks()
  }

  returnedBorrowedBook(borrowedBook: BorrowedBookResponse) {
      this.selectedBook=borrowedBook;
      this.feedbackRequest.bookId=borrowedBook.id as number;

  }

  private findAllBorrowedBooks(){
    this.bookService.findAllBorrowedBooks({
      page:this.page,
      size:this.size
    }).subscribe({
      next: results => {
        this.borrowedBooks = results;
      },
      error: err => {
        console.log(err);
      }
    })
  }


  protected goToFirstPage() {
    this.page = 0;
    this.findAllBorrowedBooks();

  }

  protected goToPreviousPage() {
    this.page--;
    this.findAllBorrowedBooks();

  }

  protected goToPage(number: number) {
    this.page = number;
    this.findAllBorrowedBooks();
  }

  protected goToNextPage() {
    this.page++;
    this.findAllBorrowedBooks();
  }

  protected gotoLastPage() {
    this.page=this.borrowedBooks.totalPages as number - 1;
    this.findAllBorrowedBooks();
  }

  get isLastPage():boolean {
    return this.page == this.borrowedBooks.totalPages as number - 1;
  }


  protected returnBook(withfeedback: boolean) {
    this.bookService.returnBook({
      "book-id":this.selectedBook?.id as number
    }).subscribe({
      next: results => {
        if(withfeedback){
          this.giveFeedback()
        }
        this.toastrService.success("Book returned successfully","Success");
        this.selectedBook=undefined;
        this.findAllBorrowedBooks()
      }
    })

  }


  private giveFeedback() {
    this.feedbackService.saveFeedback({
      body:this.feedbackRequest
    }).subscribe({
      next: results => {}
    })
  }
}
