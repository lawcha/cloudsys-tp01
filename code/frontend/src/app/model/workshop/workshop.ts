import {City} from '../city/city';
import * as parser from 'xml-js';
import xmlbuilder, {XMLElement} from 'xmlbuilder';

/**
 * Represent the structure of a single ingrendient
 */
export interface Ingredient {
  count: number;
  unit: string;
  ingredient: string;
}

/**
 * Represent the structure of an organizer
 */
export interface Organizer {
  email: string;
  firstName: string;
  lastName: string;
  phone: string;
  street: string;
  location: City;
}
/**
 * Represent a Workshop but formatted like it is used in Angular
 */
export class Workshop {
  public static readonly INGREDIENT_UNITS: string[] = ['mg', 'g', 'kg', 'ml', 'dl', 'l'];
  constructor(
    public title: string,
    public category: string,
    public emailOrganizer: string,
    public date: Date,
    public inscriptionLimit: Date,
    public street: string,
    public location: City,
    public description: string,
    public ingredients: Ingredient[],
    public minParticipants: number,
    public maxParticipants: number,
    public image: string,
    public state: string,
  ) {}

  /**
   * Convert a WorkshopProd to a Workshop object
   * @param origin the WorkshopProd object
   */
  static toLocal(origin: WorkshopProd): Workshop {
    return new Workshop(
      origin.title,
      origin.category,
      origin.emailOrganizer,
      origin.date,
      origin.inscriptionLimit,
      origin.street,
      origin.location,
      origin.description,
      this.formatToJSON(origin.ingredients), // out = [{count: 1, unit: 'kg', ingredient: 'Example'}],
      origin.minParticipants,
      origin.maxParticipants,
      origin.image,
      (typeof origin.state !== 'undefined') ? origin.state : 'draft'
    );
  }
  /**
   * Convert a Workshop to a WorkshopProd object
   * @param origin the Workshop object
   */
  static toProd(origin: Workshop): WorkshopProd {
    return new WorkshopProd(
      origin.title,
      origin.category,
      origin.emailOrganizer,
      origin.date,
      origin.inscriptionLimit,
      origin.street,
      origin.location,
      origin.description,
      this.formatToXML(origin.ingredients), // out = '<ingredients><ingredient count=1>...</ingredient></ingredients>',
      origin.minParticipants,
      origin.maxParticipants,
      origin.image,
      origin.state
    );
  }

  /**
   * Convert the given ingredients (object structure) into a XML notation
   * @param input : ingredients of the workshop
   * @return XML format like <ingredients><ingredient count=1 unit='kg'>Pain</ingredient></ingredients>
   */
  static formatToXML(input: Ingredient[]): string {
    const root: XMLElement = xmlbuilder.create('ingredients');
    input.forEach((ingredient) => {
      root.ele(
        'ingredient',
        {count: ingredient.count, unit: ingredient.unit},
        ingredient.ingredient
      );
    });
    root.end();
    return root.toString();
  }

  /**
   * Convert the given ingredients from the XML notation to an array ( Ingredient[] )
   * @param input : ingredients of the workshops in XML notation
   * @return JSON format like [{count: 1, unit: 'kg', ingredient: 'Pain'}]
   */
  static formatToJSON(input: string): Ingredient[] {
    const ingredients: Ingredient[] = [];
    try {
      // Basic parse of the string to JSON
      const json = JSON.parse(parser.xml2json(input));
      const rootElements = json.elements[0].elements;
      rootElements.forEach((element) => {
        const amount: number = +element.attributes.count;
        const xmlUnit = element.attributes.unit;
        const ingredientText = element.elements[0].text;
        ingredients.push(
          {
            ingredient: ingredientText,
            unit: xmlUnit,
            count: amount
          }
        );
      });
    } catch (e) {
      // alert('Une erreur durant l\'import des ingrédients de l\'atelier s\'est produite.');
      console.log(e);
    }
    return ingredients;
  }
}

/**
 * Represent a Workshop but formatted like it is in the database
 */
class WorkshopProd {
  constructor(
    public title: string,
    public category: string,
    public emailOrganizer: string,
    public date: Date,
    public inscriptionLimit: Date,
    public street: string,
    public location: City,
    public description: string,
    public ingredients: string,
    public minParticipants: number,
    public maxParticipants: number,
    public image: string,
    public state: string,
  ) {}
}
