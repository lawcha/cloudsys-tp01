/**
 * Represent a City composed of (npa, city)
 */
export class City {
  constructor(
    public npa: number,
    public city: string
  ) {}
  public toString = (): string => {
    return `${this.npa} ${this.city}`;
  }
}
