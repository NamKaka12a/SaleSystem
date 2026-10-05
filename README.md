# Sales Management System – Unit Test + CI

[![Sales Management System CI](https://github.com/NamKaka12a/SaleSystem/actions/workflows/maven.yml/badge.svg)](https://github.com/NamKaka12a/SaleSystem/actions/workflows/maven.yml)

Java 17 + Maven + JUnit 5 + JaCoCo. Classes: `Product`, `SalesService` (default package, as in the handout).

## Business rules
| Item | Rule |
|---|---|
| Subtotal | price × quantity |
| Discount | <1,000: 0% · 1,000–<5,000: 5% · 5,000–<10,000: 10% · ≥10,000: 15% |
| Shipping | <2,000: 50 · ≥2,000: 0 |
| Total | Subtotal − Discount + Shipping |
| Customer type | <1,000 REGULAR · 1,000–<5,000 SILVER · 5,000–<10,000 GOLD · ≥10,000 VIP |

## Defects found in the original `SalesService` (see `docs/SalesService_ORIGINAL_BUGGY.java`)
| ID | Method | Problem | Fix |
|---|---|---|---|
| B01 | calculateSubtotal | `price + quantity` | `price * quantity` |
| B02 | calculateDiscount | 1,000–<5,000 gave 10% | 5% |
| B03 | calculateShippingFee | `<= 2000` charged shipping at exactly 2,000 | `< 2000` |
| B04 | calculateTotal | `subtotal + discount + shipping` | `subtotal - discount + shipping` |
| B05 | classifyCustomer | `<= 10000` returned GOLD at exactly 10,000 | `< 10000` |

## Run
```bash
mvn test      # run unit tests (JaCoCo report generated in target/site/jacoco/index.html)
mvn verify    # tests + coverage gate: line >= 90%, branch >= 80%
```

## CI
`.github/workflows/maven.yml` runs `mvn -B verify` on every push / pull request to `main`
and uploads the JaCoCo report as a build artifact.
