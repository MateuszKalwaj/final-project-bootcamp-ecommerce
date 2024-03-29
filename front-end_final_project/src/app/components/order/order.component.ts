import { Component, OnInit } from '@angular/core';
import {Cart} from '../../models/cart';
import {CartService} from '../../_services/cart.service';
import {AuthenticationService} from '../../_services/authentication.service';
import {Router} from '@angular/router';
import {User} from '../../models/user';
import {isObject} from 'rxjs/internal-compatibility';
import {Order} from '../../models/order';
import {OrderService} from '../../_services/order.service';
import { Location} from '@angular/common';

/**
 * @author Marta Prosniak
 * order component to create new order and send it to API
 */

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit {

  order: Order;
  currentUser: User;
  loggedIn: boolean;
  cart: Cart;
  street: string;
  postcode: string;
  town: string;
  country: string;

  constructor(private cartService: CartService,
              private authService: AuthenticationService,
              private orderService: OrderService,
              private router: Router,
              private location: Location) {
    this.order = new Order();
  }

  ngOnInit() {
    // check if user is logged in
    if (!isObject(this.authService.currentUser)) {
      this.loggedIn = false;
      this.router.navigate(['/login']);
    } else {
      this.authService.currentUser.subscribe(currentUser =>
        this.currentUser = currentUser);
      this.loggedIn = true;
      this.cart = this.currentUser.cart;
    }
  }
  // create new order
  postOrder() {
    this.orderService.createOrder(this.cart.id, this.createOrder())
      .subscribe(order => this.order = order);
    alert('Order recieved!');
    this.router.navigate(['/products']);
  }

  // set order fields
  createOrder(): Order {
    this.order = new Order();
    this.order.items = this.cart.products;
    this.order.buyer = this.cart.buyer;
    this.order.value = this.cart.cartValue;
    this.order.address = this.street + ' ' + this.postcode + ' ' + this.town +
      ' ' + this.country;
    return this.order;
  }

  goBack(): void {
    this.location.back();
  }
}
