# Campus Lost and Found Management System (Terminal Edition)

A pure console (no GUI) Java application built for the **Mini Project 2** brief.
Run it in any terminal — no Swing, no external libraries.

## How to Compile & Run

```bash
cd src
javac *.java
java Main
```

The app seeds a couple of sample students and items on startup so you can
explore menus immediately (e.g. try Item Search with ID `IT0001`, or
File a Claim on `IT0002`/`IT0003` using student `S001` or `S002`).

## Menu (Expected Modules from the brief)

1.  Lost Item Registration
2.  Found Item Registration
3.  Item Search (by ID / category / location)
4.  Claim Management (file a claim on a FOUND item)
5.  Ownership Verification
6.  Return Processing
7.  Status Update
8.  History (claim & return log)
9.  Reports (counts by status / category)
10. Register Student (supporting module)
11. View All Items, sorted by date / category / status

## Where each required Java concept lives

| Concept            | File(s)                              | Notes |
|---------------------|---------------------------------------|-------|
| Classes & Objects   | `Item`, `LostItem`, `FoundItem`, `Student`, `Claim` | |
| Constructors        | Every model class                     | |
| Array               | `Item.CATEGORIES`, `Item.STATUSES`    | |
| ArrayList           | `LostFoundManager.items`, `.students` | |
| LinkedList          | `LostFoundManager.claimHistory`       | sequential claim/return log |
| HashMap             | `LostFoundManager.itemMap`, `.studentMap`, `.claimMap` | ID → object lookup |
| TreeMap             | `LostFoundManager.categoryIndex`      | items auto-sorted by category |
| CRUD                | `LostFoundManager` (register/get/update/delete for items & students) | |
| Searching           | `searchById`, `searchByCategory`, `searchByLocation` | |
| Sorting             | `sortByDate`, `sortByCategory`, `sortByStatus` (via `Comparator`) | |
| Exception Handling  | `ValidationException`, `ItemNotFoundException`, `InvalidClaimException`, caught in `Main` | |
| Validation          | `LostFoundManager` validate* methods (item fields, dates, claimant details) | |

## Item lifecycle

```
FOUND item  --fileClaim()-->  CLAIMED  --verifyOwnership()-->  (verified=true)  --processReturn()-->  RETURNED
```

A return cannot be processed until ownership has been verified, and a claim
cannot be filed on an item that isn't currently `FOUND` — both are enforced
with the custom exceptions above.
