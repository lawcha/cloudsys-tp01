import {Component, ElementRef, Input, OnInit, Renderer2, ViewChild} from '@angular/core';
import {Ingredient, Organizer, Workshop} from '../../model/workshop/workshop';
import {AbstractControl, FormControl, ValidatorFn, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {WorkshopService} from '../../services/workshop-service';
import {City} from '../../model/city/city';
import {Type} from '../admin-workshops/admin-workshops.component';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';

/**
 * Represent the structure of a category
 */
interface Category {
  value: string;
  text: string;
}
@Component({
  selector: 'app-workshop-form',
  templateUrl: './workshop-form.component.html',
  styleUrls: ['./workshop-form.component.css']
})
/**
 * Form used when add or edit a workshop.
 */
export class WorkshopFormComponent implements OnInit {
  @Input() submitLabel: string;
  @Input() titleParam: string;
  @Input() dateParam: Date;

  public classicEditor = ClassicEditor;
  editor: ClassicEditor;

  workshop: Workshop = new Workshop(
    '', '', '',  null,  null, '', null, '', [], 1, 1, '', 'draft'
  );

  @ViewChild('fileUpload', {static: false})
  fileUpload: ElementRef;
  @ViewChild('imageButton', {static: false})
  imageButton: ElementRef;
  file = null;

  oldState: string;
  oldTitle: string;
  oldDate: Date;

  categories: string[] = [];
  organizers: Organizer[] = [
  ];
  locations: City[] = [];
  readonly TYPES: Type[] = [
    {text: 'Brouillon', value: 'draft'},
    {text: 'Publié', value: 'published'},
  ];
  readonly INGREDIENT_UNITS = Workshop.INGREDIENT_UNITS;
  newIngredient: Ingredient = {
    count: 1,
    unit: this.INGREDIENT_UNITS[0],
    ingredient: ''
  };
  readonly DRAFT_STRING: string = 'draft';
  // Form controls
  title = new FormControl(this.workshop.title, [Validators.required]);
  description = new FormControl(this.workshop.description, [isSetValidator(this.workshop, this.DRAFT_STRING)]);
  category = new FormControl(this.workshop.category, [isSetValidator(this.workshop, this.DRAFT_STRING)]);
  organizer = new FormControl(this.workshop.emailOrganizer, [isSetValidator(this.workshop, this.DRAFT_STRING)]);
  dateWorkshop = new FormControl(this.workshop.date, [isSetValidator(this.workshop, this.DRAFT_STRING)]);
  inscription = new FormControl(this.workshop.inscriptionLimit, [isSetValidator(this.workshop, this.DRAFT_STRING)]);
  street = new FormControl(this.workshop.street, [isSetValidator(this.workshop, this.DRAFT_STRING)]);
  locationField = new FormControl(this.workshop.location, [isSetValidator(this.workshop, this.DRAFT_STRING)]);
  minParticipants = new FormControl(this.workshop.minParticipants, [isSetValidator(this.workshop, this.DRAFT_STRING)]);
  maxParticipants = new FormControl(this.workshop.maxParticipants, [isSetValidator(this.workshop, this.DRAFT_STRING)]);
  type = new FormControl(this.workshop.state, [Validators.required]);

  isImageMissing = false;
  isIngredientsMissing = false;
  constructor(
    private router: Router,
    private location: Location,
    private renderer: Renderer2,
    private workshopService: WorkshopService
  ) { }

  ngOnInit(): void {
    if (this.submitLabel !== 'AJOUTER') {
      this.workshopService.getWorkshop(this.titleParam, this.dateParam).then(data => {
        this.workshop.title = data.title;
        this.workshop.state = data.state;
        this.workshop.ingredients = data.ingredients;
        this.workshop.description = data.description;
        this.workshop.image = data.image;
        this.workshop.maxParticipants = data.maxParticipants;
        this.workshop.minParticipants = data.minParticipants;
        this.workshop.location = data.location;
        this.workshop.street = data.street;
        this.workshop.inscriptionLimit = data.inscriptionLimit;
        this.workshop.emailOrganizer = data.emailOrganizer;
        this.workshop.category = data.category;
        this.workshop.date = data.date;

        if (this.workshop.image !== '') {
          // @ts-ignore
          this.renderer.setStyle(this.imageButton._elementRef.nativeElement, 'background', 'url(\"' +
            this.workshop.image +
            '\") no-repeat center');
        }

        this.oldTitle = data.title;
        this.oldDate = data.date;
        this.oldState = data.state;
        this.initEditor();
      });
    } else {
      this.initEditor();
    }

    this.workshopService.getLocations().then(locations => {
      locations.forEach((location) => {
        this.locations.push(location);
      });
    });
    this.workshopService.getCategories().then(categories => {
      categories.forEach((category) => {
        this.categories.push(category);
      });
    });
    this.workshopService.getOrganizers().then(organizers => {
      organizers.forEach((organizer) => {
        this.organizers.push(organizer);
      });
    });
  }

  initEditor() {
    this.classicEditor.create( document.querySelector( '#ckeditor' ) )
      .then( editor => {
        this.editor = editor;
        this.editor.setData(this.workshop.description);
        this.description.setValue(this.workshop.description);
        this.editor.model.document.on( 'change:data', () => {
          this.workshop.description = this.editor.getData();
          this.description.setValue(this.editor.getData());
        } );
      } )
      .catch( error => {
        console.error( error );
      } );
  }

  /**
   * Open the file upload window
   */
  openImage(): void {
    const fileUpload = this.fileUpload.nativeElement;
    fileUpload.onchange = () => {
      this.file = fileUpload.files[0];
      this.uploadImage();
    };
    fileUpload.click();
  }

  /**
   * Remove an ingredient from the list
   * @param index the index of the element
   */
  removeIngredient(index: number): void {
    this.workshop.ingredients.splice(index, 1);
  }

  /**
   * Add an ingredient to the list
   */
  addIngredient(): void {
    if (this.newIngredient.ingredient === '') {
      return;
    }
    this.isIngredientsMissing = false;
    this.workshop.ingredients.push({
      count: this.newIngredient.count,
      unit: this.newIngredient.unit,
      ingredient: this.newIngredient.ingredient,
    });
    this.newIngredient.count = 1;
    this.newIngredient.unit = this.INGREDIENT_UNITS[0];
    this.newIngredient.ingredient = '';
  }

  /**
   * Navigate to the previous page
   */
  cancel(): void {
    this.location.back();
  }

  /**
   * Add or edit the workshop if the form is filled correctly.
   */
  submitForm(): void {
    if (this.isFormValid()) {
      if (this.submitLabel === 'AJOUTER') {
        this.addWorkshop();
      } else {
        this.editWorkshop();
      }
    }
  }

  /**
   * Add the workshop
   */
  async addWorkshop() {
    const status = await this.workshopService.addWorkshop(this.workshop);
    if (status) {
      alert('L\'atelier a été ajouté !');
      this.router.navigate(['admin']);
    } else {
      alert('Erreur de formulaire');
    }
  }

  /**
   * Edit the workshop
   */
  async editWorkshop() {
    let status: boolean;
    if (this.workshop.state === this.oldState) {
      status = await this.workshopService.editWorkshop(this.workshop, this.oldTitle, this.oldDate);
      if (!status) {
        alert('Erreur de formulaire');
      }
    } else {
      status = await this.workshopService.transferWorkshop(this.workshop);
      if (!status) {
        alert('Erreur: Vérifiez qu\'il n\'y ait pas d \'inscrit');
      }
    }
    if (status) {
      alert('L\'atelier a été modifié !');
      this.router.navigate(['admin']);
    }
  }

  /**
   * Get the data from the uploaded image and change the background of the image button
   */
  uploadImage() {
    // Set button background
    const reader  = new FileReader();
    reader.onload = (e) => {
      // @ts-ignore
      this.renderer.setStyle(this.imageButton._elementRef.nativeElement, 'background', 'url(\"' +
        e.target.result +
        '\") no-repeat center');
      this.workshop.image = e.target.result.toString();
    };
    reader.readAsDataURL(this.file);
  }
  onTypeChange(e): void {
    console.log('aekgn');
    this.title.updateValueAndValidity();
    this.description.updateValueAndValidity();
    this.category.updateValueAndValidity();
    this.organizer.updateValueAndValidity();
    this.inscription.updateValueAndValidity();
    this.dateWorkshop.updateValueAndValidity();
    this.street.updateValueAndValidity();
    this.locationField.updateValueAndValidity();
    this.minParticipants.updateValueAndValidity();
    this.maxParticipants.updateValueAndValidity();
    if (this.workshop.state !== '') {
      this.isIngredientsMissing = this.workshop.state !== this.DRAFT_STRING;
      this.isImageMissing = this.workshop.state !== this.DRAFT_STRING;
    }
  }

  /**
   * Return true if the form is filled correctly else false.
   */
  isFormValid(): boolean {
    this.isIngredientsMissing = this.workshop.ingredients.length ===  0 && this.workshop.state !== this.DRAFT_STRING;
    this.isImageMissing = this.workshop.image === '' && this.workshop.state !== this.DRAFT_STRING;
    console.log(
      this.title.valid + ', ' +
      this.description.valid + ', ' +
      this.category.valid + ', ' +
      this.organizer.valid + ', ' +
      this.inscription.valid + ', ' +
      this.street.valid + ', ' +
      this.locationField.valid + ', ' +
      this.minParticipants.valid + ', ' +
      this.minParticipants.valid
    );
    return (
      this.title.valid &&
      this.description.valid &&
      this.category.valid &&
      this.organizer.valid &&
      this.inscription.valid &&
      this.street.valid &&
      this.locationField.valid &&
      this.minParticipants.valid &&
      this.minParticipants.valid &&
      !this.isImageMissing &&
      !this.isIngredientsMissing
    );
  }
}

/**
 * Validator which controls only (required) if the state is not set the gived value
 * @param field the field to validate
 * @param value the valid value
 */
export function isSetValidator(field: Workshop, value: string): ValidatorFn {
  return (control: AbstractControl): {[key: string]: any} | null => {
    const isSet = (field.state === value);
    return isSet ? null : Validators.required(control);
  };
}
