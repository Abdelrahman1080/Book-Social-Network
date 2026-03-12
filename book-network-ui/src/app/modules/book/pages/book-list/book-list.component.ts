import {Component, OnInit} from '@angular/core';
import {BookService} from "../../../../services/services/book.service";
import {Router} from "@angular/router";
import {PageResponceBookResponce} from "../../../../services/models/page-responce-book-responce";
import {NgForOf} from "@angular/common";
import {BookCardComponent} from "../../components/book-card/book-card.component";

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [
    NgForOf,
    BookCardComponent
  ],
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss'
})
export class BookListComponent implements OnInit{
  bookResponse:PageResponceBookResponce={};
  private page:number = 0;
  private size:number = 5;

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

  }

  protected goToPreviousPage() {

  }
}
