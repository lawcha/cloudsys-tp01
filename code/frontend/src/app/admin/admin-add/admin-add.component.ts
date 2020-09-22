import {Component, ElementRef, OnInit, Renderer2, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {Ingredient, Workshop} from '../../model/workshop/workshop';
import {FormGroup, FormBuilder, Validators, FormControl, ValidatorFn, AbstractControl} from '@angular/forms';
import {City} from '../../model/city/city';
import {WorkshopService} from '../../services/workshop-service';

/**
 * Represent the structure of a category
 */
interface Category {
  value: string;
  text: string;
}

/**
 * Represent the structure of an organizer
 */
interface Organizer {
  value: string;
  text: string;
}

@Component({
  selector: 'app-admin-add',
  templateUrl: './admin-add.component.html',
  styleUrls: ['./admin-add.component.css']
})
/**
 * Component to add a workshop to the app. It simply call the component "WorkshopForm"
 */
export class AdminAddComponent implements OnInit {
  submitLabel = 'AJOUTER';
  constructor() { }

  ngOnInit(): void { }
}
