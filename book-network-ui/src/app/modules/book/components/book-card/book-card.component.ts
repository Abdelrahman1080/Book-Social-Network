import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BookResponce} from "../../../../services/models/book-responce";
import {NgIf} from "@angular/common";
import {RatingComponent} from "../rating/rating.component";

@Component({
  selector: 'app-book-card',
  standalone: true,
  imports: [
    NgIf,
    RatingComponent
  ],
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.scss'
})
export class BookCardComponent {

  private _book:BookResponce={};
  private _bookCover:string|undefined;
  private _manage:boolean=false ;

  get manage(): boolean {
    return this._manage;
  }

  @Input()
  set manage(value: boolean) {
    this._manage = value;
  }


  get book(): BookResponce {
    return this._book;
  }

  @Input()
  set book(value: BookResponce) {
    this._book = value;
  }

  get bookCover(): string | undefined {
    if(this._book.cover){
      return 'data:image/jpg;base64, '+this._book.cover;
    }
    return 'https://source.unsplash.com/user/c_v_r/1900x1900';
  }

  @Output() private share:EventEmitter<BookResponce>=new EventEmitter<BookResponce>();
  @Output() private archive:EventEmitter<BookResponce>=new EventEmitter<BookResponce>();
  @Output() private addToWaitingList:EventEmitter<BookResponce>=new EventEmitter<BookResponce>();
  @Output() private borrow:EventEmitter<BookResponce>=new EventEmitter<BookResponce>();
  @Output() private edit:EventEmitter<BookResponce>=new EventEmitter<BookResponce>();
  @Output() private details:EventEmitter<BookResponce>=new EventEmitter<BookResponce>();


  protected onShowDetails() {
    this.details.emit(this.book);

  }

  protected onBorrow() {
    this.borrow.emit(this.book);
  }

  protected onAddtoWaitingList() {
    this.addToWaitingList.emit(this.book);
  }

  protected onEdit() {
    this.edit.emit(this.book);
  }

  protected onShare() {
    this.share.emit(this.book);
  }

  protected onArchive() {
    this.archive.emit(this.book);
  }
}
